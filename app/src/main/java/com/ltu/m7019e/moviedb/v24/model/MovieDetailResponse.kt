package com.ltu.m7019e.moviedb.v24.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieDetailResponse(
/*
    @SerialName(value = "id")
    var id: Int = 0, */

    @SerialName(value = "genres")
    var genres: List<MovieGenre> = listOf(),

    @SerialName(value = "homepage")
    var homepage: String,

    @SerialName(value = "imdb_id")
    var imdb_id: String,
)
