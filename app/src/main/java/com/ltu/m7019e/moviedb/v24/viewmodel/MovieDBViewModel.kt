package com.ltu.m7019e.moviedb.v24.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.lifecycle.viewmodel.initializer
import com.ltu.m7019e.moviedb.v24.MovieDBApplication
import com.ltu.m7019e.moviedb.v24.database.FavoriteMoviesRepository
import com.ltu.m7019e.moviedb.v24.database.MoviesRepository
import com.ltu.m7019e.moviedb.v24.database.SavedMovieRepository
import com.ltu.m7019e.moviedb.v24.model.Movie
import com.ltu.m7019e.moviedb.v24.model.MovieDetailResponse
import com.ltu.m7019e.moviedb.v24.model.MovieReview
import com.ltu.m7019e.moviedb.v24.model.MovieVideo
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface SelectedMovieUiState {
    data class Success(
        val movie: Movie,
        val movieDetail: MovieDetailResponse,
        val videos: List<MovieVideo>,
        val movieReviews: List<MovieReview>,
        val isFavourite: Boolean
    )
        : SelectedMovieUiState

    object Error : SelectedMovieUiState
    object Loading : SelectedMovieUiState
}

sealed interface MovieListUiState {
    data class Success(val movies: List<Movie>) : MovieListUiState
    object Error : MovieListUiState
    object Loading : MovieListUiState
}

class MovieDBViewModel(private val moviesRepository: MoviesRepository, private val savedMovieRepository: SavedMovieRepository) : ViewModel() {

    var movieListUiState: MovieListUiState by mutableStateOf(MovieListUiState.Loading)
        private set

    var selectedMovieUiState: SelectedMovieUiState by mutableStateOf(SelectedMovieUiState.Loading)
        private set

    init {
        getPopularMovies()
    }

    fun getTopRatedMovies() {
        viewModelScope.launch {
            movieListUiState = MovieListUiState.Loading
            movieListUiState = try {
                MovieListUiState.Success(moviesRepository.getTopRatedMovies().results)
            } catch (e: IOException) {
                MovieListUiState.Error
            } catch (e: HttpException) {
                MovieListUiState.Error
            }
        }
    }

    fun getPopularMovies() {
        viewModelScope.launch {
            movieListUiState = MovieListUiState.Loading
            movieListUiState = try {
                MovieListUiState.Success(moviesRepository.getPopularMovies().results)
            } catch (e: IOException) {
                MovieListUiState.Error
            } catch (e: HttpException) {
                MovieListUiState.Error
            }
        }
    }

    fun getSavedMovies() {
        viewModelScope.launch {
            movieListUiState = MovieListUiState.Loading
            movieListUiState = try {
                MovieListUiState.Success(savedMovieRepository.getSavedMovies())
            } catch (e: IOException) {
                MovieListUiState.Error
            } catch (e: HttpException) {
                MovieListUiState.Error
            }
        }
    }

    fun saveMovie(movie: Movie) {
        viewModelScope.launch {
            savedMovieRepository.insertMovie(movie)
            selectedMovieUiState = SelectedMovieUiState.Success(movie, moviesRepository.getMovieDetail(movie.id), moviesRepository.getVideos(movie.id).results, moviesRepository.getMovieReviews(movie.id).results, isFavourite = true)
        }
    }

    fun deleteMovie(movie: Movie) {
        viewModelScope.launch {
            savedMovieRepository.deleteMovie(movie)
            selectedMovieUiState = SelectedMovieUiState.Success(movie, moviesRepository.getMovieDetail(movie.id), moviesRepository.getVideos(movie.id).results, moviesRepository.getMovieReviews(movie.id).results, false)
        }
    }

    fun setSelectedMovie(movie: Movie) {
            viewModelScope.launch {
                selectedMovieUiState = SelectedMovieUiState.Loading
                selectedMovieUiState = try {
                    SelectedMovieUiState.Success(movie, moviesRepository.getMovieDetail(movie.id),
                        moviesRepository.getVideos(movie.id).results, moviesRepository.getMovieReviews(movie.id).results, savedMovieRepository.getMovie(movie.id) != null)

                } catch (e: IOException) {
                    SelectedMovieUiState.Error
                } catch (e: HttpException) {
                    SelectedMovieUiState.Error
                }
            }
        }

        companion object {
            val Factory: ViewModelProvider.Factory = viewModelFactory {
                initializer {
                    val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MovieDBApplication)
                    val moviesRepository = application.container.moviesRepository
                    val savedMovieRepository = application.container.savedMovieRepository
                    MovieDBViewModel(moviesRepository = moviesRepository, savedMovieRepository = savedMovieRepository)
                }
            }
        }
    }