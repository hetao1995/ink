package xyz.itao.ink.service;

import com.github.pagehelper.PageInfo;
import xyz.itao.ink.domain.ContentDomain;
import xyz.itao.ink.domain.MetaDomain;
import xyz.itao.ink.domain.UserDomain;
import xyz.itao.ink.domain.params.MetaParam;
import xyz.itao.ink.domain.vo.MetaVo;

import java.util.List;
import java.util.Map;

/**
 * @author hetao
 * @date 2018-12-05
 */
public interface MetaService {
    /**
     * 设置meta数据
     *
     * @param type       类型
     * @param metaParam  param
     * @param userDomain 操作人
     * @return domain
     */

    MetaDomain saveMeta(String type, MetaParam metaParam, UserDomain userDomain);

    /**
     * 通过id删除meta
     *
     * @param id         id
     * @param userDomain 操作人
     * @return domain
     */
    MetaDomain deleteMetaById(Long id, UserDomain userDomain);


    /**
     * 通过type和name获取meta数据
     *
     * @param type type
     * @param name name
     * @return domain
     */
    MetaDomain getMetaDomainByTypeAndName(String type, String name);

    /**
     * 根据type获取meta
     *
     * @param type type
     * @return metavo
     */
    List<MetaVo> getMetasByType(String type);

    /**
     * 根据metaId获取articles
     *
     * @param metaId   id
     * @param pageNum  页数
     * @param pageSize 一页大小
     * @return page
     */
    PageInfo<ContentDomain> getArticlesByMetaId(Long metaId, int pageNum, int pageSize);

    /**
     * 更新category
     *
     * @param id         id
     * @param metaParam  meta
     * @param userDomain 操作人
     * @return domain
     */
    MetaDomain updateCategory(Long id, MetaParam metaParam, UserDomain userDomain);

    /**
     * 获取meta、content映射
     *
     * @param type type
     * @return 映射
     */
    Map<String, List<ContentDomain>> getMetaMapping(String type);
}
