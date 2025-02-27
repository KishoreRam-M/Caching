package com.example.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.jcache.JCacheCacheManager;

import javax.cache.Caching;
import javax.cache.spi.CachingProvider;
import java.net.URISyntaxException;
import java.net.URL;

@Configuration
@EnableCaching  // Enables Spring Boot caching support
public class EhcacheConfig {

    @Bean
    public CacheManager cacheManager() throws URISyntaxException {
        CachingProvider cachingProvider = Caching.getCachingProvider();
        URL resource = getClass().getResource("/ehcache.xml"); // Load from classpath
        javax.cache.CacheManager jCacheManager = cachingProvider.getCacheManager(resource.toURI(), getClass().getClassLoader());
        return new JCacheCacheManager(jCacheManager);
    }
}
