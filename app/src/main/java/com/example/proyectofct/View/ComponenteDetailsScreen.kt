package com.example.proyectofct.View

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.proyectofct.Model.componentes.Cpu
import com.example.proyectofct.Model.componentes.DispositivoIO
import com.example.proyectofct.Model.componentes.Gpu
import com.example.proyectofct.Model.componentes.Pci
import com.example.proyectofct.Model.componentes.PlacaBase
import com.example.proyectofct.Model.componentes.Ram
import com.example.proyectofct.Model.componentes.Rom

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComponenteDetailsScreen(navController: NavHostController, componente: Pair<Int, Any>?) {
    if (componente != null) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Detalles del componente") },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
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
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                    ) {
                        MostrarComponente(navController,componente)
                    }
                }
            }
        }
    } else {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Componente no encontrado") },
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
                    text = "No se pudo encontrar el componente",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Composable
fun MostrarComponente(navController: NavHostController, componente: Pair<Int, Any>){

    when(componente.first){
        0 -> {
            val placa = componente.second as PlacaBase
            Text(text = "Placa Base", style = MaterialTheme.typography.titleMedium)
            Text(text = "Id: ${placa.id}", style = MaterialTheme.typography.bodySmall)
            if (placa.ubicacionEquipoId != null) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "Ubicacion Equipo Id: ${placa.ubicacionEquipoId}", style = MaterialTheme.typography.bodySmall)
                    IconButton(onClick = { navController.navigate("detail_screen/${placa.ubicacionEquipoId}")}, modifier = Modifier.size(14.dp)) {
                        Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Imagen", modifier = Modifier.size(14.dp))
                    }
                }
            }
            Text(text = "Fecha Registro: ${placa.fechaRegistro?: "Indefinido"}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Estado: ${placa.estado?: "Indefinido"}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Nota: ${placa.nota?: "No hay nota"}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Nombre Serie: ${placa.nombreSerie?: "Indefinido"}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Numero Serie: ${placa.numeroSerie?: "Indefinido"}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Modelo: ${placa.modelo?: "Indefinido"}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Tipo Socket: ${placa.tipoSocket?: "Indefinido"}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Chipset: ${placa.chipset?: "Indefinido"}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Memoria Max Ram: ${placa.memoriaMaxRam?: "Indefinido"}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Ranuras Ram: ${placa.ranurasRam?: "Indefinido"}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Ranuras Pci: ${placa.ranurasPci?: "Indefinido"}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Ranuras Sata: ${placa.ranurasSata?: "Indefinido"}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Ranuras M2: ${placa.ranurasM2?: "Indefinido"}", style = MaterialTheme.typography.bodySmall)
        }
        1 -> {
            val cpu = componente.second as Cpu
            Text(text = "CPU", style = MaterialTheme.typography.titleMedium)
            Text(text = "Id: ${cpu.id}", style = MaterialTheme.typography.bodySmall)
            if (cpu.ubicacionEquipoId != null) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "Ubicacion Equipo Id: ${cpu.ubicacionEquipoId}", style = MaterialTheme.typography.bodySmall)
                    IconButton(onClick = { navController.navigate("detail_screen/${cpu.ubicacionEquipoId}")}, modifier = Modifier.size(14.dp)) {
                        Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Imagen", modifier = Modifier.size(14.dp))
                    }
                }
            }
            Text(text = "Fecha Registro: ${cpu.fechaRegistro?: "Indefinido"}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Estado: ${cpu.estado?: "Indefinido"}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Nota: ${cpu.nota?: "No hay nota"}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Nombre Serie: ${cpu.nombreSerie?: "Indefinido"}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Numero Serie Cpu: ${cpu.numeroSerieCpu?: "Indefinido"}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Modelo: ${cpu.modelo?: "Indefinido"}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Arquitectura: ${cpu.arquitectura?: "Indefinido"}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Frecuencia Ghz: ${cpu.frecuenciaGhz?: "Indefinido"}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Nucleos: ${cpu.nucleos?: "Indefinido"}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Hilos: ${cpu.hilos?: "Indefinido"}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Tdp Watts: ${cpu.tdpWatts?: "Indefinido"}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Socket: ${cpu.socket?: "Indefinido"}", style = MaterialTheme.typography.bodySmall)
        }
        2 -> {
            val gpu = componente.second as Gpu
            Text(text = "GPU", style = MaterialTheme.typography.titleMedium)
            Text(text = "Id: ${gpu.id}", style = MaterialTheme.typography.bodySmall)
            if (gpu.ubicacionEquipoId != null) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "Ubicacion Equipo Id: ${gpu.ubicacionEquipoId}", style = MaterialTheme.typography.bodySmall)
                    IconButton(onClick = { navController.navigate("detail_screen/${gpu.ubicacionEquipoId}") }, modifier = Modifier.size(14.dp)) {
                        Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Imagen", modifier = Modifier.size(14.dp))
                    }
                }
            }
            Text(text = "Fecha Registro: ${gpu.fechaRegistro?: "Indefinido"}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Estado: ${gpu.estado?: "Indefinido"}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Nota: ${gpu.nota?: "No hay nota"}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Nombre Serie: ${gpu.nombreSerie?: "Indefinido"}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Numero Serie: ${gpu.numeroSerie?: "Indefinido"}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Modelo: ${gpu.modelo?: "Indefinido"}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Memoria Vram: ${gpu.memoriaVram?: "Indefinido"}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Tipo Memoria: ${gpu.tipoMemoria?: "Indefinido"}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Consumo Watts: ${gpu.consumoWatts?: "Indefinido"}", style = MaterialTheme.typography.bodySmall)
        }
        3 -> {
            val ram = componente.second as Ram
            Text(text = "RAM", style = MaterialTheme.typography.titleMedium)
            Text(text = "Id: ${ram.id}", style = MaterialTheme.typography.bodySmall)
            if (ram.ubicacionEquipoId != null) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "Ubicacion Equipo Id: ${ram.ubicacionEquipoId}", style = MaterialTheme.typography.bodySmall)
                    IconButton(onClick = { navController.navigate("detail_screen/${ram.ubicacionEquipoId}") }, modifier = Modifier.size(14.dp)) {
                        Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Imagen", modifier = Modifier.size(14.dp))
                    }
                }
            }
            Text(text = "Fecha Registro: ${ram.fechaRegistro?: "Indefinido"}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Estado: ${ram.estado?: "Indefinido"}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Nota: ${ram.nota?: "No hay nota"}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Nombre Serie: ${ram.nombreSerie?: "Indefinido"}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Numero Serie: ${ram.numeroSerie?: "Indefinido"}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Capacidad: ${ram.capacidad?: "Indefinido"}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Frecuencia: ${ram.frecuencia?: "Indefinido"}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Latencia: ${ram.latencia?: "Indefinido"}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Tipo: ${ram.tipo?: "Indefinido"}", style = MaterialTheme.typography.bodySmall)
        }
        4 -> {
            val rom = componente.second as Rom
            Text(text = "ROM", style = MaterialTheme.typography.titleMedium)
            Text(text = "Id: ${rom.id}", style = MaterialTheme.typography.bodySmall)
            if (rom.ubicacionEquipoId != null) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "Ubicacion Equipo Id: ${rom.ubicacionEquipoId}", style = MaterialTheme.typography.bodySmall)
                    IconButton(onClick = { navController.navigate("detail_screen/${rom.ubicacionEquipoId}") }, modifier = Modifier.size(14.dp)) {
                        Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Imagen", modifier = Modifier.size(14.dp))
                    }
                }
            }
            Text(text = "Fecha Registro: ${rom.fechaRegistro?: "Indefinido"}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Estado: ${rom.estado?: "Indefinido"}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Nota: ${rom.nota?: "No hay nota"}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Nombre Serie: ${rom.nombreSerie?: "Indefinido"}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Numero Serie: ${rom.numeroSerie?: "Indefinido"}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Capacidad: ${rom.capacidad?: "Indefinido"}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Tipo: ${rom.tipo?: "Indefinido"}", style = MaterialTheme.typography.bodySmall)
        }
        5 -> {
            val pci = componente.second as Pci
            Text(text = "PCI", style = MaterialTheme.typography.titleMedium)
            Text(text = "Id: ${pci.id}", style = MaterialTheme.typography.bodySmall)
            if (pci.ubicacionEquipoId != null) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "Ubicacion Equipo Id: ${pci.ubicacionEquipoId}", style = MaterialTheme.typography.bodySmall)
                    IconButton(onClick = { navController.navigate("detail_screen/${pci.ubicacionEquipoId}") }, modifier = Modifier.size(14.dp)) {
                        Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Imagen", modifier = Modifier.size(14.dp))
                    }
                }
            }
            Text(text = "Fecha Registro: ${pci.fechaRegistro?: "Indefinido"}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Estado: ${pci.estado?: "Indefinido"}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Nota: ${pci.nota?: "No hay nota"}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Nombre Serie: ${pci.nombreSerie?: "Indefinido"}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Numero Serie: ${pci.numeroSerie?: "Indefinido"}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Tipo: ${pci.tipo?: "Indefinido"}", style = MaterialTheme.typography.bodySmall)
        }
        6 -> {
            val dispositivoIO = componente.second as DispositivoIO
            Text(text = "Dispositivo IO", style = MaterialTheme.typography.titleMedium)
            Text(text = "Id: ${dispositivoIO.id}", style = MaterialTheme.typography.bodySmall)
            if (dispositivoIO.ubicacionEquipoId != null) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "Ubicacion Equipo Id: ${dispositivoIO.ubicacionEquipoId}", style = MaterialTheme.typography.bodySmall)
                    IconButton(onClick = { navController.navigate("detail_screen/${dispositivoIO.ubicacionEquipoId}") }, modifier = Modifier.size(14.dp)) {
                        Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Imagen", modifier = Modifier.size(14.dp))
                    }
                }
            }
            Text(text = "Fecha Registro: ${dispositivoIO.fechaRegistro?: "Indefinido"}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Estado: ${dispositivoIO.estado?: "Indefinido"}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Nota: ${dispositivoIO.nota?: "No hay nota"}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Marca: ${dispositivoIO.marca?: "Indefinido"}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Modelo: ${dispositivoIO.modelo?: "Indefinido"}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Tipo: ${dispositivoIO.tipo?: "Indefinido"}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Descripcion: ${dispositivoIO.descripcion?: "Indefinido"}", style = MaterialTheme.typography.bodySmall)
        }
    }
}