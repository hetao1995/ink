package xyz.itao.ink.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import xyz.itao.ink.annotation.SysLog;
import xyz.itao.ink.constant.WebConstant;
import xyz.itao.ink.domain.DomainFactory;
import xyz.itao.ink.domain.UserDomain;
import xyz.itao.ink.utils.IpUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * 拦截SysLog注解，记录日志
 *
 * @author hetao
 * @date 2018-12-23
 */
@Slf4j
@Component
public class SysLogInterceptor extends HandlerInterceptorAdapter {
    private final DomainFactory domainFactory;

    @Autowired
    public SysLogInterceptor(DomainFactory domainFactory) {
        this.domainFactory = domainFactory;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        // 将handler强转为HandlerMethod
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        // 从方法处理器中获取出要调用的方法
        Method method = handlerMethod.getMethod();
        // 获取出方法上的SysLog注解
        SysLog sysLog = method.getAnnotation(SysLog.class);
        if (sysLog == null) {
            return true;
        }
        UserDomain userDomain = (UserDomain) request.getAttribute(WebConstant.LOGIN_USER);
        Long userId = userDomain == null ? 0 : userDomain.getId();

        domainFactory
                .createLogDomain()
                .setCreateBy(userId)
                .setUpdateBy(userId)
                .setIp(IpUtils.getIpAddrByRequest(request))
                .setAgent(request.getHeader("User-Agent"))
                .setData(request.getParameterMap().toString())
                .setAction(sysLog.value())
                .setUserId(userId)
                .save();
        return true;
    }
}
