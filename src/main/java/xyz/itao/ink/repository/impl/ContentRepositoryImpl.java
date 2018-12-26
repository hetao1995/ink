package xyz.itao.ink.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import xyz.itao.ink.constant.TypeConst;
import xyz.itao.ink.dao.ContentMapper;
import xyz.itao.ink.domain.ContentDomain;
import xyz.itao.ink.domain.DomainFactory;
import xyz.itao.ink.domain.entity.Archive;
import xyz.itao.ink.domain.entity.Content;
import xyz.itao.ink.repository.ContentRepository;
import xyz.itao.ink.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author hetao
 * @date 2018-12-10
 * @description
 */
@Repository(value = "contentRepository")
public class ContentRepositoryImpl implements ContentRepository {
    @Autowired
    ContentMapper contentMapper;
    @Autowired
    UserRepository userRepository;
    @Autowired
    DomainFactory domainFactory;

    @Override
    public ContentDomain updateContentDomain(ContentDomain domain) {
        contentMapper.updateByPrimaryKeySelective(domain.entity());
        return domain;
    }

    @Override
    public ContentDomain saveNewContentDomain(ContentDomain domain) {
        contentMapper.insertSelective(domain.entity());
        return domain;
    }

    @Override
    public ContentDomain loadActiveContentDomainById(Long id) {
        ContentDomain domain = domainFactory.createContentDomain().setId(id).setDeleted(false).setActive(true);
        List<Content> contents = contentMapper.selectByNoNulProperties(domain.entity());
        if(contents.isEmpty()){
            return null;
        }

        return domain.assemble(contents.get(0));
    }

    @Override
    public List<ContentDomain> loadAllActiveContentDomain(ContentDomain contentDomain) {
        contentDomain.setDeleted(false).setActive(true);
        List<Content> contents = contentMapper.selectByNoNulProperties(contentDomain.entity());
        return contents.stream().map(e->domainFactory.createContentDomain().assemble(e)).collect(Collectors.toList());
    }

    @Override
    public List<ContentDomain> loadAllFeedArticles() {
        ContentDomain contentDomain = domainFactory.createContentDomain().setAllowFeed(true).setDeleted(false).setActive(true).setStatus(TypeConst.PUBLISH);
        return contentMapper
                .selectByNoNulProperties(contentDomain.entity())
                .stream()
                .map(e->domainFactory.createContentDomain().assemble(e))
                .collect(Collectors.toList());
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
        contentDomain.setActive(false).setDeleted(false);
        return contentMapper
                .selectByNoNulProperties(contentDomain.entity())
                .stream()
                .map(e->domainFactory.createContentDomain().assemble(e))
                .collect(Collectors.toList());
    }

    @Override
    public List<ContentDomain> loadAllContentDomain(ContentDomain contentDomain) {
        contentDomain.setDeleted(false);
        return contentMapper
                .selectByNoNulProperties(contentDomain.entity())
                .stream()
                .map(e->domainFactory.createContentDomain().assemble(e))
                .collect(Collectors.toList());
    }

    @Override
    public List<ContentDomain> loadAllActiveContentDomainByContentIdIn(List<Long> articleIds) {
        List<Content> contents = contentMapper.selectAllContentIn(articleIds, false, true);
        return contents.stream().map(e->domainFactory.createContentDomain().assemble(e)).collect(Collectors.toList());
    }

    @Override
    public List<Archive> loadContentArchives(String type, String status) {
        return contentMapper.selectAllContentArchive(type, status);
    }

    @Override
    public List<ContentDomain> loadAllContentDomainCreatedBetween(String type, String status, Integer start, Integer end) {
        List<Content> contents = contentMapper.selectContentCreatedBetween(type,status,start,end);
        return contents.stream().map(e->domainFactory.createContentDomain().assemble(e)).collect(Collectors.toList());
    }

    @Override
    public Long countContentNum(String type, boolean active) {
        return contentMapper.countContentByTypeAndActive(type, active);
    }

    @Override
    public ContentDomain loadContentDomainById(Long id) {
        return domainFactory.createContentDomain().assemble(contentMapper.selectByPrimaryKey(id));
    }

    @Override
    public List<ContentDomain> searchContentDomain(String keyword, String type) {
        List<Content> contents = contentMapper.searchContents(keyword, type, true);
        return contents
                .stream()
                .map(e->domainFactory.createContentDomain().assemble(e))
                .collect(Collectors.toList());
    }
}
