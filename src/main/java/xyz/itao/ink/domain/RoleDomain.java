package xyz.itao.ink.domain;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @author hetao
 * @date 2018-12-03
 * @description
 */
@Data
@Builder
public class RoleDomain {
    /**
     * 角色id
     */
    private Long id;

    /**
     * 是否已经被删除
     */
    private Byte deleted;

    /**
     * 角色名
     */
    private String role;

    /**
     * 角色的详细说明
     */
    private String detail;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 是由那个用户修改的
     */
    private Long updateBy;

    /**
     * 被谁创建
     */
    private Long createBy;
}
