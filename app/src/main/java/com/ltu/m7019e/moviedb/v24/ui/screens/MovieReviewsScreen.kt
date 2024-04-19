package com.ltu.m7019e.moviedb.v24.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ltu.m7019e.moviedb.v24.viewmodel.SelectedMovieUiState

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Button
import androidx.compose.ui.platform.LocalContext
import com.ltu.m7019e.moviedb.v24.model.MovieVideo
import com.ltu.m7019e.moviedb.v24.utils.Constants.YOUTUBE_VIDEO_BASE_URL


@Composable
fun MovieReviewsScreen(
    selectedMovieUiState: SelectedMovieUiState,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        when (selectedMovieUiState) {
            is SelectedMovieUiState.Success -> {
                Text(
                    text = selectedMovieUiState.movie.title,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(16.dp)
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    text = "Videos",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(16.dp)
                )
                VideoList(videos = selectedMovieUiState.videos, modifier = modifier)
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
}


@Composable
fun VideoList(videos: List<MovieVideo>, modifier: Modifier = Modifier) {
    LazyRow(modifier = modifier) {
        items(videos) { video ->
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = video.name,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                VideoButton(urlPath = YOUTUBE_VIDEO_BASE_URL + video.key, placeHolder = "Youtube Video")
            }
        }
    }
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

