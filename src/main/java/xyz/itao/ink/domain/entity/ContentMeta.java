package xyz.itao.ink.domain.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author hetao
 * @date 2018-12-02
 * @description content_metas表的实体类
 */
@Data
public class ContentMeta {
    /**
     * 主键id
     */
    private Long id;

    /**
     * 是否被删除
     */
    private Boolean deleted;

    /**
     * 内容主键
     */
    private Long contentId;

    /**
     * 项目主键
     */
    private Long metaId;

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