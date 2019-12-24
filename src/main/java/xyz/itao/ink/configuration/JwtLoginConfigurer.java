package xyz.itao.ink.configuration;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import xyz.itao.ink.filter.JwtAuthenticationFilter;
import xyz.itao.ink.filter.MultiIdentifierAndPasswordAuthenticationFilter;
import xyz.itao.ink.handler.HttpStatusLoginFailureHandler;

/**
 * 初始化和配置JWTFilter
 *
 * @author hetao
 * @date 2018-12-01
 */
public class JwtLoginConfigurer<T extends JwtLoginConfigurer<T, B>, B extends HttpSecurityBuilder<B>> extends AbstractHttpConfigurer<T, B> {

    private JwtAuthenticationFilter authFilter;

    JwtLoginConfigurer() {
        this.authFilter = new JwtAuthenticationFilter();
    }

    @Override
    public void configure(B http) {
        authFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
        authFilter.setAuthenticationFailureHandler(new HttpStatusLoginFailureHandler());
        //将filter放到muti后
        JwtAuthenticationFilter filter = postProcess(authFilter);
        http.addFilterAfter(filter, MultiIdentifierAndPasswordAuthenticationFilter.class);
    }

    /**
     * 设置匿名用户可访问url
     *
     * @param urls 访问的url
     * @return configure
     */
    JwtLoginConfigurer<T, B> permissiveRequestUrls(String... urls) {
        authFilter.setPermissiveUrl(urls);
        return this;
    }

    /**
     * token检查通过
     *
     * @param successHandler 通过之后的处理器
     * @return this
     */
    JwtLoginConfigurer<T, B> tokenValidSuccessHandler(AuthenticationSuccessHandler successHandler) {
        authFilter.setAuthenticationSuccessHandler(successHandler);
        return this;
    }

}
