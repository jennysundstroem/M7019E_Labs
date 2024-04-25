package com.ltu.m7019e.moviedb.v24.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ltu.m7019e.moviedb.v24.model.Movie

@Dao
interface MovieDao {
    @Query("SELECT * FROM favourite_movies")
    suspend fun getFavouriteMovies(): List<Movie>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun  insertFavouriteMovie(movie: Movie)

    @Query("SELECT * FROM favourite_movies WHERE id = :id")
    suspend fun getMovie(id: Long): Movie

    @Query("DELETE FROM favourite_movies WHERE id = :id")
    suspend fun deleteFavouriteMovie(id: Long)
}