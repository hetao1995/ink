package xyz.itao.ink.domain.entity;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @author hetao
 * @date 2018-12-02
 * @description contents表的实体类
 */
@Data
@Builder
public class Content {
    /**
     * 内容的id
     */
    private Long id;

    /**
     * 作者的id
     */
    private Long authorId;

    /**
     * 是否被删除
     */
    private Boolean deleted;

    /**
     * 内容标题
     */
    private String title;

    /**
     * 内容缩略名
     */
    private String slug;

    /**
     * 内容类别
     */
    private String type;

    /**
     * 内容状态
     */
    private String status;

    /**
     * 标签列表
     */
    private String tags;

    /**
     * 分类列表
     */
    private String categories;

    /**
     * 点击次数
     */
    private Long hits;

    /**
     * 是否允许评论
     */
    private Boolean allowComment;

    /**
     * 内容所属评论数目
     */
    private Long commentsNum;

    /**
     * 是否允许ping
     */
    private Boolean allowPing;

    /**
     * 是否允许出现在聚合中
     */
    private Boolean allowFeed;

    /**
     * 内容文字
     */
    private String content;

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