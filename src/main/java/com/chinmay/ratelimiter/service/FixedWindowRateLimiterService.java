package com.chinmay.ratelimiter.service;

import com.chinmay.ratelimiter.model.UserRequest;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class FixedWindowRateLimiterService {

    private static final int LIMIT = 5;
    private static final long WINDOW_SIZE = 60_000;

    private final Map<String, UserRequest> requestMap =
            new ConcurrentHashMap<>();

    public boolean allowRequest(String userId) {

        long currentTime = System.currentTimeMillis();

        // New User
        if (!requestMap.containsKey(userId)) {

            requestMap.put(
                    userId,
                    new UserRequest(1, currentTime)
            );

            System.out.println("New User: " + userId);

            return true;
        }

        UserRequest userRequest = requestMap.get(userId);

        // Window Expired
        if (currentTime - userRequest.getWindowStartTime() >= WINDOW_SIZE) {

            userRequest.setCount(1);
            userRequest.setWindowStartTime(currentTime);

            System.out.println("Window Reset for: " + userId);

            return true;
        }

        // Limit Exceeded
        if (userRequest.getCount() >= LIMIT) {

            System.out.println("Rate Limit Exceeded: " + userId);

            return false;
        }

        // Increment Count
        userRequest.setCount(userRequest.getCount() + 1);

        System.out.println(
                "Algorithm: Fixed Window | User: " +
                        userId +
                        " | Count: " +
                        userRequest.getCount()
        );

        return true;
    }
}