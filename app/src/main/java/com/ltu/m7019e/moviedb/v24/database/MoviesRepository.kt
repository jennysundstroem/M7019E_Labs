package com.ltu.m7019e.moviedb.v24.database

import android.content.Context
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.ltu.m7019e.moviedb.v24.model.Movie
import com.ltu.m7019e.moviedb.v24.model.MovieDetailResponse
import com.ltu.m7019e.moviedb.v24.model.MovieResponse
import com.ltu.m7019e.moviedb.v24.model.MovieReviewResponse
import com.ltu.m7019e.moviedb.v24.model.MovieVideoResponse
import com.ltu.m7019e.moviedb.v24.network.MovieDBApiService
import com.ltu.m7019e.moviedb.v24.workers.ApiWorker

interface MoviesRepository {
    suspend fun getPopularMovies(): MovieResponse
    suspend fun getTopRatedMovies(): MovieResponse
    suspend fun getMovieDetail(movieId: Long): MovieDetailResponse
    suspend fun getMovieReviews(movieId: Long): MovieReviewResponse
    suspend fun getVideos(movieId: Long): MovieVideoResponse
}

class NetworkMoviesRepository(private val apiService: MovieDBApiService) : MoviesRepository {
    override suspend fun getPopularMovies(): MovieResponse {
        return apiService.getPopularMovies()
    }

    override suspend fun getTopRatedMovies(): MovieResponse {
        return apiService.getTopRatedMovies()
    }

    override suspend fun getMovieDetail(movieId: Long): MovieDetailResponse {
        return apiService.getMovieDetail(movieId)
    }

    override suspend fun getMovieReviews(movieId: Long): MovieReviewResponse {
        return apiService.getMovieReviews(movieId)
    }

    override suspend fun getVideos(movieId: Long): MovieVideoResponse {
        return apiService.getVideos(movieId)
    }
}

interface SavedMovieRepository {
    suspend fun getFavouriteMovies(): List<Movie>
    suspend fun getCachedMovies(): List<Movie>

    suspend fun insertMovie(movie: Movie)

    suspend fun getMovie(id: Long): Movie
    suspend fun deleteFavouriteMovie(movie: Movie)
    suspend fun deleteCachedMovies()
    suspend fun setFavouriteMovie(id: Long)
    suspend fun setCachedMovie(id: Long)
    suspend fun isFavorite(id : Long): Boolean
    abstract fun scheduelApiWorker(action: String)

}

class FavoriteMoviesRepository(private val movieDao: MovieDao, context: Context) : SavedMovieRepository {
    private val workManager = WorkManager.getInstance(context)

    override suspend fun getFavouriteMovies(): List<Movie> {
        return movieDao.getFavouriteMovies()
    }

    override suspend fun getCachedMovies(): List<Movie> {
        return movieDao.getCachedMovies()
    }

    override suspend fun insertMovie(movie: Movie) {
        movieDao.insertMovie(movie)
    }

    override suspend fun getMovie(id: Long): Movie {
        return movieDao.getMovie(id)
    }

    override suspend fun deleteFavouriteMovie(movie: Movie) {
        return movieDao.deleteFavouriteMovie(movie.id)
    }

    override suspend fun deleteCachedMovies() {
        return movieDao.deleteCachedMovies()
    }

    override suspend fun setFavouriteMovie(id: Long) {
        return movieDao.setFavouriteMovie(id)
    }

    override suspend fun setCachedMovie(id: Long) {
        return movieDao.setCachedMovie(id)
    }

    override suspend fun isFavorite(id: Long): Boolean {
        return movieDao.isFavorite(id)
    }
    override fun scheduelApiWorker(action: String) {
        val inputData = workDataOf("action" to action)
        val workRequest = OneTimeWorkRequestBuilder<ApiWorker>()
            .setInputData(inputData)
            .build()
        workManager.enqueue(workRequest)
    }

}