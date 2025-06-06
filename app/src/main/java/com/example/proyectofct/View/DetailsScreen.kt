package com.example.proyectofct

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DeveloperBoard
import androidx.compose.material.icons.filled.ImageAspectRatio
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material.icons.filled.SelectAll
import androidx.compose.material.icons.filled.Sip
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.ViewDay
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
import com.example.proyectofct.Model.Ubicacion
import com.example.proyectofct.Model.componentes.Cpu
import com.example.proyectofct.Model.componentes.DispositivoIO
import com.example.proyectofct.Model.componentes.Gpu
import com.example.proyectofct.Model.componentes.Pci
import com.example.proyectofct.Model.componentes.PlacaBase
import com.example.proyectofct.Model.componentes.Ram
import com.example.proyectofct.Model.componentes.Rom
import com.example.proyectofct.View.QRConDescarga

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(navController: NavHostController, equipo: Equipo?) {
    var mostrarQR by remember { mutableStateOf(false) }


    /*val equipos = listOf(
        // Primer equipo (Ordenador A)
        Equipo(
            id = 1,
            nombre = "Ordenador A",
            estado = "Disponible",
            descripcion = "Ordenador de alto rendimiento",
            fechaRegistro = "2025-01-01",
            ubicacion = Ubicacion(id = 1, nombre = "1 DAM", ubicacion = "Primera planta, pasillo 2"),
            placaBase = PlacaBase(
                id = 1,
                ubicacionId = 1,
                ubicacionEquipoId = 101,
                fechaRegistro = "2025-01-01",
                estado = "Operativo",
                nota = "Nuevo",
                nombreSerie = "MSI B450",
                numeroSerie = "ABC123",
                modelo = "MSI B450",
                tipoSocket = "AM4",
                chipset = "B450",
                memoriaRamMax = 64,
                ranurasRam = 4,
                ranurasPci = 3,
                ranurasSata = 6,
                ranurasM2 = 2
            ),
            cpu = Cpu(
                id = 1,
                ubicacion = 1,
                ubicacionEquipoId = 1,
                fechaRegistro = "2025-01-01",
                estado = "Operativo",
                nota = "Nuevo",
                nombreSerie = "Intel i7-9700K",
                numeroSerieCpu = "12345",
                modelo = "Intel i7-9700K",
                arquitectura = "x86",
                frecuenciaGhz = 3.6,
                nucleos = 8,
                hilos = 8,
                tdpWatts = 95,
                socket = "LGA1151"
            ),
            gpu = Gpu(
                id = 1,
                ubicacion = 1,
                ubicacionEquipoId = 1,
                fechaRegistro = "2025-01-01",
                estado = "Operativo",
                nota = "Nuevo",
                nombreSerie = "NVIDIA GTX 1060",
                numeroSerie = "67890",
                modelo = "NVIDIA GTX 1060",
                memoriaVram = 6,
                tipoMemoria = "GDDR5",
                consumoWatts = 120
            ),
            rams = arrayListOf(
                Ram(
                    id = 1,
                    ubicacion = 1,
                    ubicacionEquipoId = 1,
                    fechaRegistro = "2025-01-01",
                    estado = "Operativo",
                    nota = "Nuevo",
                    nombreSerie = "Corsair Vengeance",
                    numeroSerie = "RAM123",
                    capacidad = 8,
                    frecuencia = 2666,
                    latencia = "CL16",
                    tipo = "DDR4"
                ),
                Ram(
                    id = 2,
                    ubicacion = 1,
                    ubicacionEquipoId = 1,
                    fechaRegistro = "2025-01-01",
                    estado = "Operativo",
                    nota = "Nuevo",
                    nombreSerie = "Corsair Vengeance",
                    numeroSerie = "RAM124",
                    capacidad = 8,
                    frecuencia = 2666,
                    latencia = "CL16",
                    tipo = "DDR4"
                )
            ),
            roms = arrayListOf(
                Rom(
                    id = 1,
                    ubicacion = 1,
                    ubicacionEquipoId = 1,
                    fechaRegistro = "2025-01-01",
                    estado = "Operativo",
                    nota = "Nuevo",
                    nombreSerie = "Samsung SSD",
                    numeroSerie = "ROM123",
                    capacidad = 512,
                    tipo = "SSD"
                )
            ),
            pcis = arrayListOf(
                Pci(
                    id = 1,
                    ubicacion = 1,
                    ubicacionEquipoId = 1,
                    fechaRegistro = "2025-01-01",
                    estado = "Operativo",
                    nota = "Nuevo",
                    nombreSerie = "Wi-Fi Card",
                    numeroSerie = "PCI123",
                    tipo = "Red"
                )
            ),
            dispositivosIO = arrayListOf(
                DispositivoIO(
                    id = 1,
                    ubicacion = 1,
                    ubicacionEquipoId = 1,
                    fechaRegistro = "2025-01-01",
                    estado = "Operativo",
                    nota = "Nuevo",
                    marca = "Logitech",
                    modelo = "Teclado G102",
                    tipo = "Entrada",
                    descripcion = "Teclado gaming"
                ),
                DispositivoIO(
                    id = 2,
                    ubicacion = 1,
                    ubicacionEquipoId = 1,
                    fechaRegistro = "2025-01-01",
                    estado = "Operativo",
                    nota = "Nuevo",
                    marca = "Logitech",
                    modelo = "G Pro",
                    tipo = "Entrada",
                    descripcion = "Ratón óptico"
                )
            )
        ),
        // Segundo equipo (Ordenador B)
        Equipo(
            id = 2,
            nombre = "Ordenador B",
            estado = "En préstamo",
            descripcion = "Ordenador de oficina",
            fechaRegistro = "2025-02-01",
            ubicacion = Ubicacion(id = 2, nombre = "2 DAM", ubicacion = "Segunda planta, sala de reuniones"),
            placaBase = PlacaBase(
                id = 2,
                ubicacionId = 2,
                ubicacionEquipoId = 102,
                fechaRegistro = "2025-02-01",
                estado = "Operativo",
                nota = "Nuevo",
                nombreSerie = "ASUS Prime B460",
                numeroSerie = "XYZ987",
                modelo = "ASUS Prime B460",
                tipoSocket = "LGA1200",
                chipset = "B460",
                memoriaRamMax = 64,
                ranurasRam = 4,
                ranurasPci = 3,
                ranurasSata = 6,
                ranurasM2 = 2
            ),
            cpu = Cpu(
                id = 2,
                ubicacion = 2,
                ubicacionEquipoId = 2,
                fechaRegistro = "2025-02-01",
                estado = "Operativo",
                nota = "Nuevo",
                nombreSerie = "AMD Ryzen 5 3600",
                numeroSerieCpu = "67890",
                modelo = "AMD Ryzen 5 3600",
                arquitectura = "x86",
                frecuenciaGhz = 3.6,
                nucleos = 6,
                hilos = 12,
                tdpWatts = 95,
                socket = "AM4"
            ),
            gpu = Gpu(
                id = 2,
                ubicacion = 2,
                ubicacionEquipoId = 2,
                fechaRegistro = "2025-02-01",
                estado = "Operativo",
                nota = "Nuevo",
                nombreSerie = "NVIDIA GTX 1650",
                numeroSerie = "54321",
                modelo = "NVIDIA GTX 1650",
                memoriaVram = 4,
                tipoMemoria = "GDDR5",
                consumoWatts = 75
            ),
            rams = arrayListOf(
                Ram(
                    id = 3,
                    ubicacion = 2,
                    ubicacionEquipoId = 2,
                    fechaRegistro = "2025-02-01",
                    estado = "Operativo",
                    nota = "Nuevo",
                    nombreSerie = "Corsair Vengeance",
                    numeroSerie = "RAM567",
                    capacidad = 16,
                    frecuencia = 3200,
                    latencia = "CL16",
                    tipo = "DDR4"
                )
            ),
            roms = arrayListOf(
                Rom(
                    id = 2,
                    ubicacion = 2,
                    ubicacionEquipoId = 2,
                    fechaRegistro = "2025-02-01",
                    estado = "Operativo",
                    nota = "Nuevo",
                    nombreSerie = "Seagate HDD",
                    numeroSerie = "ROM567",
                    capacidad = 1000,
                    tipo = "HDD"
                )
            ),
            pcis = arrayListOf(
                Pci(
                    id = 2,
                    ubicacion = 2,
                    ubicacionEquipoId = 2,
                    fechaRegistro = "2025-02-01",
                    estado = "Operativo",
                    nota = "Nuevo",
                    nombreSerie = "Tarjeta de sonido",
                    numeroSerie = "PCI456",
                    tipo = "Audio"
                )
            ),
            dispositivosIO = arrayListOf(
                DispositivoIO(
                    id = 3,
                    ubicacion = 2,
                    ubicacionEquipoId = 2,
                    fechaRegistro = "2025-02-01",
                    estado = "Operativo",
                    nota = "Nuevo",
                    marca = "Logitech",
                    modelo = "C920",
                    tipo = "Entrada",
                    descripcion = "Cámara web"
                ),
                DispositivoIO(
                    id = 4,
                    ubicacion = 2,
                    ubicacionEquipoId = 2,
                    fechaRegistro = "2025-02-01",
                    estado = "Operativo",
                    nota = "Nuevo",
                    marca = "Microsoft",
                    modelo = "Mouse",
                    tipo = "Entrada",
                    descripcion = "Ratón inalámbrico"
                )
            )
        )
    )
    val equipo = equipos.firstOrNull { it.nombre == equipoNombre }*/

    if (equipo != null) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Detalles del equipo") },
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
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                    ){
                        Row {
                            Icon(Icons.Default.QuestionMark, contentDescription = "General")
                            Text(text = "Información general", style = MaterialTheme.typography.titleMedium)
                        }
                        Text("ID: ${equipo.id}", style = MaterialTheme.typography.bodySmall)
                        Text("Nombre: ${equipo.nombre}", style = MaterialTheme.typography.bodySmall)
                        Text("Ubicación: ${equipo.ubicacion.nombre}", style = MaterialTheme.typography.bodySmall)
                        Text("Estado: ${equipo.estado}", style = MaterialTheme.typography.bodySmall)
                        if (equipo.usuario != null) {
                            Text("Prestamista: ${equipo.usuario}", style = MaterialTheme.typography.bodySmall)
                        }
                        Text("Descripción: ${equipo.descripcion?: "No hay descripción"}", style = MaterialTheme.typography.bodySmall)
                        Text("Fecha registro: ${equipo.fechaRegistro}", style = MaterialTheme.typography.bodySmall)
                    }
                }
                Card {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                    ) {
                        Row {
                            Icon(Icons.Default.DeveloperBoard, contentDescription = "Placa Base")
                            Text(text = "Placa Base", style = MaterialTheme.typography.titleMedium)
                        }
                        Text("Id: ${equipo.placaBase.id}", style = MaterialTheme.typography.bodySmall)
                        Text("Fecha registro: ${equipo.placaBase.fechaRegistro?: "Indefinido"}", style = MaterialTheme.typography.bodySmall)
                        Text("Nombre: ${equipo.placaBase.nombreSerie?: "Indefinido"}", style = MaterialTheme.typography.bodySmall)
                        Text("Modelo: ${equipo.placaBase.modelo?: "Indefinido"}", style = MaterialTheme.typography.bodySmall)
                        Text("Número serie: ${equipo.placaBase.numeroSerie?: "Indefinido"}", style = MaterialTheme.typography.bodySmall)
                        Text("Socket: ${equipo.placaBase.tipoSocket?: "Indefinido"}", style = MaterialTheme.typography.bodySmall)
                        Text("Chipset: ${equipo.placaBase.chipset?: "Indefinido"}", style = MaterialTheme.typography.bodySmall)
                        Text("Max memoria RAM: ${equipo.placaBase.memoriaMaxRam?: "Indefinida"} GB", style = MaterialTheme.typography.bodySmall)
                        Text("Ranuras RAM: ${equipo.placaBase.ranurasRam?: "Indefinida"}", style = MaterialTheme.typography.bodySmall)
                        Text("Ranuras PCI: ${equipo.placaBase.ranurasPci?: "Indefinida"}", style = MaterialTheme.typography.bodySmall)
                        Text("Ranuras SATA: ${equipo.placaBase.ranurasSata?: "Indefinida"}", style = MaterialTheme.typography.bodySmall)
                        Text("Ranuras M.2: ${equipo.placaBase.ranurasM2?: "Indefinida"}", style = MaterialTheme.typography.bodySmall)
                        Text("Estado: ${equipo.placaBase.estado?: "Indefinido"}", style = MaterialTheme.typography.bodySmall)
                        Text("Nota: ${equipo.placaBase.nota?: "No hay nota"}", style = MaterialTheme.typography.bodySmall)
                    }
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {

                        Row {
                            Icon(Icons.Default.Memory, contentDescription = "Componentes")
                            Text(text = "CPU", style = MaterialTheme.typography.titleMedium)
                        }
                        Text("Id: ${equipo.cpu?.id}", style = MaterialTheme.typography.bodySmall)
                        Text("Fecha registro: ${equipo.cpu?.fechaRegistro?: "Indefinido"}", style = MaterialTheme.typography.bodySmall)
                        Text("Nombre: ${equipo.cpu?.nombreSerie?: "Indefinido"}", style = MaterialTheme.typography.bodySmall)
                        Text("Modelo: ${equipo.cpu?.modelo?: "Indefinido"}", style = MaterialTheme.typography.bodySmall)
                        Text("Número serie: ${equipo.cpu?.numeroSerieCpu?: "Indefinido"}", style = MaterialTheme.typography.bodySmall)
                        Text("Arquitectura: ${equipo.cpu?.arquitectura?: "Indefinida"}", style = MaterialTheme.typography.bodySmall)
                        Text("Socket: ${equipo.cpu?.socket?: "Indefinido"}", style = MaterialTheme.typography.bodySmall)
                        Text("Frecuencia: ${equipo.cpu?.frecuenciaGhz?: "Indefinida"} GHz", style = MaterialTheme.typography.bodySmall)
                        Text("Nucleos: ${equipo.cpu?.nucleos?: "Indefinido"}", style = MaterialTheme.typography.bodySmall)
                        Text("Hilos: ${equipo.cpu?.hilos?: "Indefinido"}", style = MaterialTheme.typography.bodySmall)
                        Text("TDP: ${equipo.cpu?.tdpWatts?: "Indefinido"} W", style = MaterialTheme.typography.bodySmall)
                        Text("Estado: ${equipo.cpu?.estado?: "Indefinido"}", style = MaterialTheme.typography.bodySmall)
                        Text("Nota: ${equipo.cpu?.nota?: "No hay nota"}", style = MaterialTheme.typography.bodySmall)
                    }
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                    ) {
                        Row {
                            Icon(Icons.Default.SelectAll, contentDescription = "GPU")
                            Text(text = "GPU", style = MaterialTheme.typography.titleMedium)
                        }
                        Text("Id: ${equipo.gpu?.id}", style = MaterialTheme.typography.bodySmall)
                        Text("Fecha registro: ${equipo.gpu?.fechaRegistro?: "Indefinido"}", style = MaterialTheme.typography.bodySmall)
                        Text("Nombre: ${equipo.gpu?.nombreSerie?: "Indefinido"}", style = MaterialTheme.typography.bodySmall)
                        Text("Modelo: ${equipo.gpu?.modelo?: "Indefinido"}", style = MaterialTheme.typography.bodySmall)
                        Text("Número serie: ${equipo.gpu?.numeroSerie?: "Indefinido"}", style = MaterialTheme.typography.bodySmall)
                        Text("Memoria VRAM: ${equipo.gpu?.memoriaVram?: "Indefinida"} GB", style = MaterialTheme.typography.bodySmall)
                        Text("Tipo memoria: ${equipo.gpu?.tipoMemoria?: "Indefinido"}", style = MaterialTheme.typography.bodySmall)
                        Text("Consumo energía: ${equipo.gpu?.consumoWatts?: "Indefinida"} W", style = MaterialTheme.typography.bodySmall)
                        Text("Estado: ${equipo.gpu?.estado?: "Indefinido"}", style = MaterialTheme.typography.bodySmall)
                        Text("Nota: ${equipo.gpu?.nota?: "No hay nota"}", style = MaterialTheme.typography.bodySmall)
                    }
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ){
                        Row{
                            Icon(Icons.Default.ImageAspectRatio, contentDescription = "Componentes")
                            Text(text = "RAMs", style = MaterialTheme.typography.titleMedium)
                        }
                        for (ram in equipo.rams ?: emptyList()) {
                            Text("Id: ${ram.id}", style = MaterialTheme.typography.bodySmall)
                            Text("Fecha registro: ${ram.fechaRegistro?: "Indefinido"}", style = MaterialTheme.typography.bodySmall)
                            Text("Nombre: ${ram.nombreSerie?: "Indefinido"}", style = MaterialTheme.typography.bodySmall)
                            Text("Número serie: ${ram.numeroSerie?: "Indefinido"}", style = MaterialTheme.typography.bodySmall)
                            Text("Capacidad: ${ram.capacidad?: "Indefinida"} GB", style = MaterialTheme.typography.bodySmall)
                            Text("Frecuencia: ${ram.frecuencia?: "Indefinida"} MHz", style = MaterialTheme.typography.bodySmall)
                            Text("Latencia: ${ram.latencia?: "Indefinida"}", style = MaterialTheme.typography.bodySmall)
                            Text("Tipo: ${ram.tipo?: "Indefinido"}", style = MaterialTheme.typography.bodySmall)
                            Text("Estado: ${ram.estado?: "Indefinido"}", style = MaterialTheme.typography.bodySmall)
                            Text("Nota: ${ram.nota?: "No hay nota"}", style = MaterialTheme.typography.bodySmall)
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ){
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ){
                        Row{
                            Icon(Icons.Default.Storage, contentDescription = "Discos duros")
                            Text(text = "Discos duros", style = MaterialTheme.typography.titleMedium)
                        }
                        for (rom in equipo.roms ?: emptyList()) {
                            Text("Id: ${rom.id}", style = MaterialTheme.typography.bodySmall)
                            Text("Fecha de registro: ${rom.fechaRegistro?: "Indefinido"}", style = MaterialTheme.typography.bodySmall)
                            Text("Nombre: ${rom.nombreSerie?: "Indefinido"}", style = MaterialTheme.typography.bodySmall)
                            Text("Número de serie: ${rom.numeroSerie?: "Indefinido"}", style = MaterialTheme.typography.bodySmall)
                            Text("Capacidad: ${rom.capacidad?: "Indefinida"} GB", style = MaterialTheme.typography.bodySmall)
                            Text("Tipo: ${rom.tipo?: "Indefinido"}", style = MaterialTheme.typography.bodySmall)
                            Text("Estado: ${rom.estado?: "Indefinido"}", style = MaterialTheme.typography.bodySmall)
                            Text("Nota: ${rom.nota?: "No hay nota"}", style = MaterialTheme.typography.bodySmall)
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                    ) {
                        Row {
                            Icon(Icons.Default.ViewDay, contentDescription = "PCIs")
                            Text(text = "PCIs", style = MaterialTheme.typography.titleMedium)
                        }
                        for (pci in equipo.pcis ?: emptyList()) {
                            Text("Id: ${pci.id}", style = MaterialTheme.typography.bodySmall)
                            Text("Fecha de registro: ${pci.fechaRegistro?: "Indefinido"}", style = MaterialTheme.typography.bodySmall)
                            Text("Nombre: ${pci.nombreSerie?: "Indefinido"}", style = MaterialTheme.typography.bodySmall)
                            Text("Número de serie: ${pci.numeroSerie?: "Indefinido"}", style = MaterialTheme.typography.bodySmall)
                            Text("Tipo: ${pci.tipo?: "Indefinido"}", style = MaterialTheme.typography.bodySmall)
                            Text("Estado: ${pci.estado?: "Indefinido"}", style = MaterialTheme.typography.bodySmall)
                            Text("Nota: ${pci.nota?: "No hay nota"}", style = MaterialTheme.typography.bodySmall)
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }

                }
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                    ) {
                        Row {
                            Icon(Icons.Default.Keyboard, contentDescription = "Dispositivos IO")
                            Text(text = "Dispositivos I/O", style = MaterialTheme.typography.titleMedium)
                        }
                        for (dispositivoIO in equipo.dispositivosIO ?: emptyList()) {
                            Text("Id: ${dispositivoIO.id}", style = MaterialTheme.typography.bodySmall)
                            Text("Fecha de registro: ${dispositivoIO.fechaRegistro?: "Indefinido"}", style = MaterialTheme.typography.bodySmall)
                            Text("Marca: ${dispositivoIO.marca?: "Indefinido"}", style = MaterialTheme.typography.bodySmall)
                            Text("Modelo: ${dispositivoIO.modelo?: "Indefinido"}", style = MaterialTheme.typography.bodySmall)
                            Text("Tipo: ${dispositivoIO.tipo?: "Indefinido"}", style = MaterialTheme.typography.bodySmall)
                            Text("Estado: ${dispositivoIO.estado?: "Indefinido"}", style = MaterialTheme.typography.bodySmall)
                            Text("Nota: ${dispositivoIO.nota?: "No hay nota"}", style = MaterialTheme.typography.bodySmall)
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }


                Spacer(modifier = Modifier.weight(1f))
                // Boton mostrar qr/ocultar qr
                Button(
                    onClick = {if (mostrarQR) {mostrarQR = false} else {mostrarQR = true}},
                    modifier = Modifier.fillMaxWidth()
                ){
                    Text(if (mostrarQR) "Ocultar QR" else "Mostrar QR")
                }
                if (mostrarQR) {
                    QRConDescarga(equipo.id.toString())
                }
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        // TODO:Aquí puedes implementar la lógica para prestar el equipo
                    },
                    enabled = equipo.estado == "Disponible",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                ) {
                    Text("Prestar Ordenador")
                }
            }
        }
    } else {
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
                    text = "No se pudo encontrar el equipo",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

