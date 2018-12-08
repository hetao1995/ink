package xyz.itao.ink.repository;

import xyz.itao.ink.domain.UserDomain;

/**
 * @author hetao
 * @date 2018-12-02
 * @description
 */
public interface UserRepository {
    /**
     * 通过id获取active状态的UserDomain
     * @param id 用户id
     * @return id对应的UserDomain
     */
    UserDomain loadActiveUserDomainById(Long id);
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
     * 将userdomian对象存入数据库
     * @param userDomain 需要存入的内容
     * @return 存入后的结果
     */
    UserDomain saveNewUserDomain(UserDomain userDomain);

    /**
     * 更新userdomain数据
     * @param domain 需要更新的数据
     * @return 更新结果
     */
    UserDomain updateUserDomain(UserDomain domain);
}
