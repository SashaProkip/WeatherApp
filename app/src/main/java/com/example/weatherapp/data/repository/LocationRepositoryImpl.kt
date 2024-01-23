package com.example.weatherapp.data.repository

import com.example.weatherapp.data.remote.geoencoding.GeoEncodingResponse
import com.example.weatherapp.data.utils.Response
import com.example.weatherapp.domain.models.entity.UserLocation
import com.example.weatherapp.domain.models.geoencoding.GeoEncodingDTO
import com.example.weatherapp.domain.repository.LocationRepository
import com.example.weatherapp.domain.utils.WeatherUtil.roundOfToFour
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class LocationRepositoryImpl(
    private val geoEncodingApi: GeoEncodingResponse
): LocationRepository {

    override suspend fun getNameFromCoordinates(
        latitude: String,
        longitude: String
    ): Flow<Response<GeoEncodingDTO>> = flow{
        emit(Response.Loading())
        try {
            val response = geoEncodingApi.getNameFromCoordinates(lat = latitude, lon = longitude)
            if(response.isEmpty()){
                emit(Response.Error("Sorry can't fetch location"))
                return@flow
            }

            emit(Response.Success(response[0]))
        }catch (e: HttpException){
            emit(Response.Error(message = "Oops, something went wrong"))
        }
        catch (e: IOException){
            emit(Response.Error(message = "Couldn't reach server check your internet connection"))
        }
    }

    override suspend fun getCoordinatesFromName(name: String): Flow<Response<List<UserLocation>>> = flow{
        emit(Response.Loading())
        try {
            val response = geoEncodingApi.getCoordinatesFromName(name =name).map {
                UserLocation(
                    city = it.name?:"",
                    state = it.state?:"",
                    country = it.country?:"",
                    lat = it.lat.roundOfToFour(),
                    long = it.lon.roundOfToFour()
                )
            }
            if(response.isEmpty()){
                emit(Response.Error("Sorry no result found."))
                return@flow
            }

            emit(Response.Success(response))
        }catch (e: HttpException){
            emit(Response.Error(message = "Oops, something went wrong"))
        }
        catch (e: IOException){
            emit(Response.Error(message = "Couldn't reach server check your internet connection"))
        }
    }
}