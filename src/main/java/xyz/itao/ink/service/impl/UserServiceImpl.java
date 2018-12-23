package xyz.itao.ink.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import xyz.itao.ink.common.CommonValidator;
import xyz.itao.ink.constant.WebConstant;
import xyz.itao.ink.domain.UserDomain;
import xyz.itao.ink.domain.vo.UserVo;
import xyz.itao.ink.exception.ExceptionEnum;
import xyz.itao.ink.exception.TipException;
import xyz.itao.ink.repository.UserRepository;
import xyz.itao.ink.service.AbstractBaseService;
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
public class UserServiceImpl extends AbstractBaseService<UserDomain, UserVo> implements UserService {
    /**
     * 临时token，一个小时后过期
     */
    private static final Long TEMPORARY_JWT_EXPIRE_INTERVAL = 1000*60*60L;
    /**
     * 长期token，一个月后过期
     */
    private static final Long LONG_TERM_JWT_EXPIRE_INTERVAL = Long.valueOf(WebConstant.REMEMBER_ME_INTERVAL)*1000;

    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Override
    public UserVo registerTemporaryUser(UserVo userVo) {
        CommonValidator.valid(userVo, false);
        userVo.setActive(true);
        userVo.setPermanent(false);
        userVo.setSalt(BCrypt.gensalt());
        return save(userVo, 0L);
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
    public String getJwtLoginToken(UserVo userVo, Boolean rememberMe) {
        return getJwtLoginToken(assemble(userVo), rememberMe);
    }

    @Override
    public boolean clearJwtLoginToken(UserDomain userDomain) {
        return false;
    }

    @Override
    public UserVo loadUserVoById(Long id) {
        return extract(userRepository.loadActiveUserDomainById(id));
    }

    @Override
    public UserVo loadUserVoByUsername(String username) {
        return extract(userRepository.loadUserDomainByUsername(username));
    }

    @Override
    public UserVo loadUserVoByEmail(String email) {
        return extract(userRepository.loadUserDomainByEmail(email));
    }

    @Override
    public UserVo loadUserVoByHomeUrl(String homeUrl) {
        return extract(userRepository.loadUserDomainByHomeUrl(homeUrl));
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
    public UserVo registerPermanentUser(UserVo userVo) {
        CommonValidator.valid(userVo, true);

        if(userRepository.loadUserDomainByUsername(userVo.getUsername()) != null){
            throw new TipException(ExceptionEnum.USERNAME_USED);
        }
        if(userRepository.loadUserDomainByEmail(userVo.getEmail()) != null){
            throw new TipException(ExceptionEnum.EMAIL_USED);
        }
        if(userRepository.loadUserDomainByHomeUrl(userVo.getHomeUrl()) != null){
            throw new TipException(ExceptionEnum.HOME_URL_USED);
        }
        userVo.setActive(true);
        userVo.setPermanent(true);
        userVo.setPassword(passwordEncoder.encode(userVo.getPassword()));
        userVo.setSalt(BCrypt.gensalt());
        return save(userVo, 0L);
    }

    @Override
    public void updateProfile(String screenName, String email, UserVo userVo) {
        if(screenName!=null) {
            userVo.setDisplayName(screenName);
        }
        if(email!=null) {
            userVo.setEmail(email);
        }
        CommonValidator.valid(userVo, true);
        update(userVo, userVo.getId());
    }

    @Override
    public void updatePassword(String old_password, String password, UserVo userVo) {
        UserDomain userDomain = userRepository.loadActiveUserDomainById(userVo.getId());
        if(!passwordEncoder.matches(old_password, userDomain.getPassword())){
            throw  new TipException(ExceptionEnum.WRONG_OLD_PASSWORD);
        }
        userVo.setPassword(password);
        CommonValidator.valid(userVo, true);
        userVo.setPassword(passwordEncoder.encode(password));
        update(userVo, userDomain.getId());
    }

    @Override
    public UserVo extractVo(UserDomain userDomain) {
        return extract(userDomain);
    }

    @Override
    protected UserDomain doAssemble(UserVo vo) {
        return UserDomain
                .builder()
                .id(vo.getId())
                .active(vo.getActive())
                .displayName(vo.getDisplayName())
                .email(vo.getEmail())
                .homeUrl(vo.getHomeUrl())
                .permanent(vo.getPermanent())
                .lastLogin(vo.getLastLogin())
                .salt(vo.getSalt())
                .username(vo.getUsername())
                .password(vo.getPassword())
                .build();

    }

    @Override
    protected UserVo doExtract(UserDomain domain) {
        return UserVo
                .builder()
                .id(domain.getId())
                .active(domain.getActive())
                .displayName(domain.getDisplayName())
                .email(domain.getEmail())
                .homeUrl(domain.getHomeUrl())
                .permanent(domain.getPermanent())
                .lastLogin(domain.getLastLogin())
                .salt(domain.getSalt())
                .username(domain.getUsername())
                .password(domain.getPassword())
                .build();
    }

    @Override
    protected UserDomain doUpdate(UserDomain domain) {
        return userRepository.updateUserDomain(domain);
    }

    @Override
    protected UserDomain doSave(UserDomain domain) {
        return userRepository.saveNewUserDomain(domain);
    }
}
