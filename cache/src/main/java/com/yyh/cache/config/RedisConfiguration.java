package com.yyh.cache.config;

import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfiguration {

    @Bean("redisTemplate")
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
        template.setConnectionFactory(factory);
        FastJsonRedisSerializer fastJsonRedisSerializer = new FastJsonRedisSerializer(Object.class);
//        fastJsonRedisSerializer.setFastJsonConfig();
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        /** key采用String的序列化方式 */
        template.setKeySerializer(stringRedisSerializer);
        /** hash的key也采用String的序列化方式 */
        template.setHashKeySerializer(stringRedisSerializer);
        /** value序列化方式采用fastjson */
        template.setValueSerializer(fastJsonRedisSerializer);
        /** hash的value序列化方式采用fastjson */
        template.setHashValueSerializer(fastJsonRedisSerializer);
        template.afterPropertiesSet();
        return template;
    }
}
