package xyz.itao.ink.service;

import org.springframework.security.core.userdetails.UserDetails;
import xyz.itao.ink.domain.UserDomain;
import xyz.itao.ink.domain.vo.UserVo;

/**
 * @author hetao
 * @date 2018-12-01
 * @description
 */
public interface UserService {
    /**
     * @param userVo user 信息
     */
    UserDomain registerTemporaryUser(UserVo userVo);

    /**
     * 获取Jwt登录的Token
     * @param userDomain 登录成功的UserDomain对象
     * @return jwt令牌
     */
    String getJwtLoginToken(UserDomain userDomain, Boolean rememberMe);

    String getJwtLoginToken(UserVo userVo, Boolean rememberMe);

    /**
     * 通过id加载UserDomain
     * @param id 主键
     * @return 加载的结果
     */
    UserVo loadUserVoById(Long id);

    /**
     * 通过username获取UserVo
     * @param username 用户名
     * @return 这个username对应的UserDomain
     */
    UserVo loadUserVoByUsername(String username);

    /**
     * 通过email加载UserDomain
     * @param email 邮箱
     * @return 邮箱对应的UserDomain
     */
    UserVo loadUserVoByEmail(String email);

    /**
     * 通过主页加载UserDomain
     * @param homeUrl 主页地址
     * @return 主页地址对应的UserDomain
     */
    UserVo loadUserVoByHomeUrl(String homeUrl);

    /**
     * 通过id加载UserDomain
     * @param id 主键
     * @return 加载的结果
     */
    UserDomain loadUserDomainById(Long id);

    /**
     * 通过username获取UserVo
     * @param username 用户名
     * @return 这个username对应的UserDomain
     */
    UserDomain loadUserDomainByUsername(String username);

    /**
     * 通过email加载UserDomain
     * @param email 邮箱
     * @return 邮箱对应的UserDomain
     */
    UserDomain loadUserDomainByEmail(String email);

    /**
     * 通过主页加载UserDomain
     * @param homeUrl 主页地址
     * @return 主页地址对应的UserDomain
     */
    UserDomain loadUserDomainByHomeUrl(String homeUrl);

    /**
     * 注册一个永久的用户
     * @param  userVo 注册用户信息
     */
    UserDomain registerPermanentUser(UserVo userVo);

    /**
     * 更新昵称和邮箱
     * @param screenName 昵称
     * @param email 邮箱
     * @param userDomain 更新谁的
     */
    void updateProfile(String screenName, String email, UserDomain userDomain);

    /**
     * 修改密码
     * @param old_password 旧密码
     * @param password 新密码
     * @param userDomain 修改谁的
     */
    void updatePassword(String old_password, String password, UserDomain userDomain);

}
