package xyz.itao.ink;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

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
