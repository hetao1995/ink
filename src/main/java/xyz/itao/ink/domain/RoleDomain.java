package xyz.itao.ink.domain;


import lombok.Data;
import lombok.experimental.Accessors;
import xyz.itao.ink.domain.entity.Role;
import xyz.itao.ink.domain.entity.UserRole;
import xyz.itao.ink.repository.RoleRepository;
import xyz.itao.ink.utils.DateUtils;
import xyz.itao.ink.utils.IdUtils;

import java.util.Date;

/**
 * @author hetao
 * @date 2018-12-03
 */
@Data
@Accessors(chain = true)
public class RoleDomain {

    RoleDomain(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    private RoleRepository roleRepository;
    /**
     * 角色id
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
     * 角色名
     */
    private String role;

    /**
     * 角色的详细说明
     */
    private String detail;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 是由那个用户修改的
     */
    private Long updateBy;

    /**
     * 被谁创建
     */
    private Long createBy;

    /**
     * 根据userid插入userrole
     *
     * @param userId id
     * @param operator 操作者
     */
    public boolean saveUserRole(Long userId, Long operator) {
        UserRole userRole = UserRole
                .builder()
                .id(IdUtils.nextId())
                .createTime(DateUtils.getNow())
                .updateTime(DateUtils.getNow())
                .active(true)
                .deleted(false)
                .updateBy(operator)
                .createBy(operator)
                .userId(userId)
                .roleId(this.id)
                .build();
        return roleRepository.saveNewUserRole(userRole);
    }

    public RoleDomain save() {
        this.createTime = DateUtils.getNow();
        this.updateTime = DateUtils.getNow();
        this.deleted = false;
        this.id = IdUtils.nextId();
        this.active = true;
        roleRepository.saveNewRole(this);
        return this;
    }

    public RoleDomain assemble(Role entity) {
        return this
                .setId(entity.getId())
                .setDeleted(entity.getDeleted())
                .setCreateTime(entity.getCreateTime())
                .setCreateBy(entity.getCreateBy())
                .setUpdateTime(entity.getUpdateTime())
                .setUpdateBy(entity.getUpdateBy())
                .setActive(entity.getActive())
                .setRole(entity.getRole())
                .setDetail(entity.getDetail());
    }

    public Role entity() {
        return Role
                .builder()
                .id(this.getId())
                .deleted(this.getDeleted())
                .createTime(this.getCreateTime())
                .createBy(this.getCreateBy())
                .updateTime(this.getUpdateTime())
                .updateBy(this.getUpdateBy())
                .role(this.getRole())
                .detail(this.getDetail())
                .active(this.getActive())
                .build();
    }
}
