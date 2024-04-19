package com.ltu.m7019e.moviedb.v24.network

import com.ltu.m7019e.moviedb.v24.model.MovieDetailResponse
import com.ltu.m7019e.moviedb.v24.model.MovieResponse
import com.ltu.m7019e.moviedb.v24.model.MovieVideo
import com.ltu.m7019e.moviedb.v24.model.MovieVideoResponse
import com.ltu.m7019e.moviedb.v24.utils.Constants
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieDBApiService {

    @GET("popular")
    suspend fun getPopularMovies(
        @Query("api_key")
        apiKey: String = Constants.API_KEY
    ): MovieResponse
    @GET("top_rated")
    suspend fun getTopRatedMovies(
        @Query("api_key")
        apiKey: String = Constants.API_KEY
    ): MovieResponse
    @GET("{movie_id}")
    suspend fun getMovieDetail(
        @Path("movie_id") id: Long,
        @Query("api_key") apiKey: String = Constants.API_KEY
    ): MovieDetailResponse
    @GET("{movie_id}/videos")
    suspend fun getVideos(
        @Path("movie_id") id: Long,
        @Query("api_key") apiKey: String = Constants.API_KEY
    ): MovieVideoResponse
}