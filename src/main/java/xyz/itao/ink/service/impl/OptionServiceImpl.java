package xyz.itao.ink.service.impl;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.itao.ink.common.Props;
import xyz.itao.ink.domain.DomainFactory;
import xyz.itao.ink.domain.OptionDomain;
import xyz.itao.ink.domain.UserDomain;
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
    @Autowired
    DomainFactory domainFactory;
    @Autowired
    Props props;

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
    public void deleteOption(String key, UserDomain userDomain) {
        deleteByNameLike(key+"%", userDomain);
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
    public void deleteAllThemes(UserDomain userDomain) {
        deleteByNameLike("theme_option_%", userDomain);
    }

    private void deleteByNameLike(String pattern, UserDomain userDomain){
        List<OptionDomain> optionDomains = optionRepository.loadAllOptionDomainNotDeleteLike(pattern);
        optionDomains.forEach(optionDomain -> {
            optionDomain.setUpdateBy(userDomain.getId());
            optionDomain.setDeleted(true);
            optionRepository.updateOptionDomain(optionDomain);
        });
    }
}
