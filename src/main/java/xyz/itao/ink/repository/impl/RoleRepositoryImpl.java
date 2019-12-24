package xyz.itao.ink.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import xyz.itao.ink.dao.RoleMapper;
import xyz.itao.ink.dao.UserRoleMapper;
import xyz.itao.ink.domain.DomainFactory;
import xyz.itao.ink.domain.RoleDomain;
import xyz.itao.ink.domain.entity.Role;
import xyz.itao.ink.domain.entity.UserRole;
import xyz.itao.ink.exception.ExceptionEnum;
import xyz.itao.ink.exception.InnerException;
import xyz.itao.ink.repository.RoleRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author hetao
 * @date 2018-12-03
 */
@Repository("roleRepository")
public class RoleRepositoryImpl implements RoleRepository {
    private final RoleMapper roleMapper;
    private final UserRoleMapper userRoleMapper;
    private final DomainFactory domainFactory;

    @Autowired
    public RoleRepositoryImpl(RoleMapper roleMapper, UserRoleMapper userRoleMapper, DomainFactory domainFactory) {
        this.roleMapper = roleMapper;
        this.userRoleMapper = userRoleMapper;
        this.domainFactory = domainFactory;
    }


    @Override
    public RoleDomain loadActiveRoleDomainById(Long id) {
        RoleDomain domain = domainFactory.createRoleDomain().setId(id).setActive(true).setDeleted(false);
        List<Role> roles = roleMapper.selectByNoNulProperties(domain.entity());
        if (roles.isEmpty()) {
            return null;
        }
        return domain.assemble(roles.get(0));
    }

    @Override
    public RoleDomain saveNewRole(RoleDomain roleDomain) {
        roleMapper.insertSelective(roleDomain.entity());
        return roleDomain;
    }

    @Override
    public RoleDomain loadActiveRoleDomainByRole(String role) {
        RoleDomain domain = domainFactory.createRoleDomain().setRole(role).setActive(true).setDeleted(false);
        List<Role> roles = roleMapper.selectByNoNulProperties(domain.entity());
        if (roles.isEmpty()) {
            return null;
        }
        return domain.assemble(roles.get(0));
    }

    @Override
    public List<RoleDomain> loadAllActiveRoleDomainByUserId(Long userId) {
        List<Role> roles = roleMapper.selectByUserId(userId);
        return roles.stream().map(e -> domainFactory.createRoleDomain().assemble(e)).collect(Collectors.toList());
    }

    @Override
    public boolean deleteUserRoleRelationshipByUserIdAndRoleId(Long userId, Long roleId) {
        UserRole userRole = userRoleMapper.selectByUserIdAndRoleId(userId, roleId);
        if (userRole == null) {
            throw new InnerException(ExceptionEnum.DELETE_NON_EXIST_ELEMENT);
        }
        userRole.setDeleted(true);
        return userRoleMapper.updateByPrimaryKeySelective(userRole);
    }

    @Override
    public boolean saveNewUserRole(UserRole userRole) {
        return userRoleMapper.insertSelective(userRole);
    }

}
