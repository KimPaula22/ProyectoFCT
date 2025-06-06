package com.example.proyectofct.Controler.llamadas

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.navigation.NavController
import com.example.proyectofct.Controler.ApiService
import com.example.proyectofct.Controler.RetrofitClient
import com.example.proyectofct.MainActivity.Companion.tokenDatabaseManager
import com.example.proyectofct.Model.Usuario
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

fun obtenerMisDatos(
    context: Context,
    navController: NavController,
    accessToken: String,
    intentos: Int = 0,
    onResultado: (usuario: Usuario?, mensaje: String) -> Unit
) {
    RetrofitClient.instance.getMisDatos("Bearer $accessToken")
        .enqueue(object : Callback<Usuario> {
            override fun onResponse(call: Call<Usuario>, response: Response<Usuario>) {
                if (response.isSuccessful) {
                    val usuario = response.body()
                    if (usuario != null) {
                        onResultado(usuario, "Datos obtenidos correctamente")
                    } else {
                        onResultado(null, "No se recibieron datos del usuario")
                    }
                } else if (response.code() == 403) {
                    Log.d("CrearUsuario", "Token inválido. Intentando refrescar...")
                    refrescarToken(0) { mensaje ->
                        Log.d("CrearUsuario", "Reintentando con nuevo token: $mensaje")
                        val nuevoToken = tokenDatabaseManager?.getAccessToken()
                        if (!nuevoToken.isNullOrEmpty()) {
                            obtenerMisDatos(context,navController,nuevoToken, intentos, onResultado)
                        } else {
                            onResultado(null, "No se pudo obtener un nuevo token")
                            mostrarSesionCaducadaDialog(context, navController)
                        }
                    }
                }else {
                    val errorMsg = "Error HTTP: ${response.code()} - ${response.errorBody()?.string()}"
                    Log.e("MIUSUARIO", errorMsg)
                    onResultado(null, errorMsg)
                }
            }

            override fun onFailure(call: Call<Usuario>, t: Throwable) {
                Log.e("MIUSUARIO", "Error de red: ${t.message}")
                if (intentos < 6) {
                    Log.d("MIUSUARIO", "Intento ${intentos + 1} fallido. Reintentando...")
                    Handler(Looper.getMainLooper()).postDelayed({
                        obtenerMisDatos(context,navController,accessToken, intentos + 1, onResultado)
                    }, 7000)
                } else {
                    val mensaje = (t.message ?: "Error desconocido") + " Intentos agotados."
                    Log.e("MIUSUARIO", mensaje)
                    onResultado(null, mensaje)
                }
            }
        })
}