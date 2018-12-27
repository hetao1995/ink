package xyz.itao.ink.configuration;

import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.boot.web.server.ErrorPageRegistry;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import xyz.itao.ink.exception.InnerException;
import xyz.itao.ink.exception.TipException;

/**
 * @author hetao
 * @date 2018-12-27
 * @description
 */
@Component
public class CustomerErrorPageRegistrar implements ErrorPageRegistrar {
    @Override
    public void registerErrorPages(ErrorPageRegistry registry) {
        //具体的错误码错误异常页面
        ErrorPage e404 = new ErrorPage(HttpStatus.NOT_FOUND,"/error_404");
        ErrorPage e500 = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR,"/error_500");
        ErrorPage e400 = new ErrorPage(HttpStatus.BAD_REQUEST, "/error_400");
        //指定具体异常的错误定制页面
        ErrorPage innerPage = new ErrorPage(InnerException.class,"/error_500");
        registry.addErrorPages(e404,e500,e400, innerPage);
    }
}
