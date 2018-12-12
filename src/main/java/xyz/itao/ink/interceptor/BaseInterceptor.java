package xyz.itao.ink.interceptor;

import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import xyz.itao.ink.common.Commons;
import xyz.itao.ink.constant.WebConstant;
import xyz.itao.ink.domain.vo.UserVo;
import xyz.itao.ink.service.OptionService;
import xyz.itao.ink.service.UserService;
import xyz.itao.ink.utils.IpUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        String contextPath = request.getContextPath();
        // System.out.println(contextPath);
        String uri = request.getRequestURI();

        log.debug("UserAgent: {}", request.getHeader(USER_AGENT));
        log.debug("用户访问地址: {}, 来路地址: {}", uri, IpUtils.getIpAddrByRequest(request));


        //请求拦截处理
        UserVo userVo = (UserVo) request.getAttribute(WebConstant.LOGIN_USER);

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
//        OptionVo ov = optionService.getOptionByName("site_record");
        httpServletRequest.setAttribute("commons", commons);//一些工具类和公共方法
//        httpServletRequest.setAttribute("option", ov);
//        httpServletRequest.setAttribute("adminCommons", adminCommons);
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
