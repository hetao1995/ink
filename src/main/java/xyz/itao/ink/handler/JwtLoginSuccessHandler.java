package xyz.itao.ink.handler;

import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import xyz.itao.ink.constant.WebConstant;
import xyz.itao.ink.domain.UserDomain;
import xyz.itao.ink.domain.token.JwtAuthenticationToken;
import xyz.itao.ink.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.file.Watchable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * @author hetao
 * @date 2018-12-01
 * @description
 */
public class JwtLoginSuccessHandler implements AuthenticationSuccessHandler {

    //刷新间隔5分钟
    private static final int tokenRefreshInterval = 300;
    @Autowired
    private UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) {
        DecodedJWT jwt = ((JwtAuthenticationToken) authentication).getToken();
        boolean shouldRefresh = shouldTokenRefresh(jwt.getIssuedAt());
        UserDomain userDomain = (UserDomain) authentication.getPrincipal();
        request.setAttribute(WebConstant.LOGIN_USER, userDomain);
        if (shouldRefresh) {
            String newToken = userService.getJwtLoginToken(userDomain);
            response.setHeader("Authorization", newToken);
        }
    }

    protected boolean shouldTokenRefresh(Date issueAt) {
        LocalDateTime issueTime = LocalDateTime.ofInstant(issueAt.toInstant(), ZoneId.systemDefault());
        return LocalDateTime.now().minusSeconds(tokenRefreshInterval).isAfter(issueTime);
    }

}
