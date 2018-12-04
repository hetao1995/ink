package xyz.itao.ink;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

/**
 * @author hetao
 * @date 2018-11-29
 * @description
 */

@SpringBootApplication
@MapperScan("xyz.itao.ink.dao")
public class InkApplication {

    public static void main(String[] args) {
        SpringApplication.run(InkApplication.class, args);
    }
}
