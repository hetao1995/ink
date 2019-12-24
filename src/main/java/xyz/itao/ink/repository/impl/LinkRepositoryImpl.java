package xyz.itao.ink.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import xyz.itao.ink.dao.LinkMapper;
import xyz.itao.ink.domain.DomainFactory;
import xyz.itao.ink.domain.LinkDomain;
import xyz.itao.ink.domain.entity.Link;
import xyz.itao.ink.repository.LinkRepository;

import java.util.List;
import java.util.stream.Collectors;


/**
 * @author hetao
 * @date 2018-12-11
 */
@Repository("linkRepository")
public class LinkRepositoryImpl implements LinkRepository {

    private final LinkMapper linkMapper;
    private final DomainFactory domainFactory;

    @Autowired
    public LinkRepositoryImpl(LinkMapper linkMapper, DomainFactory domainFactory) {
        this.linkMapper = linkMapper;
        this.domainFactory = domainFactory;
    }

    @Override
    public List<LinkDomain> loadAllActiveLinkDomain() {
        LinkDomain domain = domainFactory.createLinkDomain().setActive(true).setDeleted(false);
        return linkMapper
                .selectByNoNulProperties(domain.entity())
                .stream()
                .map(e -> domainFactory.createLinkDomain().assemble(e))
                .collect(Collectors.toList());
    }

    @Override
    public LinkDomain loadLinkDomainById(Long id) {
        LinkDomain domain = domainFactory.createLinkDomain().setActive(true).setDeleted(false).setId(id);
        List<Link> links = linkMapper.selectByNoNulProperties(domain.entity());
        if (links.isEmpty()) {
            return null;
        }
        return domain.assemble(links.get(0));
    }

    @Override
    public LinkDomain updateLinkDomain(LinkDomain domain) {
        linkMapper.updateByPrimaryKeySelective(domain.entity());
        return domain;
    }

    @Override
    public LinkDomain saveNewLinkDomain(LinkDomain domain) {
        linkMapper.insertSelective(domain.entity());
        return domain;
    }

    @Override
    public Long countLinkNum() {
        return linkMapper.countLink();
    }

}
