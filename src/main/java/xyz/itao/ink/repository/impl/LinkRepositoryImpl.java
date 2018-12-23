package xyz.itao.ink.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import xyz.itao.ink.dao.LinkMapper;
import xyz.itao.ink.domain.LinkDomain;
import xyz.itao.ink.domain.entity.Link;
import xyz.itao.ink.repository.AbstractBaseRepository;
import xyz.itao.ink.repository.LinkRepository;

import java.util.List;


/**
 * @author hetao
 * @date 2018-12-11
 * @description
 */
@Repository("linkRepository")
public class LinkRepositoryImpl extends AbstractBaseRepository<LinkDomain, Link> implements LinkRepository {

    @Autowired
    LinkMapper linkMapper;
    @Override
    public List<LinkDomain> loadAllActiveLinkDomain() {
        return loadByNoNullPropertiesActiveAndNotDelect(LinkDomain.builder().build());
    }

    @Override
    public LinkDomain loadLinkDomainById(Long id) {
        return loadByNoNullPropertiesActiveAndNotDelect(LinkDomain.builder().id(id).build()).get(0);
    }

    @Override
    public LinkDomain updateLinkDomain(LinkDomain domain) {
        return update(domain);
    }

    @Override
    public LinkDomain saveNewLinkDomain(LinkDomain domain) {
        return save(domain);
    }

    @Override
    public Long countLinkNum() {
        return linkMapper.countLink();
    }

    @Override
    protected boolean doSave(Link entity) {
        return linkMapper.insertSelective(entity);
    }

    @Override
    protected List<Link> doLoadByNoNullProperties(Link entity) {
        return linkMapper.selectByNoNulProperties(entity);
    }

    @Override
    protected boolean doUpdate(Link entity) {
        return linkMapper.updateByPrimaryKeySelective(entity);
    }

    @Override
    protected LinkDomain doAssemble(Link entity) {
        return LinkDomain
                .builder()
                .id(entity.getId())
                .deleted(entity.getDeleted())
                .createTime(entity.getCreateTime())
                .createBy(entity.getCreateBy())
                .updateTime(entity.getUpdateTime())
                .updateBy(entity.getUpdateBy())
                .active(entity.getActive())
                .authorId(entity.getAuthorId())
                .fileType(entity.getFileType())
                .fileName(entity.getFileName())
                .fileKey(entity.getFileKey())
                .build();
    }

    @Override
    protected Link doExtract(LinkDomain domain) {
        return Link
                .builder()
                .id(domain.getId())
                .deleted(domain.getDeleted())
                .createTime(domain.getCreateTime())
                .createBy(domain.getCreateBy())
                .updateTime(domain.getUpdateTime())
                .updateBy(domain.getUpdateBy())
                .active(domain.getActive())
                .authorId(domain.getAuthorId())
                .fileType(domain.getFileType())
                .fileName(domain.getFileName())
                .fileKey(domain.getFileKey())
                .build();
    }
}
