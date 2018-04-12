package com.pusher.newsbackend

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class NewsBackendApplication {

	static void main(String[] args) {
		SpringApplication.run NewsBackendApplication, args
	}
}
