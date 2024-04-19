package com.ltu.m7019e.moviedb.v24.database

import com.ltu.m7019e.moviedb.v24.model.MovieDetailResponse
import com.ltu.m7019e.moviedb.v24.model.MovieResponse
import com.ltu.m7019e.moviedb.v24.model.MovieReviewResponse
import com.ltu.m7019e.moviedb.v24.network.MovieDBApiService

interface MoviesRepository {
    suspend fun getPopularMovies(): MovieResponse
    suspend fun getTopRatedMovies(): MovieResponse
    suspend fun getMovieDetail(movieId: Long): MovieDetailResponse
    suspend fun getMovieReviews(movieId: Long): MovieReviewResponse

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
}