package xyz.itao.ink.repository;

import xyz.itao.ink.domain.RoleDomain;
import xyz.itao.ink.domain.entity.UserRole;

import java.util.List;

/**
 * @author hetao
 * @date 2018-12-02
 */
public interface RoleRepository {

    /**
     * 通过role的id加载激活状态的role对象
     *
     * @param id role的id
     * @return id 对应的RoleDomain对象
     */
    RoleDomain loadActiveRoleDomainById(Long id);

    /**
     * 存储新的角色
     *
     * @param roleDomain 需要存储的内容
     * @return 存储结果对应的roleDomain
     */
    RoleDomain saveNewRole(RoleDomain roleDomain);

    /**
     * 根据角色名称查找RoleDomain
     *
     * @param role 角色名称
     * @return 对应的RoleDomain对象
     */
    RoleDomain loadActiveRoleDomainByRole(String role);

    /**
     * 根据userid查找所有的RoleDomain
     *
     * @param userId userId
     * @return 角色
     */
    List<RoleDomain> loadAllActiveRoleDomainByUserId(Long userId);

    /**
     * 通过userid和roleid删除userrole
     *
     * @param userId userId
     * @param roleId id
     * @return 是否删除成功
     */
    boolean deleteUserRoleRelationshipByUserIdAndRoleId(Long userId, Long roleId);

    /**
     * 存储userRole
     *
     * @param userRole userRole
     * @return 是否存储成功
     */
    boolean saveNewUserRole(UserRole userRole);
}
