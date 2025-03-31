package com.example.proyectofct

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.proyectofct.ui.theme.ProyectoFCTTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProyectoFCTTheme {
                val navController = rememberNavController()
                // Se configura la navegación con dos destinos:
                // "greeting" y "other"
                NavHost(navController = navController, startDestination = "greeting") {
                    composable("greeting") { GreetingScreen(navController) }
                    composable("other") { OtherScreen() }
                }
            }
        }
    }
}

@Composable
fun GreetingScreen(navController: NavHostController) {
    // Al pulsar el texto se navega a la otra pantalla
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Text(
                text = "Hello Android! (Pulsar para redirigir)",
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { navController.navigate("other") }
            )
        }
    }
}

@Composable
fun OtherScreen() {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Text(text = "¡Estás en la otra pantalla!")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ProyectoFCTTheme {
        GreetingScreen(navController = rememberNavController())
    }
}
