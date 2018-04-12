package com.pusher.newsbackend

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class SectionController(
        private val api: GuardianApi
) {
    @RequestMapping("/articles")
    fun getArticles() = api.listArticles(null)

    @RequestMapping("/sections")
    fun getSections() = api.listSections()
}
