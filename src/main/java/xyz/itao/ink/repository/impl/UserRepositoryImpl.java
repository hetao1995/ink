package xyz.itao.ink.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import xyz.itao.ink.dao.UserMapper;
import xyz.itao.ink.domain.DomainFactory;
import xyz.itao.ink.domain.UserDomain;
import xyz.itao.ink.domain.entity.User;
import xyz.itao.ink.repository.AbstractBaseRepository;
import xyz.itao.ink.repository.UserRepository;
import xyz.itao.ink.repository.UserRoleRepository;

import java.util.List;

/**
 * @author hetao
 * @date 2018-12-02
 * @description
 */
@Repository("userRepository")
public class UserRepositoryImpl extends AbstractBaseRepository<UserDomain, User> implements UserRepository {
    @Autowired
    UserMapper userMapper;
    @Autowired
    UserRoleRepository userRoleRepository;
    @Autowired
    DomainFactory domainFactory;

    @Override
    public UserDomain loadActiveUserDomainById(Long id) {
        UserDomain userDomain = domainFactory.createUserDomain().setId(id);
        List<UserDomain> userDomains = loadByNoNullPropertiesActiveAndNotDelect(userDomain);
        return userDomains.isEmpty() ? null : userDomains.get(0);
    }

    @Override
    public UserDomain loadUserDomainByUsername(String username) {
        User user = User
                .builder()
                .username(username)
                .deleted(false)
                .permanent(true)
                .active(true)
                .build();
        List<User> users = doLoadByNoNullProperties(user);
        if(users.isEmpty()) return null;
        return assemble(users.get(0));
    }

    @Override
    public UserDomain loadUserDomainByEmail(String email) {
        User user = User
                .builder()
                .email(email)
                .deleted(false)
                .permanent(true)
                .active(true)
                .build();
        List<User> users = doLoadByNoNullProperties(user);
        if(users.isEmpty()) return null;
        return assemble(users.get(0));
    }

    @Override
    public UserDomain loadUserDomainByHomeUrl(String homeUrl) {
        User user = User
                .builder()
                .homeUrl(homeUrl)
                .deleted(false)
                .permanent(true)
                .active(true)
                .build();
        List<User> users = doLoadByNoNullProperties(user);
        if(users.isEmpty()) {
            return null;
        }
        return assemble(users.get(0));
    }

    @Override
    public UserDomain saveNewUserDomain(UserDomain userDomain) {
        return save(userDomain);
    }

    @Override
    public UserDomain updateUserDomain(UserDomain domain) {
        return update(domain);
    }

    @Override
    protected UserDomain doAssemble(User entity) {
        return domainFactory.createUserDomain().assemble(entity);
    }

    @Override
    protected User doExtract(UserDomain domain) {
        return domain.entity();
    }

    @Override
    protected boolean doSave(User entity) {
        return userMapper.insertSelective(entity);
    }

    @Override
    protected List<User> doLoadByNoNullProperties(User entity) {
        return userMapper.selectByNoNulProperties(entity);
    }


    @Override
    protected boolean doUpdate(User entity) {
        return userMapper.updateByPrimaryKeySelective(entity);
    }
}
