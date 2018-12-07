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
     * 在评论的时候注册临时用户
     * @param homeUrl 主页地址
     * @param email 邮箱
     * @param displayName 用户昵称
     * @return 新注册的UserDomain
     */
    UserDomain registerTemporaryUser(String homeUrl, String email, String displayName);

    /**
     * 获取Jwt登录的Token
     * @param userDomain 登录成功的UserDomain对象
     * @return jwt令牌
     */
    String getJwtLoginToken(UserDomain userDomain, Boolean rememberMe);

    /**
     * 清除JwtToken
     * @param userDomain 用户domain
     * @return 是否清除成功
     */
    boolean clearJwtLoginToken(UserDomain userDomain);

    /**
     * 通过id加载UserDomain
     * @param id 主键
     * @return 加载的结果
     */
    UserDomain loadUserDomainById(Long id);

    /**
     * 通过username获取UserDomain
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
     * @param username username
     * @param email 邮箱
     * @param homeUrl 主页地址
     * @param password 密码
     * @param displayName 展示的昵称
     * @return
     */
    UserDomain registerPermanentUser(String username, String email, String homeUrl, String password, String displayName);

    void updateProfile(String screenName, String email, UserVo userVo);

    void updatePassword(String old_password, String password, UserVo userVo);
}
