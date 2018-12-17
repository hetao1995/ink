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

import javax.servlet.http.Cookie;
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

    //少于5分钟刷新
    private static final int TOKEN_REFRESH_INTERVAL = 5*60;
    @Autowired
    private UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) {
        DecodedJWT jwt = ((JwtAuthenticationToken) authentication).getToken();
        boolean shouldRefresh = shouldTokenRefresh(jwt.getExpiresAt());
        UserDomain userDomain = (UserDomain) authentication.getPrincipal();
        request.setAttribute(WebConstant.LOGIN_USER, userService.extractVo(userDomain));
        if (shouldRefresh) {
            String newToken = userService.getJwtLoginToken(userDomain, false);
//            response.setHeader("Authorization", newToken);
            Cookie cookie = new Cookie("Authorization", newToken);
            cookie.setHttpOnly(true);
            cookie.setMaxAge(-1);
            response.addCookie(cookie);
        }
    }

    /**
     * 少于tokenRefreshToken时候刷新
     * @param expiresAt:过期时间
     * @return
     */
    protected boolean shouldTokenRefresh(Date expiresAt) {
        LocalDateTime expiresTime = LocalDateTime.ofInstant(expiresAt.toInstant(), ZoneId.systemDefault());
        return LocalDateTime.now().minusSeconds(TOKEN_REFRESH_INTERVAL).isAfter(expiresTime);
    }

}
