package com.example

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

@Composable
fun AppNavigation(
    viewModel: MainViewModel,
    navController: NavHostController = rememberNavController()
) {
    val isLoggedIn by viewModel.isLoggedIn.collectAsState()

    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn) "home" else "login"
    ) {
        composable("login") {
            LoginScreen(
                onLoginSuccess = { username ->
                    viewModel.login(username)
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }
        composable("home") {
            HomeScreen(
                viewModel = viewModel,
                onNavigateToDetail = { id -> navController.navigate("detail/$id") },
                onNavigateToAdmin = { navController.navigate("admin") },
                onLogout = {
                    viewModel.logout()
                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            )
        }
        composable(
            route = "detail/{doramaId}",
            arguments = listOf(navArgument("doramaId") { type = NavType.StringType })
        ) { backStackEntry ->
            val doramaId = backStackEntry.arguments?.getString("doramaId") ?: return@composable
            DetailScreen(
                doramaId = doramaId,
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() },
                onPlayVideo = { url ->
                    navController.navigate("player/${url.replace("/", "\\\\")}")
                }
            )
        }
        composable(
            route = "player/{videoUrl}",
            arguments = listOf(navArgument("videoUrl") { type = NavType.StringType })
        ) { backStackEntry ->
            val videoUrl = backStackEntry.arguments?.getString("videoUrl")?.replace("\\\\", "/") ?: return@composable
            PlayerScreen(
                videoUrl = videoUrl,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable("admin") {
            AdminScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
