package xyz.itao.ink.repository;

import xyz.itao.ink.domain.CommentDomain;
import xyz.itao.ink.domain.vo.CommentVo;

import java.util.List;

/**
 * @author hetao
 * @date 2018-12-10
 * @description
 */
public interface CommentRepository {
    CommentDomain updateCommentDomain(CommentDomain domain);

    List<CommentDomain> loadAllActiveRootCommentDomain();

    boolean deleteCommentDomainById(Long id, Long userId);

    CommentDomain saveNewCommentDomain(CommentDomain domain);

    CommentDomain loadCommentDomainById(Long id);
}
