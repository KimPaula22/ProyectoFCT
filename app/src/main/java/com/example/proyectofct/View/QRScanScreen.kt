package com.example.proyectofct.View

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QRScanScreen(navController: NavHostController) {
    var scanResult by remember { mutableStateOf<String?>(null) }

    // El launcher para escanear el QR
    val scanLauncher = rememberLauncherForActivityResult(ScanContract()) { result ->
        // Si el resultado no es nulo, lo guardamos en scanResult
        if (result.contents != null) {
            scanResult = result.contents
            // Aquí llamamos a la función para que nos diga los detalles del equipo
            //fetchTeamDetails(scanResult ?: "")
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
