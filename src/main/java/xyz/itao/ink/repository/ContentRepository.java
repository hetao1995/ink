package xyz.itao.ink.repository;

import xyz.itao.ink.domain.ContentDomain;
import xyz.itao.ink.domain.entity.Archive;
import xyz.itao.ink.domain.vo.ContentVo;

import java.util.List;

/**
 * @author hetao
 * @date 2018-12-05
 * @description
 */
public interface ContentRepository {
    /**
     * 更新contentdomain
     * @param domain 需要更新的domain
     * @return 更新后的结果
     */
    ContentDomain updateContentDomain(ContentDomain domain);

    /**
     * 存储一个新的contentdomain对象
     * @param domain 存入的domain
     * @return
     */
    ContentDomain saveNewContentDomain(ContentDomain domain);

    /**
     * 通过id查找active状态的
     * @param id 主键
     * @return 加载的结果
     */
    ContentDomain loadActiveContentDomainById(Long id);

    /**
     * 获取所有的激活状态的content
     * @return
     */
    List<ContentDomain> loadAllActiveContentDomain(ContentDomain contentDomain);

    /**
     * 获取所有allowfeed的文章
     * @return
     */
    List<ContentDomain> loadAllFeedArticles();

    /**
     * 点击时间发生，更新点击次数
     * 首先更新Redis中的数据
     * 到了一定时间后更新数据库中的数据
     * @param id 更新那个内容的hit
     */
    void updateHit(Long id);

    /**
     * 获取到当前的hit，先从缓存中获取，没有的话然后从数据库中获取
     * @return 当前点击数
     */
    Long getHit(Long id);




    List<ContentDomain> loadAllNotActiveContentDomain(ContentDomain contentDomain);

    List<ContentDomain> loadAllContentDomain(ContentDomain contentDomain);

    /**
     * 获取所有contentId为list范围内的active状态的contentDomain
     * @param articleIds id 范围
     * @return
     */
    List<ContentDomain> loadAllActiveContentDomainByContentIdIn(List<Long> articleIds);

    List<Archive> loadContentArchives(String type, String status);

    /**
     * 获取created 在start和end之间的contentDoamin
     * @param type
     * @param status
     * @param start
     * @param end
     * @return
     */
    List<ContentDomain> loadAllContentDomainCreatedBetween(String type, String status, Integer start, Integer end);

    /**
     * 统计content数目
     * @param type 是什么类型的content
     * @param active 是否active
     * @return
     */
    Long countContentNum(String type, boolean active);

    /**
     * 通过id获取ContentDoamin
     * @param id
     * @return
     */
    ContentDomain loadContentDomainById(Long id);

    /**
     * 采用搜索关键字
     * @param keyword
     * @param type
     * @return
     */
    List<ContentDomain> searchContentDomain(String keyword, String type);

    /**
     * 根据created加载下一个已发布的content
     * @param created created
     * @return 下一个，如果没有返回null
     */
    ContentDomain loadNextActivePublishContentDomain(Integer created, String type);

    /**
     * 根据created加载上一个已发布的content
     * @param created created
     * @return 上一个，如果没有返回null
     */
    ContentDomain loadPrevActivePublishContentDomain(Integer created, String type);
}
