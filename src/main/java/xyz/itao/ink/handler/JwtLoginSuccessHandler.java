package xyz.itao.ink.handler;

import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import xyz.itao.ink.constant.WebConstant;
import xyz.itao.ink.domain.UserDomain;
import xyz.itao.ink.domain.token.JwtAuthenticationToken;
import xyz.itao.ink.service.UserService;
import xyz.itao.ink.utils.InkUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * @author hetao
 * @date 2018-12-01
 */
public class JwtLoginSuccessHandler implements AuthenticationSuccessHandler {

    /**
     * 少于30分钟刷新
     */
    private static final int TOKEN_REFRESH_INTERVAL = 30 * 60;

    private UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) {
        DecodedJWT jwt = ((JwtAuthenticationToken) authentication).getToken();
        boolean shouldRefresh = shouldTokenRefresh(jwt.getExpiresAt());
        UserDomain userDomain = (UserDomain) authentication.getPrincipal();
        request.setAttribute(WebConstant.LOGIN_USER, userDomain);
        if (shouldRefresh) {
            String newToken = userService.getJwtLoginToken(userDomain, false);
            InkUtils.setCookie(response, WebConstant.AUTHORIZATION, newToken, -1, "/");
        }
    }

    /**
     * 少于tokenRefreshToken时候刷新
     *
     * @param expiresAt:过期时间
     * @return 是否刷新
     */
    private boolean shouldTokenRefresh(Date expiresAt) {
        LocalDateTime expiresTime = LocalDateTime.ofInstant(expiresAt.toInstant(), ZoneId.systemDefault());
        return LocalDateTime.now().plusSeconds(TOKEN_REFRESH_INTERVAL).isAfter(expiresTime);
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
