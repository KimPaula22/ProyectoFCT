package com.example.proyectofct

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.proyectofct.Model.Equipo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(navController: NavHostController, equipoNombre: String) {
    // Lista de ejemplo para mostrar los detalles (hasta que tengas una base de datos)
    val equipos = listOf(
        Equipo("Ordenador A", "Disponible", "16GB", "Intel i7", "NVIDIA GTX 1060"),
        Equipo("Ordenador B", "En préstamo", "8GB", "AMD Ryzen 5", "NVIDIA GTX 1650"),
        Equipo("Ordenador C", "Disponible", "32GB", "Intel i9", "NVIDIA RTX 3080"),
        Equipo("Ordenador D", "En reparación", "16GB", "AMD Ryzen 7", "NVIDIA RTX 2060"),
        Equipo("Ordenador E", "Disponible", "16GB", "Intel i5", "AMD Radeon RX 580")
    )

    val equipo = equipos.firstOrNull { it.nombre == equipoNombre }

    if (equipo != null) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Detalles de ${equipo.nombre}") },
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
                Text(text = "Nombre: ${equipo.nombre}", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Estado: ${equipo.estado}", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "RAM: ${equipo.ram}", style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "CPU: ${equipo.cpu}", style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "GPU: ${equipo.gpu}", style = MaterialTheme.typography.bodyLarge)
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
                    text = "No se pudo encontrar el equipo con nombre: $equipoNombre",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}
