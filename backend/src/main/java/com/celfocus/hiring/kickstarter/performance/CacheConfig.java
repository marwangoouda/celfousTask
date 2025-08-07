package com.celfocus.hiring.kickstarter.performance;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * Configuration for caching using Caffeine.
 */
@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();

        // Configure cache for products - cache for 1 hour
        cacheManager.registerCustomCache("products",
                Caffeine.newBuilder()
                        .maximumSize(1000)
                        .expireAfterWrite(1, TimeUnit.HOURS)
                        .recordStats()
                        .build());

        // Configure cache for carts - cache for 30 minutes
        cacheManager.registerCustomCache("carts",
                Caffeine.newBuilder()
                        .maximumSize(500)
                        .expireAfterWrite(30, TimeUnit.MINUTES)
                        .recordStats()
                        .build());

        return cacheManager;
    }
}

