package com.pusher.newsbackend

import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder
import java.time.Instant

@Component
open class GuardianApi(
    @Value("\${guardian.apiKey}") private val apiKey: String
) {
    private val restTemplate = RestTemplate()

    @Cacheable("sections")
    open fun listSections(): List<Section> {
        val uri = UriComponentsBuilder.fromUriString("http://content.guardianapis.com/sections")
                .queryParam("api-key", apiKey)
                .build()
                .toUri()

        return restTemplate.getForObject(uri, SectionPayload::class.java)
                .response.results
    }

    open fun listArticles(from: Instant?): List<Article> {
        val uriBuilder = UriComponentsBuilder.fromUriString("http://content.guardianapis.com/search")
                .queryParam("api-key", apiKey)
                .queryParam("rights", "syndicatable")
                .queryParam("page-size", "50")
                .queryParam("show-fields", "headline,trailText,thumbnail")
                .queryParam("order-by", "oldest")
                .queryParam("order-date", "published")
                .queryParam("use-date", "published")

        if (from != null) {
            uriBuilder.queryParam("from-date", from.toString())
        }
        val uri = uriBuilder.build().toUri()

        return restTemplate.getForObject(uri, ArticlePayload::class.java)
                .response.results
    }
}
