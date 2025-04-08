package com.example.proyectofct.View

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.proyectofct.R
import com.example.proyectofct.Model.Equipo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController) {
    val equipos = listOf(
        Equipo("Ordenador A", "Disponible", "16GB", "Intel i7", "NVIDIA GTX 1060"),
        Equipo("Ordenador B", "En préstamo", "8GB", "AMD Ryzen 5", "NVIDIA GTX 1650"),
        Equipo("Ordenador C", "Disponible", "32GB", "Intel i9", "NVIDIA RTX 3080"),
        Equipo("Ordenador D", "En reparación", "16GB", "AMD Ryzen 7", "NVIDIA RTX 2060"),
        Equipo("Ordenador E", "Disponible", "16GB", "Intel i5", "AMD Radeon RX 580")
    )

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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            items(equipos) { equipo ->
                EquipoCard(equipo = equipo, onClick = {
                    navController.navigate("detail_screen/${equipo.nombre}")
                })
                Spacer(modifier = Modifier.height(8.dp))
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
                Text(text = equipo.nombre, style = MaterialTheme.typography.titleMedium)
                Text(text = equipo.estado, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}
