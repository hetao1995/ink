package xyz.itao.ink.repository.impl;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import xyz.itao.ink.dao.UserRoleMapper;
import xyz.itao.ink.domain.UserRoleDomain;
import xyz.itao.ink.domain.entity.UserRole;
import xyz.itao.ink.repository.AbstractBaseRepository;
import xyz.itao.ink.repository.RoleRepository;
import xyz.itao.ink.repository.UserRoleRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hetao
 * @date 2018-12-03
 * @description
 */
@Repository("userRoleRepository")
public class UserRoleRepositoryImpl extends AbstractBaseRepository<UserRoleDomain, UserRole> implements UserRoleRepository {
    @Autowired
    UserRoleMapper userRoleMapper;
    @Autowired
    RoleRepository roleRepository;

    @Override
    public List<UserRoleDomain> loadAllUserRolesByUserId(Long userId) {
        UserRole userRole = UserRole
                .builder()
                .userId(userId)
                .deleted(false)
                .build();
        List<UserRole> userRoles = userRoleMapper.selectByNoNulProperties(userRole);
        List<UserRoleDomain> userRoleDomains = Lists.newArrayList();
        for(UserRole ur : userRoles){
            userRoleDomains.add(assemble(ur));
        }
        return userRoleDomains;
    }

    @Override
    protected UserRoleDomain doAssemble(UserRole entity) {
        return UserRoleDomain
                .builder()
                .id(entity.getId())
                .deleted(entity.getDeleted())
                .createTime(entity.getCreateTime())
                .createBy(entity.getCreateBy())
                .updateTime(entity.getUpdateTime())
                .updateBy(entity.getUpdateBy())
                .userId(entity.getUserId())
                .roleId(entity.getRoleId())
                .roleRepository(roleRepository)
                .build();
    }

    @Override
    protected UserRole doExtract(UserRoleDomain domain) {
        return UserRole
                .builder()
                .id(domain.getId())
                .deleted(domain.getDeleted())
                .createTime(domain.getCreateTime())
                .createBy(domain.getCreateBy())
                .updateTime(domain.getUpdateTime())
                .updateBy(domain.getUpdateBy())
                .userId(domain.getUserId())
                .roleId(domain.getRoleId())
                .build();
    }

}
