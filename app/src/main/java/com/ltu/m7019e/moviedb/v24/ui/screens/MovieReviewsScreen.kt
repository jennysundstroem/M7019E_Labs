package com.ltu.m7019e.moviedb.v24.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
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
import coil.compose.AsyncImage
import com.ltu.m7019e.moviedb.v24.model.Movie
import com.ltu.m7019e.moviedb.v24.utils.Constants
import com.ltu.m7019e.moviedb.v24.viewmodel.SelectedMovieUiState

@Composable
fun MovieReviewsScreen(
        selectedMovieUiState: SelectedMovieUiState,
        modifier: Modifier = Modifier,
    ) {
    when (selectedMovieUiState) {
        is SelectedMovieUiState.Success -> {
            Column(Modifier.width(IntrinsicSize.Max)) {

                Text(
                    text = selectedMovieUiState.movie.title,
                    style = MaterialTheme.typography.headlineSmall
                )

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


