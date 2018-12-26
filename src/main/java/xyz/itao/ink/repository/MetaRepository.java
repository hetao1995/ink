package xyz.itao.ink.repository;

import xyz.itao.ink.constant.TypeConst;
import xyz.itao.ink.domain.ContentDomain;
import xyz.itao.ink.domain.MetaDomain;
import xyz.itao.ink.domain.entity.ContentMeta;

import java.util.List;

/**
 * @author hetao
 * @date 2018-12-10
 * @description
 */
public interface MetaRepository {
    MetaDomain updateMetaDomain(MetaDomain domain);

    MetaDomain saveNewMetaDomain(MetaDomain domain);

    MetaDomain loadMetaDomainByTypeAndName(String type, String name);

    List<MetaDomain> loadMetaDomainsByType(String type);

    /**
     * 通过metaId在contentMeta中找到所有涉及该meta的文章数目
     * @param id meta的主键
     */
    Integer countArticlesByMetaId(Long id);

    /**
     * 通过metaId在contentMeta中找到所有涉及的文章id
     * @param id
     * @return
     */
    List<Long> loadAllContentIdByMetaId(Long id);

    /**
     * 通过id加载metaDomain
     * @param metaId 主键
     * @return
     */
    MetaDomain loadMetaDomainById(Long metaId);

    /**
     * 通过contentId和Type查找所有的Meta
     * @param id
     * @param type
     * @return
     */
    List<MetaDomain> loadAllMetaDomainByContentIdAndType(Long contentId, String type);

    /**
     * 通过contetId和metaId删除contentMeta
     * @param contentId
     * @param MetaId
     * @return
     */
    boolean deleteContentMetaRelationshipByContentIdAndMetaId(Long contentId, Long metaId);

    /**
     * 通过metaid加载所有激活状态的contentDomain
     * @param id
     * @return
     */
    List<ContentDomain> loadAllActiveContentDomainByMetaIdAndStatus(Long id, String status);

    /**
     * 存储contentMeta对应关系
     * @param contentMeta
     * @return
     */
    boolean saveNewContentMeta(ContentMeta contentMeta);

    /**
     * 统计cate对应的meta数目
     * @param type
     * @return
     */
    Long countMetaNum(String type);
}
