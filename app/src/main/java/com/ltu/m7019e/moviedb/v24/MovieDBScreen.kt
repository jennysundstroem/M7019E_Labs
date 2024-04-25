package com.ltu.m7019e.moviedb.v24
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.navigation.compose.composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.vectorResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ltu.m7019e.moviedb.v24.ui.screens.MovieDetailScreen
import com.ltu.m7019e.moviedb.v24.ui.screens.MovieListGridScreen
import com.ltu.m7019e.moviedb.v24.ui.screens.MovieListScreen
import com.ltu.m7019e.moviedb.v24.ui.screens.MovieReviewsScreen
import com.ltu.m7019e.moviedb.v24.viewmodel.MovieDBViewModel

enum class MovieDBScreen(@StringRes val title: Int) {
    List(title = R.string.app_name),
    ListGrid(title = R.string.app_name ),
    Detail(title = R.string.movie_detail),
    Reviews(title = R.string.movie_reviews)
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDBAppBar(
    currentScreen: MovieDBScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    switchScreen: () -> Unit,
    movieDBViewModel: MovieDBViewModel,
    modifier: Modifier = Modifier
) {
    var menuExpanded by remember { mutableStateOf(false) }
    TopAppBar(
        title = { Text(stringResource(currentScreen.title)) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        actions = {
            IconButton(onClick = {
                // Set the menu expanded state to the opposite of the current state
                menuExpanded = !menuExpanded
            }) {
                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = "Open Menu to select different movie lists"
                )
            }
            DropdownMenu(expanded = menuExpanded, onDismissRequest = { menuExpanded = false }) {
                DropdownMenuItem(
                    onClick = {
                        // Set the selected movie list to popular
                        movieDBViewModel.getPopularMovies()
                        // Set the menu expanded state to false
                        menuExpanded = false

                    },
                    text = {
                        Text("Popular movies")
                    }
                )
                DropdownMenuItem(
                    onClick = {
                        // Set the selected movie list to popular
                        movieDBViewModel.getTopRatedMovies()
                        // Set the menu expanded state to false
                        menuExpanded = false

                    },
                    text = {
                        Text("Top Rated movies")
                    }
                )
                DropdownMenuItem(
                    onClick = {
                        // Set the selected movie list to popular
                        movieDBViewModel.getSavedMovies()
                        // Set the menu expanded state to false
                        menuExpanded = false

                    },
                    text = {
                        Text("Saved movies")
                    }
                )
            }
        },
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        },
        /* actions = {
            if(currentScreen == MovieDBScreen.List || currentScreen == MovieDBScreen.ListGrid) {
                IconButton(onClick = switchScreen) {
                    if (currentScreen == MovieDBScreen.List) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.baseline_grid_view_24),
                            contentDescription = ("Switch Screens")
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.List,
                            contentDescription = ("Switch Screens")
                        )
                    }

                }
            }
        } */
    )
}

@Composable
fun MovieDBApp(
    navController: NavHostController = rememberNavController()
){
    val backStackEntry by navController.currentBackStackEntryAsState()

    val currentScreen = MovieDBScreen.valueOf(
        backStackEntry?.destination?.route ?: MovieDBScreen.List.name
    )
    val movieDBViewModel: MovieDBViewModel = viewModel(factory = MovieDBViewModel.Factory)
    Scaffold(
        topBar = {
            MovieDBAppBar(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() },
                movieDBViewModel = (movieDBViewModel
                ),
                        switchScreen = {
                    navController.navigate(
                        if (currentScreen == MovieDBScreen.List)
                            MovieDBScreen.ListGrid.name
                        else MovieDBScreen.List.name
                    )
                }
            )
        }
    ) { innerPadding ->
        val movieDBViewModel: MovieDBViewModel = viewModel(factory = MovieDBViewModel.Factory)

        NavHost(navController = navController,
            startDestination = MovieDBScreen.List.name,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            composable(route = MovieDBScreen.List.name) {
                MovieListScreen(
                    movieListUiState = movieDBViewModel.movieListUiState,
                    onMovieListItemClicked = {
                        movieDBViewModel.setSelectedMovie(it)
                        navController.navigate(MovieDBScreen.Detail.name)
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                )
            }

                composable(route = MovieDBScreen.ListGrid.name) {
                    MovieListGridScreen(
                        movieListUiState = movieDBViewModel.movieListUiState,
                        onMovieListItemClicked = {
                            movieDBViewModel.setSelectedMovie(it)
                            navController.navigate(MovieDBScreen.Detail.name)
                        },
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    )
                }
            composable(route = MovieDBScreen.Detail.name) {
                MovieDetailScreen(
                    movieDBViewModel = movieDBViewModel,
                    selectedMovieUiState = movieDBViewModel.selectedMovieUiState,
                    modifier = Modifier,
                    onMoviewReviewClicked = {
                        movieDBViewModel.setSelectedMovie(it)
                        navController.navigate(MovieDBScreen.Reviews.name)
                    },
                )
                    }
            composable(route = MovieDBScreen.Reviews.name) {
                MovieReviewsScreen(
                    selectedMovieUiState = movieDBViewModel.selectedMovieUiState,
                    modifier = Modifier
                )
            }
        }
    }
}