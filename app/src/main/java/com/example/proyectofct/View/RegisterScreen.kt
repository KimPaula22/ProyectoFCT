package com.example.proyectofct.View

import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.navigation.NavHostController
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavHostController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Registrarse") })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            // Campo para el email
            OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            // Campo para la contraseña
            OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Contraseña") }, modifier = Modifier.fillMaxWidth(), visualTransformation = PasswordVisualTransformation())
            Spacer(modifier = Modifier.height(8.dp))
            // Campo para confirmar la contraseña
            OutlinedTextField(value = confirmPassword, onValueChange = { confirmPassword = it }, label = { Text("Confirmar Contraseña") }, modifier = Modifier.fillMaxWidth(), visualTransformation = PasswordVisualTransformation())
            Spacer(modifier = Modifier.height(16.dp))
            // Botón para registrar al usuario
            Button(
                onClick = {
                    // Aquí puedes agregar la lógica para registrar al usuario y guardar sus datos en la base de datos
                    // Por ejemplo: registerUser(email, password)
                    navController.navigate("login") // Redirige al login después de registrarse
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Registrarse")
            }
            Spacer(modifier = Modifier.height(8.dp))
            // Botón para volver al login
            Button(onClick = { navController.popBackStack() }, modifier = Modifier.fillMaxWidth()) {
                Text("Volver al Login")
            }
        }
    }
}