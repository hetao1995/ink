package xyz.itao.ink.configuration;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy;
import xyz.itao.ink.filter.UsernamePasswordAuthenticationFilter;
import xyz.itao.ink.handler.HttpStatusLoginFailureHandler;

/**
 * @author hetao
 * @date 2018-12-01
 * @description
 */
public class JsonLoginConfigurer<T extends JsonLoginConfigurer<T, B>, B extends HttpSecurityBuilder<B>> extends AbstractHttpConfigurer<T, B> {

    private UsernamePasswordAuthenticationFilter authFilter;

    public JsonLoginConfigurer() {
        this.authFilter = new UsernamePasswordAuthenticationFilter();
    }

    @Override
    public void configure(B http) throws Exception {
        authFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
        authFilter.setAuthenticationFailureHandler(new HttpStatusLoginFailureHandler());
        authFilter.setSessionAuthenticationStrategy(new NullAuthenticatedSessionStrategy());

        UsernamePasswordAuthenticationFilter filter = postProcess(authFilter);
        http.addFilterAfter(filter, LogoutFilter.class);
    }

    public JsonLoginConfigurer<T,B> loginSuccessHandler(AuthenticationSuccessHandler authSuccessHandler){
        authFilter.setAuthenticationSuccessHandler(authSuccessHandler);
        return this;
    }

}
