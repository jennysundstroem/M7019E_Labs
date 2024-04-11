package com.ltu.m7019e.moviedb.v24.viewmodel

import androidx.lifecycle.ViewModel
import com.ltu.m7019e.moviedb.v24.database.MovieDBUiState
import com.ltu.m7019e.moviedb.v24.database.MovieDetails
import com.ltu.m7019e.moviedb.v24.model.Movie
import com.ltu.m7019e.moviedb.v24.model.MovieDetail
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MovieDBViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(MovieDBUiState())
    val uiState: StateFlow<MovieDBUiState> = _uiState.asStateFlow()

    fun setSelectedMovie(movie: Movie) {
        _uiState.update { currentState ->
            var movieId = movie.id
            val movieDetail = MovieDetails().getMovieDetails().find{it.id == movie.id}
            currentState.copy(
                selectedMovie = movie,
                selectedMovieDetail = movieDetail
            )
        }
    }
}