package com.example.proyectofct.View

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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

    var profesorFiltro by remember { mutableStateOf<String?>(null) }
    var aulaFiltro by remember { mutableStateOf<String?>(null) }

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
        val coincideBusqueda = it.nombre.contains(searchQuery.text, ignoreCase = true) ||
                it.estado.contains(searchQuery.text, ignoreCase = true) ||
                (it.ubicacion.nombre ?: "").contains(searchQuery.text, ignoreCase = true)

        val coincideEstado = selectedStatus == "Todos" || it.estado == selectedStatus
        val coincideProfesor = profesorFiltro == null || it.usuario?.nombre == profesorFiltro
        val coincideAula = aulaFiltro == null || it.ubicacion.nombre == aulaFiltro

        coincideBusqueda && coincideEstado && coincideProfesor && coincideAula
    }

    val filteredComponentes = componentes.filter {
        val estado = when (it) {
            is Cpu -> it.estado
            is Ram -> it.estado
            is Rom -> it.estado
            is PlacaBase -> it.estado
            is DispositivoIO -> it.estado
            is Pci -> it.estado
            else -> "Desconocido"
        }

        val texto = it.toString().lowercase()
        val coincideBusqueda = texto.contains(searchQuery.text.lowercase())
        val coincideEstado = selectedStatus == "Todos" || estado == selectedStatus

        coincideBusqueda && coincideEstado
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Inventario") },
                actions = {
                    IconButton(onClick = { navController.navigate("profile_screen") }) {
                        Icon(Icons.Default.Person, contentDescription = "Perfil")
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
                if (role == Role.ADMINISTRADOR) {
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
                listOf("Todos", "Disponible", "En préstamo").forEach { status ->
                    Button(
                        onClick = { selectedStatus = status },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(status)
                    }
                }
            }

            if (profesorFiltro != null || aulaFiltro != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        profesorFiltro = null
                        aulaFiltro = null
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary
                    )
                ) {
                    Text("Limpiar filtro de profesor/aula")
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

            when (viewMode) {
                ViewMode.EQUIPOS -> EquiposTable(
                    equipos = filteredEquipos,
                    onItemClick = { navController.navigate("detail_screen/${it.nombre}") },
                    onProfesorClick = { profesorFiltro = it },
                    onAulaClick = { aulaFiltro = it }
                )
                ViewMode.COMPONENTES -> ComponentesList(componentes = filteredComponentes)
            }
        }
    }
}

@Composable
fun EquiposTable(
    equipos: List<Equipo>,
    onItemClick: (Equipo) -> Unit,
    onProfesorClick: (String) -> Unit,
    onAulaClick: (String) -> Unit
) {
    var sortField by remember { mutableStateOf("nombre") }
    var sortAscending by remember { mutableStateOf(true) }

    val sortedEquipos = remember(equipos, sortField, sortAscending) {
        equipos.sortedWith(compareBy<Equipo> {
            when (sortField) {
                "nombre" -> it.nombre.lowercase()
                "estado" -> it.estado.lowercase()
                "ubicacion" -> it.ubicacion.nombre?.lowercase() ?: ""
                "profesor" -> it.usuario?.nombre?.lowercase() ?: ""
                else -> it.nombre.lowercase()
            }
        }.let { if (sortAscending) it else it.reversed() })
    }

    fun toggleSort(field: String) {
        if (sortField == field) {
            sortAscending = !sortAscending
        } else {
            sortField = field
            sortAscending = true
        }
    }

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Nombre", modifier = Modifier.weight(2f).clickable { toggleSort("nombre") })
            Text("Estado", modifier = Modifier.weight(1f).clickable { toggleSort("estado") })
            Text("Ubicación", modifier = Modifier.weight(2f).clickable { toggleSort("ubicacion") })
            Text("Profesor", modifier = Modifier.weight(2f).clickable { toggleSort("profesor") })
        }

        Divider()

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.fillMaxHeight()
        ) {
            items(sortedEquipos) { equipo ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onItemClick(equipo) }
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(equipo.nombre, modifier = Modifier.weight(2f))
                    Text(equipo.estado, modifier = Modifier.weight(1f))
                    Text(
                        equipo.ubicacion.nombre ?: "Sin ubicación",
                        modifier = Modifier
                            .weight(2f)
                            .clickable(enabled = equipo.ubicacion.nombre != null) {
                                equipo.ubicacion.nombre?.let { onAulaClick(it) }
                            },
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        equipo.usuario?.nombre ?: "Sin profesor",
                        modifier = Modifier
                            .weight(2f)
                            .clickable(enabled = equipo.usuario?.nombre != null) {
                                equipo.usuario?.nombre?.let { onProfesorClick(it) }
                            },
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Divider()
            }
        }
    }
}

@Composable
fun ComponentesList(componentes: List<Any>) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items(componentes) { comp ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
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

enum class ViewMode {
    EQUIPOS,
    COMPONENTES
}
