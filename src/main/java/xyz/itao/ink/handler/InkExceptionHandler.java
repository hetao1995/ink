package xyz.itao.ink.handler;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import xyz.itao.ink.common.RestResponse;
import xyz.itao.ink.exception.InnerException;
import xyz.itao.ink.exception.TipException;

import javax.servlet.http.HttpServletRequest;

/**
 * @author hetao
 * @date 2018-12-27
 * @description
 */
@ControllerAdvice
public class InkExceptionHandler {
    @ExceptionHandler(TipException.class)
    @ResponseBody
    public RestResponse<?> handlerException(Exception e){
        TipException exception = (TipException)e;
        return RestResponse.fail(exception.getCode(), exception.getMessage());
    }

    @ExceptionHandler(InnerException.class)
    public String handleException(Exception e, HttpServletRequest request){
        request.setAttribute("javax.servlet.error.status_code",500);
        request.setAttribute("error", e);
        return "forward:/error";
    }
}
