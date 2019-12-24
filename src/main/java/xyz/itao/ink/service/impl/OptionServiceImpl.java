package xyz.itao.ink.service.impl;

import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.itao.ink.common.Props;
import xyz.itao.ink.domain.DomainFactory;
import xyz.itao.ink.domain.OptionDomain;
import xyz.itao.ink.domain.UserDomain;
import xyz.itao.ink.repository.OptionRepository;
import xyz.itao.ink.service.OptionService;

import java.util.List;
import java.util.Map;

/**
 * @author hetao
 * @date 2018-12-10
 */
@Service("optionService")
public class OptionServiceImpl implements OptionService {

    private final OptionRepository optionRepository;

    @Autowired
    public OptionServiceImpl(OptionRepository optionRepository) {
        this.optionRepository = optionRepository;
    }

    @Override
    public Map<String, String> loadAllOptions() {
        Map<String, String> options = Maps.newHashMap();
        List<OptionDomain> optionDomains = optionRepository.loadAllOptionDomain();
        if (!optionDomains.isEmpty()) {
            optionDomains.forEach(optionDomain -> options.put(optionDomain.getName(), optionDomain.getValue()));
        }
        return options;
    }


    @Override
    @Transactional
    public void deleteOption(String key, UserDomain userDomain) {
        deleteByNameLike(key + "%", userDomain);
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
    @Transactional
    public void deleteAllThemes(UserDomain userDomain) {
        deleteByNameLike("theme_option_%", userDomain);
    }

    private void deleteByNameLike(String pattern, UserDomain userDomain) {
        List<OptionDomain> optionDomains = optionRepository.loadAllOptionDomainNotDeleteLike(pattern);
        optionDomains.forEach(optionDomain -> {
            optionDomain.setUpdateBy(userDomain.getId());
            optionDomain.setDeleted(true);
            optionRepository.updateOptionDomain(optionDomain);
        });
    }
}
