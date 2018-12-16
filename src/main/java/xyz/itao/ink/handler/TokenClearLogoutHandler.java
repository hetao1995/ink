package xyz.itao.ink.handler;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import xyz.itao.ink.constant.WebConstant;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author hetao
 * @date 2018-12-01
 * @description 登出的时候清除token
 */

public class TokenClearLogoutHandler implements LogoutHandler {

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
//        response.setHeader("Authorization", "");
        request.removeAttribute(WebConstant.LOGIN_USER);
        Cookie[] cookies = request.getCookies();
        if(cookies==null){
            return;
        }
        for(Cookie cookie : cookies){
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }
    }

}
