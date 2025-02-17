package com.helper.demo.conf;

import io.github.bucket4j.ConsumptionProbe;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;

@Aspect
@Component
@Slf4j
public class RateLimitAspect {
    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    @Around("@annotation(rateLimit)")
    public Object rateLimit(ProceedingJoinPoint joinPoint, RateLimit rateLimit) throws Throwable {
        String methodName = joinPoint.getSignature().toShortString();
        Bucket bucket = buckets.computeIfAbsent(methodName,
                k -> createNewBucket(rateLimit.tokens(), rateLimit.minutes()));

        // Try to consume a token with blocking
        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);
        if (probe.isConsumed()) {
            return joinPoint.proceed();
        } else {
            long waitTime = probe.getNanosToWaitForRefill() / 1_000_000; // Convert nanoseconds to milliseconds
            if (waitTime <= rateLimit.maxWaitMs()) {
                log.info("Rate limit reached for {}. Waiting {} ms before proceeding.", methodName, waitTime);
                Thread.sleep(waitTime);
                return joinPoint.proceed();
            } else {
                long backoffTime = Math.min(rateLimit.maxWaitMs(), calculateBackoffTime(waitTime));
                log.warn("Rate limit reached for {}. Applying backoff of {} ms.", methodName, backoffTime);
                Thread.sleep(backoffTime);
                return joinPoint.proceed();
            }
        }
    }

    private Bucket createNewBucket(int tokens, int minutes) {
        Bandwidth limit = Bandwidth.classic(tokens,
                Refill.intervally(tokens, Duration.ofSeconds(minutes)));
        return Bucket.builder().addLimit(limit).build();
    }

    private long calculateBackoffTime(long originalWaitTime) {
        // Simple exponential backoff with jitter
        long baseWait = 100; // Start with 100ms
        int attempt = (int) (originalWaitTime / 1000); // Convert to seconds
        long maxWait = 1000; // Max 1 second

        double jitter = Math.random() * 0.1; // 10% jitter
        long backoff = (long) (Math.min(maxWait, baseWait * Math.pow(2, attempt))
                * (1 + jitter));
        return backoff;
    }
}