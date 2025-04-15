package com.example.proyectofct.View

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.proyectofct.Controler.RetrofitClient
import com.example.proyectofct.Controler.recibirMensajeDeError
import com.example.proyectofct.MainActivity
import com.example.proyectofct.Model.Equipo
import com.example.proyectofct.Model.Requests.LoginRequest
import com.example.proyectofct.Model.Responses



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController) {
    var equipos by remember { mutableStateOf(listOf<Equipo>()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf("") }

    // Obtener equipos al iniciar la pantalla
    LaunchedEffect(Unit) {
        obtenerEquiposConReintentos(
            maxIntentos = 3,
            onSuccess = { resultado ->
                equipos = resultado
                MainActivity.equipos = resultado
                isLoading = false
                android.util.Log.d("Retrofit", "Equipos obtenidos: $resultado")
            },
            onError = { error ->
                equipos = listOf()
                isLoading = false
                errorMessage =
                    (error?.let { recibirMensajeDeError(it) } ?: "Error desconocido").toString()
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Listado de Equipos") },
                actions = {
                    IconButton(onClick = { navController.navigate("qr_scan") }) {
                        Icon(Icons.Filled.QrCodeScanner, contentDescription = "Escanear QR")
                    }
                    IconButton(onClick = { /* agregar equipo */ }) {
                        Icon(Icons.Filled.Add, contentDescription = "Añadir Ordenador")
                    }
                    IconButton(onClick = { /* prestar */ }) {
                        Icon(Icons.Filled.ShoppingCart, contentDescription = "Prestar Ordenador")
                    }
                    IconButton(onClick = { /* borrar */ }) {
                        Icon(Icons.Filled.Delete, contentDescription = "Borrar Ordenador")
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            when {
                // Mostrar el indicador de carga
                isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(50.dp)
                    )
                }

                // Mostrar mensaje de error
                errorMessage.isNotEmpty() -> {
                    Text(
                        text = "Error: $errorMessage",
                        modifier = Modifier.align(Alignment.Center),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.error
                    )

                }

                // Si no hay equipos, mostrar un mensaje
                equipos.isEmpty() -> {
                    Text(
                        text = "No se encontraron equipos",
                        modifier = Modifier.align(Alignment.Center)
                            .padding(horizontal = 16.dp, vertical = 16.dp),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                // Mostrar la lista de equipos
                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        items(equipos) { equipo ->
                            EquipoCard(equipo = equipo, onClick = {
                                navController.navigate("detail_screen/${equipo.id}")
                            })
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }

            }
            // Botón para reintentar
            if (!isLoading) {
                Spacer(modifier = Modifier.height(16.dp).align(Alignment.BottomCenter))
                Button(
                    modifier = Modifier.align(Alignment.BottomCenter),
                    onClick = {
                        // Reinicia el estado y vuelve a intentar
                        isLoading = true
                        errorMessage = ""
                        obtenerEquiposConReintentos(
                            maxIntentos = 3,
                            onSuccess = { resultado ->
                                equipos = resultado
                                MainActivity.equipos = resultado
                                isLoading = false
                            },
                            onError = { error ->
                                equipos = listOf()
                                isLoading = false
                                errorMessage =
                                    recibirMensajeDeError(error ?: Throwable("Error desconocido"))
                            }
                        )
                    }) { Icon(Icons.Default.Refresh, contentDescription = "Reintentar") }
            }
        }
    }
}

@Composable
fun EquipoCard(equipo: Equipo, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icono de ordenador (puedes cambiar esto por un recurso real si tienes uno)
            Icon(
                imageVector = Icons.Default.Computer,
                contentDescription = null,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = equipo.nombreequipo, style = MaterialTheme.typography.titleMedium)
                Text(text = equipo.estado, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

// Función para obtener equipos con reintentos
fun obtenerEquiposConReintentos(
    intentoActual: Int = 1,
    maxIntentos: Int,
    onSuccess: (List<Equipo>) -> Unit,
    onError: (Throwable?) -> Unit
) {
    RetrofitClient.instance.getEquipos(LoginRequest(MainActivity.correo, MainActivity.password))
        .enqueue(object : retrofit2.Callback<Responses.FeedResponse> {
            override fun onResponse(
                call: retrofit2.Call<Responses.FeedResponse>,
                response: retrofit2.Response<Responses.FeedResponse>
            ) {
                if (response.isSuccessful) {
                    val resultado = response.body()?.equipos ?: listOf()
                    onSuccess(resultado)
                } else {
                    if (intentoActual < maxIntentos) {
                        android.util.Log.w("Retrofit", "Intento $intentoActual fallido. Reintentando...")
                        obtenerEquiposConReintentos(intentoActual + 1, maxIntentos, onSuccess, onError)
                    } else {
                        onError(Throwable("Respuesta no exitosa"))
                    }
                }
            }

            override fun onFailure(call: retrofit2.Call<Responses.FeedResponse>, t: Throwable) {
                if (intentoActual < maxIntentos) {
                    android.util.Log.w("Retrofit", "Fallo $intentoActual: ${t.message}. Reintentando...")
                    obtenerEquiposConReintentos(intentoActual + 1, maxIntentos, onSuccess, onError)
                } else {
                    onError(t)
                }
            }
        })
}