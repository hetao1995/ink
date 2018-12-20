package xyz.itao.ink.repository;

import xyz.itao.ink.domain.MetaDomain;

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
}
