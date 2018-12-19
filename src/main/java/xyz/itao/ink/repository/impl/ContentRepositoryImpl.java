package xyz.itao.ink.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import xyz.itao.ink.dao.ContentMapper;
import xyz.itao.ink.domain.ContentDomain;
import xyz.itao.ink.domain.entity.Content;
import xyz.itao.ink.repository.AbstractBaseRepository;
import xyz.itao.ink.repository.ContentRepository;
import xyz.itao.ink.service.AbstractBaseService;

import java.util.List;

/**
 * @author hetao
 * @date 2018-12-10
 * @description
 */
@Repository(value = "contentRepository")
public class ContentRepositoryImpl extends AbstractBaseRepository<ContentDomain, Content> implements ContentRepository {
    @Autowired
    ContentMapper contentMapper;
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
        return ContentDomain
                .builder()
                .id(entity.getId())
                .deleted(entity.getDeleted())
                .createTime(entity.getCreateTime())
                .createBy(entity.getCreateBy())
                .updateTime(entity.getUpdateTime())
                .updateBy(entity.getUpdateBy())
                .active(entity.getActive())
                .allowFeed(entity.getAllowFeed())
                .allowPing(entity.getAllowPing())
                .allowComment(entity.getAllowComment())
                .commentsNum(entity.getCommentsNum())
                .type(entity.getType())
                .hits(entity.getHits())
                .status(entity.getStatus())
                .tags(entity.getTags())
                .authorId(entity.getAuthorId())
                .categories(entity.getCategories())
                .slug(entity.getSlug())
                .title(entity.getTitle())
                .content(entity.getContent())
                .created(entity.getCreated())
                .modified(entity.getModified())
                .thumbImg(entity.getThumbImg())
                .fmtType(entity.getFmtType())
                .build();
    }

    @Override
    protected Content doExtract(ContentDomain domain) {
        return Content
                .builder()
                .id(domain.getId())
                .deleted(domain.getDeleted())
                .createTime(domain.getCreateTime())
                .createBy(domain.getCreateBy())
                .updateTime(domain.getUpdateTime())
                .updateBy(domain.getUpdateBy())
                .active(domain.getActive())
                .allowFeed(domain.getAllowFeed())
                .allowPing(domain.getAllowPing())
                .allowComment(domain.getAllowComment())
                .commentsNum(domain.getCommentsNum())
                .type(domain.getType())
                .hits(domain.getHits())
                .status(domain.getStatus())
                .tags(domain.getTags())
                .authorId(domain.getAuthorId())
                .categories(domain.getCategories())
                .slug(domain.getSlug())
                .title(domain.getTitle())
                .content(domain.getContent())
                .created(domain.getCreated())
                .modified(domain.getModified())
                .thumbImg(domain.getThumbImg())
                .fmtType(domain.getFmtType())
                .build();
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
        ContentDomain domain = ContentDomain
                .builder()
                .id(id)
                .build();
        return loadByNoNullPropertiesNotDelect(domain).get(0);
    }

    @Override
    public List<ContentDomain> loadAllActiveContentDomain(ContentDomain contentDomain) {
        return loadByNoNullPropertiesActiveAndNotDelect(contentDomain);
    }

    @Override
    public List<ContentDomain> loadAllFeedArticles() {
        ContentDomain contentDomain = ContentDomain
                .builder()
                .allowFeed(true)
                .build();
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
    public List<ContentDomain> loadAllActiveContentDomainByMetaId(Long metaId) {
        // todo 通过metaId查找contentdomain
        return null;
    }

    @Override
    public List<ContentDomain> loadAllNotActiveContentDomain(ContentDomain contentDomain) {
        return loadByNoNullPropertiesNotActiveAndNotDelect(contentDomain);
    }

    @Override
    public List<ContentDomain> loadAllContentDomain(ContentDomain contentDomain) {
        return loadByNoNullPropertiesNotDelect(contentDomain);
    }
}
