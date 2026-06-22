package com.chinmay.ratelimiter;

import com.chinmay.ratelimiter.service.RateLimiterService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

@RestController
public class TestController {

    private final RateLimiterService rateLimiterService;

    public TestController(RateLimiterService rateLimiterService) {
        this.rateLimiterService = rateLimiterService;
    }



    @GetMapping("/hello")
    public ResponseEntity<String> hello(
            @RequestParam String userId) {

        System.out.println("Controller Hit: " + userId);

        if(!rateLimiterService.allowRequest(userId)) {
            return ResponseEntity
                    .status(429)
                    .body("Rate Limit Exceeded");
        }

        return ResponseEntity.ok("Hello Chinmay");
    }
}