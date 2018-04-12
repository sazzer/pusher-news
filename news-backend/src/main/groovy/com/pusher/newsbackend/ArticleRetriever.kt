package com.pusher.newsbackend

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.Instant

@Component
class ArticleRetriever(
        private val guardianApi: GuardianApi,
        private val articleNotifier: ArticleNotifier
) {
    private var lastDate: Instant? = Instant.parse("2018-01-01T00:00:00Z")

    private val lastSeenIds = mutableSetOf<String>()

    @Scheduled(fixedDelayString = "PT10M")
    fun retrieveArticles() {
        val articles = guardianApi.listArticles(lastDate)
                .filter { !lastSeenIds.contains(it.id) }

        lastSeenIds.clear()
        lastSeenIds.addAll(articles.map { it.id })

        lastDate = articles.map { it.publicationDate }
                .map(Instant::parse)
                .sorted()
                .reversed()
                .first()

        articles.forEach(articleNotifier::notify)
    }
}
