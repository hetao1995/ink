package xyz.itao.ink.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import xyz.itao.ink.dao.UserMapper;
import xyz.itao.ink.domain.UserDomain;
import xyz.itao.ink.domain.entity.User;
import xyz.itao.ink.repository.UserRepository;

/**
 * @author hetao
 * @date 2018-12-02
 * @description
 */
@Repository("userRepository")
public class UserRepositoryImpl implements UserRepository{
    @Autowired
    UserMapper userMapper;
    @Override
    public UserDomain loadUserDomainById(Long id) {
        User user = userMapper.selectByPrimaryKey(id);
        if(user == null){
            return null;
        }
        UserDomain userDomain = new UserDomain();


    }

    @Override
    public UserDomain loadUserDomainByUsername(String username) {
        return null;
    }

    @Override
    public UserDomain loadUserDomainByEmail(String email) {
        return null;
    }

    @Override
    public UserDomain loadUserDomainByHomeUrl(String homeUrl) {
        return null;
    }
}
