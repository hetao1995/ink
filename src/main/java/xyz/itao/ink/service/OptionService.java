package xyz.itao.ink.service;

import xyz.itao.ink.domain.UserDomain;

import java.util.Map;

/**
 * @author hetao
 * @date 2018-12-05
 */
public interface OptionService {
    /**
     * 获取系统配置
     *
     * @return 所有的系统配置
     */
    Map<String, String> loadAllOptions();


    /**
     * 根据key删除配置项
     *
     * @param userDomain 操作人
     * @param key        配置key
     */
    void deleteOption(String key, UserDomain userDomain);

    /**
     * 根据key获取value
     *
     * @param key key
     *            配置
     */
    String getOption(String key);

    /**
     * 获取option
     *
     * @return 配置
     */
    Map<String, Object> loadOptions();

    /**
     * 删除所以themes
     *
     * @param userDomain 操作人
     */
    void deleteAllThemes(UserDomain userDomain);
}
