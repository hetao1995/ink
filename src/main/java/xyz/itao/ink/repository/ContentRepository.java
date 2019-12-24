package xyz.itao.ink.repository;

import xyz.itao.ink.domain.ContentDomain;
import xyz.itao.ink.domain.entity.Archive;

import java.util.List;

/**
 * @author hetao
 * @date 2018-12-05
 */
public interface ContentRepository {
    /**
     * 更新contentdomain
     *
     * @param domain 需要更新的domain
     * @return 更新后的结果
     */
    ContentDomain updateContentDomain(ContentDomain domain);

    /**
     * 存储一个新的contentdomain对象
     *
     * @param domain 存入的domain
     * @return domain
     */
    ContentDomain saveNewContentDomain(ContentDomain domain);

    /**
     * 通过id查找active状态的
     *
     * @param id 主键
     * @return 加载的结果
     */
    ContentDomain loadActiveContentDomainById(Long id);

    /**
     * 获取所有的激活状态的content
     *
     * @param contentDomain 条件
     * @return domain
     */
    List<ContentDomain> loadAllActiveContentDomain(ContentDomain contentDomain);

    /**
     * 获取所有allowfeed的文章
     *
     * @return alowfeed的文章
     */
    List<ContentDomain> loadAllFeedArticles();


    /**
     * 获取到当前的hit，先从缓存中获取，没有的话然后从数据库中获取
     *
     * @param id id
     * @return 当前点击数
     */
    Long getHit(Long id);


    /**
     * 获取所有非active的文章
     *
     * @param contentDomain doamin
     * @return 所有文章
     */
    List<ContentDomain> loadAllNotActiveContentDomain(ContentDomain contentDomain);

    /**
     * 获取所有content
     *
     * @param contentDomain domain
     * @return 所有content
     */
    List<ContentDomain> loadAllContentDomain(ContentDomain contentDomain);

    /**
     * 获取所有contentId为list范围内的active状态的contentDomain
     *
     * @param articleIds id 范围
     * @return content
     */
    List<ContentDomain> loadAllActiveContentDomainByContentIdIn(List<Long> articleIds);

    /**
     * 获取归档
     *
     * @param type   type
     * @param status status
     * @return 归档
     */
    List<Archive> loadContentArchives(String type, String status);

    /**
     * 获取created 在start和end之间的contentDoamin
     *
     * @param type   type
     * @param status status
     * @param start  开始
     * @param end    结束
     * @return 符合条件的content
     */
    List<ContentDomain> loadAllContentDomainCreatedBetween(String type, String status, Integer start, Integer end);

    /**
     * 统计content数目
     *
     * @param type   是什么类型的content
     * @param active 是否active
     * @return 数目
     */
    Long countContentNum(String type, boolean active);

    /**
     * 通过id获取ContentDoamin
     *
     * @param id id
     * @return domain
     */
    ContentDomain loadContentDomainById(Long id);

    /**
     * 采用搜索关键字
     *
     * @param keyword 关键字
     * @param type    type
     * @return domain
     */
    List<ContentDomain> searchContentDomain(String keyword, String type);

    /**
     * 根据created加载下一个已发布的content
     *
     * @param created created
     * @param type    type
     * @return 下一个，如果没有返回null
     */
    ContentDomain loadNextActivePublishContentDomain(Integer created, String type);

    /**
     * 根据created加载上一个已发布的content
     *
     * @param created created
     * @param type    type
     * @return 上一个，如果没有返回null
     */
    ContentDomain loadPrevActivePublishContentDomain(Integer created, String type);
}
