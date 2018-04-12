package com.pusher.newsbackend

import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.concurrent.ConcurrentMapCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableCaching
open class CachingConfig {

    @Bean
    open fun cacheManager() = ConcurrentMapCacheManager("sections")
}
