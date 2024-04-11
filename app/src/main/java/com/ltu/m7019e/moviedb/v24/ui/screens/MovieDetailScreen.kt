package com.ltu.m7019e.moviedb.v24.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import coil.compose.AsyncImage
import com.ltu.m7019e.moviedb.v24.model.Movie
import com.ltu.m7019e.moviedb.v24.model.MovieDetail
import com.ltu.m7019e.moviedb.v24.utils.Constants

@Composable
fun MovieDetailScreen(movie: Movie, movieDetail: MovieDetail,
                      modifier: Modifier = Modifier) {
    Column {
        Box {
            AsyncImage(
                model = Constants.BACKDROP_IMAGE_BASE_URL + Constants.BACKDROP_IMAGE_WIDTH + movie.backdropPath,
                contentDescription = movie.title,
                modifier = modifier,
                contentScale = ContentScale.Crop
            )
        }
        Text(text = movie.title, style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.size(8.dp))
        Text(text = movie.releaseDate, style = MaterialTheme.typography.bodySmall)
        Spacer(modifier = Modifier.size(8.dp))
        Text(
            text = movie.overview,
            style = MaterialTheme.typography.bodySmall,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.size(8.dp))
        MovieDetailGenre(movieDetail = movieDetail)
        WebPageButton(urlPath = movieDetail.homepage, placeHolder = "Visit Homepage")
        WebPageButton(urlPath = "https://www.imdb.com/title/${movieDetail.imdbid}/", placeHolder = "Visit IMDB Page")
    }
}
@Composable
fun MovieDetailGenre(movieDetail: MovieDetail,
                      modifier: Modifier = Modifier) {
    val genresText = movieDetail.genres.joinToString(", ")
    Text(text = genresText, style = MaterialTheme.typography.bodySmall)
    Spacer(modifier = Modifier.size(8.dp))
}

@Composable
fun WebPageButton(urlPath: String, placeHolder: String, modifier: Modifier = Modifier) {
    val context = LocalContext.current

    Button(
        onClick = {
            val uri = Uri.parse(urlPath)
            val intent = Intent(Intent.ACTION_VIEW, uri)

            context.startActivity(intent)
        },
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(text = placeHolder)
    }
}