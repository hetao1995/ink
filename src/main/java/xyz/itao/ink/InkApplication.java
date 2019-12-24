package xyz.itao.ink;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import springfox.documentation.annotations.Cacheable;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.SessionCookieConfig;
import javax.servlet.SessionTrackingMode;
import java.util.Collections;

/**
 * @author hetao
 * @date 2018-11-29
 */

@SpringBootApplication
@MapperScan("xyz.itao.ink.dao")
@EnableCaching
public class InkApplication {
    public static void main(String[] args) {
        SpringApplication.run(InkApplication.class, args);
    }
}
