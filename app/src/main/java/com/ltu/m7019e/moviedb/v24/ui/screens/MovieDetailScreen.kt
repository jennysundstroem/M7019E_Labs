package com.ltu.m7019e.moviedb.v24.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
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
import com.ltu.m7019e.moviedb.v24.viewmodel.SelectedMovieUiState

@Composable
fun MovieDetailScreen(
    selectedMovieUiState: SelectedMovieUiState,
    modifier: Modifier = Modifier
) {
    when (selectedMovieUiState) {
        is SelectedMovieUiState.Success -> {
            Column(Modifier.width(IntrinsicSize.Max)) {
                Box(Modifier.fillMaxWidth().padding(0.dp)) {
                    AsyncImage(
                        model = Constants.BACKDROP_IMAGE_BASE_URL + Constants.BACKDROP_IMAGE_WIDTH + selectedMovieUiState.movie.backdropPath,
                        contentDescription = selectedMovieUiState.movie.title,
                        modifier = modifier,
                        contentScale = ContentScale.Crop
                    )
                }
                Text(
                    text = selectedMovieUiState.movie.title,
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    text = selectedMovieUiState.movie.releaseDate,
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    text = selectedMovieUiState.movie.overview,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(modifier = Modifier.size(8.dp))

            }
        }
        is SelectedMovieUiState.Loading -> {
            Text(
                text = "Loading...",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(16.dp)
            )
        }
        is SelectedMovieUiState.Error -> {
            Text(
                text = "Error...",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}


/*
*
*                 /*
                Text(
                    text = selectedMovieUiState.movieDetail.toString(),
                    style = MaterialTheme.typography.bodySmall,
                    )

                MovieDetailGenre(selectedMovieUiState.movieDetail)
                WebPageButton(urlPath = movieDetail.homepage, placeHolder = "Visit Homepage")
                WebPageButton(urlPath = "https://www.imdb.com/title/$%7BmovieDetail.imdbid%7D/", placeHolder = "Visit IMDB Page")

                Button(
                    onClick = { onMoviewReviewClicked(movie) }
                ){
                    Text(text = "Reviews")
                }
            } */
* */