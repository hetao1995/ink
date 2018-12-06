package xyz.itao.ink.service;

import com.github.pagehelper.PageInfo;
import xyz.itao.ink.domain.params.CommentParam;
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
    CommentVo deleteCommentById(Long id, UserVo userVo);

    /**
     * 修改commnet
     * @param commentVo
     * @param userVo
     */
    void updateCommentVo(CommentVo commentVo, UserVo userVo);

    /**
     * 发布一条新的评论
     * @param commentVo 评论
     * @param userParam 如果是匿名发布，此param有效
     * @param userVo 如果是登录用户，此uservo有效
     */
    void postNewComment(CommentVo commentVo, UserParam userParam, UserVo userVo);
}
