package com.yyh.publish;

import org.springframework.boot.actuate.autoconfigure.endpoint.EndpointAutoConfiguration;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;

@Configuration
@ConditionalOnClass(Endpoint.class)
@AutoConfigureAfter(EndpointAutoConfiguration.class)
public class PublishEndpointAutoConfiguration {

    @Bean
    public PublishEndpoint publishEndpoint() {
        return new PublishEndpoint();
    }

    @Bean
    @ConditionalOnProperty("publish.ip-white-list")
    public PublishProperties publishProperties() {
        return new PublishProperties();
    }

    @Bean
    @ConditionalOnProperty("publish.ip-white-list")
    @ConditionalOnClass(Filter.class)
    public FilterRegistrationBean testFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new PublishFilter(publishProperties()));
        registration.addUrlPatterns("/publish");
        registration.setName("publishFilter");
        registration.setOrder(1);
        return registration;
    }

}