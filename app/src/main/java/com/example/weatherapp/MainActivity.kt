package com.example.weatherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.navigation.compose.rememberNavController
import com.example.weatherapp.ui.presentation.navigation.WeatherNavigation
import com.example.weatherapp.ui.sharedpreferences.SharedPreferenceUtils
import com.example.weatherapp.ui.theme.WeatherAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            window.statusBarColor = MaterialTheme.colors.onBackground.toArgb()
            WeatherAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val navController = rememberNavController()
                    val sharedPref = SharedPreferenceUtils(this)
                    WeatherNavigation(navController,sharedPref.isFirstTime())
                }
            }
        }
    }
    companion object{
       const val LOCATION_PERM_MESSAGE = "Allowing an app to access your location will provide you with the most accurate and up-to-date weather information based on your current location." +
               "It will also be more convenient for you as you don't have to manually enter your location."

        const val BOTTOM_PADDING =  60
    }
}

