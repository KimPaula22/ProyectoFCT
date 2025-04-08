package com.example.proyectofct.View

import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.navigation.NavHostController
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavHostController) {
    var email by remember { mutableStateOf("") }  // Estado para el email
    var password by remember { mutableStateOf("") }  // Estado para la contraseña
    var passwordVisible by remember { mutableStateOf(false) }  // Controla si la contraseña es visible o no
    var emailError by remember { mutableStateOf("") }  // Mensaje de error para el email
    var passwordError by remember { mutableStateOf("") }  // Mensaje de error para la contraseña

    // Datos de prueba
    val usuarioPrueba = "usuario@hotmail.com" // usuario
    val passwordPrueba = "Password123"  // password

    // Función para comprobar si el email esta bien
    fun isEmailValid(email: String): Boolean {
        // Validación de que el email tiene uno de los dominios permitidos
        return email.matches(Regex("^[A-Za-z0-9._%+-]+@(hotmail\\.com|gmail\\.com|hotmail\\.es)$"))
    }

    // Función para comprobar la contraseña
    fun isPasswordValid(password: String): Boolean {
        // Validación de la contraseña: debe tener al menos 8 caracteres y al menos una letra mayúscula
        return password.length >= 8 && password.any { it.isUpperCase() }
    }

    // Scaffold para la estructura básica de la pantalla (la barra superior, etc.)
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Iniciar Sesión") })  // Barra superior con el título
        }
    ) { innerPadding ->  // innerPadding es el espacio adicional que permite que los elementos no se solapen con la barra superior
        Column(
            modifier = Modifier
                .fillMaxSize()  // Ocupa todo el espacio disponible
                .padding(innerPadding)  // Añade el espacio extra por la barra superior
                .padding(16.dp),  // Añade un margen de 16dp alrededor
            verticalArrangement = Arrangement.Center  // Centra los elementos verticalmente
        ) {
            // Campo de entrada para el email
            OutlinedTextField(
                value = email,  // El valor que tiene el campo de texto
                onValueChange = {
                    email = it
                    emailError = ""  // Limpiamos el error cuando el usuario empieza a escribir
                },  // Cambia el valor cuando el usuario lo ingresa
                label = { Text("Email") },  // Etiqueta del campo
                modifier = Modifier.fillMaxWidth(),  // Ocupa todo el ancho disponible
                isError = emailError.isNotEmpty(),  // Si hay un error, se resalta el campo
                supportingText = {
                    if (emailError.isNotEmpty()) {
                        Text(text = emailError, color = MaterialTheme.colorScheme.error)  // Mensaje de error si el email no es válido
                    }
                }
            )
            Spacer(modifier = Modifier.height(8.dp))  // Espacio de 8dp entre los campos

            // Campo de entrada para la contraseña
            OutlinedTextField(
                value = password,  // El valor que tiene el campo de contraseña
                onValueChange = {
                    password = it
                    passwordError = ""  // Limpiamos el error cuando el usuario empieza a escribir
                },  // Cambia el valor cuando el usuario lo ingresa
                label = { Text("Contraseña") },  // Etiqueta del campo
                modifier = Modifier.fillMaxWidth(),  // Ocupa todo el ancho disponible
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),  // Cambia la visualización de la contraseña (oculta o visible)
                trailingIcon = {  // Ícono al final del campo de texto (el ojito para ver/ocultar la contraseña)
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            contentDescription = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña"
                        )
                    }
                },
                isError = passwordError.isNotEmpty(),  // Si hay un error, se resalta el campo
                supportingText = {
                    if (passwordError.isNotEmpty()) {
                        Text(text = passwordError, color = MaterialTheme.colorScheme.error)  // Mensaje de error si la contraseña no es válida
                    }
                }
            )
            Spacer(modifier = Modifier.height(16.dp))  // Espacio de 16dp entre los campos

            // Botón para iniciar sesión con validación
            Button(
                onClick = {
                    // Validar el email y la contraseña
                    if (email == usuarioPrueba && password == passwordPrueba) {
                        navController.navigate("main_screen")  // Si todo es válido, navegar a la pantalla principal
                    } else {
                        // Si el email no es válido
                        if (!isEmailValid(email)) {
                            emailError = "El email debe ser de los dominios permitidos (hotmail.com, gmail.com, hotmail.es)"
                        }
                        // Si la contraseña no es válida
                        if (!isPasswordValid(password)) {
                            passwordError = "La contraseña debe tener al menos 8 caracteres y una mayúscula"
                        }
                        // Si no coinciden los datos con los de prueba
                        if (email != usuarioPrueba) {
                            emailError = "Email incorrecto"
                        }
                        if (password != passwordPrueba) {
                            passwordError = "Contraseña incorrecta"
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Iniciar Sesión")
            }

            Spacer(modifier = Modifier.height(8.dp))  // Espacio de 8dp

            // Botón para redirigir a la pantalla de registro
            Button(onClick = { navController.navigate("register_screen") }, modifier = Modifier.fillMaxWidth()) {
                Text("Registrarse")
            }
        }
    }
}
