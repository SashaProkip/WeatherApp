package com.example.weatherapp.domain.models.entity

import com.example.weatherapp.domain.mapper.HourlyForecast

data class SavedLocationWeatherOverview(
    val location: UserLocation,
    val forecast : HourlyForecast
)
