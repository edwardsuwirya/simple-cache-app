package com.enigmacamp.cacheapp.utils;

import com.enigmacamp.cacheapp.model.Customer;
import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.EntryUnit;

public class CustomerCache {
    private final String cacheName;
    private final CacheManager cacheManager;

    public CustomerCache(CacheManager cacheManager, String cacheName) {
        this.cacheName = cacheName;
        this.cacheManager = cacheManager;
        cacheManager.createCache(cacheName, CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, Customer.class, ResourcePoolsBuilder.newResourcePoolsBuilder().heap(10, EntryUnit.ENTRIES)));
    }

    public Cache<String, Customer> getCustomerCache() {
        return cacheManager.getCache(cacheName, String.class, Customer.class);
    }
}
