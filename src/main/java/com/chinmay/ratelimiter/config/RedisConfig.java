package com.chinmay.ratelimiter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(
            RedisConnectionFactory connectionFactory) {

        RedisTemplate<String, Object> template = new RedisTemplate<>();

        template.setConnectionFactory(connectionFactory);

        StringRedisSerializer stringSerializer = new StringRedisSerializer();

        // Normal key-value operations
        template.setKeySerializer(stringSerializer);
        template.setValueSerializer(stringSerializer);

        // Hash operations
        template.setHashKeySerializer(stringSerializer);
        template.setHashValueSerializer(stringSerializer);

        template.afterPropertiesSet();

        return template;
    }

    @Bean
    public RedisScript<Long> tokenBucketScript() {

        ResourceScriptSource scriptSource =
                new ResourceScriptSource(
                        new ClassPathResource(
                                "scripts/tokenBucket.lua"
                        )
                );

        DefaultRedisScript<Long> script =
                new DefaultRedisScript<>();

        script.setScriptSource(scriptSource);
        script.setResultType(Long.class);

        return script;
    }
}