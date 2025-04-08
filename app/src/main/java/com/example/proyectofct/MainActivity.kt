package com.example.proyectofct


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.*
import com.example.proyectofct.ui.theme.ProyectoFCTTheme
import com.example.proyectofct.View.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProyectoFCTTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "login") {
                    composable("login") { LoginScreen(navController) }
                    composable("main_screen") { MainScreen(navController) }
                    composable("detail_screen/{equipoNombre}") { backStackEntry ->
                        val equipoNombre = backStackEntry.arguments?.getString("equipoNombre") ?: ""
                        DetailScreen(navController, equipoNombre)
                    }
                    composable("qr_scan") { QRScanScreen(navController) }
                    composable("admin") { AdminScreen(navController) }
                }
            }
        }
    }
}
