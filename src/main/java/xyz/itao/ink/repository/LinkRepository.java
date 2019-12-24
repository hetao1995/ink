package xyz.itao.ink.repository;

import xyz.itao.ink.domain.LinkDomain;

import java.util.List;

/**
 * @author hetao
 * @date 2018-12-11
 */
public interface LinkRepository {
    /**
     * 获取所有active的link
     *
     * @return link文件对象
     */
    List<LinkDomain> loadAllActiveLinkDomain();

    /**
     * 通过id查找
     *
     * @param id id
     * @return 查找结果
     */
    LinkDomain loadLinkDomainById(Long id);

    /**
     * 更新link
     *
     * @param domain domain
     * @return 更新之后的
     */
    LinkDomain updateLinkDomain(LinkDomain domain);

    /**
     * 新建link
     *
     * @param domain link
     * @return 新建的
     */
    LinkDomain saveNewLinkDomain(LinkDomain domain);

    /**
     * 统计link数目
     *
     * @return 数目
     */
    Long countLinkNum();
}
