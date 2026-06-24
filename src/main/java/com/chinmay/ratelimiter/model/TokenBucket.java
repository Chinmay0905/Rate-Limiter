package com.chinmay.ratelimiter.model;

public class TokenBucket {

    private int tokens;
    private long lastRefillTime;

    public TokenBucket(int tokens, long lastRefillTime) {
        this.tokens = tokens;
        this.lastRefillTime = lastRefillTime;
    }

    public int getTokens() {
        return tokens;
    }

    public void setTokens(int tokens) {
        this.tokens = tokens;
    }

    public long getLastRefillTime() {
        return lastRefillTime;
    }

    public void setLastRefillTime(long lastRefillTime) {
        this.lastRefillTime = lastRefillTime;
    }

    @Override
    public String toString() {
        return "TokenBucket{" +
                "tokens=" + tokens +
                ", lastRefillTime=" + lastRefillTime +
                '}';
    }
}