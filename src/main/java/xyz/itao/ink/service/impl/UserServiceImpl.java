package xyz.itao.ink.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import xyz.itao.ink.domain.UserDomain;
import xyz.itao.ink.exception.ExceptionEnum;
import xyz.itao.ink.exception.TipException;
import xyz.itao.ink.repository.UserRepository;
import xyz.itao.ink.service.UserService;
import xyz.itao.ink.utils.DateUtils;
import xyz.itao.ink.utils.IdUtils;
import xyz.itao.ink.utils.PatternUtils;

import java.util.Date;

/**
 * @author hetao
 * @date 2018-12-01
 * @description
 */
@Service("userService")
public class UserServiceImpl implements UserService {
    /**
     * 临时token，一个小时后过期
     */
    private static final Long TEMPORARY_JWT_EXPIRE_INTERVAL = 1000*60*60L;
    /**
     * 长期token，一个月后过期
     */
    private static final Long LONG_TERM_JWT_EXPIRE_INTERVAL = 100*60*60*24*30L;

    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
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
    public String getJwtLoginToken(UserDomain userDomain, Boolean rememberMe) {
        String salt = userDomain.getSalt();
        long interval = rememberMe ? LONG_TERM_JWT_EXPIRE_INTERVAL : TEMPORARY_JWT_EXPIRE_INTERVAL;
        Algorithm algorithm = Algorithm.HMAC256(salt);
        Date date = new Date(System.currentTimeMillis()+interval);
        return JWT
                .create()
                .withSubject(String.valueOf(userDomain.getId()))
                .withExpiresAt(date)
                .withIssuedAt(DateUtils.getNow())
                .sign(algorithm);
    }

    @Override
    public boolean clearJwtLoginToken(UserDomain userDomain) {
        return false;
    }

    @Override
    public UserDomain loadUserDomainById(Long id) {
        return userRepository.loadActiveUserDomainById(id);
    }

    @Override
    public UserDomain loadUserDomainByUsername(String username) {
        return userRepository.loadUserDomainByUsername(username);
    }

    @Override
    public UserDomain loadUserDomainByEmail(String email) {
        return userRepository.loadUserDomainByEmail(email);
    }

    @Override
    public UserDomain loadUserDomainByHomeUrl(String homeUrl) {
        return userRepository.loadUserDomainByHomeUrl(homeUrl);
    }

    @Override
    public UserDomain registerPermanentUser(String username, String email, String homeUrl, String password, String displayName) {
        checkEmpty(username, email, homeUrl, password);
        checkPattern(username, email, homeUrl, password, displayName);
        if(userRepository.loadUserDomainByUsername(username) != null){
            throw new TipException(ExceptionEnum.USERNAME_USED);
        }
        if(userRepository.loadUserDomainByEmail(email) != null){
            throw new TipException(ExceptionEnum.EMAIL_USED);
        }
        if(userRepository.loadUserDomainByHomeUrl(homeUrl) != null){
            throw new TipException(ExceptionEnum.HOME_URL_USED);
        }
        UserDomain userDomain = UserDomain
                .builder()
                .displayName(displayName)
                .email(email)
                .homeUrl(homeUrl)
                .username(username)
                .password(passwordEncoder.encode(password))
                .permanent(true)
                .active(false)
                .build();
        return registerUser(userDomain);
    }

    private void checkEmpty(String username, String email, String homeUrl, String password) {
        if(StringUtils.isBlank(username)){
            throw new TipException(ExceptionEnum.USERNAME_EMPTY);
        }
        if(StringUtils.isBlank(email)){
            throw new TipException(ExceptionEnum.EMAIL_EMPTY);
        }
        if(StringUtils.isBlank(homeUrl)){
            throw new TipException(ExceptionEnum.HOME_URL_EMPTY);
        }
        if(StringUtils.isBlank(password)){
            throw new TipException((ExceptionEnum.PASSWORD_EMPTY));
        }
    }

    private void checkPattern(String username, String email, String homeUrl, String password, String displayName) {
        if(username!=null && !PatternUtils.isUsername(username)){
            throw new TipException(ExceptionEnum.USERNAME_ILLEGAL);
        }
        if(homeUrl!=null && PatternUtils.isURL(homeUrl)){
            throw new TipException(ExceptionEnum.HOME_URL_ILLEGAL);
        }
        if(email!=null && !PatternUtils.isEmail(email)){
            throw new TipException(ExceptionEnum.EMAIL_ILLEGAL);
        }
        if(password!=null && (password.length()<6 || password.length()>20)){
            throw new TipException(ExceptionEnum.PASSWORD_ILLEGAL);
        }
        if(displayName!=null && displayName.length()<2 || displayName.length()>20){
            throw new TipException(ExceptionEnum.DISPLAY_NAME_ILLEGAL);
        }
    }

    private UserDomain registerUser(UserDomain userDomain){
        //加盐，在jwt验证时候需要用到
        userDomain.setSalt(BCrypt.gensalt());
        userDomain.setCreateBy(0L);
        userDomain.setUpdateBy(0L);
        return userRepository.saveNewUserDomain(userDomain);
    }
}
