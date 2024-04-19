package com.ltu.m7019e.moviedb.v24.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieReviewResponse (

    @SerialName(value = "results")
    var results: List<MovieReview>,

    )