package xyz.itao.ink.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import xyz.itao.ink.annotation.CacheRemove;
import xyz.itao.ink.constant.TypeConst;
import xyz.itao.ink.constant.WebConstant;
import xyz.itao.ink.domain.*;
import xyz.itao.ink.domain.entity.Archive;
import xyz.itao.ink.domain.params.ArticleParam;
import xyz.itao.ink.domain.vo.ContentVo;
import xyz.itao.ink.repository.ContentRepository;
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
@CacheConfig(cacheNames = WebConstant.CONTENT_CACHE)
public class ContentServiceImpl  implements ContentService {
    @Autowired
    private ContentRepository contentRepository;
    @Autowired
    private DomainFactory domainFactory;


    @Override
    @CacheEvict(key = "#id")
    public void deleteById(Long id, UserDomain userDomain) {
        domainFactory
                .createContentDomain()
                .setId(id)
                .setUpdateBy(userDomain.getId())
                .deleteById();
    }

    @Override
    @Cacheable(key="#id")
    public ContentDomain loadContentDomainById(Long id) {
        return contentRepository.loadActiveContentDomainById(id);
    }

    @Override
    @CachePut(key="#result.id")
    @CacheRemove(value = WebConstant.CONTENT_CACHE, key = "'type_'+#contentVo.type+'*'")
    public ContentDomain publishNewContent(ContentVo contentVo, UserDomain userDomain) {
        return domainFactory
                .createContentDomain()
                .assemble(contentVo)
                .setUpdateBy(userDomain.getId())
                .setCreateBy(userDomain.getId())
                .save();
    }

    @Override
    @Cacheable(key="'type_'+#articleParam.type+'_num_'+#articleParam.pageNum+'_size_'+#articleParam.pageSize")
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
    @Cacheable(key="'type_'+#articleParam.type+'_num_'+#articleParam.pageNum+'_size_'+#articleParam.pageSize")
    public PageInfo<ContentDomain> loadAllContentDomain(ArticleParam articleParam) {
        ContentDomain contentDomain = domainFactory.createContentDomain().assemble(articleParam);
        Page page = PageHelper.startPage(articleParam.getPageNum(), articleParam.getPageSize(), articleParam.getOrderBy());
        List<ContentDomain> contentDomains = contentRepository.loadAllContentDomain(contentDomain);
        PageInfo<ContentDomain> pageInfo = new PageInfo<>(page);
        pageInfo.setList(contentDomains);
        return pageInfo;
    }

    @Override
    @Cacheable(key="'type_'+#articleParam.type+'_num_'+#articleParam.pageNum+'_size_'+#articleParam.pageSize+'publish'")
    public PageInfo<ContentDomain> loadAllActivePublishContentDomain(ArticleParam articleParam) {
        ContentDomain contentDomain = domainFactory.createContentDomain().assemble(articleParam).setActive(true).setStatus(TypeConst.PUBLISH).setType(articleParam.getType());
        Page page = PageHelper.startPage(articleParam.getPageNum(), articleParam.getPageSize(), articleParam.getOrderBy());
        List<ContentDomain> contentDomains = contentRepository.loadAllContentDomain(contentDomain);
        PageInfo<ContentDomain> pageInfo = new PageInfo<>(page);
        pageInfo.setList(contentDomains);
        return pageInfo;
    }

    @Override
    @CachePut(key = "#contentVo.id")
    public ContentDomain updateContentVo(ContentVo contentVo, UserDomain userDomain) {
        return domainFactory
                .createContentDomain()
                .assemble(contentVo)
                .setUpdateBy(userDomain.getId())
                .updateById();
    }

    @Override
    @Cacheable(key="'type_post_feed'")
    public List<ContentVo> selectAllFeedArticles() {
        List<ContentDomain> contentDomains = contentRepository.loadAllFeedArticles();
        return contentDomains.stream().map(ContentDomain::vo).collect(Collectors.toList());
    }

    @Override
    public void hit(ContentDomain contentDomain) {
        contentDomain.hit();
    }

    @Override
    @Cacheable(value = WebConstant.META_CACHE, key = "#metaDomain.id+'_article_num_'+#pageNum+'_size_'+#pageSize")
    public PageInfo<ContentDomain> getPublishArticlesByMeta(MetaDomain metaDomain, int pageNum, int pageSize) {
        Page page = PageHelper.startPage(pageNum, pageSize);
        List<ContentDomain> contentDomains = metaDomain.getActivePublishArticles();
        PageInfo<ContentDomain> pageInfo = new PageInfo<>(page);
        pageInfo.setList(contentDomains);
        return pageInfo;
    }


    @Override
    @Cacheable(key = "#idOrSlug")
    public ContentDomain loadActivePublishContentDomainByIdOrSlug(String idOrSlug) {
        return loadContentDomainByIdOrSlug(idOrSlug, TypeConst.PUBLISH);
    }

    @Override
    @Cacheable(key = "#idOrSlug")
    public ContentDomain loadDraftByIdOrSlug(String idOrSlug, UserDomain userDomain) {

        ContentDomain contentDomain = loadContentDomainByIdOrSlug(idOrSlug, TypeConst.DRAFT);
        if(contentDomain==null || !contentDomain.getAuthorId().equals( userDomain.getId())){
            return null;
        }
        return contentDomain;
    }


    private ContentDomain loadContentDomainByIdOrSlug(String idOrSlug, String status){
        ContentDomain contentDomain = domainFactory.createContentDomain().setStatus(status);
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
    @Cacheable(key="'type_'+#articleParam.type+'_num_'+#articleParam.pageNum+'_size_'+#articleParam.pageSize+'archive'")
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
