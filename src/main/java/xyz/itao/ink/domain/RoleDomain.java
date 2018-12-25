package xyz.itao.ink.domain;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import xyz.itao.ink.domain.entity.Role;
import xyz.itao.ink.domain.entity.UserRole;
import xyz.itao.ink.repository.RoleRepository;
import xyz.itao.ink.utils.DateUtils;
import xyz.itao.ink.utils.IdUtils;

import java.util.Date;
import java.util.Objects;

/**
 * @author hetao
 * @date 2018-12-03
 * @description
 */
@Data
@Accessors(chain = true)
public class RoleDomain{

    RoleDomain(RoleRepository roleRepository){
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
     * @param userId
     * @param operator
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
        roleRepository.saveNewRole(this);
        return this;
    }
}
