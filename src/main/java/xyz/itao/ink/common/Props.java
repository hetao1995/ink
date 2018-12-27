package xyz.itao.ink.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import xyz.itao.ink.domain.DomainFactory;
import xyz.itao.ink.domain.OptionDomain;
import xyz.itao.ink.domain.UserDomain;
import xyz.itao.ink.exception.ExceptionEnum;
import xyz.itao.ink.exception.InnerException;
import xyz.itao.ink.repository.OptionRepository;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

/**
 * @author hetao
 * @date 2018-12-24
 * @description
 */
@Component
@Slf4j
@CacheConfig(cacheNames = "props")
public class Props {
    @Autowired
    OptionRepository optionRepository;
    @Autowired
    DomainFactory domainFactory;

    @CachePut(key = "#name")
    public String  set(String name, Object value, UserDomain userDomain){
        OptionDomain optionDomain = optionRepository.loadOptionDomainByName(name);
        if(optionDomain == null){
            domainFactory
                    .createOptionDomain()
                    .setCreateBy(userDomain.getId())
                    .setUpdateBy(userDomain.getId())
                    .setActive(true)
                    .setName(name)
                    .setValue(value.toString())
                    .save();
        }else{
            optionDomain.setName(name).setValue(value.toString()).setUpdateBy(userDomain.getId()).updateById();
        }
        return optionDomain.getValue();
    }

    public void setAll(Map<String, String> map, UserDomain userDomain){
        for(Map.Entry<String, String> entry : map.entrySet()){
            this.set(entry.getKey(), entry.getValue(), userDomain);
        }
    }

    public void setAll(Properties properties, UserDomain userDomain){
        for(String name : properties.stringPropertyNames()){
            this.set(name, properties.getProperty(name), userDomain);
        }
    }

    /**
     * 反射设置pojo所有参数，必须有get方法
     * @param param
     * @param userDomain
     */
    public void setAll(Object param, UserDomain userDomain){
        Class clazz = param.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for(Field field : fields){
            try {
                Method method = param.getClass().getMethod("get"+this.getMethodName(field.getName()));
                this.set(field.getName(), method.invoke(param), userDomain);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("反射获取参数出错：{}",e);
                throw  new InnerException(ExceptionEnum.ILLEGAL_OPERATION);
            }
        }
    }
    // 把一个字符串的第一个字母大写
    private String getMethodName(String fildeName){
        byte[] items = fildeName.getBytes();
        items[0] = (byte) ((char) items[0] - 'a' + 'A');
        return new String(items);
    }

    public Optional<String> get(String name){
        OptionDomain optionDomain = optionRepository.loadOptionDomainByName(name);
        if(optionDomain==null){
            return  Optional.empty();
        }
        return Optional.ofNullable(optionDomain.getValue());
    }

    @Cacheable(key = "#name")
    public String get(String name, String defaultValue){
        return get(name).orElse(defaultValue);
    }

    public Optional<Integer> getInt(String name){
        Optional<String> optional = this.get(name);
        return optional.map(Integer::parseInt);
    }

    public Integer getInt(String name, Integer defaultValue){
        return getInt(name).orElse(defaultValue);
    }

    public Optional<Long> getLong(String name){
        Optional<String> optional = this.get(name);
        return optional.map(Long::parseLong);
    }

    public Long getLong(String name, Long defaultValue){
        return getLong(name).orElse(defaultValue);
    }

    public Optional<Boolean> getBoolean(String name){
        return this.get(name).map(Boolean::parseBoolean);
    }

    public Boolean getBoolean(String name, Boolean defaultValue){
        return getBoolean(name).orElse(defaultValue);
    }

    public Optional<Double> getDouble(String name){
        return this.get(name).map(Double::parseDouble);
    }
}
