package com.celfocus.hiring.kickstarter.api;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.celfocus.hiring.kickstarter.db.entity.CartEntity;
import com.celfocus.hiring.kickstarter.db.repo.CartRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.stats.CacheStats;

import java.util.ArrayList;
import java.util.Optional;

@SpringBootTest
class CartServiceIntegrationTest {

    @Autowired
    CartService cartService;

    @Autowired
    CacheManager cacheManager;

    @MockBean
    CartRepository cartRepo;

    @Test
    void cacheStatistics() {
        CartEntity fake = new CartEntity();
        fake.setUserId("bob");
        fake.setItems(new ArrayList<>());
        when(cartRepo.findByUserId("bob"))
                .thenReturn(Optional.of(fake));

        CaffeineCache springCache =
                (CaffeineCache) cacheManager.getCache("carts");
        Assertions.assertNotNull(springCache);
        Cache<?, ?> nativeCache = springCache.getNativeCache();

        // Note: stats() returns cumulative stats, so no need to clear first

        // 1st lookup → MISS
        cartService.getCart("bob");
        // 2nd lookup → HIT
        cartService.getCart("bob");

        CacheStats stats = nativeCache.stats();
        assertThat(stats.requestCount()).isEqualTo(2);
        assertThat(stats.hitCount()).isEqualTo(1);
        assertThat(stats.missCount()).isEqualTo(1);
    }
}
