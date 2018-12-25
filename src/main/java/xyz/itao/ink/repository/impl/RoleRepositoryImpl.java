package xyz.itao.ink.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import xyz.itao.ink.dao.RoleMapper;
import xyz.itao.ink.dao.UserRoleMapper;
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
 * @description
 */
@Repository("roleRepository")
public class RoleRepositoryImpl extends AbstractBaseRepository<RoleDomain, Role> implements RoleRepository {
    @Autowired
    RoleMapper roleMapper;
    @Autowired
    UserRoleMapper userRoleMapper;


    @Override
    public RoleDomain loadActiveRoleDomainById(Long id) {
        RoleDomain roleDomain = RoleDomain
                .builder()
                .id(id)
                .build();
        return loadByNoNullPropertiesActiveAndNotDelect(roleDomain).get(0);
    }

    @Override
    public RoleDomain saveNewRole(RoleDomain roleDomain) {
        return save(roleDomain);
    }

    @Override
    public RoleDomain loadActiveRoleDomainByRole(String role) {
        RoleDomain roleDomain = RoleDomain
                .builder()
                .role(role)
                .build();
        return loadByNoNullPropertiesActiveAndNotDelect(roleDomain).get(0);
    }

    @Override
    public List<RoleDomain> loadAllActiveRoleDomainByUserId(Long userId) {
        List<Role> roles = roleMapper.selectByUserId(userId);
        return roles.stream().map(e->assemble(e)).collect(Collectors.toList());
    }

    @Override
    public boolean deleteUserRoleRelationshipByUserIdAndRoleId(Long userId, Long roleId) {
        UserRole userRole = userRoleMapper.selectByUserIdAndRoleId(userId, roleId);
        if(userRole == null){
            throw new InnerException(ExceptionEnum.DELETE_NON_EXIST_ELEMENT);
        }
        userRole.setDeleted(true);
        return userRoleMapper.updateByPrimaryKeySelective(userRole);
    }

    @Override
    public boolean saveNewUserRole(UserRole userRole) {
        return userRoleMapper.insertSelective(userRole);
    }

    @Override
    protected boolean doSave(Role entity) {
        return roleMapper.insertSelective(entity);
    }

    @Override
    protected List<Role> doLoadByNoNullProperties(Role entity) {
        return roleMapper.selectByNoNulProperties(entity);
    }

    @Override
    protected boolean doUpdate(Role entity) {
        return roleMapper.updateByPrimaryKeySelective(entity);
    }

    @Override
    protected RoleDomain doAssemble(Role entity) {
        return RoleDomain
                .builder()
                .id(entity.getId())
                .deleted(entity.getDeleted())
                .createTime(entity.getCreateTime())
                .createBy(entity.getCreateBy())
                .updateTime(entity.getUpdateTime())
                .updateBy(entity.getUpdateBy())
                .role(entity.getRole())
                .detail(entity.getDetail())
                .active(entity.getActive())
                .build();
    }

    @Override
    protected Role doExtract(RoleDomain domain) {
        return Role
                .builder()
                .id(domain.getId())
                .deleted(domain.getDeleted())
                .createTime(domain.getCreateTime())
                .createBy(domain.getCreateBy())
                .updateTime(domain.getUpdateTime())
                .updateBy(domain.getUpdateBy())
                .role(domain.getRole())
                .detail(domain.getDetail())
                .active(domain.getActive())
                .build();
    }
}
