package com.howdiedoodies.chatterby.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "search") {
        composable("search") { SearchScreen(navController) }
        composable("favorites") { MainScreen(navController) }
    }
}
