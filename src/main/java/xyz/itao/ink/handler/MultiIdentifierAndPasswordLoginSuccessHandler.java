package xyz.itao.ink.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import xyz.itao.ink.annotation.SysLog;
import xyz.itao.ink.constant.WebConstant;
import xyz.itao.ink.domain.UserDomain;
import xyz.itao.ink.domain.token.MultiIdentifierAndPasswordAuthenticationToken;
import xyz.itao.ink.service.UserService;
import xyz.itao.ink.service.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author hetao
 * @date 2018-12-01
 * @description 登陆成功的handler
 */
public class MultiIdentifierAndPasswordLoginSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private UserService userService;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String token = userService.getJwtLoginToken((UserDomain) authentication.getPrincipal(), ((MultiIdentifierAndPasswordAuthenticationToken) authentication).getRememberMe());
//        response.setHeader("Authorization", token);
        Cookie cookie = new Cookie("Authorization", token);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(-1);
        if(((MultiIdentifierAndPasswordAuthenticationToken) authentication).getRememberMe()){
            cookie.setMaxAge(WebConstant.REMEMBER_ME_INTERVAL);
        }
        response.addCookie(cookie);
        request.setAttribute(WebConstant.LOGIN_USER, userService.extractVo((UserDomain) authentication.getPrincipal()));
    }
}
