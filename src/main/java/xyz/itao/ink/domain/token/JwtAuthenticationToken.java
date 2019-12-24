package xyz.itao.ink.domain.token;

import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import xyz.itao.ink.domain.UserDomain;


import java.util.Collection;
import java.util.Collections;

/**
 * @author hetao
 * @date 2018-12-03
 */
public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private UserDomain principal;
    private DecodedJWT token;

    public JwtAuthenticationToken(DecodedJWT token) {
        super(Collections.emptyList());
        this.token = token;
    }

    public JwtAuthenticationToken(UserDomain userDomain, DecodedJWT token, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = userDomain;
        this.token = token;
        this.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    public DecodedJWT getToken() {
        return token;
    }
}
