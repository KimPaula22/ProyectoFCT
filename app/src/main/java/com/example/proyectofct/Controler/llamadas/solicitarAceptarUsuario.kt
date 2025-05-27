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
                refrescarToken(0) { mensaje ->
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
            if (intento < 3) {
                Handler(Looper.getMainLooper()).postDelayed({
                    solicitarAceptarUsuario(context, navController, dni, callback, intento + 1)
                }, 7000)
            } else {
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