package com.example.proyectofct


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.*
import com.example.proyectofct.Model.Equipo
import com.example.proyectofct.ui.theme.ProyectoFCTTheme
import com.example.proyectofct.View.*

class MainActivity : ComponentActivity() {
    //variables staticas para pasar datos entre pantallas
    companion object {
        var correo: String = ""
        var password: String = ""
        var rol: String = ""
        var equipos: List<Equipo> = listOf()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProyectoFCTTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "login") {
                    composable("login") { LoginScreen(navController) }
                    composable("main_screen") { MainScreen(navController) }
                    composable("detail_screen/{idEquipo}") { backStackEntry ->
                        val idEquipo = backStackEntry.arguments?.getString("idEquipo") ?: ""
                        DetailScreen(navController, idEquipo.toInt(), equipos)
                    }
                    composable("qr_scan") { QRScanScreen(navController) }
                    composable("admin") { AdminScreen(navController) }
                }
            }
        }
    }
}
