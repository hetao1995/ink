package xyz.itao.ink.repository;

import xyz.itao.ink.domain.RoleDomain;
import xyz.itao.ink.domain.entity.Role;
import xyz.itao.ink.domain.entity.UserRole;

import java.util.List;

/**
 * @author hetao
 * @date 2018-12-02
 * @description
 */
public interface RoleRepository {

    /**
     * 通过role的id加载role对象
     * @param id role的id
     * @return id 对应的RoleDomain对象
     */
    RoleDomain loadRoleById(Long id);
}
