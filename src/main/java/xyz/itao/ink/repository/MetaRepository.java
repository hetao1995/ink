package xyz.itao.ink.repository;

import xyz.itao.ink.domain.ContentDomain;
import xyz.itao.ink.domain.MetaDomain;
import xyz.itao.ink.domain.entity.ContentMeta;

import java.util.List;

/**
 * @author hetao
 * @date 2018-12-10
 */
public interface MetaRepository {
    /**
     * update
     *
     * @param domain domain
     * @return result
     */
    MetaDomain updateMetaDomain(MetaDomain domain);

    /**
     * save
     *
     * @param domain domain
     * @return result
     */
    MetaDomain saveNewMetaDomain(MetaDomain domain);

    /**
     * 通type和name查找
     *
     * @param type type
     * @param name name
     * @return 结果
     */
    MetaDomain loadMetaDomainByTypeAndName(String type, String name);

    /**
     * 通过type查找
     *
     * @param type type
     * @return 结果
     */
    List<MetaDomain> loadMetaDomainsByType(String type);

    /**
     * 通过metaId在contentMeta中找到所有涉及该meta的文章数目
     *
     * @param id meta的主键
     * @return 数目
     */
    Integer countArticlesByMetaId(Long id);

    /**
     * 通过metaId在contentMeta中找到所有涉及的文章id
     *
     * @param id id
     * @return 文章id
     */
    List<Long> loadAllContentIdByMetaId(Long id);

    /**
     * 通过id加载metaDomain
     *
     * @param metaId 主键
     * @return domain
     */
    MetaDomain loadMetaDomainById(Long metaId);

    /**
     * 通过contentId和Type查找所有的Meta
     *
     * @param contentId content主键
     * @param type      type
     * @return domain
     */
    List<MetaDomain> loadAllMetaDomainByContentIdAndType(Long contentId, String type);

    /**
     * 通过contetId和metaId删除contentMeta
     *
     * @param contentId content主键
     * @param metaId    meta主键
     * @return 是否成功
     */
    boolean deleteContentMetaRelationshipByContentIdAndMetaId(Long contentId, Long metaId);

    /**
     * 通过metaid加载所有激活状态的contentDomain
     *
     * @param id     id
     * @param status status
     * @return 激活状态的domain
     */
    List<ContentDomain> loadAllActiveContentDomainByMetaIdAndStatus(Long id, String status);

    /**
     * 存储contentMeta对应关系
     *
     * @param contentMeta contentMeta
     * @return 是否存储成功
     */
    boolean saveNewContentMeta(ContentMeta contentMeta);

    /**
     * 统计cate对应的meta数目
     *
     * @param type type
     * @return 数目
     */
    Long countMetaNum(String type);
}
