package com.ltu.m7019e.moviedb.v24.database

import com.ltu.m7019e.moviedb.v24.model.Movie
import com.ltu.m7019e.moviedb.v24.model.MovieDetail

data class MovieDBUiState(
    val selectedMovie: Movie? = null,
    val selectedMovieDetail: MovieDetail? = null
)
