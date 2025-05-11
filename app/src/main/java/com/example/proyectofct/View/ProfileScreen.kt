package com.example.proyectofct.View

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.proyectofct.Model.Equipo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavHostController
) {
    // Obtener datos previos desde savedStateHandle
    val backStackEntry = navController.currentBackStackEntry!!
    val savedHandle = backStackEntry.savedStateHandle
    val initialName = savedHandle.get<String>("userName") ?: "Usuario Demo"
    val initialRole = savedHandle.get<String>("role") ?: "Estudiante"

    // Estado local editable
    var name by rememberSaveable { mutableStateOf(initialName) }
    var role by rememberSaveable { mutableStateOf(initialRole) }

    // Ejemplo de equipos asignados (podrías cargar reales)
    val assigned = backStackEntry.savedStateHandle.get<List<Equipo>>("assigned") ?: emptyList()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Perfil") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.AccountCircle, contentDescription = "Volver")
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
            verticalArrangement = Arrangement.Top
        ) {
            Text(text = "Edita tu perfil:", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            Text(text = "Rol:", style = MaterialTheme.typography.bodyMedium)
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = role == "Estudiante",
                    onClick = { role = "Estudiante" }
                )
                Text("Estudiante", modifier = Modifier.clickable { role = "Estudiante" })
                Spacer(Modifier.width(16.dp))
                RadioButton(
                    selected = role == "Profesor",
                    onClick = { role = "Profesor" }
                )
                Text("Profesor", modifier = Modifier.clickable { role = "Profesor" })
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Equipos asignados:", style = MaterialTheme.typography.titleSmall)
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
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Button(onClick = {
                    // Guardar cambios
                    savedHandle.set("userName", name)
                    savedHandle.set("role", role)
                }) {
                    Text("Guardar")
                }
                Button(onClick = {
                    // Cerrar sesión
                    navController.navigate("login_screen") {
                        popUpTo("login_screen") { inclusive = true }
                    }
                }) {
                    Text("Cerrar sesión")
                }
            }
        }
    }
}
