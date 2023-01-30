package com.enigmacamp.cacheapp.config;

import com.enigmacamp.cacheapp.model.Customer;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.jsr107.Eh107Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.spi.CachingProvider;
import java.time.Duration;

@Configuration
@EnableCaching
public class CacheConfig {
    @Value("${cache.customer.expiryTimeMillis}")
    Integer expiryTime;

    @Bean
    public CacheManager EhcacheManager() {
        CacheConfigurationBuilder<String, Customer> cacheConfiguration = CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, Customer.class,
                ResourcePoolsBuilder.heap(10));
        if (expiryTime != null) {
            cacheConfiguration = cacheConfiguration
                    .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofMillis(expiryTime)));
        }
        CachingProvider cachingProvider = Caching.getCachingProvider();
        CacheManager manager = cachingProvider.getCacheManager();
        javax.cache.configuration.Configuration<String, Customer> configuration = Eh107Configuration.fromEhcacheCacheConfiguration(cacheConfiguration);
        manager.createCache("customer-cache", configuration);
        return manager;
    }
}
