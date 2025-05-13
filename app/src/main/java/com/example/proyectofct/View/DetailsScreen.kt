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
import com.example.proyectofct.Model.Ubicacion
import com.example.proyectofct.Model.componentes.Cpu
import com.example.proyectofct.Model.componentes.DispositivoIO
import com.example.proyectofct.Model.componentes.Gpu
import com.example.proyectofct.Model.componentes.Pci
import com.example.proyectofct.Model.componentes.PlacaBase
import com.example.proyectofct.Model.componentes.Ram
import com.example.proyectofct.Model.componentes.Rom

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(navController: NavHostController, equipoNombre: String) {

    val equipo = MainActivity.equipos.firstOrNull { it.nombre == equipoNombre }

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
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "Nombre: ${equipo.nombre}", style = MaterialTheme.typography.titleLarge)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Estado: ${equipo.estado}", style = MaterialTheme.typography.bodyMedium)
                        Text(text = "Ubicación: ${equipo.ubicacion}", style = MaterialTheme.typography.bodyMedium)
                        Text(text = "RAM: ${equipo.rams}", style = MaterialTheme.typography.bodyLarge)
                        Text(text = "CPU: ${equipo.cpu}", style = MaterialTheme.typography.bodyLarge)
                        Text(text = "GPU: ${equipo.gpu}", style = MaterialTheme.typography.bodyLarge)
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = {
                        // Aquí puedes implementar la lógica para prestar el equipo
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
                    text = "No se pudo encontrar el equipo con nombre: $equipoNombre",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}
