package com.ltu.m7019e.moviedb.v24.model

import kotlinx.serialization.SerialName

data class MovieReviewAuthor (
    @SerialName(value = "username")
    var username: String,
    var rating : Int,
)