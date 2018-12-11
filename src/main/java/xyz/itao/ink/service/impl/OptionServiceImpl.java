package xyz.itao.ink.service.impl;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.itao.ink.domain.OptionDomain;
import xyz.itao.ink.domain.vo.UserVo;
import xyz.itao.ink.repository.OptionRepository;
import xyz.itao.ink.service.AbstractBaseService;
import xyz.itao.ink.service.OptionService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hetao
 * @date 2018-12-10
 * @description
 */
@Service("optionService")
public class OptionServiceImpl implements OptionService {

    @Autowired
    OptionRepository optionRepository;

    @Override
    public Map<String, String> loadAllOptions() {
        Map<String, String> options = Maps.newHashMap();
        List<OptionDomain> optionDomains = optionRepository.loadAllOptionDomain();
        if (optionDomains.isEmpty()) {
            optionDomains.forEach(optionDomain -> options.put(optionDomain.getName(), optionDomain.getValue()));
        }
        return options;
    }

    @Override
    public void saveOption(String k, String v) {
        if(StringUtils.isBlank(k) || StringUtils.isBlank(v)){
            return;
        }
        OptionDomain optionDomain = optionRepository.loadOptionDomainByName(k);
        if(optionDomain != null){
            optionDomain.setValue(v);
            optionRepository.updateOptionDomain(optionDomain);
        }else{
            optionDomain = OptionDomain
                    .builder()
                    .name(k)
                    .value(v)
                    .build();
            optionRepository.saveNewOptionDomain(optionDomain);
        }
    }

    @Override
    public void deleteOption(String key, UserVo userVo) {
        deleteByNameLike(key+"%", userVo);
    }

    @Override
    public String getOption(String key) {

        OptionDomain optionDomain = optionRepository.loadOptionDomainByName(key);
        if (null != optionDomain) {
            return optionDomain.getValue();
        }
        return null;
    }

    @Override
    public Map<String, Object> loadOptions() {
        return null;
    }

    @Override
    public void deleteAllThemes(UserVo userVo) {
        deleteByNameLike("theme_option_%", userVo);
    }

    private void deleteByNameLike(String pattern, UserVo userVo){
        List<OptionDomain> optionDomains = optionRepository.loadAllOptionDomainNotDeleteLike(pattern);
        optionDomains.forEach(optionDomain -> {
            optionDomain.setUpdateBy(userVo.getId());
            optionDomain.setDeleted(true);
            optionRepository.updateOptionDomain(optionDomain);
        });
    }
}
