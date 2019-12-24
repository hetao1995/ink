package xyz.itao.ink.interceptor;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import xyz.itao.ink.common.Props;
import xyz.itao.ink.utils.IpUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Set;

/**
 * @author hetao
 * @date 2018-12-12
 */
@Slf4j
@Component
public class BaseInterceptor implements HandlerInterceptor {
    private static final String USER_AGENT = "user-agent";


    private Props props;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) {
        String uri = request.getRequestURI();

        log.debug("UserAgent: {}", request.getHeader(USER_AGENT));
        log.debug("用户访问地址: {}, 来路地址: {}", uri, IpUtils.getIpAddrByRequest(request));
        Set<String> blockIps = props.getBlockIps();
        return !blockIps.contains(IpUtils.getIpAddrByRequest(request));
    }

    @Override
    @SuppressWarnings("unchecked")
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object o, ModelAndView modelAndView) {
        //一些公共属性
        request.setAttribute("props", props);
        String themJson = props.getThemeOption();
        if (StringUtils.isBlank(themJson)) {
            return;
        }
        Map<String, String> map = JSON.parseObject(themJson, Map.class);
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (StringUtils.isBlank(entry.getValue()) || StringUtils.isBlank(entry.getKey())) {
                continue;
            }
            request.setAttribute(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {

    }

    @Autowired
    public void setProps(Props props) {
        this.props = props;
    }
}
