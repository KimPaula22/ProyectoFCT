package com.example.proyectofct.View

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.proyectofct.Controler.llamadas.obtenerMisDatos
import com.example.proyectofct.Controler.llamadas.revisarEstadoServidor
import com.example.proyectofct.MainActivity.Companion.currentUserRole
import com.example.proyectofct.MainActivity.Companion.miUsuario
import com.example.proyectofct.Model.Role
import com.example.proyectofct.R
import kotlinx.coroutines.delay

/**
 * Pantalla de conexión con el servidor
 * Muestra el estado de la conexión e intenta conectar con el servidor
 */
@Composable
fun ConnectionScreen(navController: NavHostController) {
    val context = LocalContext.current

    // Estado para controlar el mensaje que se muestra
    var mensaje by remember { mutableStateOf("Conectando con el servidor... Esto puede durar unos minutos.") }

    // Estado para controlar si se está realizando la comprobación
    var comprobando by remember { mutableStateOf(true) }

    // Estado para el botón de recargar
    var mostrarBotonRecargar by remember { mutableStateOf(false) }

    // Animación de rotación para el icono de recarga
    val infiniteTransition = rememberInfiniteTransition(label = "rotación")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotación"
    )

    // Efecto para revisar el estado del servidor automáticamente al entrar a la pantalla
    LaunchedEffect(comprobando) {
        if (comprobando) {
            revisarEstadoServidor(
                0,
                estadoServidor = { estado ->
                    when {
                        estado.esValido == true -> {
                            mensaje = "Obteniendo tus datos..."
                            obtenerMisDatos(
                                context = context,
                                navController = navController,
                                accessToken = null,
                                intentos = 0,
                                onResultado = { usuario, mensaj ->
                                    if (usuario != null) {
                                        miUsuario = usuario
                                        currentUserRole = if (usuario.rol == "admin") {
                                            Role.ADMIN
                                        } else {
                                            Role.PROFESOR
                                        }
                                        navController.navigate("main_screen") {
                                            popUpTo("connection_screen") { inclusive = true }
                                        }
                                    } else {
                                        mensaje = "Error al obtener tus datos del servidor: $mensaj"
                                        comprobando = false
                                        mostrarBotonRecargar = true
                                    }
                                }
                            )
                        }
                        estado.esValido == false -> {
                            navController.navigate("login") {
                                popUpTo("connection_screen") { inclusive = true }
                            }
                        }
                        else -> {
                            mensaje = estado.mensaje
                            comprobando = false
                            mostrarBotonRecargar = true
                        }
                    }
                }
            )
        }
    }

    // UI de la pantalla
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Logo de la aplicación
        Image(
            painter = painterResource(id = R.drawable.ayalagest),
            contentDescription = "Logo de la aplicación",
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Icono de recarga animado (solo se muestra mientras comprueba la conexión)
        if (comprobando) {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = "Comprobando conexión",
                modifier = Modifier
                    .size(48.dp)
                    .rotate(rotation),
                tint = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Mensaje de estado
        Text(
            text = mensaje,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = if (mostrarBotonRecargar) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Botón para recargar (solo visible después de un error)
        if (mostrarBotonRecargar) {
            Button(
                onClick = {
                    mensaje = "Conectando con el servidor..."
                    comprobando = true
                    mostrarBotonRecargar = false
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Reintentar")
            }
        }
    }
}
