package com.base.sbc.config.i18n;

import com.base.sbc.config.JacksonHttpMessageConverter;
import com.base.sbc.config.RequestInterceptor;
import com.base.sbc.config.adviceadapter.EnumConverter;
import com.base.sbc.config.common.CustomStrictHttpFirewall;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import javax.annotation.Resource;
import java.util.List;
import java.util.Locale;

/**
 * @author Fred
 * @data 创建时间:2020/2/3
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan
public class LocaleConfig implements WebMvcConfigurer {

    @Resource
    private RequestInterceptor requestInterceptor;

    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver slr = new SessionLocaleResolver();
        // 默认语言
        slr.setDefaultLocale(Locale.CHINA);
        return slr;
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
        // 参数名
        lci.setParamName("lang");
        return lci;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
        registry.addInterceptor(requestInterceptor);
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        /* springboot2+  WebMvcConfigurerAdapter-> WebMvcConfigurer
        * https://blog.51cto.com/u_11812862/3033110
        * spring:
            jackson:
              serialization:
                WRITE_DATES_AS_TIMESTAMPS: true
        * */
        converters.add(new JacksonHttpMessageConverter());
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverterFactory(new EnumConverter());
    }

    /* Spring5.0+, Spring Security 阻止了有 // 的请求(说不规范)
    * 1、启用下面的Bean配置, 将严格模式中的阻止关闭
    * 2、修改nginx配置, 将location /pdm -> location /pdm/ 即多替换掉一个/ (最好,但是不现实)
    *  */
    @Bean
    public HttpFirewall allowUrlEncodedSlashHttpFirewall() {
//        StrictHttpFirewall firewall = new StrictHttpFirewall();
//        firewall.setAllowUrlEncodedSlash(true);
//        return firewall;
        return new CustomStrictHttpFirewall();
    }

    /* Spring Boot >= 2.4.0 启用*/
//    @Bean
//    RequestRejectedHandler requestRejectedHandler() {
//        return new HttpStatusRequestRejectedHandler();
//    }

}
