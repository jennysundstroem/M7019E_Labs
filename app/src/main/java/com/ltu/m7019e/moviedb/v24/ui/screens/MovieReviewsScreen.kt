package com.ltu.m7019e.moviedb.v24.ui.screens

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.items
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

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Button
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.ltu.m7019e.moviedb.v24.model.MovieVideo
import com.ltu.m7019e.moviedb.v24.utils.Constants.YOUTUBE_VIDEO_BASE_URL


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
                Text(
                    text = "Videos",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(16.dp)
                )
                Spacer(modifier = Modifier.size(8.dp))

                VideoList(videos = selectedMovieUiState.videos, modifier = modifier)

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
        Spacer(modifier = Modifier.size(8.dp))

    }
}


@Composable
fun VideoList(videos: List<MovieVideo>, modifier: Modifier = Modifier) {
    LazyRow(modifier = modifier) {
        items(videos) { video ->
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = video.name,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp),
                )
                ExoPlayerView(urlPath = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4")
                VideoButton(urlPath = YOUTUBE_VIDEO_BASE_URL + video.key, placeHolder = "Watch on Youtube")
            }
        }
    }
}
@OptIn(UnstableApi::class)
@Composable
fun ExoPlayerView(urlPath: String) {
    val context = LocalContext.current
    val exoPlayer = ExoPlayer.Builder(context).build()

    // Create a MediaSource
    val mediaSource = remember(urlPath) {
        MediaItem.fromUri(urlPath)
    }

    // Set MediaSource to ExoPlayer
    LaunchedEffect(mediaSource) {
        exoPlayer.setMediaItem(mediaSource)
        exoPlayer.prepare()
    }

    // Manage lifecycle events
    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }

    // Use AndroidView to embed an Android View (PlayerView) into Compose
    AndroidView(
        factory = { ctx ->
            PlayerView(ctx).apply {
                player = exoPlayer
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp) // Set your desired height
    )
}

@Composable
fun VideoButton(urlPath: String, placeHolder: String, modifier: Modifier = Modifier) {
    if (urlPath.isNotEmpty()) {
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
}
