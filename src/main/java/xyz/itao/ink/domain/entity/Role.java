package xyz.itao.ink.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * roles表的实体类
 *
 * @author hetao
 * @date 2018-12-02
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    /**
     * 角色id
     */
    private Long id;

    /**
     * 是否已经被删除
     */
    private Boolean deleted;

    /**
     * 是否处于激活状态
     */
    private Boolean active;

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