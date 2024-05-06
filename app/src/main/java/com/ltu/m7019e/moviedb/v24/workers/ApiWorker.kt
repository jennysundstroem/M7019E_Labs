package com.ltu.m7019e.moviedb.v24.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.ltu.m7019e.moviedb.v24.database.DefaultAppContainer
import com.ltu.m7019e.moviedb.v24.database.SavedMovieRepository
import com.ltu.m7019e.moviedb.v24.network.MovieDBApiService

class ApiWorker(
    ctx: Context,
    params: WorkerParameters
) : CoroutineWorker(ctx, params) {
    private val container = DefaultAppContainer(ctx)
    override suspend fun doWork(): Result {
        return try {
            val action = inputData.getString("action")
            when (action) {
                "getPopularMovies" -> {
                    val movies = container.moviesRepository.getPopularMovies().results
                    container.savedMovieRepository.deleteCachedMovies()
                    //for all movies in the list, save them to the database
                    movies.forEach { movie ->
                        container.savedMovieRepository.insertMovie(movie)
                        container.savedMovieRepository.setCachedMovie(movie.id)
                    }
                }
                "getTopRatedMovies" -> {
                    val movies = container.moviesRepository.getPopularMovies().results
                    movies.forEach { movie ->
                        container.savedMovieRepository.insertMovie(movie)
                        container.savedMovieRepository.setCachedMovie(movie.id)
                    }
                }
            }
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}