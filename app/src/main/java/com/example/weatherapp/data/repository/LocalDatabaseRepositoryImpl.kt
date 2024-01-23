package com.example.weatherapp.data.repository

import com.example.weatherapp.data.local.WeatherDao
import com.example.weatherapp.domain.models.entity.UserLocation
import com.example.weatherapp.domain.repository.LocalDatabaseRepository
import kotlinx.coroutines.flow.Flow

class LocalDatabaseRepositoryImpl(
    private val dao: WeatherDao
): LocalDatabaseRepository {

    override fun getAllPlaces(): Flow<List<UserLocation>> {
       return dao.getAllPlaces()
    }

    override suspend fun insertPlace(place: UserLocation) {
        dao.insertPlace(place)
    }

    override suspend fun deletePlace(place: UserLocation) {
        dao.deletePlace(place)
    }

    override suspend fun getLocationByLat(lat:String): UserLocation?{
        return dao.getLocationByLat(lat)
    }
}