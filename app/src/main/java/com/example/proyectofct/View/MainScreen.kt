package com.example.proyectofct.View

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.proyectofct.Controler.llamadas.obtenerComponentes
import com.example.proyectofct.Controler.llamadas.obtenerEquipos
import com.example.proyectofct.MainActivity
import com.example.proyectofct.Model.Equipo
import com.example.proyectofct.Model.Role
import com.example.proyectofct.Model.componentes.Cpu
import com.example.proyectofct.Model.componentes.DispositivoIO
import com.example.proyectofct.Model.componentes.Gpu
import com.example.proyectofct.Model.componentes.Pci
import com.example.proyectofct.Model.componentes.PlacaBase
import com.example.proyectofct.Model.componentes.Ram
import com.example.proyectofct.Model.componentes.Rom
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController) {
    val role = MainActivity.currentUserRole
    var viewMode by remember { mutableStateOf(ViewMode.EQUIPOS) }

    val equipos = remember { mutableStateListOf<Equipo>().apply { addAll(MainActivity.equipos) } }
    val componentes = remember { mutableStateListOf<Pair<Int, Any>>().apply { addAll(MainActivity.componentes) } }
    val context = LocalContext.current

    var profesorFiltro by remember { mutableStateOf<String?>(null) }
    var aulaFiltro by remember { mutableStateOf<String?>(null) }
    var componentesView by remember { mutableStateOf("Todos") }
    val componentesOptions = listOf("Todos", "Placa Base", "CPU", "GPU", "RAM", "ROM", "PCI", "Dispositivo I/O")
    val statusOptions = if (viewMode == ViewMode.EQUIPOS) listOf("Todos", "Disponible", "Prestado","En uso", "Averiado", "Otro", "Desconocido")
                        else listOf("Todos","Operativo", "Averiado", "Sin instalar", "Desconocido")

    var expandedView by remember { mutableStateOf(false) }
    var expandedEstado by remember { mutableStateOf(false) }
    var expandedComponentes by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    fun recargarDatos() {
        isLoading = true
        if (viewMode == ViewMode.EQUIPOS) {
            obtenerEquipos(
                context = context,
                callback = { resultado ->
                    if (resultado != null) {
                        equipos.clear()
                        MainActivity.equipos.clear()
                        equipos.addAll(resultado)
                        MainActivity.equipos.addAll(resultado)
                    }
                    isLoading = false
                },
                navController = navController,
                intento = 0
            )
        } else {
            obtenerComponentes(
                context = context,
                callback = { resultado ->
                    if (resultado != null) {
                        componentes.clear()
                        MainActivity.componentes.clear()
                        componentes.addAll(resultado)
                        MainActivity.componentes.addAll(resultado)
                    }
                    isLoading = false
                },
                navController = navController,
                estado = "Operativo"
            )
        }
    }

    if(viewMode == ViewMode.EQUIPOS && equipos.isEmpty()){
        recargarDatos()
    }else if(viewMode == ViewMode.COMPONENTES && componentes.isEmpty()){
        recargarDatos()
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

    val filteredComponentes = componentes.filter { (tipo, componente) ->
        val estadoComponente = when (tipo) {
            0 -> (componente as PlacaBase).estado
            1 -> (componente as Cpu).estado
            2 -> (componente as Gpu).estado
            3 -> (componente as Ram).estado
            4 -> (componente as Rom).estado
            5 -> (componente as Pci).estado
            6 -> (componente as DispositivoIO).estado
            else -> null
        }

        val textoAComparar = componente.toString().lowercase()

        val coincideBusqueda = textoAComparar.contains(searchQuery.text.lowercase())
        val coincideEstado = selectedStatus == "Todos" || estadoComponente == selectedStatus

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
                if (role == Role.ADMIN) {
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

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                // Vista
                ExposedDropdownMenuBox(
                    expanded = expandedView,
                    onExpandedChange = { expandedView = !expandedView },
                    modifier = Modifier.weight(1f)
                ) {
                    TextField(
                        value = when (viewMode) {
                            ViewMode.EQUIPOS -> "Equipos"
                            ViewMode.COMPONENTES -> "Componentes"
                        },
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Vista") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedView)
                        },
                        modifier = Modifier
                            .menuAnchor()
                            .weight(1f)
                    )
                    ExposedDropdownMenu(
                        expanded = expandedView,
                        onDismissRequest = { expandedView = false }
                    ) {
                        ViewMode.values().forEach { mode ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        when (mode) {
                                            ViewMode.EQUIPOS -> "Equipos"
                                            ViewMode.COMPONENTES -> "Componentes"
                                        }
                                    )
                                },
                                onClick = {
                                    viewMode = mode
                                    expandedView = false
                                }
                            )
                        }
                    }
                }

                // Estado
                ExposedDropdownMenuBox(
                    expanded = expandedEstado,
                    onExpandedChange = { expandedEstado = !expandedEstado },
                    modifier = Modifier.weight(0.8f)
                ) {
                    TextField(
                        value = selectedStatus,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Estado") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedEstado)
                        },
                        modifier = Modifier
                            .menuAnchor()
                            .weight(1f)
                    )
                    ExposedDropdownMenu(
                        expanded = expandedEstado,
                        onDismissRequest = { expandedEstado = false }
                    ) {
                        statusOptions.forEach { status ->
                            DropdownMenuItem(
                                text = { Text(status) },
                                onClick = {
                                    selectedStatus = status
                                    expandedEstado = false
                                }
                            )
                        }
                    }
                }
                IconButton(
                    onClick = { recargarDatos() },
                    modifier = Modifier.align(Alignment.CenterVertically)
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = "Recargar")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Filtro componentes
            if(viewMode == ViewMode.COMPONENTES) {
                ExposedDropdownMenuBox(
                    expanded = expandedComponentes,
                    onExpandedChange = { expandedComponentes = !expandedComponentes },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextField(
                        value = componentesView,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Tipo") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedComponentes)
                        },
                        modifier = Modifier
                            .menuAnchor()
                            .weight(1f)
                    )
                    ExposedDropdownMenu(
                        expanded = expandedComponentes,
                        onDismissRequest = { expandedComponentes = false }
                    ) {
                        componentesOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    componentesView = option
                                    expandedComponentes = false
                                }
                            )
                        }
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


            Spacer(modifier = Modifier.height(24.dp))

            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                when (viewMode) {
                    ViewMode.EQUIPOS -> EquiposTable(
                        equipos = filteredEquipos,
                        onItemClick = { navController.navigate("detail_screen/${it.id}") },
                        onProfesorClick = { profesorFiltro = it },
                        onAulaClick = { aulaFiltro = it }
                    )
                    ViewMode.COMPONENTES -> ComponentesList(
                        navController = navController,
                        componentes = filteredComponentes,
                        filtro = componentesView
                    )
                }
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
    val horizontalScrollState = rememberScrollState()

    val sortedEquipos = remember(equipos, sortField, sortAscending) {
        equipos.sortedWith(compareBy<Equipo> {
            when (sortField) {
                "nombre" -> it.nombre.lowercase()
                "estado" -> it.estado.lowercase()
                "ubicacion" -> it.ubicacion.nombre?.lowercase()
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

    Column(
        modifier = Modifier
            .horizontalScroll(horizontalScrollState)
    ) {
        // Cabecera
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Nombre", modifier = Modifier.width(150.dp).clickable { toggleSort("nombre") })
            Text("Estado", modifier = Modifier.width(100.dp).clickable { toggleSort("estado") })
            Text("Tipo", modifier = Modifier.width(150.dp).clickable { toggleSort("tipo") })
            Text("Ubicación", modifier = Modifier.width(150.dp).clickable { toggleSort("ubicacion") })
            Text("Profesor", modifier = Modifier.width(150.dp).clickable { toggleSort("profesor") })
        }

        HorizontalDivider()

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier
                .fillMaxHeight()
        ) {
            items(sortedEquipos) { equipo ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onItemClick(equipo) }
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text(equipo.nombre, modifier = Modifier.width(150.dp))
                    Text(equipo.estado, modifier = Modifier.width(100.dp))
                    Text(equipo.tipo, modifier = Modifier.width(150.dp))
                    Text(
                        equipo.ubicacion.nombre ?: "Sin ubicación",
                        modifier = Modifier
                            .width(150.dp)
                            .clickable(enabled = equipo.ubicacion.nombre != null) {
                                equipo.ubicacion.nombre?.let { onAulaClick(it) }
                            },
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        equipo.usuario?.nombre ?: "Sin profesor",
                        modifier = Modifier
                            .width(150.dp)
                            .clickable(enabled = equipo.usuario?.nombre != null) {
                                equipo.usuario?.nombre?.let { onProfesorClick(it) }
                            },
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                HorizontalDivider()
            }
        }
        // Para hacer al usuario saber que es un scroll
        LaunchedEffect(Unit) {
            delay(500)
            horizontalScrollState.animateScrollTo(150)
            delay(100)
            horizontalScrollState.animateScrollTo(300)
            delay(400)
            horizontalScrollState.animateScrollTo(0)
        }
    }
}

@Composable
fun ComponentesList(
    navController: NavHostController,
    componentes: List<Pair<Int, Any>>,
    filtro: String? = null
) {
    val componentesFiltrados = when (filtro) {
        "Placa Base" -> componentes.filter { it.first == 0 }
        "CPU" -> componentes.filter { it.first == 1 }
        "GPU" -> componentes.filter { it.first == 2 }
        "RAM" -> componentes.filter { it.first == 3 }
        "ROM" -> componentes.filter { it.first == 4 }
        "PCI" -> componentes.filter { it.first == 5 }
        "Dispositivo I/O" -> componentes.filter { it.first == 6 }
        else -> componentes
    }

    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        itemsIndexed(componentesFiltrados) { index, (tipo, comp) ->
            val id = when (tipo) {
                0 -> (comp as? PlacaBase)?.id
                1 -> (comp as? Cpu)?.id
                2 -> (comp as? Gpu)?.id
                3 -> (comp as? Ram)?.id
                4 -> (comp as? Rom)?.id
                5 -> (comp as? Pci)?.id
                6 -> (comp as? DispositivoIO)?.id
                else -> null
            }

            if (id != null) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            navController.navigate("componente_detail_screen/$id")
                        },
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        when (tipo) {
                            0 -> {
                                val comp0 = comp as PlacaBase
                                Text("Placa Base: ${comp0.modelo}", style = MaterialTheme.typography.titleMedium)
                                Text("Socket: ${comp0.tipoSocket}, Chipset: ${comp0.chipset}")
                            }
                            1 -> {
                                val comp1 = comp as Cpu
                                Text("CPU: ${comp1.modelo ?: "Sin modelo"}", style = MaterialTheme.typography.titleMedium)
                                Text("Núcleos: ${comp1.nucleos ?: "?"}, Frecuencia: ${comp1.frecuenciaGhz ?: "?"} GHz")
                            }
                            2 -> {
                                val comp2 = comp as Gpu
                                Text("GPU: ${comp2.modelo ?: "Sin modelo"}", style = MaterialTheme.typography.titleMedium)
                                Text("Memoria: ${comp2.memoriaVram} GB")
                            }
                            3 -> {
                                val comp3 = comp as Ram
                                Text("RAM: ${comp3.capacidad} GB ${comp3.tipo}", style = MaterialTheme.typography.titleMedium)
                                Text("Frecuencia: ${comp3.frecuencia} MHz, Latencia: ${comp3.latencia}")
                            }
                            4 -> {
                                val comp4 = comp as Rom
                                Text("ROM: ${comp4.capacidad} GB (${comp4.tipo})", style = MaterialTheme.typography.titleMedium)
                            }
                            5 -> {
                                val comp5 = comp as Pci
                                Text("PCI: ${comp5.tipo} (${comp5.nombreSerie})", style = MaterialTheme.typography.titleMedium)
                            }
                            6 -> {
                                val comp6 = comp as DispositivoIO
                                Text("Dispositivo I/O: ${comp6.tipo} - ${comp6.marca} ${comp6.modelo}", style = MaterialTheme.typography.titleMedium)
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
}


enum class ViewMode {
    EQUIPOS,
    COMPONENTES
}
