package xyz.itao.ink.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import xyz.itao.ink.dao.MetaMapper;
import xyz.itao.ink.domain.MetaDomain;
import xyz.itao.ink.domain.entity.Meta;
import xyz.itao.ink.repository.AbstractBaseRepository;
import xyz.itao.ink.repository.MetaRepository;

import java.util.List;

/**
 * @author hetao
 * @date 2018-12-11
 * @description
 */
@Repository("metaRepository")
public class MetaRepositoryImpl extends AbstractBaseRepository<MetaDomain, Meta> implements MetaRepository {

    @Autowired
    MetaMapper metaMapper;
    @Override
    public MetaDomain updateMetaDomain(MetaDomain domain) {
        return update(domain);
    }

    @Override
    public MetaDomain saveNewMetaDomain(MetaDomain domain) {
        return save(domain);
    }

    @Override
    public MetaDomain loadMetaDomainByTypeAndName(String type, String name) {
        MetaDomain metaDomain = MetaDomain
                .builder()
                .type(type)
                .name(name)
                .build();
        return loadByNoNullPropertiesActiveAndNotDelect(metaDomain).get(0);
    }

    @Override
    public List<MetaDomain> loadMetaDomainsByType(String type) {
        MetaDomain metaDomain = MetaDomain
                .builder()
                .type(type)
                .build();
        return loadByNoNullPropertiesActiveAndNotDelect(metaDomain);
    }

    @Override
    protected boolean doSave(Meta entity) {
        return metaMapper.insertSelective(entity);
    }

    @Override
    protected List<Meta> doLoadByNoNullProperties(Meta entity) {
        return metaMapper.selectByNoNulProperties(entity);
    }

    @Override
    protected boolean doUpdate(Meta entity) {
        return metaMapper.updateByPrimaryKeySelective(entity);
    }

    @Override
    protected MetaDomain doAssemble(Meta entity) {
        return MetaDomain
                .builder()
                .id(entity.getId())
                .deleted(entity.getDeleted())
                .createTime(entity.getCreateTime())
                .createBy(entity.getCreateBy())
                .updateTime(entity.getUpdateTime())
                .updateBy(entity.getUpdateBy())
                .active(entity.getActive())
                .name(entity.getName())
                .slug(entity.getSlug())
                .parentId(entity.getParentId())
                .type(entity.getType())
                .sort(entity.getSort())
                .detail(entity.getDetail())
                .build();

    }

    @Override
    protected Meta doExtract(MetaDomain domain) {
        return Meta
                .builder()
                .id(domain.getId())
                .deleted(domain.getDeleted())
                .createTime(domain.getCreateTime())
                .createBy(domain.getCreateBy())
                .updateTime(domain.getUpdateTime())
                .updateBy(domain.getUpdateBy())
                .active(domain.getActive())
                .name(domain.getName())
                .slug(domain.getSlug())
                .parentId(domain.getParentId())
                .type(domain.getType())
                .sort(domain.getSort())
                .detail(domain.getDetail())
                .build();
    }
}
