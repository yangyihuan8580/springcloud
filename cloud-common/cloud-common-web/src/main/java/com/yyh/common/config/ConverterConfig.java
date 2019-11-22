package com.yyh.common.config;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;

import java.nio.charset.Charset;
import java.util.Arrays;

/***
 *
 * 使用fastjson替换jackson进行序列化
 */
@Configuration
public class ConverterConfig {

    @Bean
    public HttpMessageConverters fastJsonHttpMessageConverters() {
        //创建FastJson信息转换对象
        FastJsonHttpMessageConverter fastJsonHttpMessageConverter =
                new FastJsonHttpMessageConverter();
        //创建FastJson对象并设定序列化规则
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);
        /*fastJsonConfig.setSerializerFeatures(
                SerializerFeature.WriteMapNullValue,        // 是否输出值为null的字段,默认为false,我们将它打开
                SerializerFeature.WriteNullListAsEmpty,     // 将Collection类型字段的字段空值输出为[]
                SerializerFeature.WriteNullStringAsEmpty,   // 将字符串类型字段的空值输出为空字符串
                SerializerFeature.WriteNullNumberAsZero,    // 将数值类型字段的空值输出为0
                SerializerFeature.WriteDateUseDateFormat,
                SerializerFeature.DisableCircularReferenceDetect    // 禁用循环引用
        );*/
        fastJsonHttpMessageConverter.setFastJsonConfig(fastJsonConfig);
        fastJsonHttpMessageConverter.setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON_UTF8));
        //规则赋予转换对象
        fastJsonHttpMessageConverter.setFastJsonConfig(fastJsonConfig);
        StringHttpMessageConverter stringHttpMessageConverter =
                new StringHttpMessageConverter(Charset.forName("UTF-8"));
        return new HttpMessageConverters(fastJsonHttpMessageConverter, stringHttpMessageConverter);
    }
}
