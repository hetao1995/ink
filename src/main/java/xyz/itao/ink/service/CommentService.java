package xyz.itao.ink.service;

import com.github.pagehelper.PageInfo;
import xyz.itao.ink.domain.CommentDomain;
import xyz.itao.ink.domain.ContentDomain;
import xyz.itao.ink.domain.UserDomain;
import xyz.itao.ink.domain.params.ArticleParam;
import xyz.itao.ink.domain.params.CommentParam;
import xyz.itao.ink.domain.params.PageParam;
import xyz.itao.ink.domain.params.UserParam;
import xyz.itao.ink.domain.vo.CommentVo;
import xyz.itao.ink.domain.vo.UserVo;

/**
 * @author hetao
 * @date 2018-12-05
 * @description
 */
public interface CommentService {
    /**
     * 加载所有的commnet
     * @param commentParam param
     * @return 分页的commentVo
     */
    PageInfo<CommentVo> loadAllActiveCommentVo(CommentParam commentParam);


    /**
     * 通过id删除commentg
     * @param id 删除的commnet id
     * @param userDomain 操作的用户
     * @return
     */
    CommentDomain deleteCommentById(Long id, UserDomain userDomain);

    /**
     * 修改commnet
     * @param commentVo comment
     * @param userDomain 操作的用户
     */
    void updateCommentVo(CommentVo commentVo, UserDomain userDomain);

    /**
     * 发布一条新的评论
     * @param commentVo 评论
     * @param userDomain 登录用户
     */
    UserDomain postNewComment(CommentVo commentVo,  UserDomain userDomain);

    /**
     * 加载所有conentDoamin中所有审核通过的评论
     * @param pageParam param
     * @param contentDomain contents
     * @return 分页
     */
    PageInfo<CommentDomain> loadAllActiveCommentDomain(PageParam pageParam, ContentDomain contentDomain);

    /**
     * 根据commentParam加载所有的CommentDomain
     * @param commentParam param
     * @return 分页
     */
    PageInfo<CommentDomain> loadAllActiveApprovedCommentDomain(CommentParam commentParam);
}
