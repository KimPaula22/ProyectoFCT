package com.example.proyectofct.View

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.proyectofct.Model.Equipo
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.material3.TopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEquipoScreen(navController: NavHostController, equipos: MutableList<Equipo>, onAddEquipo: (Equipo) -> Unit) {
    var nombre by rememberSaveable { mutableStateOf("") }
    var estado by rememberSaveable { mutableStateOf("") }
    var ubicacion by rememberSaveable { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Añadir Equipo") },
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre del equipo") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = estado,
                onValueChange = { estado = it },
                label = { Text("Estado del equipo") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = ubicacion,
                onValueChange = { ubicacion = it },
                label = { Text("Ubicación") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    if (nombre.isNotBlank() && estado.isNotBlank() && ubicacion.isNotBlank()) {
                        val nuevoEquipo = Equipo(nombre = nombre, estado = estado, ubicacion = ubicacion)
                        onAddEquipo(nuevoEquipo) // Añadir equipo a la lista
                        navController.popBackStack()  // Volver atrás después de añadir
                    } else {
                        // Mostrar un mensaje de error si alguno de los campos está vacío
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Añadir Equipo")
            }
        }
    }
}
