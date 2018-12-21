package xyz.itao.ink.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import xyz.itao.ink.dao.ContentMapper;
import xyz.itao.ink.domain.ContentDomain;
import xyz.itao.ink.domain.DomainFactory;
import xyz.itao.ink.domain.entity.Content;
import xyz.itao.ink.repository.AbstractBaseRepository;
import xyz.itao.ink.repository.ContentRepository;
import xyz.itao.ink.repository.UserRepository;
import xyz.itao.ink.service.AbstractBaseService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author hetao
 * @date 2018-12-10
 * @description
 */
@Repository(value = "contentRepository")
public class ContentRepositoryImpl extends AbstractBaseRepository<ContentDomain, Content> implements ContentRepository {
    @Autowired
    ContentMapper contentMapper;
    @Autowired
    UserRepository userRepository;
    @Autowired
    DomainFactory domainFactory;
    @Override
    protected boolean doSave(Content entity) {
        return contentMapper.insertSelective(entity);
    }

    @Override
    protected List<Content> doLoadByNoNullProperties(Content entity) {
        return contentMapper.selectByNoNulProperties(entity);
    }

    @Override
    protected boolean doUpdate(Content entity) {
        return contentMapper.updateByPrimaryKeySelective(entity);
    }

    @Override
    protected ContentDomain doAssemble(Content entity) {
        return domainFactory.createContentDomain().assemble(entity);
    }

    @Override
    protected Content doExtract(ContentDomain domain) {
        return domain.entity();
    }

    @Override
    public ContentDomain updateContentDomain(ContentDomain domain) {
        return update(domain);
    }

    @Override
    public ContentDomain saveNewContentDomain(ContentDomain domain) {
        return save(domain);
    }

    @Override
    public ContentDomain loadActiveContentDomainById(Long id) {
        ContentDomain domain = domainFactory.createContentDomain().setId(id);
        return loadByNoNullPropertiesNotDelect(domain).get(0);
    }

    @Override
    public List<ContentDomain> loadAllActiveContentDomain(ContentDomain contentDomain) {
        return loadByNoNullPropertiesActiveAndNotDelect(contentDomain);
    }

    @Override
    public List<ContentDomain> loadAllFeedArticles() {
        ContentDomain contentDomain = domainFactory.createContentDomain().setAllowFeed(true);
        return loadByNoNullPropertiesActiveAndNotDelect(contentDomain);
    }

    @Override
    public void updateHit(Long id) {
        // todo 先放入缓存，缓存达到条件后更新到数据库
    }

    @Override
    public Long getHit(Long id) {
        // todo 先从缓存中获取
        return loadActiveContentDomainById(id).getHits();
    }


    @Override
    public List<ContentDomain> loadAllNotActiveContentDomain(ContentDomain contentDomain) {
        return loadByNoNullPropertiesNotActiveAndNotDelect(contentDomain);
    }

    @Override
    public List<ContentDomain> loadAllContentDomain(ContentDomain contentDomain) {
        return loadByNoNullPropertiesNotDelect(contentDomain);
    }

    @Override
    public List<ContentDomain> loadAllActiveContentDomainByContentIdIn(List<Long> articleIds) {
        List<Content> contents = contentMapper.selectAllContentIn(articleIds, false, true);
        return contents.stream().map(e->assemble(e)).collect(Collectors.toList());
    }
}
