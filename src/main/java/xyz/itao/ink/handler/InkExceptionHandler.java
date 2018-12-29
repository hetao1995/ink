package xyz.itao.ink.handler;

import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class InkExceptionHandler {
    @ExceptionHandler(value = {TipException.class, InnerException.class})
    @ResponseBody
    public RestResponse<?> handlerException(Exception e){
        if(e instanceof TipException){
            TipException exception = (TipException)e;
            return RestResponse.fail(exception.getCode(), exception.getMessage());
        }else {
            InnerException exception = (InnerException)e;
            log.error("出现了内部错误,{}",e);
            return RestResponse.fail(exception.getCode(), exception.getMessage());
        }
    }

}
