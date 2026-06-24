package com.chinmay.ratelimiter.service;

import com.chinmay.ratelimiter.model.TokenBucket;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TokenBucketRateLimiterService {

    private static final int CAPACITY = 5;

    // 1 token every 12 seconds
    private static final long REFILL_INTERVAL = 12_000;

    private final Map<String, TokenBucket> bucketMap =
            new ConcurrentHashMap<>();

    public boolean allowRequest(String userId) {

        long currentTime = System.currentTimeMillis();

        bucketMap.putIfAbsent(
                userId,
                new TokenBucket(CAPACITY, currentTime)
        );

        TokenBucket bucket = bucketMap.get(userId);

        refillTokens(bucket, currentTime);

        if (bucket.getTokens() <= 0) {

            System.out.println(
                    "Rate Limit Exceeded for: " + userId
            );

            return false;
        }

        bucket.setTokens(bucket.getTokens() - 1);

        System.out.println(
                "Algorithm: Token Bucket ");

        System.out.println(
                "User: " + userId +
                        " Remaining Tokens: " +
                        bucket.getTokens()
        );

        return true;
    }

    private void refillTokens(
            TokenBucket bucket,
            long currentTime) {

        long elapsedTime =
                currentTime - bucket.getLastRefillTime();

        long newTokens =
                elapsedTime / REFILL_INTERVAL;

        if (newTokens <= 0) {
            return;
        }

        bucket.setTokens(
                Math.min(
                        CAPACITY,
                        bucket.getTokens() + (int) newTokens
                )
        );

        bucket.setLastRefillTime(
                bucket.getLastRefillTime()
                        + (newTokens * REFILL_INTERVAL)
        );
    }
}