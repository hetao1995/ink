package xyz.itao.ink.domain.entity;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @author hetao
 * @date 2018-12-02
 * @description comments表的实体类
 */
@Data
@Builder
public class Comment {
    /**
     * 评论的id
     */
    private Long id;

    /**
     * 是否被删除
     */
    private Boolean deleted;

    /**
     * 是否处于激活状态
     */
    private Boolean active;

    /**
     * 品论所属内容
     */
    private Long contentId;

    /**
     * 评论的作者id
     */
    private Long authorId;

    /**
     * 父级评论id
     */
    private Long parentId;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 评论类型
     */
    private String type;

    /**
     * 评论状态
     */
    private String status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 被谁创建
     */
    private Long createBy;

    /**
     * 被谁修改
     */
    private Long updateBy;

}