package com.ltu.m7019e.moviedb.v24.database

import com.ltu.m7019e.moviedb.v24.model.MovieDetail
class MovieDetails {
    fun getMovieDetails(): List<MovieDetail> {
        return listOf<MovieDetail>(
            MovieDetail(
                1011985,
                genres = arrayOf("Animation", "Action", "Adventure", "Comedy", "Family"),
                "https://www.dreamworks.com/movies/kung-fu-panda-4",
                "tt21692408"
            ),
            MovieDetail(
                693134,
                arrayOf("Science Fiction", "Adventure"),
                "https://www.dunemovie.com",
                "tt15239678"
            ),
            MovieDetail(
                823464,
                arrayOf("Action", "Science Fiction", "Adventure", "Fantasy"),
                "https://www.godzillaxkongmovie.com",
                "tt14539740"
            ),
            MovieDetail(
                359410,
                arrayOf("Action", "Thriller"),
                "https://www.amazon.com/gp/video/detail/B0CH5YQPZQ",
                "tt3359350"
            ),
            MovieDetail(
                984324,
                arrayOf("Action", "Thriller"),
                "https://www.netflix.com/title/81654966",
                "tt27487473"
            ),
            MovieDetail(
                935271,
                arrayOf("Science Fiction", "Action"),
                "",
                "tt12774526"
            ),
            MovieDetail(
                1181548,
                arrayOf("Action", "Mystery", "Thriller"),
                "https://www.netflix.com/title/81579704",
                "tt28943278"
            ),
            MovieDetail(
                634492,
                arrayOf("Action", "Fantasy"),
                "https://www.madameweb.movie",
                "tt11057302"
            ),
            MovieDetail(
                1125311,
                arrayOf("Horror", "Mystery", "Thriller"),
                "https://www.imaginary.movie/",
                "tt26658104"
            ),
            MovieDetail(
                940551,
                arrayOf("Animation", "Action", "Adventure", "Comedy", "Family"),
                "https://www.migration.movie",
                "tt6495056"
            )
        )
    }
}
