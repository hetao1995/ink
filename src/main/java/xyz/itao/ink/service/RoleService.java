package xyz.itao.ink.service;

import xyz.itao.ink.domain.RoleDomain;
import xyz.itao.ink.domain.UserRoleDomain;

/**
 * @author hetao
 * @date 2018-12-04
 * @description
 */
public interface RoleService {
    /**
     * 为user添加角色
     * @param role
     * @param userId 需要添加角色的id
     * @param operator 是谁添加
     */
    UserRoleDomain addRoleToUser(String role, Long userId, Long operator);

    /**
     * 新加一个角色
     * @param role 角色名称
     * @param detail 角色详情
     * @param operator 谁填加的
     */
    RoleDomain addRole(String role, String detail, Long operator);
}
