package com.celfocus.hiring.kickstarter.performance;

import com.celfocus.hiring.kickstarter.db.entity.CartEntity;
import com.celfocus.hiring.kickstarter.db.repo.CartRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Scheduler for cleaning up inactive carts.
 */
@Component
public class CartCleanupScheduler {

    private static final Logger logger = LoggerFactory.getLogger(CartCleanupScheduler.class);

    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CacheManager cacheManager;

    @Value("${cart.expiration.minutes:2}")
    private int cartExpirationMinutes;

    /**
     * Runs every 1 mins to clean up expired carts.
     */
    @Scheduled(cron = "0 0/1 * * * *") // 1 mins in milliseconds
    @Transactional

    public void cleanupExpiredCarts() {
        logger.info("Starting cart cleanup process");

        try {
            LocalDateTime expirationTime = LocalDateTime.now().minusMinutes(cartExpirationMinutes);
            logger.info("Would clean up carts older than: {}", expirationTime);

            List<CartEntity> expired = cartRepository.findByLastModifiedDateBefore(expirationTime);
            if (expired.isEmpty()) {
                logger.info("No expired carts found");
                return;
            }

            cartRepository.deleteAll(expired);
            logger.info("Deleted {} expired carts", expired.size());

            Cache cartsCache = cacheManager.getCache("carts");
            expired.stream()
                    .map(CartEntity::getUserId)
                    .forEach(username -> {
                        cartsCache.evict(username);
                        logger.info("Evicted cache for user={}", username);
                    });

            logger.info("Cart cleanup process completed");

        } catch (Exception e) {
            logger.error("Error during cart cleanup process", e);
        }
    }
}


