package xyz.itao.ink.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import xyz.itao.ink.dao.ContentMetaMapper;
import xyz.itao.ink.dao.MetaMapper;
import xyz.itao.ink.domain.DomainFactory;
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

    @Autowired
    ContentMetaMapper contentMetaMapper;

    @Autowired
    DomainFactory domainFactory;

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
        MetaDomain metaDomain = domainFactory.createMetaDomain().setType(type).setName(name);
        List<MetaDomain> metaDomains = loadByNoNullPropertiesActiveAndNotDelect(metaDomain);
        return metaDomains.isEmpty() ? null : metaDomains.get(0);
    }

    @Override
    public List<MetaDomain> loadMetaDomainsByType(String type) {
        MetaDomain metaDomain = domainFactory.createMetaDomain().setType(type);
        return loadByNoNullPropertiesActiveAndNotDelect(metaDomain);
    }

    @Override
    public Integer countArticlesByMetaId(Long id) {
        // todo 先从缓存中查找
        return contentMetaMapper.countContentsByMetaId(id);
    }

    @Override
    public List<Long> loadAllContentIdByMetaId(Long id) {
        // todo 先从缓存中查找
        return contentMetaMapper.selectAllContentIdByMetaId(id);
    }

    @Override
    public MetaDomain loadMetaDomainById(Long metaId) {
        List<MetaDomain> metaDomains = loadByNoNullPropertiesNotActiveAndNotDelect(domainFactory.createMetaDomain().setId(metaId));
        if(metaDomains == null){
            return null;
        }
        return metaDomains.get(0);
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
        return domainFactory.createMetaDomain().assemble(entity);

    }

    @Override
    protected Meta doExtract(MetaDomain domain) {
        return domain.entity();
    }
}
