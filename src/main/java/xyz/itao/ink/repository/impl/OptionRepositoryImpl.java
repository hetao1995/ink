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
public class OptionRepositoryImpl  extends AbstractBaseRepository<OptionDomain, Option> implements OptionRepository {

    @Autowired
    OptionMapper optionMapper;
    @Autowired
    DomainFactory domainFactory;


    @Override
    public List<OptionDomain> loadAllOptionDomain() {
        return loadByNoNullPropertiesNotDelect(domainFactory.createOptionDomain());
    }

    @Override
    public OptionDomain loadOptionDomainByName(String name) {
        List<OptionDomain> optionDomains = loadByNoNullPropertiesActiveAndNotDelect(domainFactory.createOptionDomain().setName(name));
        if(optionDomains.isEmpty()){
            return null;
        }
        return optionDomains.get(0);
    }

    @Override
    public void updateOptionDomain(OptionDomain optionDomain) {
        update(optionDomain);
    }

    @Override
    public void saveNewOptionDomain(OptionDomain optionDomain) {
        save(optionDomain);
    }

    @Override
    public List<OptionDomain> loadAllOptionDomainNotDeleteLike(String pattern) {
        List<Option> options = optionMapper.selectNotDeleteByNamePattern(pattern);
        return options.stream().map(e->assemble(e)).collect(Collectors.toList());
    }

    @Override
    public OptionDomain loadOptionDomainById(Long id) {
        return domainFactory.createOptionDomain().assemble(optionMapper.selectByPrimaryKey(id));
    }


    @Override
    protected boolean doSave(Option entity) {
        return optionMapper.insertSelective(entity);
    }

    @Override
    protected List<Option> doLoadByNoNullProperties(Option entity) {
        return optionMapper.selectByNoNulProperties(entity);
    }

    @Override
    protected boolean doUpdate(Option entity) {
        return optionMapper.updateByPrimaryKeySelective(entity);
    }

    @Override
    protected OptionDomain doAssemble(Option entity) {
        return domainFactory.createOptionDomain().assemble(entity);
    }

    @Override
    protected Option doExtract(OptionDomain domain) {
        return  domain.entity();
    }
}
