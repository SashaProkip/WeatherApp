package com.example.weatherapp.domain.utils

import kotlin.math.roundToInt

object GeoEncodingUtil {
    const val API_KEY = "26183ec9b33cd84fd457399caa3eeb21"
    const val baseUrl = "https://api.openweathermap.org"
}

object WeatherUtil{
    const val baseUrl = "https://api.open-meteo.com"

    fun Double.roundOfToFour():Double{
        return (this * 10000.0).roundToInt() /10000.0
    }
}

