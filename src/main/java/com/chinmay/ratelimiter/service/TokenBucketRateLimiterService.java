package com.chinmay.ratelimiter.service;

import org.springframework.stereotype.Service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import java.util.List;

@Service
public class TokenBucketRateLimiterService {

    private static final int CAPACITY = 5;

    // 1 token every 12 seconds
    private static final long REFILL_INTERVAL = 12_000;

    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisScript<Long> tokenBucketScript;

    public TokenBucketRateLimiterService(RedisTemplate<String, Object> redisTemplate, RedisScript<Long> tokenBucketScript) {

        this.redisTemplate = redisTemplate;
        this.tokenBucketScript = tokenBucketScript;
    }


    public boolean allowRequest(String userId) {

        long currentTime = System.currentTimeMillis();

        String key = "bucket:" + userId;

        Long allowed =
                redisTemplate.execute(
                        tokenBucketScript,
                        List.of(key),
                        String.valueOf(currentTime),
                        String.valueOf(CAPACITY),
                        String.valueOf(REFILL_INTERVAL)
                );

        if (allowed == 1) {

            System.out.println("Algorithm: Token Bucket");
            System.out.println("Request Allowed: " + userId);

            return true;
        }

        System.out.println("Rate Limit Exceeded: " + userId);

        return false;
    }
}