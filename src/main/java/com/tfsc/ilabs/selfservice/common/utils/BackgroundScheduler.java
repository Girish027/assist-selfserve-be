package com.tfsc.ilabs.selfservice.common.utils;

import com.tfsc.ilabs.selfservice.security.service.SessionValidatorWithOKTA;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;

/**
 * Scheduler to run at specified intervals in background
 */
@Component
public class BackgroundScheduler {
    private static final Logger logger = LoggerFactory.getLogger(BackgroundScheduler.class);

    @Scheduled(fixedRateString = "${cache.token.scheduler.fixedrate.millis}",
            initialDelayString = "${cache.token.scheduler.initialdelay.millis}")
    public void checkAndRemoveExpiredTokens() {
        logger.debug("Running BackgroundScheduler - Removing expired tokens from Map : {}", Instant.now());
        SessionValidatorWithOKTA.getTokenMap().entrySet()
                .removeIf(t -> Instant.now().getEpochSecond() > t.getValue().getExp());
        logger.debug("tokenMap Size: {}", SessionValidatorWithOKTA.getTokenMap().size());
    }
}
