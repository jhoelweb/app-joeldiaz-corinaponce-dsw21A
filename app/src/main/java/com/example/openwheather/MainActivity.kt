package com.example.openwheather

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

import com.example.openwheather.ui.WeatherUiState
import com.example.openwheather.ui.WeatherViewModel
import com.example.openwheather.ui.theme.OpenWheatherTheme
private const val OPEN_WEATHER_API_KEY = "551cbc9b56c2e57b180724bf13083953"
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            OpenWheatherTheme {
                WeatherScreen()
            }
        }
    }
}

@Composable
fun WeatherScreen(
    weatherViewModel: WeatherViewModel = viewModel()
) {

    var city by remember {
        mutableStateOf("")
    }

    val uiState by weatherViewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Open Weather"
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            OutlinedTextField(
                value = city,
                onValueChange = {
                    city = it
                },
                modifier = Modifier.weight(1f),
                label = {
                    Text("Ciudad")
                },
                singleLine = true
            )

            Button(
                onClick = {
                    weatherViewModel.searchWeather(
                        city = city,
                        apiKey = OPEN_WEATHER_API_KEY
                    )
                }
            ) {
                Text("Buscar")
            }
        }

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        when (val state = uiState) {

            is WeatherUiState.Idle -> {

                Text(
                    text = "Escribe una ciudad para consultar el clima"
                )
            }

            is WeatherUiState.Loading -> {

                CircularProgressIndicator()
            }

            is WeatherUiState.Success -> {

                Text(
                    text = "Ciudad: ${state.weather.name}"
                )

                Text(
                    text = "Temperatura: ${state.weather.main.temp} °C"
                )

                Text(
                    text = "Sensación térmica: ${state.weather.main.feels_like} °C"
                )

                Text(
                    text = "Humedad: ${state.weather.main.humidity}%"
                )

                Text(
                    text = "Presión: ${state.weather.main.pressure} hPa"
                )

                Text(
                    text = "Condición: ${
                        state.weather.weather.firstOrNull()?.description
                            ?: "Sin información"
                    }"
                )

                Text(
                    text = "Viento: ${state.weather.wind.speed} m/s"
                )
            }

            is WeatherUiState.Error -> {

                Text(
                    text = state.message
                )
            }
        }
    }
}