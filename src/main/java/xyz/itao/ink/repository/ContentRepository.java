package xyz.itao.ink.repository;

import xyz.itao.ink.domain.ContentDomain;
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

    List<ContentDomain> loadAllActiveContentDomain();

    List<ContentVo> loadAllFeedArticles();
}
