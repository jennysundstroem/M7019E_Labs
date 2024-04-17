package com.ltu.m7019e.moviedb.v24.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieGenre(
    @SerialName(value = "id")
    var page: Int = 0,

    @SerialName(value = "name")
    var name: String = "",
)