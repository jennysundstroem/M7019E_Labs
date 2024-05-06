package com.ltu.m7019e.moviedb.v24.viewmodel

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
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
}

class MovieDBViewModel(private val moviesRepository: MoviesRepository, private val savedMovieRepository: SavedMovieRepository, private val context: Context) : ViewModel() {

    var movieListUiState: MovieListUiState by mutableStateOf(MovieListUiState.Loading)
        private set

    var selectedMovieUiState: SelectedMovieUiState by mutableStateOf(SelectedMovieUiState.Loading)
        private set

    private var lastCached = "getPopularMovies"
    init {
        scheduleApiWorker("getPopularMovies")
        getPopularMovies()
    }

    fun getTopRatedMovies() {
        viewModelScope.launch {
            movieListUiState = MovieListUiState.Loading
            if (isNetworkAvailable()) {
                scheduleApiWorker("getTopRatedMovies")
                lastCached = "getTopRatedMovies"
                movieListUiState = try {
                    MovieListUiState.Success(moviesRepository.getTopRatedMovies().results)
                } catch (e: IOException) {
                    MovieListUiState.Error
                } catch (e: HttpException) {
                    MovieListUiState.Error
                }
            } else {
                if(lastCached == "getTopRatedMovies"){
                    movieListUiState = try {
                        MovieListUiState.Success(savedMovieRepository.getCachedMovies())
                    } catch (e: IOException) {
                        MovieListUiState.Error
                    } catch (e: HttpException) {
                        MovieListUiState.Error
                    }
                }
                else{
                    MovieListUiState.Error
                    delay(2000)
                    getTopRatedMovies()
                }
            }
        }
    }

    fun getPopularMovies() {
        viewModelScope.launch {
            movieListUiState = MovieListUiState.Loading
            if (isNetworkAvailable()) {
                scheduleApiWorker("getPopularMovies")
                lastCached = "getPopularMovies"
                movieListUiState = try {
                    MovieListUiState.Success(moviesRepository.getPopularMovies().results)
                } catch (e: IOException) {
                    MovieListUiState.Error
                } catch (e: HttpException) {
                    MovieListUiState.Error
                }
            } else {
                if(lastCached == "getPopularMovies"){
                    movieListUiState = try {
                        MovieListUiState.Success(savedMovieRepository.getCachedMovies())
                    } catch (e: IOException) {
                        MovieListUiState.Error
                    } catch (e: HttpException) {
                        MovieListUiState.Error
                    }
                }
                else{
                    MovieListUiState.Error
                    delay(2000)
                    getPopularMovies()
                }
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
                        moviesRepository.getVideos(movie.id).results, moviesRepository.getMovieReviews(movie.id).results, savedMovieRepository.getMovie(movie.id) != null)

                } catch (e: IOException) {
                    SelectedMovieUiState.Error
                } catch (e: HttpException) {
                    SelectedMovieUiState.Error
                }
            }
        }
    fun scheduleApiWorker(action: String) {
        savedMovieRepository.scheduelApiWorker(action)
    }
    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
        return networkCapabilities != null &&
                networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    }
        companion object {
            val Factory: ViewModelProvider.Factory = viewModelFactory {
                initializer {
                    val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MovieDBApplication)
                    val moviesRepository = application.container.moviesRepository
                    val savedMovieRepository = application.container.savedMovieRepository
                    MovieDBViewModel(moviesRepository = moviesRepository, savedMovieRepository = savedMovieRepository, context = application.applicationContext)
                }
            }
        }
    }