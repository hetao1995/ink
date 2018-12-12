package xyz.itao.ink.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import xyz.itao.ink.domain.token.MultiIdentifierAndPasswordAuthenticationToken;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;


/**
 * @author hetao
 * @date 2018-11-30
 * @description
 */
public class MultiIdentifierAndPasswordAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    public MultiIdentifierAndPasswordAuthenticationFilter() {
        //拦截url为 "/admin/login" 的POST请求
        super(new AntPathRequestMatcher("/login", "POST"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        //从json中获取identifier和password
        String body = StreamUtils.copyToString(request.getInputStream(), Charset.forName("UTF-8"));
        String identifier = null, password = null;
        Boolean rememberMe = false;
        if (StringUtils.hasText(body)) {
            JSONObject jsonObj = JSON.parseObject(body);
            identifier = jsonObj.getString("identifier");
            password = jsonObj.getString("password");
            rememberMe = jsonObj.getBoolean("rememberMe");
        }

        if (identifier == null){
            identifier = "";
        }
        if (password == null){
            password = "";
        }
        if(rememberMe == null){
            rememberMe = false;
        }
        identifier = identifier.trim();
        //封装到token中提交
        MultiIdentifierAndPasswordAuthenticationToken authRequest = new MultiIdentifierAndPasswordAuthenticationToken(identifier, password, rememberMe);

        return this.getAuthenticationManager().authenticate(authRequest);

    }

}

