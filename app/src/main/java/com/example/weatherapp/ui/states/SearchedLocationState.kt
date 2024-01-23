package com.example.weatherapp.ui.states

import com.example.weatherapp.domain.models.entity.UserLocation

data class SearchedLocationState(
    val data:List<UserLocation> = emptyList(),
    val isLoading:Boolean = true,
    val error:String = ""
)