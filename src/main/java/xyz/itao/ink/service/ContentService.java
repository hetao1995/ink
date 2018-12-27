package xyz.itao.ink.service;

import com.github.pagehelper.PageInfo;
import xyz.itao.ink.domain.ArchiveDomain;
import xyz.itao.ink.domain.ContentDomain;
import xyz.itao.ink.domain.MetaDomain;
import xyz.itao.ink.domain.UserDomain;
import xyz.itao.ink.domain.entity.Archive;
import xyz.itao.ink.domain.params.ArticleParam;
import xyz.itao.ink.domain.vo.ContentVo;
import xyz.itao.ink.domain.vo.UserVo;

import java.util.List;

/**
 * @author hetao
 * @date 2018-12-04
 * @description
 */
public interface ContentService {
    /**
     * 通过id删除文章
     * @param id content的id
     */
    void deleteById(Long id, UserDomain userDomain);

    /**
     * 通过id加载ContentVo
     * @param id content的主键
     * @return 加载的结果
     */
    ContentDomain loadContentDomainById(Long id);

    /**
     * 发布一个新的内容
     * @param contentVo 发布的内容
     * @return  发布后的domain
     */
    ContentDomain publishNewContent(ContentVo contentVo, UserDomain userVo);

    /**
     * 分页加载所有的contentvo
     * @return 分页
     */
    PageInfo<ContentVo> loadAllContentVo(ArticleParam articleParam);

    /**
     * 分页加载所有的contentDomain
     * @param articleParam
     * @return 分页
     */
    PageInfo<ContentDomain> loadAllContentDomain(ArticleParam articleParam);

    /**
     * 分页加载所有的active contentDomain
     * @param articleParam param
     * @return 分页
     */
    PageInfo<ContentDomain> loadAllActivePublishContentDomain(ArticleParam articleParam);

    /**
     * 修改文章
     * @param contentVo 需要修改的文章
     * @param userDomain 修改人
     */
    ContentDomain updateContentVo(ContentVo contentVo, UserDomain userDomain);

    /**
     * 获取所有的feed页
     * @return contentvo
     */
    List<ContentVo> selectAllFeedArticles();

    void hit(ContentDomain contentDomain);

    PageInfo<ContentDomain> getPublishArticlesByMeta(MetaDomain metaDomain, int pageNum, int pageSize);


    ContentDomain loadActivePublishContentDomainByIdOrSlug(String idOrSlug);

    /**
     * 通过id或者slug查找草稿
     * @param idOrSlug id或者slug
     * @param userDomain userDomain
     * @return content
     */
    ContentDomain loadDraftByIdOrSlug(String idOrSlug, UserDomain userDomain);

    /**
     * 加载所有content的归档
     * @param articleParam param
     * @return page
     */
    PageInfo<ArchiveDomain> loadContentArchives(ArticleParam articleParam);

    /**
     * 搜索所有的文章
     * @param keyword 关键词
     * @param articleParam 文章param
     * @return page
     */
    PageInfo<ContentDomain> searchArticles(String keyword, ArticleParam articleParam);
}
