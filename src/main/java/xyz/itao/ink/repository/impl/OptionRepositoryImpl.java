package xyz.itao.ink.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import xyz.itao.ink.dao.OptionMapper;
import xyz.itao.ink.domain.DomainFactory;
import xyz.itao.ink.domain.OptionDomain;
import xyz.itao.ink.domain.entity.Option;
import xyz.itao.ink.repository.OptionRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author hetao
 * @date 2018-12-11
 * @description
 */
@Repository("optionRepository")
public class OptionRepositoryImpl  implements OptionRepository {

    @Autowired
    OptionMapper optionMapper;
    @Autowired
    DomainFactory domainFactory;


    @Override
    public List<OptionDomain> loadAllOptionDomain() {
        OptionDomain domain = domainFactory.createOptionDomain().setDeleted(false);
        List<Option> options = optionMapper.selectByNoNulProperties(domain.entity());
        return options
                .stream()
                .map(e->domainFactory.createOptionDomain().assemble(e))
                .collect(Collectors.toList());
    }

    @Override
    public OptionDomain loadOptionDomainByName(String name) {
        OptionDomain domain = domainFactory.createOptionDomain().setDeleted(false).setName(name);
        List<Option> options = optionMapper.selectByNoNulProperties(domain.entity());
       if(options.isEmpty()){
            return null;
        }
        return domain.assemble(options.get(0));
    }

    @Override
    public void updateOptionDomain(OptionDomain optionDomain) {
        optionMapper.updateByPrimaryKeySelective(optionDomain.entity());
    }

    @Override
    public void saveNewOptionDomain(OptionDomain optionDomain) {
        optionMapper.insertSelective(optionDomain.entity());
    }

    @Override
    public List<OptionDomain> loadAllOptionDomainNotDeleteLike(String pattern) {
        List<Option> options = optionMapper.selectNotDeleteByNamePattern(pattern);
        return options.stream().map(e->domainFactory.createOptionDomain().assemble(e)).collect(Collectors.toList());
    }

    @Override
    public OptionDomain loadOptionDomainById(Long id) {
        return domainFactory.createOptionDomain().assemble(optionMapper.selectByPrimaryKey(id));
    }

}
