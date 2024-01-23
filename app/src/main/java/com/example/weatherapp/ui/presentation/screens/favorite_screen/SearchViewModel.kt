package com.example.weatherapp.ui.presentation.screens.favorite_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.utils.Response
import com.example.weatherapp.domain.mapper.HourlyForecast
import com.example.weatherapp.domain.models.entity.UserLocation
import com.example.weatherapp.domain.mapper.toDailyHourlyForecast
import com.example.weatherapp.domain.repository.LocalDatabaseRepository
import com.example.weatherapp.ui.states.HourlyWeatherState
import com.example.weatherapp.domain.repository.LocationRepository
import com.example.weatherapp.domain.repository.WeatherRepository
import com.example.weatherapp.ui.states.SavedPlaceState
import com.example.weatherapp.ui.states.SearchedLocationState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val locationRepository: LocationRepository,
    private val localDbRepository: LocalDatabaseRepository
) : ViewModel() {

    init {
        getSavedWeather()
    }

    private val _locationSearchResult = MutableStateFlow(SearchedLocationState())
    val locationSearchResult = _locationSearchResult.asStateFlow()

    private val _currentForecast = MutableStateFlow(HourlyWeatherState())

    private val _savedPlacesState = MutableStateFlow(SavedPlaceState())
    val savedPlaceState = _savedPlacesState.asStateFlow()

    private val savedWeatherState = MutableStateFlow(SavedWeatherState())


    fun getAllSavedPlaces(): Flow<List<UserLocation>> {
        return localDbRepository.getAllPlaces()
    }

    fun getSavedWeather() {
        viewModelScope.launch(Dispatchers.IO) {
            weatherRepository.getWeatherForSavedPlaces().collectLatest { res ->
                when (res) {
                    is Response.Loading -> _savedPlacesState.update {
                        it.copy(
                            isLoading = true,
                            error = "",
                            isEmpty = false
                        )
                    }
                    is Response.Success -> {
                        res.data?.let { savedWeathers ->
                            //To remove same location from search result.
                            _savedPlacesState.update {
                                it.copy(
                                    isLoading = false,
                                    error = "",
                                    isEmpty = false,
                                    savedPlaces = savedWeathers
                                )
                            }
                        }
                    }
                    is Response.Error -> _savedPlacesState.update {
                        it.copy(
                            isLoading = false,
                            isEmpty = false,
                            error = res.message ?: "Something went wrong."
                        )
                    }
                }
            }
        }
    }

    fun addPlace(place: UserLocation) {
        viewModelScope.launch(Dispatchers.IO) {
            localDbRepository.insertPlace(place)
        }
    }

    fun deletePlace(place: UserLocation) {
        viewModelScope.launch {
            localDbRepository.deletePlace(place)
        }
    }

    fun removePlace(place: UserLocation) {
        deletePlace(place)
        getSavedWeather()
    }

    suspend fun getPlaceByLat(lat: String): UserLocation? {
        return localDbRepository.getLocationByLat(lat)
    }

    fun savePlace(place: UserLocation) {
        addPlace(place)
        getSavedWeather()
    }

    private fun getCurrentForecast(
        lat: String,
        long: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            weatherRepository.getHourlyForecast(lat, long).collectLatest { res ->
                when (res) {
                    is Response.Loading -> _currentForecast.update {
                        it.copy(
                            isLoading = true,
                            error = ""
                        )
                    }
                    is Response.Error -> _currentForecast.update {
                        Log.e("ViewModel", res.message.toString())
                        it.copy(
                            isLoading = false,
                            error = res.message ?: "An unknown error occurred"
                        )
                    }
                    is Response.Success -> {
                        if (res.data == null) {
                            _currentForecast.update {
                                it.copy(
                                    isLoading = false,
                                    error = res.message ?: "Sorry can't fetch weather right now."
                                )
                            }
                        } else {
                            val data = res.data.toDailyHourlyForecast()
                            _currentForecast.update {
                                it.copy(
                                    forecasts = data,
                                    isLoading = false,
                                    error = ""
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    fun getLocation(city: String) {
        viewModelScope.launch {
            locationRepository.getCoordinatesFromName(city).collectLatest { res ->
                when (res) {
                    is Response.Loading -> _locationSearchResult.update {
                        it.copy(
                            isLoading = true,
                            error = ""
                        )
                    }
                    is Response.Success -> {
                        res.data?.let { userLoc ->
                            //To remove same location from search result.
                            val searchResult = userLoc.distinctBy { it.city + it.state }
                            _locationSearchResult.update {
                                it.copy(
                                    data = searchResult,
                                    isLoading = false,
                                    error = ""
                                )
                            }
                        }
                    }
                    is Response.Error -> _locationSearchResult.update {
                        it.copy(
                            isLoading = false,
                            error = res.message ?: "Something went wrong."
                        )
                    }
                }
            }
        }
    }


    companion object {
        const val TAG = "Search Viewmodel"
    }
}

data class SavedWeatherState(
    val weather: List<HourlyForecast> = emptyList(),
    val error: String = ""
)