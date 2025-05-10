package com.example.proyectofct.View

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.proyectofct.Model.Equipo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavHostController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
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
                visualTransformation = PasswordVisualTransformation()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    if (email == usuarioPrueba && password == passwordPrueba) {
                        // Preparar datos para mostrar dialogo
                        inputName = deriveName(email)
                        selectedRole = deriveRole(email)
                        showDialog = true
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Iniciar Sesión")
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { /* no cerrar */ },
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
                    Row {
                        RadioButton(
                            selected = selectedRole == "Estudiante",
                            onClick = { selectedRole = "Estudiante" }
                        )
                        Text("Estudiante", modifier = Modifier.clickable { selectedRole = "Estudiante" })
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
                    // Guardar en NavController para ProfileScreen
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
