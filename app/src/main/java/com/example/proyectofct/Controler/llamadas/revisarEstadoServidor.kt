package com.example.proyectofct.Controler.llamadas

import android.util.Log
import com.example.proyectofct.Controler.RetrofitClient
import com.example.proyectofct.Controler.recibirMensajeDeError
import com.example.proyectofct.MainActivity.Companion.tokenDatabaseManager
import com.example.proyectofct.Model.EstadoServidor
import com.example.proyectofct.Model.Requests.ValidateRefreshRequest
import com.example.proyectofct.Model.Responses.ValidateRefreshResponse
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Revisa el estado del servidor usando validateRefreshToken
 * @param refreshToken Token de refresco para validar (puede ser vacío)
 * @param intentos Número de reintentos en caso de fallos (poner 0 significa 5 intentos, si se pone 1, serán 6 intentos, etc.)
 * @return Estado del servidor (true = operativo, false = token inválido, null = error de conexión)
 */
@OptIn(DelicateCoroutinesApi::class)
fun revisarEstadoServidor(
    intentos: Int = 0,
    estadoServidor: (EstadoServidor) -> Unit
) {
    val refreshToken = tokenDatabaseManager?.getRefreshToken() ?: ""
    try {
        RetrofitClient.instance.validateRefreshToken(ValidateRefreshRequest(refreshToken))
            .enqueue(object : Callback<ValidateRefreshResponse> {
                override fun onResponse(
                    call: Call<ValidateRefreshResponse>,
                    response: Response<ValidateRefreshResponse>
                ) {
                    when (response.code()) {
                        200 -> {
                            // Respuesta correcta
                            val esValido = response.body()?.valido ?: false
                            val mensaje =
                                if (esValido) "Token válido" else "Token no válido pero servidor operativo"
                            Log.d("ESTADO_SERVIDOR", mensaje)
                            estadoServidor(EstadoServidor(true, mensaje))
                        }
                        400 -> {
                            // Error 400 - Token no válido pero servidor operativo
                            Log.d("ESTADO_SERVIDOR", "Token no válido")
                            estadoServidor(EstadoServidor(false, "Token no válido"))
                        }
                        503 -> {
                            // Error 503 - Servidor no disponible temporalmente
                            val mensaje = "No se ha podido conectar con el servidor. Intente más tarde."
                            Log.e("ESTADO_SERVIDOR", mensaje)
                            estadoServidor(EstadoServidor(null, mensaje))
                        }
                        else -> {
                            // Otros errores HTTP
                            val mensaje = "Error HTTP: ${response.code()}"
                            Log.e("ESTADO_SERVIDOR", mensaje)
                            estadoServidor(EstadoServidor(null, mensaje))
                        }
                    }
                }

                override fun onFailure(call: Call<ValidateRefreshResponse>, t: Throwable) {
                    Log.e("ESTADO_SERVIDOR", "Error de red: ${t.message}")
                    if (intentos < 6) {
                        // Reintentar tras un pequeño delay
                        GlobalScope.launch {
                            delay(2000)
                            revisarEstadoServidor(intentos + 1, estadoServidor)
                        }
                    } else {
                        // Máximo de intentos alcanzado
                        val mensaje = recibirMensajeDeError(t) + " Intentos agotados."
                        Log.e("ESTADO_SERVIDOR", mensaje)
                        estadoServidor(EstadoServidor(null, mensaje))
                    }
                }
            })
    } catch (e: Exception) {
        if (intentos < 6) {
            GlobalScope.launch {
                delay(2000)
                revisarEstadoServidor(intentos + 1, estadoServidor)
            }
        } else {
            estadoServidor(EstadoServidor(null, "Demasiados intentos fallidos: ${e.message}"))
        }
    }
}
