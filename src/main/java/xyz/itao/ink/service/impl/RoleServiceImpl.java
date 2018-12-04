package xyz.itao.ink.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import xyz.itao.ink.domain.RoleDomain;
import xyz.itao.ink.domain.UserDomain;
import xyz.itao.ink.domain.UserRoleDomain;
import xyz.itao.ink.exception.ExceptionEnum;
import xyz.itao.ink.exception.TipException;
import xyz.itao.ink.repository.RoleRepository;
import xyz.itao.ink.repository.UserRepository;
import xyz.itao.ink.repository.UserRoleRepository;
import xyz.itao.ink.service.RoleService;

/**
 * @author hetao
 * @date 2018-12-04
 * @description
 */
public class RoleServiceImpl implements RoleService {
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    UserRoleRepository userRoleRepository;
    @Autowired
    UserRepository userRepository;
    @Override
    public UserRoleDomain addRoleToUser(String role, Long userId, Long operator) {
        RoleDomain roleDomain = roleRepository.loadActiveRoleDomainByRole(role);
        if(roleDomain == null){
            throw new TipException(ExceptionEnum.ROLE_NOT_FOUND);
        }
        UserDomain userDomain = userRepository.loadActiveUserDomainById(userId);
        if(userDomain == null){
            throw new TipException(ExceptionEnum.USER_NOT_FIND);
        }
        UserRoleDomain userRoleDomain = UserRoleDomain
                .builder()
                .userId(userId)
                .roleId(roleDomain.getId())
                .active(true)
                .createBy(operator)
                .updateBy(operator)
                .build();
        return userRoleRepository.saveNewUserRole(userRoleDomain);
    }

    @Override
    public RoleDomain addRole(String role, String detail, Long operator) {
        RoleDomain roleDomain = RoleDomain
                .builder()
                .role(role)
                .active(true)
                .createBy(operator)
                .updateBy(operator)
                .build();
        return roleRepository.saveNewRole(roleDomain);
    }
}
