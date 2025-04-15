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
import com.example.proyectofct.Controler.RetrofitClient
import com.example.proyectofct.MainActivity
import retrofit2.Call
//Importacion de los requests y responses necesarios para el login
import com.example.proyectofct.Model.Responses.LoginResponse
import com.example.proyectofct.Model.Requests.LoginRequest
import com.example.proyectofct.Controler.recibirMensajeDeError


//Usuarios:
//prueba@gmail.com"    "123456Us"
//pruebaad@gmail.com"  "123456Ad"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavHostController) {
    var email by remember { mutableStateOf("") }  // Estado para el email
    var password by remember { mutableStateOf("") }  // Estado para la contraseña
    var passwordVisible by remember { mutableStateOf(false) }  // Controla si la contraseña es visible o no
    var emailError by remember { mutableStateOf("") }  // Mensaje de error para el email
    var passwordError by remember { mutableStateOf("") }  // Mensaje de error para la contraseña


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
                    // Validaciones locales
                    if (!isEmailValid(email)) {
                        emailError = "El email debe ser de los dominios permitidos (hotmail.com, gmail.com, hotmail.es)"
                        return@Button
                    }
                    if (!isPasswordValid(password)) {
                        passwordError = "La contraseña debe tener al menos 8 caracteres y una mayúscula"
                        return@Button
                    }

                    // Limpiamos errores anteriores
                    emailError = ""
                    passwordError = ""

                    // Preparar login
                    val loginRequest = LoginRequest(email, password)
                    var intentos = 0

                    // Función para intentar iniciar sesión
                    // Si la respuesta es correcta y el mensaje es "Login exitoso", navega a la pantalla principal
                    // Si la respuesta es incorrecta
                    // Si el código de error es 401, muestra un mensaje de error "Credenciales incorrectas"
                    // Si no, muestra un mensaje de error con el código de error
                    // Si la llamada falla y ya hemos intentado 3 veces, muestra un mensaje de error con el mensaje de error de Retrofit
                    fun intentarLogin() {
                        RetrofitClient.instance.login(loginRequest)
                            .enqueue(object : retrofit2.Callback<LoginResponse> {
                                override fun onResponse(
                                    call: Call<LoginResponse>,
                                    response: retrofit2.Response<LoginResponse>
                                ) {
                                    if (response.isSuccessful) {
                                        val loginResponse = response.body()
                                        if (loginResponse?.mensaje == "Login exitoso") {
                                            MainActivity.correo = email
                                            MainActivity.password = password
                                            MainActivity.rol = loginResponse.rol
                                            navController.navigate("main_screen")
                                        }
                                    } else {
                                        val errorCod = response.code()
                                        if (errorCod == 401) {
                                            emailError = "Credenciales incorrectas"
                                            passwordError = "Credenciales incorrectas"
                                        } else {
                                            passwordError = "Error en la respuesta del servidor: $errorCod"
                                        }
                                    }
                                }

                                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                                    intentos++
                                    if (intentos < 3) {
                                        intentarLogin()  // Reintenta
                                    } else {
                                        val errorMsg = recibirMensajeDeError(t)
                                        emailError = errorMsg
                                        passwordError = errorMsg
                                    }
                                }
                            })
                    }

                    // Llamamos por primera vez
                    intentarLogin()
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
