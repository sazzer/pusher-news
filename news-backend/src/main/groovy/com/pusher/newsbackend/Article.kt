package com.pusher.newsbackend

import com.fasterxml.jackson.annotation.JsonProperty

data class Article(
        val id: String,
        @JsonProperty("webUrl") val url: String,
        @JsonProperty("webPublicationDate") val publicationDate: String,
        val fields: ArticleFields,
        val sectionId: String
)

