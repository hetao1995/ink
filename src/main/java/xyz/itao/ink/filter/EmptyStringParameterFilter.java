package xyz.itao.ink.filter;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;

/**
 * @author hetao
 * @date 2018-12-20
 * @description
 */
public class EmptyStringParameterFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        filterChain.doFilter(new HttpServletRequestWrapper(request){

            /**
             * 修改此方法主要是因为当RequestMapper中的参数为pojo类型时，
             * 会通过此方法获取所有的请求参数并进行遍历，对pojo属性赋值
             * @return
             */
            @Override
            public Enumeration<String> getParameterNames() {
                Enumeration<String> enumeration = super.getParameterNames();
                ArrayList<String> list = Collections.list(enumeration);
                Iterator iterator = list.iterator();
                while(iterator.hasNext()){
                    String name = (String) iterator.next();
                    if(StringUtils.isBlank(getParameter(name))){
                        iterator.remove();
                    }
                }
                return Collections.enumeration(list);
            }
        }, response);
    }
}
