package xyz.itao.ink.service;

import xyz.itao.ink.domain.UserDomain;
import xyz.itao.ink.domain.vo.UserVo;

import java.util.Map;

/**
 * @author hetao
 * @date 2018-12-05
 * @description
 */
public interface OptionService {
    /**
     * 获取系统配置
     */
    Map<String, String> loadAllOptions();


    /**
     * 根据key删除配置项
     *
     * @param key 配置key
     */
    void deleteOption(String key, UserDomain userDomain);

    /**
     * 根据key获取value
     */
    String getOption(String key);

    Map<String, Object> loadOptions();

    void deleteAllThemes(UserDomain userDomain);
}
