package xyz.itao.ink.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import xyz.itao.ink.domain.UserDomain;
import xyz.itao.ink.repository.UserRepository;
import xyz.itao.ink.service.UserService;
import xyz.itao.ink.utils.IdUtils;

/**
 * @author hetao
 * @date 2018-12-01
 * @description
 */
@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;
    @Override
    public UserDomain registerTemporaryUser(String homeUrl, String email, String displayName) {
       UserDomain userDomain = UserDomain
               .builder()
               .homeUrl(homeUrl)
               .email(email)
               .displayName(displayName)
               .id(IdUtils.nextId())
               .build();
       return userRepository.saveNewUserDomain(userDomain);
    }

    @Override
    public String getJwtLoginToken(UserDetails user) {
        return null;
    }
}
