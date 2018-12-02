package xyz.itao.ink.service.impl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import xyz.itao.ink.domain.UserDomain;
import xyz.itao.ink.service.UserService;

/**
 * @author hetao
 * @date 2018-12-01
 * @description
 */
@Service("userService")
public class UserServiceImpl implements UserService {

    @Override
    public UserDomain registerTemporaryUser(String homeUrl, String email, String displayName) {
        return null;
    }

    @Override
    public String getJwtLoginToken(UserDetails user) {
        return null;
    }
}
