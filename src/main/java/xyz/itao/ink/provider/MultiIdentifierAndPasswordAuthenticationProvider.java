package xyz.itao.ink.provider;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import xyz.itao.ink.domain.UserDomain;
import xyz.itao.ink.domain.token.MultiIdentifierAndPasswordAuthenticationToken;
import xyz.itao.ink.service.UserService;
import xyz.itao.ink.utils.PatternUtils;

/**
 * @author hetao
 * @date 2018-12-03
 */
@Slf4j
public class MultiIdentifierAndPasswordAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    UserService userService;
    private PasswordEncoder passwordEncoder;
    public MultiIdentifierAndPasswordAuthenticationProvider(PasswordEncoder passwordEncoder){
        this.passwordEncoder = passwordEncoder;
    }
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String identifier = ((MultiIdentifierAndPasswordAuthenticationToken) authentication).getIdentifier();
        String password = ((MultiIdentifierAndPasswordAuthenticationToken) authentication).getPassword();
        UserDomain userDomain;
        if(PatternUtils.isEmail(identifier)){
            userDomain = userService.loadUserDomainByEmail(identifier);
        }else if(PatternUtils.isURL(identifier)){
            userDomain = userService.loadUserDomainByHomeUrl(identifier);
        }else{
            userDomain = userService.loadUserDomainByUsername(identifier);
        }
        if(userDomain == null){
            log.debug("没有找到identifier对应的user", identifier);
            throw new BadCredentialsException("Fail to find the user");
        }
        if(!passwordEncoder.matches(password, userDomain.getPassword())){
            log.debug("密码错误 ", identifier, password);
            throw new BadCredentialsException("Fail to check password");
        }
        return new MultiIdentifierAndPasswordAuthenticationToken(userDomain, ((MultiIdentifierAndPasswordAuthenticationToken) authentication).getRememberMe(),userDomain.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(MultiIdentifierAndPasswordAuthenticationToken.class);
    }
}
