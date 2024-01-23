package com.example.weatherapp.domain.repository

import com.example.weatherapp.data.utils.Response
import com.example.weatherapp.domain.models.entity.UserLocation
import com.example.weatherapp.domain.models.geoencoding.GeoEncodingDTO
import kotlinx.coroutines.flow.Flow

interface LocationRepository {

    suspend fun getNameFromCoordinates(
        latitude: String,
        longitude: String
    ): Flow<Response<GeoEncodingDTO>>

    suspend fun getCoordinatesFromName(name: String): Flow<Response<List<UserLocation>>>

}