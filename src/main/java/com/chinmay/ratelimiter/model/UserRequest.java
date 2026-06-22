package com.chinmay.ratelimiter.model;

public class UserRequest {

    private int count;
    private long windowStartTime;

    public UserRequest(int count, long windowStartTime) {
        this.count = count;
        this.windowStartTime = windowStartTime;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public long getWindowStartTime() {
        return windowStartTime;
    }

    public void setWindowStartTime(long windowStartTime) {
        this.windowStartTime = windowStartTime;
    }
}