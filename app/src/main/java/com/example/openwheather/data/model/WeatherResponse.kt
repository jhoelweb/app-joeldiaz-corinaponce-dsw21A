package com.example.openweather.data.model

data class WeatherResponse(
    val name: String,
    val main: MainWeather,
    val weather: List<Weather>,
    val wind: Wind
)

data class MainWeather(
    val temp: Double,
    val feels_like: Double,
    val humidity: Int,
    val pressure: Int
)

data class Weather(
    val main: String,
    val description: String,
    val icon: String
)

data class Wind(
    val speed: Double
)