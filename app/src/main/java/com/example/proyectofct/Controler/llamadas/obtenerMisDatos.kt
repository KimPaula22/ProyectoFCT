package com.example.proyectofct.Controler.llamadas

import android.app.AlertDialog
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

/**
 * Realiza una llamada a la API para obtener los datos del usuario actual.
 *
 * @param context El contexto de la aplicación.
 * @param navController El controlador de navegación para manejar la navegación.
 * @param accessToken El token de acceso del usuario, si está disponible.
 * @param intentos Número de reintentos en caso de fallos (poner 0 significa 7 intentos, si se pone 1, serán 6 intentos, etc.)
 * @param onResultado Callback que se llama con el resultado de la operación.
 */
fun obtenerMisDatos(
    context: Context,
    navController: NavController,
    accessToken: String?,
    intentos: Int = 0,
    onResultado: (usuario: Usuario?, mensaje: String) -> Unit
) {
    var token = accessToken
    if (token.isNullOrEmpty()) {
        token = tokenDatabaseManager?.getAccessToken()
    }
    RetrofitClient.instance.getMisDatos("Bearer $token")
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
                    Log.d("MIUSUARIO", "Token inválido. Intentando refrescar...")
                    refrescarToken(0) { mensaje ->
                        Log.d("MIUSUARIO", "Reintentando con nuevo token: $mensaje")
                        val nuevoToken = tokenDatabaseManager?.getAccessToken()
                        if (!nuevoToken.isNullOrEmpty()) {
                            obtenerMisDatos(context,navController,nuevoToken, intentos, onResultado)
                        } else {
                            onResultado(null, "No se pudo obtener un nuevo token")
                            mostrarSesionCaducadaDialog(context, navController)
                        }
                    }
                }else {
                    val errorMsg = "No se pudieron obtener tus datos. Por favor, inténtalo de nuevo más tarde. (Código: ${response.code()})"
                    Log.e("MIUSUARIO", errorMsg)
                    mostrarErrorDialog(context, errorMsg)
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
                    val mensaje = "No se pudo conectar con el servidor. Por favor, revisa tu conexión a internet e inténtalo de nuevo."
                    Log.e("MIUSUARIO", mensaje)
                    mostrarErrorDialog(context, mensaje)
                    onResultado(null, mensaje)
                }
            }
        })
}

