package com.ltu.m7019e.moviedb.v24.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.ltu.m7019e.moviedb.v24.database.MoviesRepository
import com.ltu.m7019e.moviedb.v24.database.SavedMovieRepository

class ApiWorker(ctx: Context, params: WorkerParameters, private val moviesRepository: MoviesRepository, private val savedMovieRepository: SavedMovieRepository) : CoroutineWorker(ctx, params) {
    override suspend fun doWork(): Result {
        return try {
            val action = inputData.getString("action")
            when (action) {
                "getPopularMovies" -> {
                    val movies = moviesRepository.getPopularMovies().results
                    savedMovieRepository.deleteCachedMovies()
                    //for all movies in the list, save them to the database
                    movies.forEach { movie ->
                        savedMovieRepository.insertMovie(movie)
                        savedMovieRepository.setCachedMovie(movie.id)
                    }                }
                "getTopRatedMovies" -> {
                    val movies = moviesRepository.getTopRatedMovies().results
                    movies.forEach { movie ->
                        savedMovieRepository.insertMovie(movie)
                        savedMovieRepository.setCachedMovie(movie.id)
                    }
                }

            }
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}