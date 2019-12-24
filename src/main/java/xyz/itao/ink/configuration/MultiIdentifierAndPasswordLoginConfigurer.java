package xyz.itao.ink.configuration;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy;
import xyz.itao.ink.filter.MultiIdentifierAndPasswordAuthenticationFilter;
import xyz.itao.ink.handler.HttpStatusLoginFailureHandler;

/**
 * 多种登录方式登录验证配置
 *
 * @author hetao
 * @date 2018-12-01
 */
public class MultiIdentifierAndPasswordLoginConfigurer<T extends MultiIdentifierAndPasswordLoginConfigurer<T, B>, B extends HttpSecurityBuilder<B>> extends AbstractHttpConfigurer<T, B> {

    private MultiIdentifierAndPasswordAuthenticationFilter authFilter;

    MultiIdentifierAndPasswordLoginConfigurer() {
        this.authFilter = new MultiIdentifierAndPasswordAuthenticationFilter();
    }

    @Override
    public void configure(B http) {
        authFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
        authFilter.setAuthenticationFailureHandler(new HttpStatusLoginFailureHandler());
        authFilter.setSessionAuthenticationStrategy(new NullAuthenticatedSessionStrategy());
        //将filter放到logoutFilter之前
        MultiIdentifierAndPasswordAuthenticationFilter filter = postProcess(authFilter);
        http.addFilterBefore(filter, LogoutFilter.class);
    }

    MultiIdentifierAndPasswordLoginConfigurer<T, B> loginSuccessHandler(AuthenticationSuccessHandler authSuccessHandler) {
        authFilter.setAuthenticationSuccessHandler(authSuccessHandler);
        return this;
    }

}
