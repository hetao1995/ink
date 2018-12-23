package xyz.itao.ink.repository;

import xyz.itao.ink.domain.CommentDomain;
import xyz.itao.ink.domain.ContentDomain;
import xyz.itao.ink.domain.entity.Comment;
import xyz.itao.ink.domain.vo.CommentVo;

import java.util.List;

/**
 * @author hetao
 * @date 2018-12-10
 * @description
 */
public interface CommentRepository {
    CommentDomain updateCommentDomain(CommentDomain domain);

    List<CommentDomain> loadAllActiveRootCommentDomain(CommentDomain domain);

    List<CommentDomain> loadAllRootCommentDomain(CommentDomain domain);

    List<CommentDomain> loadAllActiveRootCommentDomainByContentId(Long id);

    boolean deleteCommentDomainById(Long id, Long userId);

    CommentDomain saveNewCommentDomain(CommentDomain domain);

    CommentDomain loadCommentDomainById(Long id);

    List<CommentDomain> loadAllRootCommentDomain();

}
