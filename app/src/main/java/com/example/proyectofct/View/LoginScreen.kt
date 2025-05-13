package com.example.proyectofct.View

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavHostController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var inputName by remember { mutableStateOf("") }
    var selectedRole by remember { mutableStateOf("Estudiante") }

    // Datos de prueba
    val usuarioPrueba = "usuario@hotmail.com"
    val passwordPrueba = "Password123"

    fun deriveName(email: String): String {
        return email.substringBefore("@").replaceFirstChar { it.uppercaseChar() }
    }

    fun deriveRole(email: String): String {
        return if (email.contains("prof")) "Profesor" else "Estudiante"
    }

    Scaffold(topBar = { TopAppBar(title = { Text("Iniciar Sesión") }) }) { innerPadding ->
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
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { showPassword = !showPassword }) {
                        Icon(
                            imageVector = if (showPassword) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            contentDescription = if (showPassword) "Ocultar contraseña" else "Mostrar contraseña"
                        )
                    }
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    if (email == usuarioPrueba && password == passwordPrueba) {
                        inputName = deriveName(email)
                        selectedRole = deriveRole(email)
                        showDialog = true
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Iniciar Sesión")
            }
            Spacer(modifier = Modifier.height(8.dp))
            TextButton(
                onClick = {
                    // Aquí podrías navegar a una pantalla de registro real
                    navController.navigate("registro")
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("¿No tienes cuenta? Regístrate")
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = {},
            title = { Text("Bienvenido") },
            text = {
                Column {
                    OutlinedTextField(
                        value = inputName,
                        onValueChange = { inputName = it },
                        label = { Text("¿Cómo te llamas?") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Selecciona tu rol:")
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = selectedRole == "Administrador",
                            onClick = { selectedRole = "Administrador" }
                        )
                        Text("Administrador", modifier = Modifier.clickable { selectedRole = "Administrador" })
                        Spacer(modifier = Modifier.width(16.dp))
                        RadioButton(
                            selected = selectedRole == "Profesor",
                            onClick = { selectedRole = "Profesor" }
                        )
                        Text("Profesor", modifier = Modifier.clickable { selectedRole = "Profesor" })
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    navController.currentBackStackEntry?.savedStateHandle?.set("userName", inputName)
                    navController.currentBackStackEntry?.savedStateHandle?.set("role", selectedRole)
                    showDialog = false
                    navController.navigate("main_screen")
                }) {
                    Text("Continuar")
                }
            }
        )
    }
}
