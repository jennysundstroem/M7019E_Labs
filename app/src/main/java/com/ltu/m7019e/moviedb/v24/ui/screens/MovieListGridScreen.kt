package com.ltu.m7019e.moviedb.v24.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ltu.m7019e.moviedb.v24.model.Movie
import com.ltu.m7019e.moviedb.v24.utils.Constants
import com.ltu.m7019e.moviedb.v24.viewmodel.MovieListUiState

@Composable
fun MovieListGridScreen(
    movieListUiState: MovieListUiState,
    onMovieListItemClicked: (Movie) -> Unit,
    modifier: Modifier = Modifier,
    columns: Int = 3
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(columns)
    ) {
        when(movieListUiState) {
            is MovieListUiState.Success -> {
                items(movieListUiState.movies) { movie ->
                    MovieListGridItem(
                        movie = movie,
                        onMovieListItemClicked,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
            is MovieListUiState.Loading -> {
                item {
                    LoadingItem()
                }
            }
            is MovieListUiState.Error -> {
                item {
                    ErrorItem()
                }
            }

            MovieListUiState.NoNetwork ->
                item {
                    Column {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "No Internet Connection",
                            modifier = Modifier.size(200.dp)
                        )
                        Text(
                            text = "No Network Connection!",
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieListGridItem(
    movie: Movie,
    onMovieListItemClicked: (Movie) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(200.dp),
        onClick = { onMovieListItemClicked(movie) }
    ) {
        Column {
            Text(
                text = movie.title,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.size(4.dp))
            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp)
            ) {

                AsyncImage(
                    model = Constants.POSTER_IMAGE_BASE_URL + Constants.POSTER_IMAGE_WIDTH + movie.posterPath,
                    contentDescription = movie.title,
                    contentScale = ContentScale.Crop,
                    modifier = modifier.width(92.dp)
                        .height(138.dp),
                )
            }

        }
    }
}
@Composable
fun LoadingItem() {
    Text(
        text = "Loading...",
        style = MaterialTheme.typography.bodySmall,
        modifier = Modifier.padding(16.dp)
    )
}

@Composable
fun ErrorItem() {
    Text(
        text = "Error: Something went wrong!",
        style = MaterialTheme.typography.bodySmall,
        modifier = Modifier.padding(16.dp)
    )
}
