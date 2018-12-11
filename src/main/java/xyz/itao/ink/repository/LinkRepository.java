package xyz.itao.ink.repository;

import xyz.itao.ink.domain.LinkDomain;

import java.util.List;

/**
 * @author hetao
 * @date 2018-12-11
 * @description
 */
public interface LinkRepository {
    List<LinkDomain> loadAllActiveLinkDomain();

    LinkDomain loadLinkDomainById(Long id);

    LinkDomain updateLinkDomain(LinkDomain domain);

    LinkDomain saveNewLinkDomain(LinkDomain domain);
}
