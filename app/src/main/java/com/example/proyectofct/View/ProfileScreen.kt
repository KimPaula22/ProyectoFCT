package com.example.proyectofct.View

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.proyectofct.Controler.llamadas.cerrarSesion
import com.example.proyectofct.Controler.llamadas.cerrarSesionTodos
import com.example.proyectofct.Controler.llamadas.obtenerMisDatos
import com.example.proyectofct.MainActivity
import com.example.proyectofct.MainActivity.Companion.miUsuario
import com.example.proyectofct.Model.Equipo
import com.example.proyectofct.Model.Role
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavHostController
) {
    // Estado local editable
    var name by rememberSaveable { mutableStateOf("") }
    var apellidos by rememberSaveable { mutableStateOf("") }
    var role by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }

    // Estados para los diálogos
    var mostrarDialogoCerrarSesion by remember { mutableStateOf(false) }
    var mostrarDialogoMensaje by remember { mutableStateOf(false) }
    var mensajeRespuesta by remember { mutableStateOf("") }

    // Bandera para controlar si se está cerrando sesión
    var estaLogout by rememberSaveable { mutableStateOf(false) }

    // Obtener datos del usuario desde MainActivity.miUsuario solo si no se está cerrando sesión
    if (!estaLogout) {
        obtenerMisDatos(
            context = navController.context,
            navController = navController,
            intentos = 0,
            onResultado = { usuario, mensaje ->
                if (usuario != null) {
                    miUsuario = usuario
                    name = usuario.nombre
                    apellidos = usuario.apellidos
                    role = usuario.rol.uppercase(Locale.getDefault())
                    email = usuario.email
                } else {
                    Toast.makeText(navController.context, mensaje, Toast.LENGTH_SHORT).show()
                }
            },
            accessToken = ""
        )

        // Inicializar datos si usuario ya está disponible
        LaunchedEffect(Unit) {
            miUsuario?.let { usuario ->
                name = usuario.nombre
                apellidos = usuario.apellidos
                role = usuario.rol
                email = usuario.email
            }
        }
    }

    // Obtener equipos asignados
    val backStackEntry = navController.currentBackStackEntry!!
    val savedHandle = backStackEntry.savedStateHandle
    val assigned = savedHandle.get<List<Equipo>>("assigned") ?: emptyList()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Perfil") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Título
                Text(
                    text = "Datos del usuario",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Campo nombre
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Campo apellidos
                OutlinedTextField(
                    value = apellidos,
                    onValueChange = { apellidos = it },
                    label = { Text("Apellidos") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Campo email (no editable)
                OutlinedTextField(
                    value = email,
                    onValueChange = {},
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = false
                )

                // Campo rol (no editable)
                OutlinedTextField(
                    value = role,
                    onValueChange = {},
                    label = { Text("Rol") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = false
                )

                // Selector de vista (solo para administradores)
                if (role.uppercase(Locale.getDefault()) == Role.ADMIN.name) {
                    Text(text = "Vista (Solo administrador):", style = MaterialTheme.typography.bodyMedium)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = role == Role.PROFESOR.name,
                            onClick = { role = Role.PROFESOR.name }
                        )
                        Spacer(Modifier.width(4.dp))
                        Text("Profesor", modifier = Modifier.clickable { role = Role.PROFESOR.name })
                        Spacer(Modifier.width(16.dp))
                        RadioButton(
                            selected = role == Role.ADMIN.name,
                            onClick = { role = Role.ADMIN.name }
                        )
                        Spacer(Modifier.width(4.dp))
                        Text("Administrador", modifier = Modifier.clickable { role = Role.ADMIN.name })
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Spacer(modifier = Modifier.height(24.dp))

                // Lista de equipos asignados
                if (assigned.isEmpty()) {
                    Text(text = "No tienes equipos asignados.", style = MaterialTheme.typography.bodySmall)
                } else {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(assigned) { equipo ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { /* Detalle si quieres */ },
                                elevation = CardDefaults.cardElevation(4.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(text = equipo.nombre, style = MaterialTheme.typography.titleMedium)
                                    Text(text = "Ubicación: ${equipo.ubicacion}", style = MaterialTheme.typography.bodySmall)
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                // Botones de acción
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Button(onClick = {
                        // Guardar cambios en MainActivity
                        MainActivity.currentUserRole = Role.valueOf(role.uppercase())
                        savedHandle.set("userName", name)
                        navController.popBackStack()
                    }) {
                        Text("Guardar")
                    }
                    Button(onClick = {
                        // Cerrar sesión
                        mostrarDialogoCerrarSesion = true
                    }) {
                        Text("Cerrar sesión")
                    }
                }
            }
        }
    )

    // Diálogo de confirmación de cierre de sesión
    if (mostrarDialogoCerrarSesion) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoCerrarSesion = false },
            title = { Text("Cerrar sesión") },
            text = { Text("¿Qué tipo de cierre de sesión deseas realizar?") },
            confirmButton = {
                Column {
                    Button(
                        onClick = {
                            mostrarDialogoCerrarSesion = false
                            // Activamos la bandera para evitar obtenerMisDatos
                            estaLogout = true
                            // Cerrar sesión en dispositivo actual
                            cerrarSesion(0) { mensaje ->
                                mensajeRespuesta = mensaje
                                mostrarDialogoMensaje = true
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Cerrar en este dispositivo")
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = {
                            mostrarDialogoCerrarSesion = false
                            // Activamos la bandera para evitar obtenerMisDatos
                            estaLogout = true
                            // Cerrar sesión en todos los dispositivos
                            cerrarSesionTodos(0) { mensaje ->
                                mensajeRespuesta = mensaje
                                mostrarDialogoMensaje = true
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Cerrar en todos los dispositivos")
                    }
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogoCerrarSesion = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    // Diálogo de mensaje de respuesta
    if (mostrarDialogoMensaje) {
        AlertDialog(
            onDismissRequest = {
                mostrarDialogoMensaje = false
                // Navegar a la pantalla de login después de cerrar el diálogo
                navController.navigate("login") {
                    popUpTo("login") { inclusive = true }
                }
            },
            title = { Text("Información") },
            text = { Text(mensajeRespuesta) },
            confirmButton = {
                TextButton(
                    onClick = {
                        mostrarDialogoMensaje = false
                        // Navegar a la pantalla de login después de pulsar aceptar
                        navController.navigate("login") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                ) {
                    Text("Aceptar")
                }
            }
        )
    }
}
