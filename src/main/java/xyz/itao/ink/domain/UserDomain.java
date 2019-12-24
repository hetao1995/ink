package xyz.itao.ink.domain;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.util.Assert;
import xyz.itao.ink.constant.WebConstant;
import xyz.itao.ink.domain.entity.User;
import xyz.itao.ink.domain.vo.UserVo;
import xyz.itao.ink.exception.ExceptionEnum;
import xyz.itao.ink.exception.InnerException;
import xyz.itao.ink.repository.RoleRepository;
import xyz.itao.ink.repository.UserRepository;
import xyz.itao.ink.utils.CryptoUtils;
import xyz.itao.ink.utils.DateUtils;
import xyz.itao.ink.utils.IdUtils;

import java.util.*;

/**
 * user域
 * @author hetao
 * @date 2018-11-29
 */
@Data
@Accessors(chain = true)
public class UserDomain implements CredentialsContainer {

    UserDomain(UserRepository userRepository, RoleRepository roleRepository, CacheManager cacheManager){
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.cacheManager = cacheManager;
        this.cache = cacheManager.getCache(WebConstant.USER_CACHE);
    }
    /**
     * 角色的repository，用于获取当前用户的角色
     */
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private CacheManager cacheManager;
    private Cache cache;
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
     * 用户角色
     */
    private List<RoleDomain> roles;

    /**
     * 通过 RoleRepository 获取角色
     *
     * @return 所有角色数组
     */
    @SuppressWarnings("unchecked")
    public List<RoleDomain> getRoles() {
        // id未初始化，直接返回空List
        if (id == null) {
            return Lists.newArrayList();
        }
        String key = "roles:" + this.id;
        Cache.ValueWrapper wrapper = cache.get(key);
        if(wrapper != null){
            return (List<RoleDomain>) wrapper.get();
        }
        List<RoleDomain> roleDomains =  roleRepository.loadAllActiveRoleDomainByUserId(id);
        cache.put(key, roleDomains);
        return roleDomains;
    }
    public UserDomain setRoles(List<RoleDomain> roles) {
        this.roles = roles;
        return this;
    }
    private UserDomain saveRoles(){
        Set<RoleDomain> set = Sets.newHashSet(roles);
        for(RoleDomain roleDomain : getRoles()){
            if(set.remove(roleDomain)){
                continue;
            }
            roleRepository.deleteUserRoleRelationshipByUserIdAndRoleId(id, roleDomain.getId());
        }
        for(RoleDomain roleDomain : set){
            roleDomain.saveUserRole(id, this.updateBy);
        }
        cache.put("roles:" + this.id, this.roles);
        return this;
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
        for (RoleDomain roleDomain : getRoles()) {
            String role = roleDomain.getRole();
            Assert.isTrue(!role.startsWith("ROLE_"), () -> role
                    + " cannot start with ROLE_ (it is automatically added)");
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
        }
        return authorities;
    }
    
    public UserDomain assemble(User entity){
        if(entity==null){
            return null;
        }

        return this.setId(entity.getId())
                .setDeleted(entity.getDeleted())
                .setCreateTime(entity.getCreateTime())
                .setCreateBy(entity.getCreateBy())
                .setUpdateTime(entity.getUpdateTime())
                .setUpdateBy(entity.getUpdateBy())
                .setActive(entity.getActive())
                .setPermanent(entity.getPermanent())
                .setSalt(entity.getSalt())
                .setDisplayName(entity.getDisplayName())
                .setUsername(entity.getUsername())
                .setEmail(entity.getEmail())
                .setHomeUrl(entity.getHomeUrl())
                .setPassword(entity.getPassword())
                .setLastLogin(entity.getLastLogin());
    }
    
    public UserDomain assemble(UserVo vo){
        return this
                .setId(vo.getId())
                .setActive(vo.getActive())
                .setDisplayName(vo.getDisplayName())
                .setEmail(vo.getEmail())
                .setHomeUrl(vo.getHomeUrl())
                .setPermanent(vo.getPermanent())
                .setLastLogin(vo.getLastLogin())
                .setSalt(vo.getSalt())
                .setUsername(vo.getUsername())
                .setPassword(vo.getPassword());
    }
    
    public User entity(){
        return User
                .builder()
                .id(this.getId())
                .deleted(this.getDeleted())
                .createTime(this.getCreateTime())
                .createBy(this.getCreateBy())
                .updateTime(this.getUpdateTime())
                .updateBy(this.getUpdateBy())
                .permanent(this.getPermanent())
                .active(this.getActive())
                .salt(this.getSalt())
                .displayName(this.getDisplayName())
                .username(this.getUsername())
                .email(this.getEmail())
                .homeUrl(this.getHomeUrl())
                .password(this.getPassword())
                .lastLogin(this.getLastLogin())
                .build();
    }
    
    public UserVo vo(){
        return UserVo
                .builder()
                .id(this.getId())
                .active(this.getActive())
                .displayName(this.getDisplayName())
                .email(this.getEmail())
                .homeUrl(this.getHomeUrl())
                .permanent(this.getPermanent())
                .lastLogin(this.getLastLogin())
                .salt(this.getSalt())
                .username(this.getUsername())
                .password(this.getPassword())
                .build();
    }

    public UserDomain updateById() {
        if(id==null){
            throw new InnerException(ExceptionEnum.ILLEGAL_OPERATION);
        }
        this.setUpdateTime(DateUtils.getNow());
        userRepository.updateUserDomain(this);
        if(roles!=null){
            this.saveRoles();
        }
        Objects.requireNonNull(cacheManager.getCache(WebConstant.USER_CACHE)).evict(this.id);
        return this;
    }

    public UserDomain save() {
        this.id = IdUtils.nextId();
        this.setCreateTime(DateUtils.getNow());
        this.setUpdateTime(DateUtils.getNow());
        this.setDeleted(false);
        this.setSalt(BCrypt.gensalt());
        userRepository.saveNewUserDomain(this);
        if(roles!=null){
            this.saveRoles();
        }
        return this;
    }

    /**
     * @return 返回github头像地址
     */
    public String getGravatar(){
        final String avatarUrl = "https://github.com/identicons/";
        String email = this.getEmail();
        if(email==null){
            email = "default";
        }
        String hash = CryptoUtils.MD5encode(email.trim().toLowerCase());
        return avatarUrl + hash + ".png";
    }
}
