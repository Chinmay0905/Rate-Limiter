package com.chinmay.ratelimiter.service;

import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SlidingWindowRateLimiterService {

    private static final int LIMIT = 5;
    private static final long WINDOW_SIZE = 60_000;

    private final Map<String, Queue<Long>> requestMap =
            new ConcurrentHashMap<>();

    public boolean allowRequest(String userId) {

        long currentTime = System.currentTimeMillis();

        requestMap.putIfAbsent(
                userId,
                new LinkedList<>()
        );

        Queue<Long> queue = requestMap.get(userId);

        // Remove timestamps older than 60 seconds
        while (!queue.isEmpty()
                && queue.peek() <= currentTime - WINDOW_SIZE) {

            queue.poll();
        }

        // Check Limit
        if (queue.size() >= LIMIT) {

            System.out.println(
                    "Rate Limit Exceeded: " + userId
            );

            return false;
        }

        // Add Current Timestamp
        queue.offer(currentTime);

        System.out.println(
                "Algorithm: Sliding Window ");

        System.out.println(
                "User: " + userId +
                        " Requests in last minute: " +
                        queue.size()
        );

        return true;
    }
}