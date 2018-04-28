package com.pusher.newsbackend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class NewsBackendApplication

fun main(args: Array<String>) {
    runApplication<NewsBackendApplication>(*args)
}
