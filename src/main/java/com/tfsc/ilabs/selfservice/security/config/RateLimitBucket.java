package com.tfsc.ilabs.selfservice.security.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;

import java.time.Duration;

/**
 * https://github.com/vladimir-bukhtoyarov/bucket4j
 */
public class RateLimitBucket {
    private int tokens;
    private int capacity;
    private Duration duration;
    private int initialTokens;

    public RateLimitBucket(int tokens, int capacity, Duration duration, int initialTokens) {
        this.tokens = tokens;
        this.capacity = capacity;
        this.duration = duration;
        this.initialTokens = initialTokens;
    }

    public Bucket get() {
        Refill refill = Refill.greedy(tokens, duration);
        Bandwidth limit = Bandwidth.classic(capacity, refill)
                .withInitialTokens(initialTokens);
        return Bucket4j.builder()
                .addLimit(limit).build();
    }
}
