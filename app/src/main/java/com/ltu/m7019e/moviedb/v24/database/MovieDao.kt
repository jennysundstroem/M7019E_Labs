package com.ltu.m7019e.moviedb.v24.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ltu.m7019e.moviedb.v24.model.Movie

@Dao
interface MovieDao {
    @Query("SELECT * FROM movies WHERE isFavourite = 1")
    suspend fun getFavouriteMovies(): List<Movie>
    @Query("UPDATE movies SET isFavourite = 1 WHERE id = :id")
    suspend fun setFavouriteMovie(id: Long)
    @Query("UPDATE movies SET isFavourite = 0 WHERE id = :id")
    suspend fun deleteFavouriteMovie(id: Long)
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun  insertMovie(movie: Movie)
    @Query("UPDATE movies SET isCached = 1 WHERE id = :id")
    suspend fun setCachedMovie(id: Long)
    @Query("SELECT * FROM movies WHERE id = :id")
    suspend fun getMovie(id: Long): Movie
    @Query("DELETE FROM movies WHERE isCached = 1 AND isFavourite = 0")
    suspend fun deleteCachedMovies()
    @Query("SELECT * FROM movies WHERE isCached = 1")
    suspend fun getCachedMovies(): List<Movie>
}