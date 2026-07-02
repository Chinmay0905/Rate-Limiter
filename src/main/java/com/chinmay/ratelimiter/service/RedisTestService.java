package com.chinmay.ratelimiter.service;

import jakarta.annotation.PostConstruct;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisTestService {

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisTestService(
            RedisTemplate<String, Object> redisTemplate) {

        this.redisTemplate = redisTemplate;
    }

    @PostConstruct
    public void testRedis() {

        redisTemplate.opsForValue().set("name", "Chinmay");

        Object value =
                redisTemplate.opsForValue().get("name");

        System.out.println(value);
    }
}