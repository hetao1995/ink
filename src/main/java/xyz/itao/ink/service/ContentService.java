package xyz.itao.ink.service;

import xyz.itao.ink.domain.ContentDomain;

/**
 * @author hetao
 * @date 2018-12-04
 * @description
 */
public interface ContentService {
    /**
     * 通过id删除文章
     * @param id content的id
     * @return 是否删除成功
     */
    boolean deleteById(Long id);

    /**
     * 通过id加载ContentDomain
     * @param id content的主键
     * @return 加载的结果
     */
    ContentDomain loadContentDomainById(String id);
}
