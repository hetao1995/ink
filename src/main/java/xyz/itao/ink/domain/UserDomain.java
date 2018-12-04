package xyz.itao.ink.domain;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.Assert;
import xyz.itao.ink.repository.UserRoleRepository;

import java.util.*;

/**
 * @author hetao
 * @date 2018-11-29
 * @description user域
 */
@Data
@Builder
public class UserDomain extends BaseDomain implements CredentialsContainer {

    /**
     * id
     */
    protected Long id;

    /**
     * 是否已经被删除了
     */
    protected Boolean deleted;
    /**
     * 创建用户的时间戳
     */
    protected Date createTime;

    /**
     * 更新的时间戳
     */
    protected Date updateTime;

    /**
     * 被谁创建
     */
    protected Long createBy;

    /**
     * 被谁修改
     */
    protected Long updateBy;

    /**
     * 是否是长期用户 true：是 false：不是
     */
    private Boolean permanent;

    /**
     * 是否处于激活状态
     */
    private Boolean active;

    /**
     * 用户名，由字母、数字、下划线组成，长度在30以下，不能重复
     */
    private String username;

    /**
     * 密码加盐sha512散列之后的值
     */
    private String password;

    /**
     * 显示的用户名
     */
    private String displayName;

    /**
     * 邮箱，不能重复
     */
    private String email;

    /**
     * 加密的盐，这个用户所有需要生成摘要的地方都用这个
     */
    private String salt;

    /**
     * 最近一次登陆时间
     */
    private Date lastLogin;


    /**
     * 用户的主页，不能重复
     */
    private String homeUrl;

    /**
     * 角色的repository，用于获取当前用户的角色
     */
    private UserRoleRepository userRoleRepository;

    /**
     * 用户角色
     */
    private List<String> roles;

    /**
     * 通过 RoleRepository 获取角色
     *
     * @return 所有角色数组
     */
    public List<String> getRoles() {
        // roles已经加载或者id未初始化，直接返回roles
        if (roles != null || id == null || userRoleRepository == null) {
            return roles;
        }
        List<UserRoleDomain> userRoleDomains = userRoleRepository.loadAllActiveUserRolesByUserId(id);

        roles = Lists.newArrayList();
        for (UserRoleDomain userRoleDomain : userRoleDomains) {
            roles.add(userRoleDomain.getRole());
        }
        return roles;
    }

    /**
     * SpringSecurity调用后擦除的内容
     */
    @Override
    public void eraseCredentials() {
        password = null;
    }

    public Set<GrantedAuthority>  getAuthorities() {
        Set<GrantedAuthority> authorities = Sets.newHashSet();
        for (String role : getRoles()) {
            Assert.isTrue(!role.startsWith("ROLE_"), () -> role
                    + " cannot start with ROLE_ (it is automatically added)");
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
        }
        return authorities;
    }
}
