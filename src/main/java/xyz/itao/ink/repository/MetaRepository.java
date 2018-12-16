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
}
