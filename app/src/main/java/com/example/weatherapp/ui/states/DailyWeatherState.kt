package com.example.weatherapp.ui.states

import com.example.weatherapp.domain.mapper.DayWiseForecast

data class DailyWeatherState(
    val forecasts:List<DayWiseForecast> = emptyList(),
    val isLoading:Boolean = true,
    val error:String = ""
)
