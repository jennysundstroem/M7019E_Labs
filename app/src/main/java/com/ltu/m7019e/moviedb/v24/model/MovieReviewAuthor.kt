package com.ltu.m7019e.moviedb.v24.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieReviewAuthor (
    @SerialName(value = "username")
    var username: String,

    @SerialName(value = "rating")
    var rating : Float?,
)