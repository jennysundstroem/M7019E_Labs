package com.ltu.m7019e.moviedb.v24.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ltu.m7019e.moviedb.v24.model.MovieGenre
import com.ltu.m7019e.moviedb.v24.model.MovieReview
import com.ltu.m7019e.moviedb.v24.model.MovieReviewResponse
import com.ltu.m7019e.moviedb.v24.viewmodel.SelectedMovieUiState

@Composable
fun MovieReviewsScreen(
    selectedMovieUiState: SelectedMovieUiState,
    modifier: Modifier = Modifier,
) {
    when (selectedMovieUiState) {
        is SelectedMovieUiState.Success -> {
            Column(modifier) {
                Text(
                    text = selectedMovieUiState.movie.title,
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    text = "Reviews:",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
                MovieReviewList(reviews = selectedMovieUiState.movieReviews)
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

@Composable
fun MovieReviewList(reviews: List<MovieReview>) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        items(reviews) { review ->
            MovieReviewItem(review = review)
        }
    }
}

@Composable
fun MovieReviewItem(review: MovieReview) {
    Column(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .width(180.dp)
    ) {
        Text(
            text = review.movieReviewAuthor.username,
            style = MaterialTheme.typography.bodySmall,
            color = Color.Black,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = review.content,
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray,
            maxLines = 3,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = review.movieReviewAuthor.rating?.toString() ?: "--",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Black,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth())
    }
}
