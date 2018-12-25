package xyz.itao.ink.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import xyz.itao.ink.dao.UserMapper;
import xyz.itao.ink.domain.DomainFactory;
import xyz.itao.ink.domain.UserDomain;
import xyz.itao.ink.domain.entity.User;
import xyz.itao.ink.repository.UserRepository;

import java.util.List;

/**
 * @author hetao
 * @date 2018-12-02
 * @description
 */
@Repository("userRepository")
public class UserRepositoryImpl  implements UserRepository {
    @Autowired
    UserMapper userMapper;
    @Autowired
    DomainFactory domainFactory;

    @Override
    public UserDomain loadActiveUserDomainById(Long id) {
        UserDomain userDomain = domainFactory.createUserDomain().setId(id).setActive(true).setDeleted(false);
        List<User> users = userMapper.selectByNoNulProperties(userDomain.entity());
        return users.isEmpty() ? null : userDomain.assemble(users.get(0));
    }

    @Override
    public UserDomain loadUserDomainByUsername(String username) {
        UserDomain domain = domainFactory
                .createUserDomain()
                .setUsername(username)
                .setDeleted(false)
                .setPermanent(true)
                .setActive(true);
        List<User> users = userMapper.selectByNoNulProperties(domain.entity());
        return users.isEmpty() ? null : domain.assemble(users.get(0));
    }

    @Override
    public UserDomain loadUserDomainByEmail(String email) {
        UserDomain domain = domainFactory
                .createUserDomain()
                .setEmail(email)
                .setDeleted(false)
                .setPermanent(true)
                .setActive(true);

        List<User> users = userMapper.selectByNoNulProperties(domain.entity());
        return users.isEmpty() ? null : domain.assemble(users.get(0));
    }

    @Override
    public UserDomain loadUserDomainByHomeUrl(String homeUrl) {
        UserDomain domain = domainFactory
                .createUserDomain()
                .setHomeUrl(homeUrl)
                .setDeleted(false)
                .setPermanent(true)
                .setActive(true);

        List<User> users = userMapper.selectByNoNulProperties(domain.entity());
        return users.isEmpty() ? null : domain.assemble(users.get(0));
    }

    @Override
    public UserDomain saveNewUserDomain(UserDomain userDomain) {
        userMapper.insertSelective(userDomain.entity());
        return  userDomain;
    }

    @Override
    public UserDomain updateUserDomain(UserDomain domain) {
        userMapper.updateByPrimaryKeySelective(domain.entity());
        return domain;
    }


}
