package com.example.weatherapp.domain.repository

import com.example.weatherapp.data.utils.Response
import com.example.weatherapp.domain.models.entity.SavedLocationWeatherOverview
import com.example.weatherapp.domain.models.weather.daily_forecast.DailyForecastDTO
import com.example.weatherapp.domain.models.weather.hourly_forecast.HourlyForecastDTO
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {

    suspend fun getHourlyForecast(lat:String,long:String):Flow<Response<HourlyForecastDTO>>
    suspend fun getDailyForecast(lat:String,long:String):Flow<Response<DailyForecastDTO>>

    suspend fun getWeatherForSavedPlaces():Flow<Response<List<SavedLocationWeatherOverview>>>
}