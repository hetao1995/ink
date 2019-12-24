package xyz.itao.ink.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;
import xyz.itao.ink.annotation.CacheRemove;
import xyz.itao.ink.constant.WebConstant;
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
import xyz.itao.ink.repository.MetaRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author hetao
 * @date 2018-12-11
 */
@Repository("metaRepository")
public class MetaRepositoryImpl implements MetaRepository {

    private final MetaMapper metaMapper;

    private final ContentMetaMapper contentMetaMapper;

    private final DomainFactory domainFactory;

    @Autowired
    public MetaRepositoryImpl(MetaMapper metaMapper, ContentMetaMapper contentMetaMapper, DomainFactory domainFactory) {
        this.metaMapper = metaMapper;
        this.contentMetaMapper = contentMetaMapper;
        this.domainFactory = domainFactory;
    }

    @Override
    public MetaDomain updateMetaDomain(MetaDomain domain) {
        metaMapper.updateByPrimaryKeySelective(domain.entity());
        return domain;
    }

    @Override
    public MetaDomain saveNewMetaDomain(MetaDomain domain) {
        metaMapper.insertSelective(domain.entity());
        return domain;
    }

    @Override
    public MetaDomain loadMetaDomainByTypeAndName(String type, String name) {
        MetaDomain metaDomain = domainFactory.createMetaDomain().setType(type).setName(name).setDeleted(false).setActive(true);
        List<Meta> metas = metaMapper.selectByNoNulProperties(metaDomain.entity());
        if (metas.isEmpty()) {
            return null;
        }
        return metaDomain.assemble(metas.get(0));
    }

    @Override
    public List<MetaDomain> loadMetaDomainsByType(String type) {
        MetaDomain metaDomain = domainFactory.createMetaDomain().setType(type).setDeleted(false).setActive(true);
        List<Meta> metas = metaMapper.selectByNoNulProperties(metaDomain.entity());
        return metas
                .stream()
                .map(e -> domainFactory.createMetaDomain().assemble(e))
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = WebConstant.META_CACHE, key = "#id+'_count_article'")
    public Integer countArticlesByMetaId(Long id) {
        return contentMetaMapper.countContentsByMetaId(id);
    }

    @Override
    public List<Long> loadAllContentIdByMetaId(Long id) {
        return contentMetaMapper.selectAllContentIdByMetaId(id);
    }

    @Override
    public MetaDomain loadMetaDomainById(Long metaId) {
        MetaDomain metaDomain = domainFactory.createMetaDomain().setDeleted(false).setId(metaId);
        List<Meta> metas = metaMapper.selectByNoNulProperties(metaDomain.entity());
        if (metas.isEmpty()) {
            return null;
        }
        return metaDomain.assemble(metas.get(0));
    }

    @Override
    public List<MetaDomain> loadAllMetaDomainByContentIdAndType(Long contentId, String type) {
        List<Meta> metas = metaMapper.selectByContentIdAndType(contentId, type);
        return metas.stream().map(e -> domainFactory.createMetaDomain().assemble(e)).collect(Collectors.toList());
    }

    @Override
    public boolean deleteContentMetaRelationshipByContentIdAndMetaId(Long contentId, Long metaId) {

        ContentMeta contentMeta = contentMetaMapper.selectByContentIdAndMetaId(contentId, metaId);
        if (contentMeta == null) {
            throw new InnerException(ExceptionEnum.DELETE_NON_EXIST_ELEMENT);
        }
        contentMeta.setDeleted(true);
        return contentMetaMapper.updateByPrimaryKeySelective(contentMeta);
    }


    @Override
    public List<ContentDomain> loadAllActiveContentDomainByMetaIdAndStatus(Long id, String status) {
        Meta meta = Meta.builder().id(id).active(true).deleted(false).build();
        List<Content> contents = metaMapper.selectContentByMetaAndStatus(meta, status);
        return contents.stream().map(content -> domainFactory.createContentDomain().assemble(content)).collect(Collectors.toList());
    }

    @Override
    @CacheRemove(value = WebConstant.META_CACHE, key = "#contentMeta.metaId+'*'")
    public boolean saveNewContentMeta(ContentMeta contentMeta) {
        return contentMetaMapper.insertSelective(contentMeta);
    }

    @Override
    @Cacheable(value = WebConstant.META_CACHE, key = "'type_'+#type+'_count'")
    public Long countMetaNum(String type) {
        return metaMapper.countMetaByType(type);
    }

}
