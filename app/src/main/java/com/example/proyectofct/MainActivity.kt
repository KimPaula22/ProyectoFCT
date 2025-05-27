package com.example.proyectofct

import android.database.sqlite.SQLiteOpenHelper
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.proyectofct.Controler.DatabaseHelper
import com.example.proyectofct.Controler.TokenDatabaseManager
import com.example.proyectofct.Controler.llamadas.*
import com.example.proyectofct.Model.Equipo
import com.example.proyectofct.Model.Role
import com.example.proyectofct.View.AddEquipoScreen
import com.example.proyectofct.View.AdminScreen
import com.example.proyectofct.View.LoginScreen
import com.example.proyectofct.View.MainScreen
import com.example.proyectofct.View.ProfileScreen
import com.example.proyectofct.View.QRScanScreen
import com.example.proyectofct.View.RegisterScreen
import com.example.proyectofct.ui.theme.ProyectoFCTTheme


class MainActivity : ComponentActivity() {

    companion object {
        val equipos = mutableListOf<Equipo>()
        var databaseHelper: DatabaseHelper? = null
        var tokenDatabaseManager: TokenDatabaseManager? = null
        val componentes : MutableList<Any> = mutableListOf() // Lista para almacenar los componentes
        var currentUserRole: Role = Role.PROFESOR

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ATENCION, TE DA ERRORES DE SQLite DESCOMENTA LA LINEA DE ABAJO Y LO EJECUTAS, DEPUES COMENTALO
        // applicationContext.deleteDatabase("tokens.db")

        // Inicializa la base de datos y el manejador de tokens de manera segura
        databaseHelper = DatabaseHelper(applicationContext)
        tokenDatabaseManager = TokenDatabaseManager(applicationContext)

        enableEdgeToEdge()

        setContent {
            ProyectoFCTTheme {

                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "login") {
                    composable("login") { LoginScreen(navController) }
                    composable("register_screen") { RegisterScreen(navController) }
                    composable("add_screen") {
                        AddEquipoScreen(
                            navController = navController,
                            equipos = equipos,
                            onAddEquipo = { nuevoEquipo ->
                                equipos.add(nuevoEquipo)
                            }
                        )
                    }
                    composable("profile_screen") { ProfileScreen(navController) }
                    composable("main_screen") { MainScreen(navController) }
                    composable("detail_screen/{equipoNombre}") { backStackEntry ->
                        val equipoNombre = backStackEntry.arguments?.getString("equipoNombre") ?: ""
                        DetailScreen(navController, equipoNombre)
                    }
                    composable("qr_scan") { QRScanScreen(navController) }
                    composable("admin_screen") { AdminScreen(navController) }
                }
            }
        }
    }
}
