package xyz.itao.ink.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author hetao
 * @date 2018-12-02
 * @description user_roles表的实体类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRole {
    /**
     * 用户角色记录的id
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
     * 用户id
     */
    private Long userId;

    /**
     * 角色id
     */
    private Long roleId;

    /**
     * 创建用户的时间戳
     */
    private Date createTime;

    /**
     * 创建用户的时间戳
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