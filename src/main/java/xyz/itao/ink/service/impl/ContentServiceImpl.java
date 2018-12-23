package xyz.itao.ink.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.itao.ink.constant.TypeConst;
import xyz.itao.ink.domain.ArchiveDomain;
import xyz.itao.ink.domain.ContentDomain;
import xyz.itao.ink.domain.DomainFactory;
import xyz.itao.ink.domain.MetaDomain;
import xyz.itao.ink.domain.entity.Archive;
import xyz.itao.ink.domain.params.ArticleParam;
import xyz.itao.ink.domain.vo.ContentVo;
import xyz.itao.ink.domain.vo.UserVo;
import xyz.itao.ink.repository.ContentRepository;
import xyz.itao.ink.repository.MetaRepository;
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
@Slf4j
public class ContentServiceImpl extends AbstractBaseService<ContentDomain, ContentVo> implements ContentService {
    @Autowired
    private ContentRepository contentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DomainFactory domainFactory;
    @Autowired
    private MetaRepository metaRepository;

    @Override
    protected ContentDomain doAssemble(ContentVo vo) {
        return domainFactory.createContentDomain().assemble(vo);

    }

    @Override
    protected ContentVo doExtract(ContentDomain domain) {
        return domain.vo();

    }

    @Override
    protected ContentDomain doUpdate(ContentDomain domain) {
        return domain.updateById();
    }

    @Override
    protected ContentDomain doSave(ContentDomain domain) {
        return domain.save();
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
        if(TypeConst.PUBLISH.equals(contentVo.getStatus())){
            contentVo.setActive(true);
        }else{
            contentVo.setActive(false);
        }
        return save(contentVo, userVo.getId());
    }

    @Override
    public PageInfo<ContentVo> loadAllContentVo(ArticleParam articleParam) {
        ContentDomain contentDomain = domainFactory.createContentDomain().assemble(articleParam);
        Page page = PageHelper.startPage(articleParam.getPageNum(), articleParam.getPageSize(), articleParam.getOrderBy());
        List<ContentDomain> contentDomains = contentRepository.loadAllContentDomain(contentDomain);
        List<ContentVo> contentVos = contentDomains.stream().map(d->extract(d)).collect(Collectors.toList());
        PageInfo<ContentVo> pageInfo = new PageInfo<>(page);
        pageInfo.setList(contentVos);
        return pageInfo;
    }

    @Override
    public PageInfo<ContentDomain> loadAllContentDomain(ArticleParam articleParam) {
        ContentDomain contentDomain = domainFactory.createContentDomain().assemble(articleParam);
        Page page = PageHelper.startPage(articleParam.getPageNum(), articleParam.getPageSize(), articleParam.getOrderBy());
        List<ContentDomain> contentDomains = contentRepository.loadAllContentDomain(contentDomain);
        PageInfo<ContentDomain> pageInfo = new PageInfo<>(page);
        pageInfo.setList(contentDomains);
        return pageInfo;
    }

    @Override
    public PageInfo<ContentDomain> loadAllActiveContentDomain(ArticleParam articleParam) {
        ContentDomain contentDomain = domainFactory.createContentDomain().assemble(articleParam).setActive(true);
        Page page = PageHelper.startPage(articleParam.getPageNum(), articleParam.getPageSize(), articleParam.getOrderBy());
        List<ContentDomain> contentDomains = contentRepository.loadAllContentDomain(contentDomain);
        PageInfo<ContentDomain> pageInfo = new PageInfo<>(page);
        pageInfo.setList(contentDomains);
        return pageInfo;
    }

    @Override
    public void updateContentVo(ContentVo contentVo, UserVo userVo) {
        if(TypeConst.PUBLISH.equals(contentVo.getStatus())){
            contentVo.setActive(true);
        }else{
            contentVo.setActive(false);
        }
        update(contentVo, userVo.getId());
    }

    @Override
    public List<ContentVo> selectAllFeedArticles() {
        List<ContentDomain> contentDomains = contentRepository.loadAllFeedArticles();
        return contentDomains.stream().map((d)->extract(d)).collect(Collectors.toList());
    }

    @Override
    public void hit(ContentDomain contentDomain) {
        contentRepository.updateHit(contentDomain.getId());
    }

    @Override
    public PageInfo<ContentDomain> getArticlesByMeta(MetaDomain metaDomain, int pageNum, int pageSize) {
        Page page = PageHelper.startPage(pageNum, pageSize);
        List<ContentDomain> contentDomains = metaDomain.getActiveArticles();
        PageInfo<ContentDomain> pageInfo = new PageInfo<>(page);
        pageInfo.setList(contentDomains);
        return pageInfo;
    }


    @Override
    public PageInfo<ContentVo> searchArticles(String keyword, int page, int limit) {
        //todo 通过elasticsearch搜索文章
        return null;
    }

    @Override
    public ContentDomain loadActiveContentDomainByIdOrSlug(String idOrSlug) {
        ContentDomain contentDomain = domainFactory.createContentDomain();
        if(PatternUtils.isNumber(idOrSlug)){
            contentDomain.setId(Long.parseLong(idOrSlug));
        }else{
            contentDomain.setSlug(idOrSlug);
        }
        List<ContentDomain> contentDomains = contentRepository.loadAllActiveContentDomain(contentDomain);
        if(contentDomains.isEmpty()){
            return null;
        }
        return contentDomains.get(0);
    }

    @Override
    public ContentDomain loadDraftByIdOrSlug(String idOrSlug, UserVo userVo) {
        ContentDomain contentDomain = domainFactory.createContentDomain().setStatus(TypeConst.DRAFT);
        if(PatternUtils.isNumber(idOrSlug)){
            contentDomain.setId(Long.parseLong(idOrSlug));
        }else{
            contentDomain.setSlug(idOrSlug);
        }
        List<ContentDomain> contentDomains = contentRepository.loadAllNotActiveContentDomain(contentDomain);
        if(contentDomains.isEmpty()){
            return null;
        }
        contentDomain = contentDomains.get(0);
        if(!contentDomain.getAuthorId().equals( userVo.getId())){
            return null;
        }
        return contentDomain;
    }

    @Override
    public PageInfo<ArchiveDomain> loadContentArchives(ArticleParam articleParam) {
        Page page = PageHelper.startPage(articleParam.getPageNum(), articleParam.getPageSize(),articleParam.getOrderBy());
        List<Archive> archives = contentRepository.loadContentArchives(articleParam.getType(), articleParam.getStatus());
        List<ArchiveDomain> archiveDomains = archives.stream().map(e->domainFactory.createArchiveDomain().assemble(e).setType(articleParam.getType()).setStatus(articleParam.getStatus())).collect(Collectors.toList());
        PageInfo<ArchiveDomain> pageInfo = new PageInfo<>(page);
        pageInfo.setList(archiveDomains);
        return pageInfo;
    }
}
