package xyz.itao.ink.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * content_metas表的实体类
 *
 * @author hetao
 * @date 2018-12-02
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContentMeta {
    /**
     * 主键
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