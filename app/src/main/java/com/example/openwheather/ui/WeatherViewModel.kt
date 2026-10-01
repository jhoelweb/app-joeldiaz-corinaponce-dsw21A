package com.example.openwheather.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.openweather.data.model.WeatherResponse
import com.example.openweather.data.remote.RetrofitInstance
import com.example.openweather.data.repository.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class WeatherUiState {

    object Idle : WeatherUiState()

    object Loading : WeatherUiState()

    data class Success(
        val weather: WeatherResponse
    ) : WeatherUiState()

    data class Error(
        val message: String
    ) : WeatherUiState()
}

class WeatherViewModel : ViewModel() {

    private val repository = WeatherRepository(
        RetrofitInstance.api
    )

    private val _uiState =
        MutableStateFlow<WeatherUiState>(
            WeatherUiState.Idle
        )

    val uiState: StateFlow<WeatherUiState> =
        _uiState.asStateFlow()

    fun searchWeather(
        city: String,
        apiKey: String
    ) {
        if (city.isBlank()) {
            _uiState.value = WeatherUiState.Error(
                "Escribe una ciudad"
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = WeatherUiState.Loading

            try {
                val weather = repository.getWeatherByCity(
                    city = city,
                    apiKey = apiKey
                )

                _uiState.value =
                    WeatherUiState.Success(weather)

            } catch (e: Exception) {
                _uiState.value =
                    WeatherUiState.Error(
                        e.message
                            ?: "Ocurrió un error al consultar el clima"
                    )
            }
        }
    }
}