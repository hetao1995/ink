package xyz.itao.ink.service;

import com.github.pagehelper.PageInfo;
import xyz.itao.ink.domain.ArchiveDomain;
import xyz.itao.ink.domain.ContentDomain;
import xyz.itao.ink.domain.MetaDomain;
import xyz.itao.ink.domain.UserDomain;
import xyz.itao.ink.domain.params.ArticleParam;
import xyz.itao.ink.domain.vo.ContentVo;

import java.util.List;

/**
 * @author hetao
 * @date 2018-12-04
 */
public interface ContentService {
    /**
     * 通过id删除文章
     *
     * @param userDomain userDomain
     * @param id         content的id
     * @return content
     */
    ContentDomain deleteById(Long id, UserDomain userDomain);

    /**
     * 通过id加载ContentVo
     *
     * @param id content的主键
     * @return 加载的结果
     */
    ContentDomain loadContentDomainById(Long id);

    /**
     * 发布一个新的内容
     *
     * @param contentVo 发布的内容
     * @param userVo    vo
     * @return 发布后的domain
     */
    ContentDomain publishNewContent(ContentVo contentVo, UserDomain userVo);

    /**
     * 分页加载所有的contentvo
     *
     * @param articleParam param
     * @return 分页
     */
    PageInfo<ContentVo> loadAllContentVo(ArticleParam articleParam);

    /**
     * 分页加载所有的contentDomain
     *
     * @param articleParam param
     * @return 分页
     */
    PageInfo<ContentDomain> loadAllContentDomain(ArticleParam articleParam);

    /**
     * 分页加载所有的active contentDomain
     *
     * @param articleParam param
     * @return 分页
     */
    PageInfo<ContentDomain> loadAllActivePublishContentDomain(ArticleParam articleParam);

    /**
     * 修改文章
     *
     * @param contentVo  需要修改的文章
     * @param userDomain 修改人
     * @return domain
     */
    ContentDomain updateContentVo(ContentVo contentVo, UserDomain userDomain);

    /**
     * 获取所有的feed页
     *
     * @return contentvo
     */
    List<ContentVo> selectAllFeedArticles();

    /**
     * 点击文章
     *
     * @param contentDomain 文章domain
     */
    void hit(ContentDomain contentDomain);

    /**
     * 通过meta获取所有发布的article
     *
     * @param metaDomain meta
     * @param pageNum    第几页
     * @param pageSize   一页大小
     * @return pageInfo
     */
    PageInfo<ContentDomain> getPublishArticlesByMeta(MetaDomain metaDomain, int pageNum, int pageSize);


    /**
     * 通过id获取名称获取content
     *
     * @param idOrSlug id或者slug
     * @return domain
     */
    ContentDomain loadActivePublishContentDomainByIdOrSlug(String idOrSlug);

    /**
     * 通过id或者slug查找草稿
     *
     * @param idOrSlug   id或者slug
     * @param userDomain userDomain
     * @return content
     */
    ContentDomain loadDraftByIdOrSlug(String idOrSlug, UserDomain userDomain);

    /**
     * 加载所有content的归档
     *
     * @param articleParam param
     * @return page
     */
    PageInfo<ArchiveDomain> loadContentArchives(ArticleParam articleParam);

    /**
     * 搜索所有的文章
     *
     * @param keyword      关键词
     * @param articleParam 文章param
     * @return page
     */
    PageInfo<ContentDomain> searchArticles(String keyword, ArticleParam articleParam);
}
