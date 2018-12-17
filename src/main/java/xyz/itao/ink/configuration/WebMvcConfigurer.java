package xyz.itao.ink.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.FormContentFilter;
import org.springframework.web.filter.HttpPutFormContentFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import xyz.itao.ink.interceptor.BaseInterceptor;

/**
 * @author hetao
 * @date 2018-12-08
 * @description
 */
@Configuration
public class WebMvcConfigurer extends WebMvcConfigurationSupport {
    @Autowired
    BaseInterceptor baseInterceptor;
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/user/**").addResourceLocations("classpath:/templates/themes/default/static/");
        registry.addResourceHandler("/admin/**").addResourceLocations("classpath:/templates/admin/static/");
        super.addResourceHandlers(registry);
    }

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(baseInterceptor);
        super.addInterceptors(registry);
    }

    /**
     * 解决put、delete form传值
     * @return
     */
    @Bean
    public FormContentFilter formContentFilter(){
        return new FormContentFilter();
    }
}
