package com.howdiedoodies.chatterby.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun SearchScreen(
    navController: NavController,
    viewModel: com.howdiedoodies.chatterby.viewmodel.SearchViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    favoriteViewModel: com.howdiedoodies.chatterby.viewmodel.FavoriteViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {
        TextField(
            value = uiState.query,
            onValueChange = { viewModel.onQueryChanged(it) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Search streamers...") }
        )
        LazyColumn {
            items(uiState.results) { cam ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(cam.username)
                    Button(onClick = { favoriteViewModel.addFavorite(cam.username) }) {
                        Text("Add")
                    }
                }
            }
        }
    }
}
