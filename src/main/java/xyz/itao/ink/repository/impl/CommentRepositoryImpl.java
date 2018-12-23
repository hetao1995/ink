package xyz.itao.ink.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import xyz.itao.ink.dao.CommentMapper;
import xyz.itao.ink.domain.BaseDomain;
import xyz.itao.ink.domain.CommentDomain;
import xyz.itao.ink.domain.ContentDomain;
import xyz.itao.ink.domain.DomainFactory;
import xyz.itao.ink.domain.entity.Comment;
import xyz.itao.ink.domain.vo.CommentVo;
import xyz.itao.ink.exception.ExceptionEnum;
import xyz.itao.ink.exception.InnerException;
import xyz.itao.ink.repository.AbstractBaseRepository;
import xyz.itao.ink.repository.CommentRepository;
import xyz.itao.ink.repository.UserRepository;

import java.util.List;

/**
 * @author hetao
 * @date 2018-12-10
 * @description
 */
@Repository("commentRepository")
public class CommentRepositoryImpl extends AbstractBaseRepository<CommentDomain, Comment> implements CommentRepository {

    @Autowired
    CommentMapper commentMapper;
    @Autowired
    UserRepository userRepository;
    @Autowired
    DomainFactory domainFactory;

    @Override
    protected boolean doSave(Comment entity) {
        return commentMapper.insertSelective(entity);
    }

    @Override
    protected List<Comment> doLoadByNoNullProperties(Comment entity) {
        return commentMapper.selectByNoNulProperties(entity);
    }

    @Override
    protected boolean doUpdate(Comment entity) {
        return commentMapper.updateByPrimaryKeySelective(entity);
    }

    @Override
    protected CommentDomain doAssemble(Comment entity) {
        return domainFactory.createCommentDomain().assemble(entity);
    }

    @Override
    protected Comment doExtract(CommentDomain domain) {
        return domain.entity();
    }

    @Override
    public CommentDomain updateCommentDomain(CommentDomain domain) {
        return update(domain);
    }



    @Override
    public List<CommentDomain> loadAllActiveRootCommentDomain(CommentDomain domain) {
        domain.setParentId(0L);
        return loadByNoNullPropertiesActiveAndNotDelect(domain);
    }

    @Override
    public List<CommentDomain> loadAllRootCommentDomain(CommentDomain domain) {
        domain.setParentId(0L);
        return loadByNoNullPropertiesNotDelect(domain);
    }

    @Override
    public List<CommentDomain> loadAllActiveRootCommentDomainByContentId(Long contentId) {
        return loadAllActiveRootCommentDomain(domainFactory.createCommentDomain().setContentId(contentId));
    }


    @Override
    public boolean deleteCommentDomainById(Long id, Long userId) {
        CommentDomain commentDomain = loadCommentDomainById(id);
        if(commentDomain==null) {
            throw new InnerException(ExceptionEnum.DELETE_NON_EXIST_ELEMENT);
        }
        commentDomain.setDeleted(true);
        return update(commentDomain)==null;
    }

    @Override
    public CommentDomain saveNewCommentDomain(CommentDomain domain) {
        return save(domain);
    }

    @Override
    public CommentDomain loadCommentDomainById(Long id) {
         List<CommentDomain> commentDomains = loadByNoNullPropertiesNotDelect(domainFactory.createCommentDomain().setId(id));
         if(commentDomains.isEmpty()){
             return null;
         }
         return commentDomains.get(0);
    }

    @Override
    public List<CommentDomain> loadAllRootCommentDomain() {
        return loadByNoNullPropertiesNotDelect(domainFactory.createCommentDomain());
    }

}
