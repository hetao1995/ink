package xyz.itao.ink.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 拦截重复提交,key为拦截的键，同一key不能重复提交，通过ip判断是否重复提交。interval是间隔时间多久
 *
 * @author hetao
 * @date 2018-12-06
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface StopRepeatSubmit {
    String key();

    int interval() default 30;
}
