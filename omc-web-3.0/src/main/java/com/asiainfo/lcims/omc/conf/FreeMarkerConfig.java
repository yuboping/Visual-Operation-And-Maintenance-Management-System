package com.asiainfo.lcims.omc.conf;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

@Configuration
public class FreeMarkerConfig {

    private static final String VIEW_RESOLVER_PREFIX = "views/";
    private static final String VIEW_RESOLVER_SUFFIX = ".ftl";
    private static final String CONTENT_TYPE = "text/html;charset=UTF-8";

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Value("classpath:freemarker.properties")
    private Resource ftlSettings;

    @Bean
    public FreeMarkerConfigurer freemarkerConfig() throws IOException {
        FreeMarkerConfigurer conf = new FreeMarkerConfigurer();
        PropertiesFactoryBean ppc = new PropertiesFactoryBean();
        ppc.setLocation(ftlSettings);
        ppc.afterPropertiesSet();
        conf.setFreemarkerSettings(ppc.getObject());
        conf.setTemplateLoaderPath("/WEB-INF/freemarker/");
        conf.setDefaultEncoding("UTF-8");
        return conf;
    }

    @Bean
    public FreeMarkerViewResolver freeMarkerViewResolver() {
        FreeMarkerViewResolver fvr = new FreeMarkerViewResolver();
        fvr.setCache(true);
        fvr.setPrefix(VIEW_RESOLVER_PREFIX);
        fvr.setSuffix(VIEW_RESOLVER_SUFFIX);
        fvr.setExposeSpringMacroHelpers(true);
        fvr.setContentType(CONTENT_TYPE);
        fvr.setOrder(1);
        return fvr;
    }
}
