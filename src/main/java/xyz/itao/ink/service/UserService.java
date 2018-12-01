package xyz.itao.ink.service;

/**
 * @author hetao
 * @date 2018-12-01 21:05
 * @description
 */
public interface UserService {
    UserDomain loadUserDomainByUserName();
    UserDomain loadUserDomainByUserEmail();
    UserDomain loadUserDomainByHomeUrl();
    UserDomain registerNewUser()
}
