package xyz.itao.ink.domain.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author hetao
 * @date 2018-12-05
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommentVo {
    /**
     * 评论的id
     */
    private Long id;


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
     * 作者姓名
     */
    private String author;

    /**
     * 作者邮箱
     */
    private String mail;

    /**
     * 作者主页
     */
    private String url;
}
