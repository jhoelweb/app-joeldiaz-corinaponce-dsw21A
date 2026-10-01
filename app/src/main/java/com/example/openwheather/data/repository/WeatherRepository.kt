package com.example.openweather.data.repository

import com.example.openweather.data.model.WeatherResponse
import com.example.openweather.data.remote.ApiService

class WeatherRepository(
    private val apiService: ApiService
) {

    suspend fun getWeatherByCity(
        city: String,
        apiKey: String
    ): WeatherResponse {

        val locations = apiService.getLocation(
            city = city,
            apiKey = apiKey
        )

        if (locations.isEmpty()) {
            throw Exception("Ciudad no encontrada")
        }

        val location = locations.first()

        return apiService.getWeather(
            latitude = location.lat,
            longitude = location.lon,
            apiKey = apiKey
        )
    }
}