package com.ratelimiter.poc.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RateLimiter {
    private final Map<String, Long> requestCounts = new ConcurrentHashMap<>();
    private final long resetTimeInterval;
    private final int maxRequests;

    public RateLimiter(int maxRequests, long resetTimeInterval) {
        this.maxRequests = maxRequests;
        this.resetTimeInterval = resetTimeInterval;
    }

    public boolean allowRequest(String ipAddress) {
        long currentTime = System.currentTimeMillis();
        long currentCount = requestCounts.compute(ipAddress, (key, oldValue) -> (oldValue == null) ? 1 : oldValue + 1);
        if (currentCount > maxRequests) {
            return false;
        }
        if (currentCount == 1) {
            removeOldEntriesAfterTimeout(ipAddress, currentTime);
        }
        return true;
    }

    private void removeOldEntriesAfterTimeout(String ipAddress, long currentTime) {
        new Thread(() -> {
            try {
                Thread.sleep(resetTimeInterval);
                requestCounts.remove(ipAddress);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}

