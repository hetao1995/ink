package xyz.itao.ink.service.impl;

import org.springframework.stereotype.Service;
import xyz.itao.ink.service.OptionService;

import java.util.Map;

/**
 * @author hetao
 * @date 2018-12-10
 * @description
 */
@Service("optionService")
public class OptionServiceImpl implements OptionService {
    @Override
    public Map<String, String> loadAllOptions() {
        return null;
    }

    @Override
    public void saveOption(String k, String s) {
        // todo
    }

    @Override
    public void deleteOption(String key) {

    }

    @Override
    public String getOption(String key) {
        return null;
    }

    @Override
    public Map<String, Object> loadOptions() {
        return null;
    }

    @Override
    public void deleteAllThemes() {

    }
}
