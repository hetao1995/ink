package xyz.itao.ink.domain;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import xyz.itao.ink.repository.RoleRepository;

import java.util.Date;

/**
 * @author hetao
 * @date 2018-12-03
 * @description
 */
@Data
@Builder
@Accessors(chain = true)
public class UserRoleDomain extends BaseDomain{
    /**
     * 用户角色记录的id
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
     * 用户id
     */
    private Long userId;

    /**
     * 角色id
     */
    private Long roleId;

    /**
     * 创建用户的时间戳
     */
    private Date createTime;

    /**
     * 创建用户的时间戳
     */
    private Date updateTime;

    /**
     * 被谁创建
     */
    private Long createBy;

    /**
     * 被谁修改
     */
    private Long updateBy;

    /**
     * RoleRepository对象，使用此对象获取role
     */
    private RoleRepository roleRepository;

    /**
     * 角色，使用roleRepository获取
     */
    private String role;

    /**
     * 通过roleRepository获取Role
     *
     * @return role字符串
     */
    public String getRole() {
        if (role != null || roleId == null || roleRepository == null) {
            return role;
        }
        // 根据roleRepository获取RoleDomain对象
        RoleDomain roleDomain = roleRepository.loadActiveRoleDomainById(roleId);
        if (roleDomain == null) {
            return role;
        }
        role = roleDomain.getRole();
        return role;
    }
}
