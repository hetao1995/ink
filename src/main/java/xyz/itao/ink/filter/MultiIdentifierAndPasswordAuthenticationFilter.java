package xyz.itao.ink.filter;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import xyz.itao.ink.domain.token.MultiIdentifierAndPasswordAuthenticationToken;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * @author hetao
 * @date 2018-11-30
 */
public class MultiIdentifierAndPasswordAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    public MultiIdentifierAndPasswordAuthenticationFilter() {
        //拦截url为 "/admin/login" 的POST请求
        super(new AntPathRequestMatcher("/login", "POST"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        //从请求参数中获取账号、密码、rememberMe
        String identifier = request.getParameter("identifier"), password = request.getParameter("password");
        Boolean rememberMe = Boolean.valueOf(request.getParameter("remember_me"));

        if (identifier == null) {
            identifier = "";
        }
        if (password == null) {
            password = "";
        }
        identifier = identifier.trim();
        //封装到token中提交
        MultiIdentifierAndPasswordAuthenticationToken authRequest = new MultiIdentifierAndPasswordAuthenticationToken(identifier, password, rememberMe);

        return this.getAuthenticationManager().authenticate(authRequest);

    }

}

