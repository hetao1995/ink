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
import xyz.itao.ink.utils.InkUtils;

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
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)  {
        String token = userService.getJwtLoginToken((UserDomain) authentication.getPrincipal(), ((MultiIdentifierAndPasswordAuthenticationToken) authentication).getRememberMe());
        if(((MultiIdentifierAndPasswordAuthenticationToken) authentication).getRememberMe()){
            InkUtils.setCookie(response, WebConstant.AUTHORIZATION, token, WebConstant.REMEMBER_ME_INTERVAL, "/");
        }else{
            InkUtils.setCookie(response, WebConstant.AUTHORIZATION, token, -1, "/");
        }
        request.setAttribute(WebConstant.LOGIN_USER,  authentication.getPrincipal());
    }
}
