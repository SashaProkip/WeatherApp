package com.example.weatherapp.ui.states

import com.example.weatherapp.domain.models.entity.UserLocation

data class LocationState(
    val location: UserLocation = UserLocation(),
    val isLoading:Boolean = true,
    val error:String = ""
)
