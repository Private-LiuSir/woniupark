package com.woniu.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.nio.charset.Charset;

//redis的配置类
@Configuration
public class RedisConfig {
    @Bean
    public RedisTemplate<String,Object> redisTemplate(RedisConnectionFactory redisConnectionFactory){
        RedisTemplate<String, Object> stringObjectRedisTemplate = new RedisTemplate<>();
        stringObjectRedisTemplate.setConnectionFactory(redisConnectionFactory);
        //key序列化为string
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer(Charset.forName("utf-8"));
        stringObjectRedisTemplate.setKeySerializer(stringRedisSerializer);
        stringObjectRedisTemplate.setHashKeySerializer(stringRedisSerializer);
        //value序列化为JSON
        Jackson2JsonRedisSerializer<Object> objectJackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        stringObjectRedisTemplate.setValueSerializer(objectJackson2JsonRedisSerializer);
        stringObjectRedisTemplate.setHashValueSerializer(stringRedisSerializer);
        return stringObjectRedisTemplate;
    }
}
