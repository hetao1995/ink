package xyz.itao.ink.repository;

import xyz.itao.ink.domain.UserDomain;

/**
 * @author hetao
 * @date 2018-12-02
 * @description
 */
public interface UserRepository {
    /**
     * 通过id获取UserDomain
     * @param id 用户id
     * @return id对应的UserDomain
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
}
