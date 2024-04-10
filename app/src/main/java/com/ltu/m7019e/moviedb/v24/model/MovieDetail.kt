package com.ltu.m7019e.moviedb.v24.model
data class MovieDetail (
    var id: Long = 0L,
    var genres : Array<String>,
    var homepage : String,
    var imdbid : String,
)