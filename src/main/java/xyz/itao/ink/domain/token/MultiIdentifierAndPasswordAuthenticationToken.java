package xyz.itao.ink.domain.token;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import xyz.itao.ink.domain.UserDomain;

import java.util.Collection;
import java.util.Collections;

/**
 * @author hetao
 * @date 2018-12-03
 * @description
 */
public class MultiIdentifierAndPasswordAuthenticationToken extends AbstractAuthenticationToken {
    private UserDomain principal ;
    private String identifier;
    private String password;
    private Boolean rememberMe;



    public MultiIdentifierAndPasswordAuthenticationToken(String identifier, String password, Boolean rememberMe){
        super(Collections.emptyList());
        this.identifier = identifier;
        this.password = password;
        this.rememberMe = rememberMe;
    }

    public MultiIdentifierAndPasswordAuthenticationToken(UserDomain userDomain, Boolean rememberMe, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = userDomain;
        this.rememberMe = rememberMe;
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

    public String getIdentifier() {
        return identifier;
    }

    public String getPassword()
    {
        return password;
    }

    public Boolean getRememberMe() {
        return rememberMe;
    }

}
