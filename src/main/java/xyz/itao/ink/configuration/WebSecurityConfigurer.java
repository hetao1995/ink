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
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.header.Header;
import org.springframework.security.web.header.writers.StaticHeadersWriter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import xyz.itao.ink.filter.JwtAuthenticationFilter;
import xyz.itao.ink.filter.OptionRequestFilter;
import xyz.itao.ink.handler.MultiIdentifierAndPasswordLoginSuccessHandler;
import xyz.itao.ink.handler.JwtLoginSuccessHandler;
import xyz.itao.ink.handler.TokenClearLogoutHandler;
import xyz.itao.ink.provider.JwtAuthenticationProvider;
import xyz.itao.ink.provider.MultiIdentifierAndPasswordAuthenticationProvider;

import java.util.Arrays;

/**
 * @author hetao
 * @date 2018-11-30
 * @description
 */
@EnableWebSecurity(debug = true)
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter{
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                //静态资源访问无需认证
                .antMatchers("/**/*.js", "/lang/*.json", "/**/*.css", "/**/*.js", "/**/*.map", "/**/*.html", "/**/*.png","/**/*.svg","/**/*.otf","/**/*.eot", "/**/*.ttf","/**/*.woff","/**/*.woff2").permitAll()
                .antMatchers("/*").permitAll()
//                .antMatchers("/admin/login").permitAll()
                //admin开头的请求，需要admin权限
                .antMatchers("/admin/**").hasAnyRole("ADMIN")
                //需登陆才能访问的url
                .antMatchers("/backend/**").hasRole("USER")
                //默认其它的请求都需要认证
                .anyRequest().authenticated()
//                .anyRequest().permitAll()
                .and()
                //CRSF禁用，因为不使用session
                .csrf().disable()
                //禁用session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .formLogin()
                .loginPage("/login")
//                .failureForwardUrl("/login?error")
//                .successForwardUrl("/admin/index")
                //拒绝form登录
//                .disable()
                .and()
                //支持跨域
                .cors()
                //添加header设置，支持跨域和ajax请求
                .and()
                .headers().addHeaderWriter(new StaticHeadersWriter(Arrays.asList(
                new Header("Access-control-Allow-Origin","*"))))
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
//              .logoutUrl("/logout")   //默认就是"/logout"
                //logout时清除token
                .addLogoutHandler(tokenClearLogoutHandler())
                //logout成功后返回200
                .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler())
                .and()
                .sessionManagement().disable();
        // 禁用缓存
        http.headers().frameOptions().sameOrigin().cacheControl();
    }
    //配置provider
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
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
    protected AuthenticationProvider multiIdentifierAndPasswordAuthenticationProvider() throws Exception{
        return new MultiIdentifierAndPasswordAuthenticationProvider(passwordEncoder());
    }
    @Bean("PasswordEncoder")
    public PasswordEncoder passwordEncoder(){
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
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET","POST","HEAD", "OPTION","DELETE","PUT"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.addExposedHeader("Authorization");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
