package xyz.itao.ink.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author hetao
 * @date 2018-12-05
 * @description
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContentVo {
    /**
     * 内容的id
     */
    private Long id;

    /**
     * 作者的id
     */
    private Long authorId;


    /**
     * 是否处于激活状态
     */
    private Boolean active;

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
     * 缩略图的地址
     */
    private String thumbImg;

    /**
     * 是那种格式的，Markdown或者html
     */
    private String fmtType;

    /**
     * 显示的创建时间戳
     */
    private Integer created;

    /**
     * 展示的修改时间戳
     */
    private Integer modified;

}
