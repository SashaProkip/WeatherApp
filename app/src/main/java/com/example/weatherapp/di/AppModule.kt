package com.example.weatherapp.di

import android.content.Context
import androidx.room.Room
import com.example.weatherapp.data.local.WeatherDatabase
import com.example.weatherapp.data.remote.geoencoding.GeoEncodingResponse
import com.example.weatherapp.data.remote.weather.WeatherResponse
import com.example.weatherapp.data.repository.LocalDatabaseRepositoryImpl
import com.example.weatherapp.data.repository.LocationRepositoryImpl
import com.example.weatherapp.data.repository.WeatherRepositoryImpl
import com.example.weatherapp.domain.repository.WeatherRepository
import com.example.weatherapp.domain.utils.GeoEncodingUtil
import com.example.weatherapp.domain.location.DefaultLocationManager
import com.example.weatherapp.domain.location.LocationClient
import com.example.weatherapp.domain.repository.LocalDatabaseRepository
import com.example.weatherapp.domain.repository.LocationRepository
import com.example.weatherapp.domain.utils.WeatherUtil
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun getDefaultLocationManager(@ApplicationContext context: Context): LocationClient {
        return DefaultLocationManager(
            context,
            LocationServices.getFusedLocationProviderClient(context)
        )
    }

    @Singleton
    @Provides
    fun provideGeoEncodingApi(): GeoEncodingResponse {
        return Retrofit.Builder()
            .baseUrl(GeoEncodingUtil.baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GeoEncodingResponse::class.java)
    }

    @Singleton
    @Provides
    fun provideWeatherApi(): WeatherResponse {
        return Retrofit.Builder()
            .baseUrl(WeatherUtil.baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherResponse::class.java)
    }

    @Singleton
    @Provides
    fun provideRoomDatabase(@ApplicationContext context: Context): WeatherDatabase =
        Room.databaseBuilder(
            context = context,
            klass = WeatherDatabase::class.java,
            name = WeatherDatabase.DATABASE_NAME
        ).fallbackToDestructiveMigration()
            .build()

    @Singleton
    @Provides
    fun getLocationRepository(
        geoEncodingApi: GeoEncodingResponse
    ): LocationRepository = LocationRepositoryImpl(geoEncodingApi)

    @Singleton
    @Provides
    fun getWeatherRepository(
        weatherApi: WeatherResponse,
        db: WeatherDatabase,
        @ApplicationContext context: Context
    ): WeatherRepository = WeatherRepositoryImpl(weatherApi,db.dao,context)

    @Singleton
    @Provides
    fun getLocalDatabaseRepository(
        db: WeatherDatabase
    ): LocalDatabaseRepository = LocalDatabaseRepositoryImpl(db.dao)
}