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
     * 通过role的id加载激活状态的role对象
     * @param id role的id
     * @return id 对应的RoleDomain对象
     */
    RoleDomain loadActiveRoleDomainById(Long id);

    /**
     * 存储新的角色
     * @param roleDomain 需要存储的内容
     * @return 存储结果对应的roleDomain
     */
    RoleDomain saveNewRole(RoleDomain roleDomain);

    /**
     * 根据角色名称查找RoleDomain
     * @param role 角色名称
     * @return 对应的RoleDomain对象
     */
    RoleDomain loadActiveRoleDomainByRole(String role);
}
