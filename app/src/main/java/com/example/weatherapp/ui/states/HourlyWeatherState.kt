package com.example.weatherapp.ui.states

import com.example.weatherapp.domain.mapper.HourlyForecast


data class HourlyWeatherState(
    val forecasts:Map<Int,List<HourlyForecast>> = emptyMap(),
    val isLoading:Boolean = true,
    val error:String = ""
)
