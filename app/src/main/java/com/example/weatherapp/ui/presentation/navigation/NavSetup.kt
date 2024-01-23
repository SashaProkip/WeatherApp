package com.example.weatherapp.ui.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.weatherapp.ui.presentation.screens.favorite_screen.SearchResultScreen
import com.example.weatherapp.ui.presentation.screens.favorite_screen.FavouriteScreen
import com.example.weatherapp.ui.presentation.screens.favorite_screen.SearchViewModel
import com.example.weatherapp.ui.presentation.screens.main_screen.BottomNavItem
import com.example.weatherapp.ui.presentation.screens.main_screen.MainScreen
import com.example.weatherapp.ui.presentation.screens.main_screen.MainViewModel
import com.example.weatherapp.ui.presentation.screens.main_weather_screen.MainWeatherScreen
import com.example.weatherapp.ui.presentation.screens.onboard.OnBoardingScreen
import com.example.weatherapp.ui.presentation.screens.setting_screen.SettingScreen
import com.example.weatherapp.ui.presentation.navigation.Screens

@Composable
fun WeatherNavigation(
    navController: NavHostController,
    isFirstTime: Boolean
) {
    val startRoute = if (isFirstTime) Screens.OnBoardingScreen.route else Screens.MainScreen.route

    NavHost(navController = navController, startDestination = startRoute) {
        composable(route = Screens.OnBoardingScreen.route) {
            OnBoardingScreen(navController = navController)
        }
        composable(route = Screens.MainScreen.route) {
            MainScreen()
        }
    }
}

@Composable
fun MainNavigation(
    navController: NavHostController,
    searchViewModel: SearchViewModel = hiltViewModel(),
    mainViewModel: MainViewModel = hiltViewModel()
) {
    NavHost(navController = navController, startDestination = BottomNavItem.Home.route) {
        composable(route = BottomNavItem.Home.route) {
            MainWeatherScreen(navController = navController, viewModel = mainViewModel)
        }
        composable(route = Screens.Favourite.route) {
            FavouriteScreen(navController, viewModel = searchViewModel)
        }
        composable(route = Screens.Settings.route) {
            SettingScreen(mainViewModel)
        }

        composable(
            route = Screens.SearchResult.route + "/{query}",
            arguments = listOf(
                navArgument("query") {
                    type = NavType.StringType
                })
        ) { backstack ->
            val query = backstack.arguments?.getString("query") ?: ""

            SearchResultScreen(
                query = query,
                navController = navController,
                viewModel = searchViewModel
            )
        }


    }
}
