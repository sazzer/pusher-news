package com.pusher.newsbackend

import com.fasterxml.jackson.annotation.JsonProperty

data class Section(
        val id: String,
        @JsonProperty("webTitle") val title: String
)
