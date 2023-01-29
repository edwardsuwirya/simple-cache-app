package com.enigmacamp.cacheapp.utils;

import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheManagerBuilder;

public class CacheFactory {

    private final CacheManager cacheManager;

    public CacheFactory() {
        cacheManager = CacheManagerBuilder.newCacheManagerBuilder().build(true);
    }

    public CacheManager getCacheManager() {
        return cacheManager;
    }
}
