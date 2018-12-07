package xyz.itao.ink.service;

import java.util.Map;

/**
 * @author hetao
 * @date 2018-12-05
 * @description
 */
public interface OptionService {
    Map<String, String> loadAllOptions();

    void saveOption(String k, String s);

    void deleteOption(String key);

    String getOption(String key);
}
