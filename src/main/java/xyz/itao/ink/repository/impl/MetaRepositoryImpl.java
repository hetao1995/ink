package xyz.itao.ink.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import xyz.itao.ink.dao.ContentMetaMapper;
import xyz.itao.ink.dao.MetaMapper;
import xyz.itao.ink.domain.ContentDomain;
import xyz.itao.ink.domain.DomainFactory;
import xyz.itao.ink.domain.MetaDomain;
import xyz.itao.ink.domain.entity.Content;
import xyz.itao.ink.domain.entity.ContentMeta;
import xyz.itao.ink.domain.entity.Meta;
import xyz.itao.ink.exception.ExceptionEnum;
import xyz.itao.ink.exception.InnerException;
import xyz.itao.ink.repository.AbstractBaseRepository;
import xyz.itao.ink.repository.MetaRepository;
import xyz.itao.ink.utils.DateUtils;
import xyz.itao.ink.utils.IdUtils;

import java.util.List;
import java.util.stream.Collectors;

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
        List<MetaDomain> metaDomains = loadByNoNullPropertiesNotDelect(domainFactory.createMetaDomain().setId(metaId));
        if(metaDomains.isEmpty()){
            return null;
        }
        return metaDomains.get(0);
    }

    @Override
    public List<MetaDomain> loadAllMetaDomainByContentIdAndType(Long contentId, String type) {
        List<Meta> metas = metaMapper.selectByContentIdAndType(contentId, type);
        return metas.stream().map(e->assemble(e)).collect(Collectors.toList());
    }

    @Override
    public boolean deleteContentMetaRelationshipByContentIdAndMetaId(Long contentId, Long metaId) {

         ContentMeta contentMeta = contentMetaMapper.selectByContentIdAndMetaId(contentId, metaId);
         if(contentMeta == null){
             throw new InnerException(ExceptionEnum.DELETE_NON_EXIST_ELEMENT);
         }
         contentMeta.setDeleted(true);
         return contentMetaMapper.updateByPrimaryKeySelective(contentMeta);
    }


    @Override
    public List<ContentDomain> loadAllActiveContentDomainByMetaId(Long id) {
        Meta meta = Meta.builder().id(id).active(true).deleted(false).build();
        List<Content> contents = metaMapper.selectContentByMeta(meta);
        return contents.stream().map(content -> domainFactory.createContentDomain().assemble(content)).collect(Collectors.toList());
    }

    @Override
    public boolean saveNewContentMeta(ContentMeta contentMeta) {
        return contentMetaMapper.insertSelective(contentMeta);
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
