package com.example.proyectofct.Controler.llamadas

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.navigation.NavController
import com.example.proyectofct.Controler.RetrofitClient
import com.example.proyectofct.MainActivity.Companion.tokenDatabaseManager
import com.example.proyectofct.Model.Requests
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Realiza una llamada a la API para aceptar un usuario por su DNI.
 *
 * @param context El contexto de la aplicación.
 * @param navController El controlador de navegación para manejar la navegación.
 * @param dni El DNI del usuario a aceptar.
 * @param callback Callback que se llama con el resultado de la operación.
 * @param intento Número de reintentos en caso de fallos (poner 0 significa 7 intentos, si se pone 1, serán 6 intentos, etc.)
 */
fun solicitarAceptarUsuario(
    context: Context,
    navController: NavController,
    dni: String,
    callback: (String?) -> Unit,
    intento: Int = 0
) {
    val accessToken = tokenDatabaseManager?.getAccessToken()
    val authHeader = "Bearer $accessToken"
    //log token
    Log.d("Token", "Token: $accessToken")

    RetrofitClient.instance.aceptarUsuario(authHeader, dni).enqueue(object : Callback<Requests.MensajeResponse> {
        override fun onResponse(call: Call<Requests.MensajeResponse>, response: Response<Requests.MensajeResponse>) {
            if (response.isSuccessful) {
                callback(response.body()?.mensaje)
            } else if (response.code() == 403) {
                Log.d("CrearUsuario", "Token inválido. Intentando refrescar...")
                refrescarToken(0) { mensaje ->
                    Log.d("CrearUsuario", "Reintentando con nuevo token: $mensaje")
                    val nuevoToken = tokenDatabaseManager?.getAccessToken()
                    if (!nuevoToken.isNullOrEmpty()) {
                        solicitarAceptarUsuario(context, navController, dni, callback)
                    } else {
                        mostrarSesionCaducadaDialog(context, navController)
                        callback(null)
                    }
                }
            } else {
                val errorMsg = try {
                    val errorJson = JSONObject(response.errorBody()?.string() ?: "{}")
                    errorJson.optString("mensaje", "Error desconocido")
                } catch (e: Exception) {
                    "Error desconocido"
                }

                callback("Error: ${response.code()} - ${errorMsg}")
            }
        }

        override fun onFailure(call: Call<Requests.MensajeResponse>, t: Throwable) {
            Log.e("CrearUsuario", "Error de red: ${t.message}")
            if (intento < 6) {
                Log.d("CrearUsuario", "Intento $intento fallido. Reintentando...")
                Handler(Looper.getMainLooper()).postDelayed({
                    solicitarAceptarUsuario(context, navController, dni, callback, intento + 1)
                }, 7000)
            } else {
                Toast.makeText(context, "No se pudo conectar con el servidor", Toast.LENGTH_LONG).show()
                callback("Error de red: ${t.message}")
            }
        }
    })
}
/*
solicitarAceptarUsuario(this,navController,"123456789S",{
    Log.d("CrearUsuario", it.toString())
},0)
 */