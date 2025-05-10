package com.example.proyectofct.View

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.proyectofct.Model.Componente
import com.example.proyectofct.Model.Equipo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController) {
    var viewMode by remember { mutableStateOf(ViewMode.EQUIPOS) }

    val equipos = listOf(
        Equipo("Ordenador A", "Disponible", ubicacion = "1 DAM"),
        Equipo("Ordenador B", "En préstamo", ubicacion = "2 DAM"),
        Equipo("Ordenador C", "Disponible", ubicacion = "1 SMR"),
        Equipo("Ordenador D", "En reparación", ubicacion = "2 SMR"),
        Equipo("Ordenador E", "Disponible", ubicacion = "AULA 6")
    )

    val componentes = listOf(
        Componente("CPU Intel i7", ubicacion = "AULA VIDEOJUEGOS"),
        Componente("GPU NVIDIA GTX 1060", ubicacion = "DEPARTAMENTO DE INFORMATICA"),
        Componente("RAM 16GB", ubicacion = "1 DAM"),
        Componente("Disco SSD 512GB", ubicacion = "2 SMR"),
        Componente("PCIe WiFi", ubicacion = "1 SMR")
    )

    // Estados de la búsqueda y filtros
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    var selectedStatus by remember { mutableStateOf("Todos") }
    var selectedLocation by remember { mutableStateOf("Todos") }

    // Filtrar equipos según búsqueda y filtros
    val filteredEquipos = equipos.filter {
        (it.nombre.contains(searchQuery.text, ignoreCase = true) ||
                it.estado.contains(searchQuery.text, ignoreCase = true) ||
                it.ubicacion.contains(searchQuery.text, ignoreCase = true)) &&
                (selectedStatus == "Todos" || it.estado == selectedStatus) &&
                (selectedLocation == "Todos" || it.ubicacion == selectedLocation)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Inventario") },
                actions = {
                    IconButton(onClick = { navController.navigate("profile_screen") }) {
                        Icon(Icons.Filled.AccountCircle, contentDescription = "Perfil")
                    }
                }
            )
        },
        floatingActionButton = {
            Row(
                modifier = Modifier.padding(end = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                FloatingActionButton(
                    onClick = { navController.navigate("qr_scan") },
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(Icons.Default.QrCodeScanner, contentDescription = "Escanear QR")
                }

                FloatingActionButton(
                    onClick = { navController.navigate("add_screen") },
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Añadir equipo")
                }

                FloatingActionButton(
                    onClick = { navController.navigate("prestar_equipo_screen") },
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(Icons.Default.ArrowForward, contentDescription = "Prestar equipo")
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Barra de búsqueda
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Buscar equipos...") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Filtros de Estado y Ubicación
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = { selectedStatus = "Todos" },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Todos")
                }
                Button(
                    onClick = { selectedStatus = "Disponible" },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Disponible")
                }
                Button(
                    onClick = { selectedStatus = "En préstamo" },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("En préstamo")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Segunda fila modificada (ubicaciones reemplazadas por Equipos y Componentes)
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = { viewMode = ViewMode.EQUIPOS },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (viewMode == ViewMode.EQUIPOS)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Text("Equipos")
                }
                Button(
                    onClick = { viewMode = ViewMode.COMPONENTES },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (viewMode == ViewMode.COMPONENTES)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Text("Componentes")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Vista de Equipos o Componentes
            when (viewMode) {
                ViewMode.EQUIPOS -> EquiposList(equipos = filteredEquipos) {
                    navController.navigate("detail_screen/${it.nombre}")
                }
                ViewMode.COMPONENTES -> ComponentesList(componentes = componentes)
            }
        }
    }
}

@Composable
fun EquiposList(equipos: List<Equipo>, onItemClick: (Equipo) -> Unit) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items(equipos) { equipo ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onItemClick(equipo) },
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = equipo.nombre, style = MaterialTheme.typography.titleMedium)
                    Text(text = "Estado: ${equipo.estado}", style = MaterialTheme.typography.bodyMedium)
                    Text(text = "Ubicación: ${equipo.ubicacion}", style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}

@Composable
fun ComponentesList(componentes: List<Componente>) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items(componentes) { comp ->
            Card(
                elevation = CardDefaults.cardElevation(4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = comp.nombre, style = MaterialTheme.typography.titleMedium)
                    Text(text = "Ubicación: ${comp.ubicacion}", style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}

enum class ViewMode { EQUIPOS, COMPONENTES }
