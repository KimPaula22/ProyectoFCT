package com.example.proyectofct.View

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.proyectofct.Controler.RetrofitClient
import com.example.proyectofct.Controler.llamadas.obtenerEquipo
import com.example.proyectofct.Controler.recibirMensajeDeError
import com.example.proyectofct.DetailScreen
import com.example.proyectofct.MainActivity
import com.example.proyectofct.Model.Equipo
import com.example.proyectofct.Model.Requests.LoginRequest
import com.example.proyectofct.Model.Responses.EquipoResponse
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import retrofit2.Call
import retrofit2.Response

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QRScanScreen(navController: NavHostController) {
    var scanResult by remember { mutableStateOf<String?>(null) }
    val scannedId = scanResult?.toIntOrNull()

    // El launcher para escanear el QR
    val scanLauncher = rememberLauncherForActivityResult(ScanContract()) { result ->
        // Si el resultado no es nulo, lo guardamos en scanResult
        if (result.contents != null) {
            scanResult = result.contents
            val scannedId = scanResult?.toIntOrNull()
            //buscar si esta en el mainactivity.equipos
            if (scannedId != null) {
                val equipoEncontrado = MainActivity.equipos.find { it.id == scannedId }
                if (equipoEncontrado != null) {
                    navController.navigate("detail_screen/${equipoEncontrado.id}")
                } else {
                    Toast.makeText(
                        navController.context,
                        "Obteniendo equipo de la base de datos...",
                        Toast.LENGTH_LONG
                    ).show()
                    obtenerEquipo(scannedId, navController.context, {
                        if (it != null) {
                            MainActivity.equipos.add(it)
                            navController.navigate("detail/${it.id}")
                        } else {
                            Toast.makeText(
                                navController.context,
                                "No se pudo obtener el equipo o no existe",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }, navController, 0)
                }
            }else{
                Toast.makeText(
                    navController.context,
                    "QR no valido",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Escanear QR para Equipo") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            // Muestra el resultado si ya se escaneó un QR
            scanResult?.let { result ->
                Text(text = "Código QR leído: $result", style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(16.dp))
            }
            // Botón para iniciar el escaneo
            Button(
                onClick = {
                    // Configura las opciones del escáner; por ejemplo, sólo códigos QR
                    val options = ScanOptions().apply {
                        setDesiredBarcodeFormats(ScanOptions.QR_CODE)
                        setPrompt("Escanea el código QR")
                        setCameraId(0) // 0 es la cámara trasera
                    }
                    scanLauncher.launch(options)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Abrir Escáner QR")
            }
        }
    }
}
