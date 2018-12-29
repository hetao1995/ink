package xyz.itao.ink.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import xyz.itao.ink.annotation.StopRepeatSubmit;
import xyz.itao.ink.common.RestResponse;
import xyz.itao.ink.constant.WebConstant;
import xyz.itao.ink.utils.EhCacheUtils;
import xyz.itao.ink.utils.InkUtils;
import xyz.itao.ink.utils.IpUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;

/**
 * @author hetao
 * @date 2018-12-29
 * @description 拦截重复提交
 */
@Component
public class StopRepeatSubmitInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean  preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {

        if(!(handler instanceof HandlerMethod)){
            return true;
        }
        // 将handler强转为HandlerMethod
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        // 从方法处理器中获取出要调用的方法
        Method method = handlerMethod.getMethod();
        // 获取出方法上的SysLog注解
        StopRepeatSubmit stopRepeatSubmit = method.getAnnotation(StopRepeatSubmit.class);
        if(stopRepeatSubmit==null){
            return true;
        }
        String key = IpUtils.getIpAddrByRequest(request)+stopRepeatSubmit.key();
        if(EhCacheUtils.get(WebConstant.REPEAT_SUBMIT_CACHE, key) != null){
            InkUtils.returnJson(response, RestResponse.fail("你提交的太频繁，请过一段时间再提交"));
            return false;
        }else{
            EhCacheUtils.put(WebConstant.REPEAT_SUBMIT_CACHE, key, "", stopRepeatSubmit.interval());
        }
        return true;
    }
}
