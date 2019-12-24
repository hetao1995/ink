package xyz.itao.ink.provider;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.NonceExpiredException;
import xyz.itao.ink.domain.UserDomain;
import xyz.itao.ink.domain.token.JwtAuthenticationToken;
import xyz.itao.ink.service.UserService;

import java.util.Calendar;

/**
 * @author hetao
 * @date 2018-12-01
 */
@Slf4j
public class JwtAuthenticationProvider implements AuthenticationProvider {
    private UserService userService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        DecodedJWT jwt = ((JwtAuthenticationToken) authentication).getToken();
        if (jwt.getExpiresAt().before(Calendar.getInstance().getTime())) {
            log.debug("jwt token 失败", jwt);
            throw new NonceExpiredException("Token expires");
        }
        // 获取jwt中的id
        Long id = Long.valueOf(jwt.getSubject());
        UserDomain userDomain = userService.loadUserDomainById(id);
        if (userDomain == null || userDomain.getSalt() == null) {
            log.debug("没有找到userDomain或者userDomian中salt为空 ", userDomain);
            throw new NonceExpiredException("Token expires");
        }
        // 获取jwt中的盐
        String encryptSalt = userDomain.getSalt();
        try {
            Algorithm algorithm = Algorithm.HMAC256(encryptSalt);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withSubject(String.valueOf(id))
                    .build();
            verifier.verify(jwt.getToken());
        } catch (Exception e) {
            log.debug("jwt 验证失败 ", jwt);
            throw new BadCredentialsException("JWT token verify fail", e);
        }
        // 返回jwtToken
        return new JwtAuthenticationToken(userDomain, jwt, userDomain.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(JwtAuthenticationToken.class);
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
