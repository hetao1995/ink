package xyz.itao.ink.repository.impl;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import xyz.itao.ink.dao.OptionMapper;
import xyz.itao.ink.domain.OptionDomain;
import xyz.itao.ink.domain.entity.Option;
import xyz.itao.ink.repository.AbstractBaseRepository;
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


    @Override
    public List<OptionDomain> loadAllOptionDomain() {
        return null;
    }

    @Override
    public OptionDomain loadOptionDomainByName(String k) {
        return null;
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
        return OptionDomain
                .builder()
                .id(entity.getId())
                .deleted(entity.getDeleted())
                .createTime(entity.getCreateTime())
                .createBy(entity.getCreateBy())
                .updateTime(entity.getUpdateTime())
                .updateBy(entity.getUpdateBy())
                .active(entity.getActive())
                .name(entity.getName())
                .detail(entity.getDetail())
                .value(entity.getValue())
                .build();
    }

    @Override
    protected Option doExtract(OptionDomain domain) {
        return Option
                .builder()
                .id(domain.getId())
                .deleted(domain.getDeleted())
                .createTime(domain.getCreateTime())
                .createBy(domain.getCreateBy())
                .updateTime(domain.getUpdateTime())
                .updateBy(domain.getUpdateBy())
                .active(domain.getActive())
                .name(domain.getName())
                .detail(domain.getDetail())
                .value(domain.getValue())
                .build();
    }
}
