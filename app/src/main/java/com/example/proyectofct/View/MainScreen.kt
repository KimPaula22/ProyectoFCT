package com.example.proyectofct

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.proyectofct.ui.theme.ProyectoFCTTheme

class MainScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProyectoFCTTheme {
                val navController = rememberNavController()
                // Definición de la navegación entre pantallas
                NavHost(navController = navController, startDestination = "login") {
                    composable("login") { LoginScreen(navController) }
                    composable("main_screen") { MainScreen(navController) }
                    composable("detail_screen") { DetailScreen(navController) }
                    composable("qr_scan") { QRScanScreen(navController) }
                    composable("admin") { AdminScreen(navController) }
                }
            }
        }
    }
}

//
// PANTALLA DE LOGIN: Para iniciar sesión
//
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavHostController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Iniciar Sesión") }
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
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    // Lógica para validación de login y redirección al listado de equipos
                    navController.navigate("main_screen")
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Iniciar Sesión")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { navController.navigate("qr_scan") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Iniciar Sesión con QR")
            }
        }
    }
}

//
// PANTALLA PRINCIPAL: Listado de Equipos
//
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Listado de Equipos") },
                actions = {
                    // Ejemplo de botones en la barra superior
                    IconButton(onClick = { /* Lógica para agregar equipo */ }) {
                        Icon(Icons.Filled.Add, contentDescription = "Añadir Ordenador")
                    }
                    IconButton(onClick = { /* Lógica para préstamo */ }) {
                        Icon(Icons.Filled.ShoppingCart, contentDescription = "Prestar Ordenador")
                    }
                    IconButton(onClick = { /* Lógica para borrar */ }) {
                        Icon(Icons.Filled.Delete, contentDescription = "Borrar Ordenador")
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
            // Aquí se mostraría el listado de equipos
            // Para este ejemplo, solo un texto:
            Text(text = "Listado de Equipos (datos de ejemplo)")

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Ir a Detalle",
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { navController.navigate("detail_screen") }
                    .padding(16.dp)
            )
        }
    }
}

//
// PANTALLA DE DETALLE: Tiene botón de retroceso
//
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del Equipo") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
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
            Text(text = "Aquí se muestran los detalles del equipo")
            // Puedes incluir botones para modificar, añadir o borrar componentes
        }
    }
}

//
// PANTALLA DE ESCANEO DE QR: Con flecha para volver
//
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QRScanScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Escanear QR para Login") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
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
            Text("Simulación de Escaneo de QR")
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { navController.navigate("login") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Simular Lectura y Volver al Login")
            }
        }
    }
}

//
// PANTALLA DE ADMINISTRACIÓN: Con flecha para volver
//
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Panel de Administración") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
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
            Text("Opciones para Superadmin", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { /* Lógica para gestionar usuarios */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Gestionar Usuarios")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { /* Lógica para configuraciones avanzadas */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Configuraciones")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Volver al Inicio")
            }
        }
    }
}

//
// PREVIEW de la Pantalla Principal (para ver en Android Studio)
//
@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    ProyectoFCTTheme {
        MainScreen(navController = rememberNavController())
    }
}
