package com.example.proyectofct.Controler.llamadas

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.navigation.NavController
import com.example.proyectofct.Controler.RetrofitClient
import com.example.proyectofct.MainActivity.Companion.tokenDatabaseManager
import com.example.proyectofct.Model.Requests
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

fun solicitarCrearUsuario(
    context: Context,
    navController: NavController,
    usuarioRequest: Requests.UsuarioRequest,
    rol: String,
    callback: (String?) -> Unit,
    intento: Int = 0
) {
    val accessToken = tokenDatabaseManager?.getAccessToken()
    val authHeader = "Bearer $accessToken"

    RetrofitClient.instance.crearUsuario(authHeader, rol, usuarioRequest).enqueue(object : Callback<Requests.MensajeResponse> {
        override fun onResponse(call: Call<Requests.MensajeResponse>, response: Response<Requests.MensajeResponse>) {
            if (response.isSuccessful) {
                callback(response.body()?.mensaje)
            } else if (response.code() == 403) {
                Log.d("CrearUsuario", "Token inválido. Intentando refrescar...")
                refrescarToken(0) {
                    Log.d("CrearUsuario", "Reintentando con nuevo token: $it")
                    val nuevoToken = tokenDatabaseManager?.getAccessToken()
                    if (!nuevoToken.isNullOrEmpty()) {
                        solicitarCrearUsuario(context, navController, usuarioRequest, rol, callback)
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
                    solicitarCrearUsuario(context, navController, usuarioRequest, rol, callback, intento + 1)
                }, 7000)
            } else {
                callback("Error de red: ${t.message}")
            }
        }
    })
}

/*
solicitarCrearUsuario(this, navController,
Requests.UsuarioRequest("juan.perez@example.com", null.toString(),"Juan", "Perez", "123456"),"usuario", { mensaje ->
    if (mensaje != null) {
        Log.d("CrearUsuario", mensaje)
    }
}, 0
 */