package com.howdiedoodies.chatterby.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.howdiedoodies.chatterby.data.Favorite
import com.howdiedoodies.chatterby.viewmodel.FavoriteViewModel

@Composable
fun MainScreen(navController: NavController, viewModel: FavoriteViewModel = viewModel()) {
    val favorites by viewModel.favorites.collectAsState(initial = emptyList())
    
    Column(modifier = Modifier.fillMaxSize()) {
        Button(onClick = { navController.navigate("search") }) {
            Text("Go to Search")
        }
        Text(
            text = "Favorites",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(16.dp)
        )
        LazyColumn {
            items(favorites) { favorite ->
                FavoriteItem(favorite)
            }
        }
    }
}

@Composable
fun FavoriteItem(favorite: Favorite) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = favorite.username,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = if (favorite.isOnline) "LIVE" else "Offline",
                color = if (favorite.isOnline) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
            )
        }
    }
}