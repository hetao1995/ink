package xyz.itao.ink.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import xyz.itao.ink.constant.WebConstant;
import xyz.itao.ink.dao.CommentMapper;
import xyz.itao.ink.domain.CommentDomain;
import xyz.itao.ink.domain.DomainFactory;
import xyz.itao.ink.domain.entity.Comment;
import xyz.itao.ink.exception.ExceptionEnum;
import xyz.itao.ink.exception.InnerException;
import xyz.itao.ink.repository.CommentRepository;
import xyz.itao.ink.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author hetao
 * @date 2018-12-10
 */
@Repository("commentRepository")
public class CommentRepositoryImpl implements CommentRepository {

    private final CommentMapper commentMapper;
    private final UserRepository userRepository;
    private final DomainFactory domainFactory;

    @Autowired
    public CommentRepositoryImpl(CommentMapper commentMapper, UserRepository userRepository, DomainFactory domainFactory) {
        this.commentMapper = commentMapper;
        this.userRepository = userRepository;
        this.domainFactory = domainFactory;
    }


    @Override
    public CommentDomain updateCommentDomain(CommentDomain domain) {
        commentMapper.updateByPrimaryKeySelective(domain.entity());
        return domain;
    }


    @Override
    public List<CommentDomain> loadAllActiveRootCommentDomain(CommentDomain domain) {
        Comment comment = domain.setParentId(0L).setActive(true).setDeleted(false).entity();
        List<Comment> comments = commentMapper.selectByNoNulProperties(comment);
        return comments.stream().map(e -> domainFactory.createCommentDomain().assemble(e)).collect(Collectors.toList());
    }

    @Override
    public List<CommentDomain> loadAllRootCommentDomain(CommentDomain domain) {
        Comment comment = domainFactory.createCommentDomain().setParentId(0L).entity();
        List<Comment> comments = commentMapper.selectByNoNulProperties(comment);
        return comments.stream().map(e -> domainFactory.createCommentDomain().assemble(e)).collect(Collectors.toList());
    }

    @Override
    public List<CommentDomain> loadAllActiveRootCommentDomainByContentId(Long contentId) {
        return loadAllActiveRootCommentDomain(domainFactory.createCommentDomain().setContentId(contentId));
    }

    @Override
    public List<CommentDomain> loadAllRootCommentDomainByContentId(Long contentId) {
        return loadAllRootCommentDomain(domainFactory.createCommentDomain().setContentId(contentId));
    }

    @Override
    public List<CommentDomain> loadAllCommentDomainByContentId(Long contentId) {
        CommentDomain domain = domainFactory.createCommentDomain().setContentId(contentId).setDeleted(false);
        return commentMapper
                .selectByNoNulProperties(domain.entity())
                .stream()
                .map(e -> domainFactory.createCommentDomain().assemble(e))
                .collect(Collectors.toList());
    }


    @Override
    public boolean deleteCommentDomainById(Long id, Long userId) {
        CommentDomain commentDomain = loadCommentDomainById(id);
        if (commentDomain == null) {
            throw new InnerException(ExceptionEnum.DELETE_NON_EXIST_ELEMENT);
        }
        commentDomain.setDeleted(true).deleteById();
        return true;
    }

    @Override
    public CommentDomain saveNewCommentDomain(CommentDomain domain) {
        commentMapper.insertSelective(domain.entity());
        return domain;
    }

    @Override
    public CommentDomain loadCommentDomainById(Long id) {
        CommentDomain domain = domainFactory.createCommentDomain().setDeleted(false).setId(id);
        List<Comment> comments = commentMapper.selectByNoNulProperties(domain.entity());
        if (comments.isEmpty()) {
            return null;
        }

        return domainFactory.createCommentDomain().assemble(comments.get(0));
    }

    @Override
    public List<CommentDomain> loadAllRootCommentDomain() {
        CommentDomain domain = domainFactory.createCommentDomain().setDeleted(false).setParentId(0L);
        List<Comment> comments = commentMapper.selectByNoNulProperties(domain.entity());
        return comments.stream().map(e -> domainFactory.createCommentDomain().assemble(e)).collect(Collectors.toList());
    }

    @Override
    public Long countCommentNum(boolean active) {
        return commentMapper.countCommentByActive(active);
    }

    @Override
    public List<CommentDomain> loadAllActiveApprovedCommentDomainByParentId(Long pid) {
        CommentDomain domain = domainFactory.createCommentDomain().setParentId(pid).setDeleted(false).setActive(true).setStatus(WebConstant.COMMENT_APPROVED);
        return commentMapper
                .selectByNoNulProperties(domain.entity())
                .stream()
                .map(e -> domainFactory.createCommentDomain().assemble(e))
                .collect(Collectors.toList());

    }

    @Override
    public List<CommentDomain> loadAllCommentDomain() {
        return commentMapper
                .selectByNoNulProperties(domainFactory.createCommentDomain().setDeleted(false).entity())
                .stream()
                .map(e -> domainFactory.createCommentDomain().assemble(e))
                .collect(Collectors.toList());
    }

    @Override
    public List<CommentDomain> loadAllActiveCommentDomain() {
        return commentMapper
                .selectByNoNulProperties(domainFactory.createCommentDomain().setDeleted(false).setActive(true).entity())
                .stream()
                .map(e -> domainFactory.createCommentDomain().assemble(e))
                .collect(Collectors.toList());
    }

    @Override
    public Long countActiveApprovedCommentByContentId(Long contentId) {
        return commentMapper.countCommentByActiveStatusAndContentId(true, WebConstant.COMMENT_APPROVED, contentId);
    }

}
