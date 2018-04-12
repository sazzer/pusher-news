package com.pusher.newsbackend

import com.pusher.pushnotifications.PushNotifications
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class ArticleNotifier(
        @Value("\${pusher.instanceId}") private val instanceId: String,
        @Value("\${pusher.secretKey}") private val secretKey: String
) {
    private val pusher = PushNotifications(instanceId, secretKey)

    fun notify(article: Article) {
        System.out.println(article)
        pusher.publish(
                listOf(article.sectionId),
                mapOf(
                        "fcm" to mapOf(
                                "data" to mapOf(
                                        "url" to article.url,
                                        "published" to article.publicationDate,
                                        "section" to article.sectionId,
                                        "headline" to article.fields.headline,
                                        "trailText" to article.fields.trailText,
                                        "thumbnail" to article.fields.thumbnail
                                )
                        )
                )
        )
    }
}
