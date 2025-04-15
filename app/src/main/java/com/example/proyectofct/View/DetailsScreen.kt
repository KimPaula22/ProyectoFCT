package com.example.proyectofct

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.proyectofct.Model.Equipo
import com.example.proyectofct.View.MainScreen
import com.example.proyectofct.View.QRConDescarga

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(navController: NavHostController, idEquipo: Int, equipos: List<Equipo>) {
    val equipo = equipos.firstOrNull { it.id == idEquipo }
    var mostrarQR by remember { mutableStateOf(false) }

    if (equipo != null) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Detalles de ${equipo.nombreequipo}") },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                        }
                    }
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
            ) {
                Text(text = "Nombre: ${equipo.nombreequipo}", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Estado: ${equipo.estado}", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "RAM: ${equipo.ram}", style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "CPU: ${equipo.cpu}", style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "GPU: ${equipo.gpu}", style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "ROM:", style = MaterialTheme.typography.bodyLarge)
                equipo.rom?.forEach { rom ->
                    Text(text = "- $rom", style = MaterialTheme.typography.bodyMedium)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Dispositivos PCIe:", style = MaterialTheme.typography.bodyLarge)
                equipo.dispositivos_pcie?.forEach { dispositivo ->
                    Text(text = "- $dispositivo", style = MaterialTheme.typography.bodyMedium)
                }
                Spacer(modifier = Modifier.height(8.dp))

                // Botón para mostrar/ocultar el QR
                Button(
                    onClick = { mostrarQR = !mostrarQR },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(if (mostrarQR) "Ocultar QR" else "Ver QR")
                }
                if (mostrarQR) {
                    Spacer(modifier = Modifier.height(16.dp))
                    QRConDescarga(numero = equipo.id.toString())
                }
            }
        }
    } else {
        // En caso de que no se encuentre el equipo
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Equipo no encontrado") },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
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
                Text(
                    text = "No se pudo encontrar el equipo con id: $idEquipo",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}
