package com.ltu.m7019e.moviedb.v24.viewmodel

import ConnectionManager
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.lifecycle.viewmodel.initializer
import com.ltu.m7019e.moviedb.v24.MovieDBApplication
import com.ltu.m7019e.moviedb.v24.database.MoviesRepository
import com.ltu.m7019e.moviedb.v24.database.SavedMovieRepository
import com.ltu.m7019e.moviedb.v24.model.Movie
import com.ltu.m7019e.moviedb.v24.model.MovieDetailResponse
import com.ltu.m7019e.moviedb.v24.model.MovieReview
import com.ltu.m7019e.moviedb.v24.model.MovieVideo
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
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
    object NoNetwork : MovieListUiState
}

class MovieDBViewModel(
    private val moviesRepository: MoviesRepository,
    private val savedMovieRepository: SavedMovieRepository,
    private val connectionManager: ConnectionManager
) : ViewModel() {

    var movieListUiState: MovieListUiState by mutableStateOf(MovieListUiState.Loading)
        private set

    var selectedMovieUiState: SelectedMovieUiState by mutableStateOf(SelectedMovieUiState.Loading)
        private set

    // job for getting top rated movies
    var getTopRatedMovieJob: Job? = null

    // job for getting popular movies
    var getPopularMovieJob: Job? = null

    private var lastCached : String = ""
    init {
        scheduleApiWorker("getPopularMovies")
        getPopularMovies()
    }
    fun getTopRatedMovies() {
        getPopularMovieJob?.cancel()
        getTopRatedMovieJob =
            viewModelScope.launch {
                if(lastCached == "getTopRated"){
                    Log.w("myApp", "no network, but cached top rated");
                    movieListUiState = MovieListUiState.Success(savedMovieRepository.getCachedMovies())
                }
                else if(connectionManager.isNetworkAvailable){
                    Log.w("myApp", "has network, getting top rated");
                    movieListUiState = MovieListUiState.Loading
                    scheduleApiWorker("getTopRatedMovies")
                    lastCached = "getTopRated"
                    movieListUiState = try{
                        MovieListUiState.Success(moviesRepository.getTopRatedMovies().results)
                    }
                    catch (e: IOException){
                        MovieListUiState.Error
                    }
                    catch (e: HttpException){
                        MovieListUiState.Error
                    }

                } else {
                    Log.w("myApp", "no network, no cached top rated");
                    movieListUiState = MovieListUiState.NoNetwork
                    delay(2000)
                    getTopRatedMovies()
                }
            }
    }

    fun getPopularMovies() {
        getTopRatedMovieJob?.cancel()
        getPopularMovieJob =
            viewModelScope.launch {
                if(lastCached == "getPopular"){
                    Log.w("myApp", "no network, but cached popular");
                    movieListUiState = MovieListUiState.Success(savedMovieRepository.getCachedMovies())
                }
                else if(connectionManager.isNetworkAvailable){
                    Log.w("myApp", "has network, getting popular");
                    movieListUiState = MovieListUiState.Loading
                    scheduleApiWorker("getPopularMovies")
                    lastCached = "getPopular"
                    movieListUiState = try{
                        MovieListUiState.Success(moviesRepository.getPopularMovies().results)
                    }
                    catch (e: IOException){
                        MovieListUiState.Error
                    }
                    catch (e: HttpException){
                        MovieListUiState.Error
                    }

                } else {
                    Log.w("myApp", "no network, no popular");
                    movieListUiState = MovieListUiState.NoNetwork
                    delay(2000)
                    getPopularMovies()
                }
            }
    }


    fun getSavedMovies() {
        viewModelScope.launch {
            movieListUiState = MovieListUiState.Loading
            movieListUiState = try {
                MovieListUiState.Success(savedMovieRepository.getFavouriteMovies())
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
            savedMovieRepository.setFavouriteMovie(movie.id)
            selectedMovieUiState = SelectedMovieUiState.Success(movie, moviesRepository.getMovieDetail(movie.id), moviesRepository.getVideos(movie.id).results, moviesRepository.getMovieReviews(movie.id).results, isFavourite = true)
        }
    }

    fun deleteMovie(movie: Movie) {
        viewModelScope.launch {
            savedMovieRepository.deleteFavouriteMovie(movie)
            selectedMovieUiState = SelectedMovieUiState.Success(movie, moviesRepository.getMovieDetail(movie.id), moviesRepository.getVideos(movie.id).results, moviesRepository.getMovieReviews(movie.id).results, false)
        }
    }

    fun setSelectedMovie(movie: Movie) {
        viewModelScope.launch {
            selectedMovieUiState = SelectedMovieUiState.Loading
            selectedMovieUiState = try {
                SelectedMovieUiState.Success(movie, moviesRepository.getMovieDetail(movie.id),
                    moviesRepository.getVideos(movie.id).results, moviesRepository.getMovieReviews(movie.id).results, savedMovieRepository.isFavorite(movie.id))

            } catch (e: IOException) {
                SelectedMovieUiState.Error
            } catch (e: HttpException) {
                SelectedMovieUiState.Error
            }
        }
    }
    private fun scheduleApiWorker(action: String) {
        savedMovieRepository.scheduelApiWorker(action)
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MovieDBApplication)
                val moviesRepository = application.container.moviesRepository
                val savedMovieRepository = application.container.savedMovieRepository
                val connectionManager = ConnectionManager(application.applicationContext)
                MovieDBViewModel(
                    moviesRepository = moviesRepository,
                    savedMovieRepository = savedMovieRepository,
                    connectionManager = connectionManager
                )

            }
        }
    }
}