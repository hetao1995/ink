package xyz.itao.ink.repository;

import xyz.itao.ink.domain.CommentDomain;

import java.util.List;

/**
 * @author hetao
 * @date 2018-12-10
 */
public interface CommentRepository {
    /**
     * update方法
     *
     * @param domain contentDomain
     * @return 修改之后的domain
     */
    CommentDomain updateCommentDomain(CommentDomain domain);

    /**
     * 获取所有active状态的rootcomment
     *
     * @param domain 查询条件
     * @return active的对象
     */
    List<CommentDomain> loadAllActiveRootCommentDomain(CommentDomain domain);

    /**
     * 获取所有的root评论对象
     *
     * @param domain 查询条件
     * @return active对象
     */
    List<CommentDomain> loadAllRootCommentDomain(CommentDomain domain);

    /**
     * 通过contentId查找active的comment
     *
     * @param contentId 文章id
     * @return active对象
     */
    List<CommentDomain> loadAllActiveRootCommentDomainByContentId(Long contentId);

    /**
     * 查询所有root comment
     *
     * @param contentId 文章id
     * @return 所有的root comment
     */
    List<CommentDomain> loadAllRootCommentDomainByContentId(Long contentId);

    /**
     * 查询所有的comment
     *
     * @param contentId 文章id
     * @return 所有comment
     */
    List<CommentDomain> loadAllCommentDomainByContentId(Long contentId);

    /**
     * 通过id删除domain
     *
     * @param id     id
     * @param userId userId
     * @return 是否删除
     */
    boolean deleteCommentDomainById(Long id, Long userId);

    /**
     * 存储新的comment
     *
     * @param domain domain对象
     * @return 存储的comment
     */
    CommentDomain saveNewCommentDomain(CommentDomain domain);

    /**
     * 通过id获取domain
     *
     * @param id 主键
     * @return domain
     */
    CommentDomain loadCommentDomainById(Long id);

    /**
     * 获取所有的根评论
     *
     * @return 根评论
     */
    List<CommentDomain> loadAllRootCommentDomain();

    /**
     * 计算评论数
     *
     * @param active 是否激活
     * @return 评论数
     */
    Long countCommentNum(boolean active);

    /**
     * 获取所有通过的评论
     *
     * @param pid 上一级评论id
     * @return 评论
     */
    List<CommentDomain> loadAllActiveApprovedCommentDomainByParentId(Long pid);

    /**
     * 获取所有的评论
     *
     * @return 所有评论
     */
    List<CommentDomain> loadAllCommentDomain();

    /**
     * 获取所有active的评论
     *
     * @return 评论
     */
    List<CommentDomain> loadAllActiveCommentDomain();

    /**
     * 通过文章id获取通过的评论数
     *
     * @param contentId 文章id
     * @return 文章通过的评论数
     */
    Long countActiveApprovedCommentByContentId(Long contentId);
}
