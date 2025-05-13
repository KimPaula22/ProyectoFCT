package com.example.proyectofct.View

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.proyectofct.Controler.llamadas.obtenerComponentes
import com.example.proyectofct.MainActivity
import com.example.proyectofct.Model.Equipo
import com.example.proyectofct.Model.Role
import com.example.proyectofct.Model.componentes.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController) {
    val role = MainActivity.currentUserRole
    var viewMode by remember { mutableStateOf(ViewMode.EQUIPOS) }

    val equipos = MainActivity.equipos
    var componentes by remember { mutableStateOf<List<Any>>(emptyList()) }
    val context = LocalContext.current

    // Cargar componentes desde servidor
    LaunchedEffect(Unit) {
        obtenerComponentes(
            context = context,
            callback = { resultado ->
                if (resultado != null) {
                    componentes = resultado
                    Log.d("MainScreen", "Componentes cargados: ${componentes.size}")
                } else {
                    Log.e("MainScreen", "No se cargaron componentes")
                }
            },
            navController = navController,
            estado = "Operativo"
        )
    }

    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    var selectedStatus by remember { mutableStateOf("Todos") }

    val filteredEquipos = equipos.filter {
        (it.nombre.contains(searchQuery.text, ignoreCase = true) ||
                it.estado.contains(searchQuery.text, ignoreCase = true) ||
                (it.ubicacion.nombre ?: "").contains(searchQuery.text, ignoreCase = true)) &&
                (selectedStatus == "Todos" || it.estado == selectedStatus)
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Inventario") })
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
                    Icon(Icons.Default.QrCodeScanner, "Escanear QR")
                }
                if (role == Role.ADMINISTRADOR) {
                    FloatingActionButton(
                        onClick = { navController.navigate("add_screen") },
                        containerColor = MaterialTheme.colorScheme.primary
                    ) {
                        Icon(Icons.Default.Add, "Añadir equipo")
                    }
                    FloatingActionButton(
                        onClick = { navController.navigate("prestar_equipo_screen") },
                        containerColor = MaterialTheme.colorScheme.primary
                    ) {
                        Icon(Icons.Default.ArrowForward, "Prestar equipo")
                    }
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
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Buscar...") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(onClick = { selectedStatus = "Todos" }, modifier = Modifier.weight(1f)) {
                    Text("Todos")
                }
                Button(onClick = { selectedStatus = "Disponible" }, modifier = Modifier.weight(1f)) {
                    Text("Disponible")
                }
                Button(onClick = { selectedStatus = "En préstamo" }, modifier = Modifier.weight(1f)) {
                    Text("En préstamo")
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
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
                ) { Text("Equipos") }
                Button(
                    onClick = { viewMode = ViewMode.COMPONENTES },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (viewMode == ViewMode.COMPONENTES)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.secondaryContainer
                    )
                ) { Text("Componentes") }
            }
            Spacer(modifier = Modifier.height(24.dp))
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
                    Text(text = "Ubicación: ${equipo.ubicacion.nombre}", style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}

@Composable
fun ComponentesList(componentes: List<Any>) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items(componentes) { comp ->
            Card(modifier = Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(4.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    when (comp) {
                        is Cpu -> {
                            Text("CPU: ${comp.modelo ?: "Sin modelo"}", style = MaterialTheme.typography.titleMedium)
                            Text("Núcleos: ${comp.nucleos ?: "?"}, Frecuencia: ${comp.frecuenciaGhz ?: "?"} GHz")
                        }
                        is Ram -> {
                            Text("RAM: ${comp.capacidad} GB ${comp.tipo}", style = MaterialTheme.typography.titleMedium)
                            Text("Frecuencia: ${comp.frecuencia} MHz, Latencia: ${comp.latencia}")
                        }
                        is Rom -> {
                            Text("ROM: ${comp.capacidad} GB (${comp.tipo})", style = MaterialTheme.typography.titleMedium)
                        }
                        is PlacaBase -> {
                            Text("Placa Base: ${comp.modelo}", style = MaterialTheme.typography.titleMedium)
                            Text("Socket: ${comp.tipoSocket}, Chipset: ${comp.chipset}")
                        }
                        is DispositivoIO -> {
                            Text("Dispositivo I/O: ${comp.tipo} - ${comp.marca} ${comp.modelo}", style = MaterialTheme.typography.titleMedium)
                        }
                        is Pci -> {
                            Text("PCI: ${comp.tipo} (${comp.nombreSerie})", style = MaterialTheme.typography.titleMedium)
                        }
                        else -> {
                            Text("Componente desconocido", style = MaterialTheme.typography.titleMedium)
                        }
                    }
                }
            }
        }
    }
}

enum class ViewMode { EQUIPOS, COMPONENTES }
