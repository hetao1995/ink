package xyz.itao.ink.repository;


import xyz.itao.ink.domain.UserRoleDomain;
import xyz.itao.ink.domain.entity.UserRole;

import java.util.List;

/**
 * @author hetao
 * @date 2018-12-02
 * @description
 */
public interface UserRoleRepository {
    /**
     * 通过userId加载该user的所有权限
     * @param userId 用户id
     * @return 所有Role的集合
     */
    List<UserRoleDomain> loadAllActiveUserRolesByUserId(Long userId);

    UserRoleDomain saveNewUserRole(UserRoleDomain userRoleDomain);
}
