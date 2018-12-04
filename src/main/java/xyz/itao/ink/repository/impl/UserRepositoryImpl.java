package xyz.itao.ink.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import xyz.itao.ink.dao.UserMapper;
import xyz.itao.ink.domain.UserDomain;
import xyz.itao.ink.domain.entity.User;
import xyz.itao.ink.repository.AbstractBaseRepository;
import xyz.itao.ink.repository.UserRepository;
import xyz.itao.ink.repository.UserRoleRepository;

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

    @Override
    public UserDomain loadActiveUserDomainById(Long id) {
        UserDomain userDomain = UserDomain
                .builder()
                .id(id)
                .build();
        return loadByNoNullPropertiesActiveAndNotDelect(userDomain);
    }

    @Override
    public UserDomain loadUserDomainByUsername(String username) {
        User user = User
                .builder()
                .username(username)
                .deleted(false)
                .permanent(true)
                .build();
        user = doLoadByNoNullProperties(user);
        return assemble(user);
    }

    @Override
    public UserDomain loadUserDomainByEmail(String email) {
        User user = User
                .builder()
                .email(email)
                .deleted(false)
                .permanent(true)
                .build();
        user = doLoadByNoNullProperties(user);
        return assemble(user);
    }

    @Override
    public UserDomain loadUserDomainByHomeUrl(String homeUrl) {
        User user = User
                .builder()
                .homeUrl(homeUrl)
                .deleted(false)
                .permanent(true)
                .build();
        user = doLoadByNoNullProperties(user);
        return assemble(user);
    }

    @Override
    public UserDomain saveNewUserDomain(UserDomain userDomain) {
        return save(userDomain);
    }

    @Override
    protected UserDomain doAssemble(User entity) {
        return UserDomain
                .builder()
                .id(entity.getId())
                .deleted(entity.getDeleted())
                .createTime(entity.getCreateTime())
                .createBy(entity.getCreateBy())
                .updateTime(entity.getUpdateTime())
                .updateBy(entity.getUpdateBy())
                .permanent(entity.getPermanent())
                .active(entity.getActive())
                .salt(entity.getSalt())
                .displayName(entity.getDisplayName())
                .username(entity.getUsername())
                .email(entity.getEmail())
                .homeUrl(entity.getHomeUrl())
                .password(entity.getPassword())
                .lastLogin(entity.getLastLogin())
                .userRoleRepository(userRoleRepository)
                .build();
    }

    @Override
    protected User doExtract(UserDomain domain) {
        return User
                .builder()
                .id(domain.getId())
                .deleted(domain.getDeleted())
                .createTime(domain.getCreateTime())
                .createBy(domain.getCreateBy())
                .updateTime(domain.getUpdateTime())
                .updateBy(domain.getUpdateBy())
                .permanent(domain.getPermanent())
                .active(domain.getActive())
                .salt(domain.getSalt())
                .displayName(domain.getDisplayName())
                .username(domain.getUsername())
                .email(domain.getEmail())
                .homeUrl(domain.getHomeUrl())
                .password(domain.getPassword())
                .lastLogin(domain.getLastLogin())
                .build();
    }

    @Override
    protected boolean doSave(User entity) {
        return userMapper.insertSelective(entity);
    }

    @Override
    protected User doLoadByNoNullProperties(User entity) {
        return userMapper.selectByNoNulProperties(entity);
    }


    @Override
    protected boolean doUpdate(User entity) {
        return userMapper.updateByPrimaryKeySelective(entity);
    }
}
