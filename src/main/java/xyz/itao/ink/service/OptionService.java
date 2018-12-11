package xyz.itao.ink.service;

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
     * 保存配置
     *
     * @param key   配置key
     * @param value 配置值
     */
    void saveOption(String key, String value);

    /**
     * 根据key删除配置项
     *
     * @param key 配置key
     */
    void deleteOption(String key);

    /**
     * 根据key获取value
     */
    String getOption(String key);

    Map<String, Object> loadOptions();

    void deleteAllThemes();
}
