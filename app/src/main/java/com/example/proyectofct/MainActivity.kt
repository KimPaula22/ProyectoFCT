package com.example.proyectofct

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.*
import com.example.proyectofct.ui.theme.ProyectoFCTTheme
import com.example.proyectofct.View.*
import com.example.proyectofct.Model.Equipo

class MainActivity : ComponentActivity() {
    // Definir la lista de equipos
    private val equipos = mutableListOf<Equipo>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProyectoFCTTheme {
                // Crea el controlador de navegación
                val navController = rememberNavController()

                // Definimos el NavHost con las rutas que tendrá tu aplicación
                NavHost(navController = navController, startDestination = "login") {
                    // Ruta para la pantalla de login
                    composable("login") { LoginScreen(navController) }

                    // Ruta para la pantalla de registro
                    composable("register_screen") { RegisterScreen(navController) }

                    // Ruta para la pantalla de añadir
                    composable("add_screen") {
                        //  lista de equipos y la función que maneja la adición de un equipo
                        AddEquipoScreen(
                            navController = navController,
                            equipos = equipos, // lista de equipos
                            onAddEquipo = { nuevoEquipo ->
                                equipos.add(nuevoEquipo) //  añades el equipo a la lista
                            }
                        )
                    }

                    // Ruta para la pantalla de perfil
                    composable("profile_screen") { ProfileScreen(navController) }

                    // Ruta para la pantalla principal
                    composable("main_screen") { MainScreen(navController) }

                    // Ruta para la pantalla de detalle de un equipo
                    composable("detail_screen/{equipoNombre}") { backStackEntry ->
                        val equipoNombre = backStackEntry.arguments?.getString("equipoNombre") ?: ""
                        DetailScreen(navController, equipoNombre)
                    }

                    // Ruta para la pantalla de escaneo QR
                    composable("qr_scan") { QRScanScreen(navController) }

                    // Ruta para la pantalla de administrador
                    composable("admin_screen") { AdminScreen(navController) }
                }
            }
        }
    }
}
