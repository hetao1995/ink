package xyz.itao.ink.interceptor;

import com.alibaba.fastjson.JSON;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import xyz.itao.ink.common.Commons;
import xyz.itao.ink.common.Props;
import xyz.itao.ink.constant.WebConstant;
import xyz.itao.ink.domain.vo.UserVo;
import xyz.itao.ink.service.OptionService;
import xyz.itao.ink.service.UserService;
import xyz.itao.ink.utils.IpUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author hetao
 * @date 2018-12-12
 * @description
 */
@Slf4j
@Component
public class BaseInterceptor implements HandlerInterceptor {
    private static final String USER_AGENT = "user-agent";

    @Autowired
    private UserService userService;

    @Autowired
    private OptionService optionService;


    @Autowired
    private Commons commons;

    @Autowired
    private Props props;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        String contextPath = request.getContextPath();
        // System.out.println(contextPath);
        String uri = request.getRequestURI();

        log.debug("UserAgent: {}", request.getHeader(USER_AGENT));
        log.debug("用户访问地址: {}, 来路地址: {}", uri, IpUtils.getIpAddrByRequest(request));


        request.setAttribute("props", props);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object o, ModelAndView modelAndView) throws Exception {
//        OptionVo ov = optionService.getOptionByName("site_record");
        request.setAttribute("commons", commons);//一些工具类和公共方法
        String themJson = props.get("theme_default_options", "");
        System.out.println("themJson"+themJson);
        Map<String, String> map = JSON.parseObject(themJson, Map.class);
        for(Map.Entry<String, String> entry : map.entrySet()){
            request.setAttribute(entry.getKey(), entry.getValue());
        }
//        httpServletRequest.setAttribute("option", ov);
//        httpServletRequest.setAttribute("adminCommons", adminCommons);
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
