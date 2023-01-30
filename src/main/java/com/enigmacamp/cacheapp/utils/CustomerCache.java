package com.enigmacamp.cacheapp.utils;

import com.enigmacamp.cacheapp.model.Customer;
import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;

import java.time.Duration;

public class CustomerCache {
    private final String cacheName;
    private final CacheManager cacheManager;

    public CustomerCache(CacheManager cacheManager, String cacheName, Integer expiryTime) {
        this.cacheName = cacheName;
        this.cacheManager = cacheManager;

        CacheConfigurationBuilder<String, Customer> cacheConfiguration = CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, Customer.class,
                ResourcePoolsBuilder.heap(10));
        if (expiryTime != null) {
            cacheConfiguration = cacheConfiguration
                    .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofMillis(expiryTime)));
        }
        cacheManager.createCache(cacheName, cacheConfiguration.build());
    }

    public Cache<String, Customer> getCustomerCache() {
        return cacheManager.getCache(cacheName, String.class, Customer.class);
    }
}
