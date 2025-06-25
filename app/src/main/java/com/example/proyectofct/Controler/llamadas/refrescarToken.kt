package com.example.proyectofct.Controler.llamadas

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.proyectofct.Controler.RetrofitClient
import com.example.proyectofct.Controler.recibirMensajeDeError
import com.example.proyectofct.MainActivity.Companion.tokenDatabaseManager
import com.example.proyectofct.Model.Requests
import com.example.proyectofct.Model.Responses
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Realiza una llamada a la API para refrescar el token de acceso utilizando el refresh token almacenado.
 *
 * @param intentos Número de reintentos en caso de fallos (poner 0 significa 7 intentos, si se pone 1, serán 6 intentos, etc.)
 * @param onResultado Callback que se llama con el resultado de la operación.
 */
fun refrescarToken(intentos: Int = 0, onResultado: (mensaje: String) -> Unit) {
    // Obtener el refresh token almacenado en la base de datos
    val refreshToken = tokenDatabaseManager?.getRefreshToken()

    if (refreshToken != null) {
        val refreshRequest = Requests.RefreshRequest(refreshToken)

        RetrofitClient.instance.refreshToken(refreshRequest).enqueue(object : Callback<Responses.RefreshResponse> {
            override fun onResponse(call: Call<Responses.RefreshResponse>, response: Response<Responses.RefreshResponse>) {
                if (response.isSuccessful) {
                    val newTokens = response.body()
                    if (newTokens?.accessToken != null) {
                        // Guardar el nuevo accessToken en la base de datos
                        tokenDatabaseManager?.guardarTokens(newTokens.accessToken, refreshToken)

                        // Pasar el nuevo accessToken al callback
                        onResultado("Token refrescado exitosamente")
                    } else {
                        onResultado("Error al obtener el nuevo accessToken.")
                        tokenDatabaseManager?.guardarTokens("", "")
                    }
                } else {
                    onResultado("Error HTTP: ${response.code()} - ${response.errorBody()?.string()}")
                    tokenDatabaseManager?.guardarTokens("", "")
                }
            }

            override fun onFailure(call: Call<Responses.RefreshResponse>, t: Throwable) {
                Log.e("REFRESH", "Error de red: ${t.message}")
                if (intentos < 6) {
                    val mensajeError = recibirMensajeDeError(t)
                    Handler(Looper.getMainLooper()).postDelayed({
                    refrescarToken(intentos + 1, onResultado)}, 7000)
                    Log.d("REFRESH", "Intento ${intentos + 1} fallido. Reintentando... $mensajeError")
                } else {
                    onResultado(recibirMensajeDeError(t) + " Intentos agotados.")
                    tokenDatabaseManager?.guardarTokens("", "")
                }
            }
        })
    } else {
        onResultado("No se encontró el refreshToken en la base de datos.")
    }
}


