package xyz.itao.ink.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.itao.ink.constant.TypeConst;
import xyz.itao.ink.constant.WebConstant;
import xyz.itao.ink.dao.ContentMapper;
import xyz.itao.ink.domain.ContentDomain;
import xyz.itao.ink.domain.params.ArticleParam;
import xyz.itao.ink.domain.vo.ContentVo;
import xyz.itao.ink.domain.vo.UserVo;
import xyz.itao.ink.repository.ContentRepository;
import xyz.itao.ink.repository.UserRepository;
import xyz.itao.ink.service.AbstractBaseService;
import xyz.itao.ink.service.ContentService;
import xyz.itao.ink.utils.PatternUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author hetao
 * @date 2018-12-05
 * @description
 */
@Service("contentService")
public class ContentServiceImpl extends AbstractBaseService<ContentDomain, ContentVo> implements ContentService {
    @Autowired
    private ContentRepository contentRepository;
    @Autowired
    private UserRepository userRepository;

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
                .created(vo.getCreated())
                .modified(vo.getModified())
                .thumbImg(vo.getThumbImg())
                .fmtType(vo.getFmtType())
                .author(vo.getAuthor())
                .mail(vo.getMail())
                .url(vo.getUrl())
                .userRepository(userRepository)
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
                .created(domain.getCreated())
                .modified(domain.getModified())
                .thumbImg(domain.getThumbImg())
                .fmtType(domain.getFmtType())
                .author(domain.getAuthor())
                .mail(domain.getMail())
                .url(domain.getUrl())
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

    @Override
    public ContentVo publishNewContent(ContentVo contentVo, UserVo userVo) {
        contentVo.setActive(true);
        return save(contentVo, userVo.getId());
    }

    @Override
    public PageInfo<ContentVo> loadAllContentVo(ArticleParam articleParam) {
        ContentDomain contentDomain = ContentDomain
                .builder()
                .status(articleParam.getStatus())
                .type(articleParam.getType())
                .categories(articleParam.getCategories())
                .title(articleParam.getTitle())
                .build();
        PageHelper.startPage(articleParam.getPageNum(), articleParam.getPageSize());
        List<ContentDomain> contentDomains = contentRepository.loadAllContentDomain(contentDomain);
        List<ContentVo> contentVos = contentDomains.stream().map(content-> extract(content)).collect(Collectors.toList());
        return new PageInfo<>(contentVos);
    }

    @Override
    public void updateArticle(ContentVo contentVo, UserVo userVo) {
        update(contentVo, userVo.getId());
    }

    @Override
    public List<ContentVo> selectAllFeedArticles() {
        List<ContentDomain> contentDomains = contentRepository.loadAllFeedArticles();
        return contentDomains.stream().map((d)->extract(d)).collect(Collectors.toList());
    }

    @Override
    public void hit(ContentVo contentVo) {
        contentRepository.updateHit(contentVo.getId());
    }

    @Override
    public PageInfo<ContentVo> getArticles(Long metaId, int page, int limit) {
        PageHelper.startPage(page, limit);
        List<ContentDomain> contentDomains = contentRepository.loadAllActiveContentDomainByMetaId(metaId);
        List<ContentVo> contentVos = contentDomains.stream().map((d)->extract(d)).collect(Collectors.toList());
        return new PageInfo<>(contentVos);
    }

    @Override
    public PageInfo<ContentVo> searchArticles(String keyword, int page, int limit) {
        //todo 通过elasticsearch搜索文章
        return null;
    }

    @Override
    public ContentVo loadActiveContentVoByIdOrSlug(String idOrSlug) {
        ContentDomain contentDomain = ContentDomain.builder().build();
        if(PatternUtils.isNumber(idOrSlug)){
            contentDomain.setId(Long.parseLong(idOrSlug));
        }else{
            contentDomain.setSlug(idOrSlug);
        }
        List<ContentDomain> contentDomains = contentRepository.loadAllActiveContentDomain(contentDomain);
        if(contentDomains.isEmpty()){
            return null;
        }
        return extract(contentDomains.get(0));
    }

    @Override
    public ContentVo loadDraftByIdOrSlug(String idOrSlug, UserVo userVo) {
        ContentDomain contentDomain = ContentDomain.builder().type(TypeConst.DRAFT).build();
        if(PatternUtils.isNumber(idOrSlug)){
            contentDomain.setId(Long.parseLong(idOrSlug));
        }else{
            contentDomain.setSlug(idOrSlug);
        }
        List<ContentDomain> contentDomains = contentRepository.loadAllNotActiveContentDomain(contentDomain);
        if(contentDomains == null){
            return null;
        }
        contentDomain = contentDomains.get(0);
        if(contentDomain.getAuthorId() != userVo.getId()){
            return null;
        }
        return extract(contentDomain);
    }
}
