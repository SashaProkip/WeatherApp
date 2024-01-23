package com.example.weatherapp.domain.location

import android.location.Location
import com.example.weatherapp.data.utils.Response
import kotlinx.coroutines.flow.Flow

interface LocationClient {

     fun getLocation(): Flow<Response<Location>>

    class LocationException( message:String):Exception(message)
}
