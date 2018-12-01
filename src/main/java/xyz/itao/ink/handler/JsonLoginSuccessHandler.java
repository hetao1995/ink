package xyz.itao.ink.handler;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author hetao
 * @date 2018-12-01
 * @description 登陆成功的handler
 */
public class JsonLoginSuccessHandler implements AuthenticationSuccessHandler {

    private JwtUserService jwtUserService;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String token = jwtUserService.saveUserLoginInfo((UserDetails)authentication.getPrincipal());
        response.setHeader("Authorization", token);
    }
}
