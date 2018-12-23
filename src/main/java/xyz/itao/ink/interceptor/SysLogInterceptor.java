package xyz.itao.ink.interceptor;

import org.springframework.lang.Nullable;
import org.springframework.messaging.handler.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import xyz.itao.ink.annotation.SysLog;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @author hetao
 * @date 2018-12-23
 * @description 拦截SysLog注解，记录日志
 */
public class SysLogInterceptor extends HandlerInterceptorAdapter {
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           @Nullable ModelAndView modelAndView) throws Exception {
        // 将handler强转为HandlerMethod
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        // 从方法处理器中获取出要调用的方法
        Method method = handlerMethod.getMethod();
        // 获取出方法上的SysLog注解
        SysLog sysLog = method.getAnnotation(SysLog.class);
        if(sysLog==null){
            return ;
        }
        // todo
    }
}
