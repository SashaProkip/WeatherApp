package com.example.weatherapp.domain.mapper

import com.example.weatherapp.domain.mapper.HourlyForecast

data class IndexedWeatherInfo(
    val index:Int,
    val hourlyForecast: HourlyForecast
)
