package xyz.itao.ink.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
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


    @Override
    public Map<String, String> loadAllOptions() {
        Map<String, String> options = new HashMap<>();
        AnimaQuery<Options> animaQuery = select().from(Options.class);
        List<Options> optionsList = animaQuery.all();
        if (null != optionsList) {
            optionsList.forEach(option -> options.put(option.getName(), option.getValue()));
        }
        return options;
    }

    @Override
    public void saveOption(String k, String s) {
        if (StringKit.isNotBlank(key) && StringKit.isNotBlank(value)) {
            Options options = new Options();
            options.setName(key);

            long count = select().from(Options.class).where(Options::getName, key).count();

            if (count == 0) {
                options = new Options();
                options.setName(key);
                options.setValue(value);
                options.save();
            } else {
                options = new Options();
                options.setValue(value);
                options.updateById(key);
            }
        }
    }

    @Override
    public void deleteOption(String key) {

    }

    @Override
    public String getOption(String key) {
        Options options = select().from(Options.class).byId(key);
        if (null != options) {
            return options.getValue();
        }
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
