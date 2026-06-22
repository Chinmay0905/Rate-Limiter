package com.chinmay.ratelimiter.service;

import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import com.chinmay.ratelimiter.model.UserRequest;

@Service
public class RateLimiterService {

    private static final int LIMIT = 5;
    private static final long WINDOW_SIZE = 60_000;

    private final Map<String, UserRequest> requestMap = new ConcurrentHashMap<>();

    public boolean allowRequest(String userId) {

        long currentTime = System.currentTimeMillis();

        if(!requestMap.containsKey(userId)) {
            requestMap.put(userId, new UserRequest(1, currentTime));
            System.out.println("New User: " + userId);
            return true;
        }

        UserRequest userRequest = requestMap.get(userId);

        if (currentTime - userRequest.getWindowStartTime() >= WINDOW_SIZE) {

            userRequest.setCount(1);
            userRequest.setWindowStartTime(currentTime);

            System.out.println("Window Reset for: " + userId);
            return true;
        }

        if (userRequest.getCount() >= LIMIT) {

            System.out.println("Rate Limit Exceeded for: " + userId);
            return false;
        }

        userRequest.setCount(userRequest.getCount() + 1);

        System.out.println(
                "User: " + userId +
                        " Count: " + userRequest.getCount());

        return true;
    }
}