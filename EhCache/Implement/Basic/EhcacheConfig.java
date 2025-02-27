package Restart.Ehcache.Config;

import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class EhcacheConfig {

    @Bean
    public CacheManager cacheManager() {
        CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
                .withCache("StudentCache",
                        CacheConfigurationBuilder.newCacheConfigurationBuilder(
                                Long.class, String.class, ResourcePoolsBuilder.heap(200))
                )
                .build(true); // ✅ Initializes the cache manager

        return cacheManager;
    }
}
