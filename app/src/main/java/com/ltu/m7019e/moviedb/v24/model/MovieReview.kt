package com.ltu.m7019e.moviedb.v24.model

import kotlinx.serialization.SerialName

data class MovieReview(
    @SerialName(value = "author_details")
    var movieReviewAuthor: MovieReviewAuthor,

    @SerialName(value = "content")
    var content: String,

    @SerialName(value = "created_at")
    var created_at: String.Companion = String,
)
