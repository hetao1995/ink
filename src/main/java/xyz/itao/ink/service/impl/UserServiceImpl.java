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
import xyz.itao.ink.domain.DomainFactory;
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
    DomainFactory domainFactory;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Override
    public UserDomain registerTemporaryUser(UserVo userVo) {
        CommonValidator.valid(userVo, false);
        userVo.setActive(true);
        userVo.setPermanent(false);
        return domainFactory
                .createUserDomain()
                .assemble(userVo)
                .setUpdateBy(0L)
                .setCreateBy(0L)
                .save();
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
    public UserDomain registerPermanentUser(UserVo userVo) {
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
        return assemble(save(userVo, 0L));
    }

    @Override
    public void updateProfile(String screenName, String email, UserDomain userDomain) {
        UserVo userVo = UserVo.builder().displayName(screenName).email(email).build();
        CommonValidator.valid(userVo, true);
        if(screenName!=null) {
            userDomain.setDisplayName(screenName);
        }
        if(email!=null) {
            userDomain.setEmail(email);
        }
        userDomain
                .setUpdateBy(userDomain.getId())
                .updateById();
    }

    @Override
    public void updatePassword(String old_password, String password, UserDomain userDomain) {
        if(!passwordEncoder.matches(old_password, userDomain.getPassword())){
            throw  new TipException(ExceptionEnum.WRONG_OLD_PASSWORD);
        }
        UserVo userVo = UserVo.builder().password(password).build();
        CommonValidator.valid(userVo, true);
        userDomain
                .setPassword(passwordEncoder.encode(password))
                .setUpdateBy(userDomain.getId())
                .updateById();
    }

    @Override
    public UserVo extractVo(UserDomain userDomain) {
        return extract(userDomain);
    }

    @Override
    protected UserDomain doAssemble(UserVo vo) {
        return domainFactory.createUserDomain().assemble(vo);

    }

    @Override
    protected UserVo doExtract(UserDomain domain) {
        return domain.vo();
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
