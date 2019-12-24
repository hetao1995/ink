package xyz.itao.ink.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.ForwardLogoutSuccessHandler;
import org.springframework.security.web.header.Header;
import org.springframework.security.web.header.writers.StaticHeadersWriter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import xyz.itao.ink.constant.WebConstant;
import xyz.itao.ink.filter.OptionRequestFilter;
import xyz.itao.ink.handler.MultiIdentifierAndPasswordLoginSuccessHandler;
import xyz.itao.ink.handler.JwtLoginSuccessHandler;
import xyz.itao.ink.handler.TokenClearLogoutHandler;
import xyz.itao.ink.provider.JwtAuthenticationProvider;
import xyz.itao.ink.provider.MultiIdentifierAndPasswordAuthenticationProvider;

import java.util.Arrays;
import java.util.Collections;

/**
 * spring security配置
 *
 * @author hetao
 * @date 2018-11-30
 */
@EnableWebSecurity(debug = false)
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                //静态资源访问无需认证
                .antMatchers("/**/*.js", "/lang/*.json", "/**/*.css", "/**/*.js", "/**/*.map", "/**/*.html", "/**/*.png", "/**/*.svg", "/**/*.otf", "/**/*.eot", "/**/*.ttf", "/**/*.woff", "/**/*.woff2").permitAll()
                .antMatchers("/admin/**").hasAnyRole("ADMIN")
                .antMatchers("/*", "/themes/**/static/**", "/upload/**", WebConstant.CDN_URI + "/**").permitAll()
                .and()
                //CRSF禁用，因为不使用session
                .csrf().disable()
                //禁用session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .formLogin()
                .loginPage("/login")
                .and()
                //支持跨域
                .cors()
                //添加header设置，支持跨域和ajax请求
                .and()
                .headers().addHeaderWriter(new StaticHeadersWriter(Collections.singletonList(
                new Header("Access-control-Allow-Origin", "*"))))
//                new Header("Access-Control-Expose-Headers","Authorization"))))
                .and()
                //拦截OPTIONS请求，直接返回header
                .addFilterAfter(new OptionRequestFilter(), CorsFilter.class)
                //添加登录filter
                .apply(new MultiIdentifierAndPasswordLoginConfigurer<>()).loginSuccessHandler(jsonLoginSuccessHandler())
                .and()
                //添加token的filter
                .apply(new JwtLoginConfigurer<>()).tokenValidSuccessHandler(jwtRefreshSuccessHandler()).permissiveRequestUrls("/logout")
                .and()
                //使用默认的logoutFilter
                .logout()
                //logout时清除token
                .addLogoutHandler(tokenClearLogoutHandler())
                //logout成功后返回200
                .logoutSuccessHandler(new ForwardLogoutSuccessHandler("/"))
                .and()
                .sessionManagement().disable();
        http.sessionManagement().enableSessionUrlRewriting(false);
    }

    /**
     * 配置provider
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(multiIdentifierAndPasswordAuthenticationProvider()).authenticationProvider(jwtAuthenticationProvider());
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean("jwtAuthenticationProvider")
    protected AuthenticationProvider jwtAuthenticationProvider() {
        return new JwtAuthenticationProvider();
    }

    @Bean("multiIdentifierAndPasswordAuthenticationProvider")
    protected AuthenticationProvider multiIdentifierAndPasswordAuthenticationProvider() {
        return new MultiIdentifierAndPasswordAuthenticationProvider(passwordEncoder());
    }

    @Bean("PasswordEncoder")
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean("jsonLoginSuccessHandler")
    protected MultiIdentifierAndPasswordLoginSuccessHandler jsonLoginSuccessHandler() {
        return new MultiIdentifierAndPasswordLoginSuccessHandler();
    }

    @Bean
    protected JwtLoginSuccessHandler jwtRefreshSuccessHandler() {
        return new JwtLoginSuccessHandler();
    }

    @Bean
    protected TokenClearLogoutHandler tokenClearLogoutHandler() {
        return new TokenClearLogoutHandler();
    }

    @Bean
    protected CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Collections.singletonList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "HEAD", "OPTION", "DELETE", "PUT"));
        configuration.setAllowedHeaders(Collections.singletonList("*"));
        configuration.addExposedHeader("Authorization");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
