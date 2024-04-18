package com.ltu.m7019e.moviedb.v24.model

import kotlinx.serialization.SerialName

data class MovieReviewResponse (
    @SerialName(value = "page")
    var page: Int = 0,

    @SerialName(value = "results")
    var results: List<MovieResponse> = listOf(),

    @SerialName(value = "total_pages")
    var total_pages: Int = 0,

    @SerialName(value = "total_results")
    var total_results: Int = 0,

    )