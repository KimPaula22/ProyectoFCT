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
import com.example.proyectofct.Model.Usuario
import com.example.proyectofct.Model.componentes.Cpu
import com.example.proyectofct.Model.componentes.DispositivoIO
import com.example.proyectofct.Model.componentes.Gpu
import com.example.proyectofct.Model.componentes.Pci
import com.example.proyectofct.Model.componentes.PlacaBase
import com.example.proyectofct.Model.componentes.Ram
import com.example.proyectofct.Model.componentes.Rom
import com.example.proyectofct.View.AddEquipoScreen
import com.example.proyectofct.View.AdminScreen
import com.example.proyectofct.View.ComponenteDetailsScreen
import com.example.proyectofct.View.LoginScreen
import com.example.proyectofct.View.MainScreen
import com.example.proyectofct.View.ProfileScreen
import com.example.proyectofct.View.QRScanScreen
import com.example.proyectofct.View.RegisterScreen
import com.example.proyectofct.View.ConnectionScreen
import com.example.proyectofct.ui.theme.ProyectoFCTTheme


class MainActivity : ComponentActivity() {

    companion object {
        var miUsuario: Usuario? = null
        val equipos = mutableListOf<Equipo>()
        var databaseHelper: DatabaseHelper? = null
        var tokenDatabaseManager: TokenDatabaseManager? = null
        val componentes = mutableListOf<Pair<Int, Any>>()
        var currentUserRole: Role = Role.PROFESOR
        var usuariosDenegados = mutableListOf<Usuario>()
        var usuariosAceptados = mutableListOf<Usuario>()
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
                val token = tokenDatabaseManager?.getAccessToken()

                NavHost(navController = navController, startDestination = "connection_screen") {
                    composable("connection_screen") { ConnectionScreen(navController) }
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
                    composable("detail_screen/{id}") { backStackEntry ->
                        // buscar por id que es un int
                        val equipo = equipos.find { it.id == backStackEntry.arguments?.getString("id")?.toInt()}
                        DetailScreen(navController, equipo)
                    }
                    composable("componente_detail_screen/{componenteId}") { backStackEntry ->
                        val componenteIdStr = backStackEntry.arguments?.getString("componenteId")
                        val componenteId = componenteIdStr?.toIntOrNull()
                        Log.d("ComponenteDetailScreen", "Componente ID: $componenteId")
                        val componente = componenteId?.let { id ->
                            componentes.find { (tipo, comp) ->
                                when (tipo) {
                                    0 -> (comp as? PlacaBase)?.id == id
                                    1 -> (comp as? Cpu)?.id == id
                                    2 -> (comp as? Gpu)?.id == id
                                    3 -> (comp as? Ram)?.id == id
                                    4 -> (comp as? Rom)?.id == id
                                    5 -> (comp as? Pci)?.id == id
                                    6 -> (comp as? DispositivoIO)?.id == id
                                    else -> false
                                }
                            }
                        }
                        ComponenteDetailsScreen(navController, componente)
                    }
                    composable("qr_scan") { QRScanScreen(navController) }
                    composable("admin_screen") { AdminScreen(navController) }
                }
            }
        }
    }
}
