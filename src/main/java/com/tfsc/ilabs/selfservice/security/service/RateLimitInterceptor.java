package com.tfsc.ilabs.selfservice.security.service;

import com.tfsc.ilabs.selfservice.security.config.RateLimitBucket;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import static com.tfsc.ilabs.selfservice.common.utils.Constants.*;

public class RateLimitInterceptor implements HandlerInterceptor {
    final String DEFAULT_KEY = "defaultKey";
    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();
    private final RateLimitBucket bucketGenerator;
    private final int numTokens;

    public RateLimitInterceptor(RateLimitBucket bucketGenerator, int tokenConsumedPerRequest) {
        this.bucketGenerator = bucketGenerator;
        this.numTokens = tokenConsumedPerRequest;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Bucket clientBucket;
        String referenceKey = getReferenceKey(request);
        if (referenceKey != null && !referenceKey.isBlank()) {
            clientBucket = this.buckets.computeIfAbsent(referenceKey, key -> this.bucketGenerator.get());
        } else {
            // this is for requests without X-api-key in header
            clientBucket = this.buckets.computeIfAbsent(DEFAULT_KEY, key -> this.bucketGenerator.get());
        }

        ConsumptionProbe probe = clientBucket.tryConsumeAndReturnRemaining(this.numTokens);
        if (probe.isConsumed()) {
            response.addHeader(X_RATE_LIMIT_REMAINING, Long.toString(probe.getRemainingTokens()));
            return true;
        }

        response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value()); // 429
        response.addHeader(X_RATE_LIMIT_RETRY_AFTER_MILLISECS,
                Long.toString(TimeUnit.NANOSECONDS.toMillis(probe.getNanosToWaitForRefill())));

        return false;
    }

    private String getReferenceKey(HttpServletRequest request) {
        return Arrays.stream(request.getCookies())
                .filter(c -> c.getName().equals(COOKIE_NAME))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);
    }
}
