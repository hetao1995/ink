package xyz.itao.ink.handler;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import xyz.itao.ink.constant.WebConstant;
import xyz.itao.ink.utils.InkUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登陆失败的handler, 如果是admin开头，则返回登录页面，如果不是，则继续处理
 *
 * @author hetao
 * @date 2018-12-01
 */
public class HttpStatusLoginFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        String uri = request.getRequestURI();
        InkUtils.removeCookie(request, response, WebConstant.AUTHORIZATION);
        if (StringUtils.startsWith(uri, WebConstant.ADMIN_URI)) {
            request.getRequestDispatcher(WebConstant.LOGIN_URI).forward(request, response);
        } else {
            request.getRequestDispatcher(uri).forward(request, response);
        }
//        response.setStatus(HttpStatus.UNAUTHORIZED.value());

    }


}
