package xyz.itao.ink.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.itao.ink.constant.TypeConst;
import xyz.itao.ink.domain.*;
import xyz.itao.ink.domain.entity.Archive;
import xyz.itao.ink.domain.params.ArticleParam;
import xyz.itao.ink.domain.vo.ContentVo;
import xyz.itao.ink.repository.ContentRepository;
import xyz.itao.ink.repository.MetaRepository;
import xyz.itao.ink.repository.UserRepository;
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
public class ContentServiceImpl  implements ContentService {
    @Autowired
    private ContentRepository contentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DomainFactory domainFactory;
    @Autowired
    private MetaRepository metaRepository;


    @Override
    public void deleteById(Long id, UserDomain userDomain) {
        domainFactory
                .createContentDomain()
                .setId(id)
                .setUpdateBy(userDomain.getId())
                .deleteById();
    }

    @Override
    public ContentVo loadContentVoById(Long id) {
        return contentRepository.loadActiveContentDomainById(id).vo();
    }

    @Override
    public ContentVo publishNewContent(ContentVo contentVo, UserDomain userDomain) {
        return domainFactory
                .createContentDomain()
                .assemble(contentVo)
                .setUpdateBy(userDomain.getId())
                .setCreateBy(userDomain.getId())
                .save()
                .vo();
    }

    @Override
    public PageInfo<ContentVo> loadAllContentVo(ArticleParam articleParam) {
        ContentDomain contentDomain = domainFactory.createContentDomain().assemble(articleParam);
        Page page = PageHelper.startPage(articleParam.getPageNum(), articleParam.getPageSize(), articleParam.getOrderBy());
        List<ContentDomain> contentDomains = contentRepository.loadAllContentDomain(contentDomain);
        List<ContentVo> contentVos = contentDomains.stream().map(d->d.vo()).collect(Collectors.toList());
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
    public PageInfo<ContentDomain> loadAllActivePublishContentDomain(ArticleParam articleParam) {
        ContentDomain contentDomain = domainFactory.createContentDomain().assemble(articleParam).setActive(true).setStatus(TypeConst.PUBLISH);
        Page page = PageHelper.startPage(articleParam.getPageNum(), articleParam.getPageSize(), articleParam.getOrderBy());
        List<ContentDomain> contentDomains = contentRepository.loadAllContentDomain(contentDomain);
        PageInfo<ContentDomain> pageInfo = new PageInfo<>(page);
        pageInfo.setList(contentDomains);
        return pageInfo;
    }

    @Override
    public void updateContentVo(ContentVo contentVo, UserDomain userDomain) {
        domainFactory
                .createContentDomain()
                .assemble(contentVo)
                .setUpdateBy(userDomain.getId())
                .updateById();
    }

    @Override
    public List<ContentVo> selectAllFeedArticles() {
        List<ContentDomain> contentDomains = contentRepository.loadAllFeedArticles();
        return contentDomains.stream().map(ContentDomain::vo).collect(Collectors.toList());
    }

    @Override
    public void hit(ContentDomain contentDomain) {
        contentRepository.updateHit(contentDomain.getId());
    }

    @Override
    public PageInfo<ContentDomain> getPublishArticlesByMeta(MetaDomain metaDomain, int pageNum, int pageSize) {
        Page page = PageHelper.startPage(pageNum, pageSize);
        List<ContentDomain> contentDomains = metaDomain.getActivePublishArticles();
        PageInfo<ContentDomain> pageInfo = new PageInfo<>(page);
        pageInfo.setList(contentDomains);
        return pageInfo;
    }


    @Override
    public ContentDomain loadActivePublishContentDomainByIdOrSlug(String idOrSlug) {
        ContentDomain contentDomain = domainFactory.createContentDomain().setStatus(TypeConst.PUBLISH);
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
    public ContentDomain loadDraftByIdOrSlug(String idOrSlug, UserDomain userDomain) {
        ContentDomain contentDomain = domainFactory.createContentDomain().setStatus(TypeConst.DRAFT);
        if(PatternUtils.isNumber(idOrSlug)){
            contentDomain.setId(Long.parseLong(idOrSlug));
        }else{
            contentDomain.setSlug(idOrSlug);
        }
        List<ContentDomain> contentDomains = contentRepository.loadAllActiveContentDomain(contentDomain);
        if(contentDomains.isEmpty()){
            return null;
        }
        contentDomain = contentDomains.get(0);
        if(!contentDomain.getAuthorId().equals( userDomain.getId())){
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

    @Override
    public PageInfo<ContentDomain> searchArticles(String keyword, ArticleParam articleParam) {
        Page page = PageHelper.startPage(articleParam.getPageNum(), articleParam.getPageSize(), articleParam.getOrderBy());
        List<ContentDomain> contentDomains = contentRepository.searchContentDomain(keyword, articleParam.getType());
        PageInfo<ContentDomain> pageInfo = new PageInfo<>(page);
        pageInfo.setList(contentDomains);
        return pageInfo;
    }

}
