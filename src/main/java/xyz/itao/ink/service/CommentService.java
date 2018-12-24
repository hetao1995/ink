package xyz.itao.ink.service;

import com.github.pagehelper.PageInfo;
import xyz.itao.ink.domain.CommentDomain;
import xyz.itao.ink.domain.ContentDomain;
import xyz.itao.ink.domain.UserDomain;
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
     * @param commentParam
     * @return
     */
    PageInfo<CommentVo> loadAllCommentVo(CommentParam commentParam);


    /**
     * 通过id删除commentg
     * @param id 删除的commnet id
     * @param userVo 操作的用户
     * @return
     */
    void deleteCommentById(Long id, UserDomain userDomain);

    /**
     * 修改commnet
     * @param commentVo
     * @param userVo
     */
    void updateCommentVo(CommentVo commentVo, UserDomain userDomain);

    /**
     * 发布一条新的评论
     * @param commentVo 评论
     * @param userParam 如果是匿名发布，此param有效
     * @param userVo 如果是登录用户，此uservo有效
     */
    UserDomain postNewComment(CommentVo commentVo,  UserDomain userDomain);

    /**
     * 加载所有conentDoamin中所有审核通过的评论
     * @param pageParam
     * @param contentDomain
     * @return
     */
    PageInfo<CommentDomain> loadAllActiveCommentDomain(PageParam pageParam, ContentDomain contentDomain);

    /**
     * 根据commentParam加载所有的CommentDomain
     * @param commentParam
     * @return
     */
    PageInfo<CommentDomain> loadAllActiveCommentDomain(CommentParam commentParam);
}
