package xyz.itao.ink.domain.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author hetao
 * @date 2018-12-02
 * @description metas表的实体类
 */
@Data
public class Meta {
    /**
     * 主键id
     */
    private Long id;

    /**
     * 是否被删除
     */
    private Boolean deleted;

    /**
     * 父级项目
     */
    private Long parentId;

    /**
     * 项目名称
     */
    private String name;

    /**
     * 项目缩略名
     */
    private String slug;

    /**
     * 项目类型
     */
    private String type;

    /**
     * 项目详情
     */
    private String detail;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 创建者
     */
    private Long createBy;

    /**
     * 修改者
     */
    private Long updateBy;

}