package xyz.itao.ink.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.itao.ink.dao.ContentMapper;
import xyz.itao.ink.domain.ContentDomain;
import xyz.itao.ink.domain.vo.ContentVo;
import xyz.itao.ink.domain.vo.UserVo;
import xyz.itao.ink.repository.ContentRepository;
import xyz.itao.ink.service.AbstractBaseService;
import xyz.itao.ink.service.ContentService;

/**
 * @author hetao
 * @date 2018-12-05
 * @description
 */
@Service("contentService")
public class ContentServiceImpl extends AbstractBaseService<ContentDomain, ContentVo> implements ContentService {
    @Autowired
    private ContentRepository contentRepository;

    @Override
    protected ContentDomain doAssemble(ContentVo vo) {
        return ContentDomain
                .builder()
                .id(vo.getId())
                .active(vo.getActive())
                .title(vo.getTitle())
                .slug(vo.getSlug())
                .categories(vo.getCategories())
                .authorId(vo.getAuthorId())
                .content(vo.getContent())
                .tags(vo.getTags())
                .status(vo.getStatus())
                .type(vo.getType())
                .hits(vo.getHits())
                .allowComment(vo.getAllowComment())
                .commentsNum(vo.getCommentsNum())
                .allowFeed(vo.getAllowFeed())
                .allowPing(vo.getAllowPing())
                .build();
    }

    @Override
    protected ContentVo doExtract(ContentDomain domain) {
        return ContentVo
                .builder()
                .id(domain.getId())
                .active(domain.getActive())
                .title(domain.getTitle())
                .slug(domain.getSlug())
                .categories(domain.getCategories())
                .authorId(domain.getAuthorId())
                .content(domain.getContent())
                .tags(domain.getTags())
                .status(domain.getStatus())
                .type(domain.getType())
                .hits(domain.getHits())
                .allowComment(domain.getAllowComment())
                .commentsNum(domain.getCommentsNum())
                .allowFeed(domain.getAllowFeed())
                .allowPing(domain.getAllowPing())
                .build();

    }

    @Override
    protected ContentDomain doUpdate(ContentDomain domain) {
        return contentRepository.updateContentDomain(domain);
    }

    @Override
    protected ContentDomain doSave(ContentDomain domain) {
        return contentRepository.saveNewContentDomain(domain);
    }

    @Override
    public boolean deleteById(Long id, UserVo userVo) {
        return delete(ContentVo.builder().id(id).build(), userVo.getId());
    }

    @Override
    public ContentVo loadContentVoById(Long id) {
        ContentDomain contentDomain =  contentRepository.loadActiveContentDomainById(id);
        return extract(contentDomain);
    }
}
