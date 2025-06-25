package com.example.proyectofct.Controler.llamadas

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.proyectofct.Controler.RetrofitClient
import com.example.proyectofct.Controler.recibirMensajeDeError
import com.example.proyectofct.MainActivity.Companion.tokenDatabaseManager
import com.example.proyectofct.Model.Responses
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Cierra la sesión del usuario actual en todos los dispositivos
 * @param intentos Número de reintentos en caso de fallos (poner 0 significa 7 intentos, si se pone 1, serán 6 intentos, etc.)
 * @param onResultado Callback con el resultado de la operación
 */
fun cerrarSesionTodos(
    intentos: Int = 0,
    onResultado: (mensaje: String) -> Unit
) {
    Log.d("LOGOUT_ALL", "Intentando cerrar sesión en todos los dispositivos. Intento: $intentos")
    // Obtener token de acceso
    val accessToken = tokenDatabaseManager?.getAccessToken()

    // Petición al servidor para cerrar sesión en todos los dispositivos
    RetrofitClient.instance.logoutAll("Bearer $accessToken")
        .enqueue(object : Callback<Responses.LogoutResponse> {
            override fun onResponse(call: Call<Responses.LogoutResponse>, response: Response<Responses.LogoutResponse>) {
                if (response.isSuccessful) {
                    // Respuesta exitosa
                    val logoutResponse = response.body()
                    val mensaje = logoutResponse?.mensaje ?: "Sesión cerrada en todos los dispositivos"
                    tokenDatabaseManager?.borrarTokens()
                    Log.d("LOGOUT_ALL", mensaje)
                    onResultado(mensaje)
                } else if (response.code() == 401) {
                    // Error de autenticación
                    Log.e("LOGOUT_ALL", "Token inválido o expirado")
                    onResultado("Sesión expirada o inválida")
                    tokenDatabaseManager?.borrarTokens()
                } else if (response.code() == 403) {
                    // Error de permisos
                    val errorJson = response.errorBody()?.string()
                    val mensaje = try {
                        JSONObject(errorJson ?: "").getString("mensaje")
                    } catch (e: Exception) {
                        "Acceso denegado. No tienes permisos para cerrar todas las sesiones."
                    }
                    Log.e("LOGOUT_ALL", "Acceso denegado: $mensaje")
                    onResultado(mensaje)
                } else {
                    // Otros errores HTTP
                    Log.e("LOGOUT_ALL", "Error HTTP: ${response.code()} - ${response.errorBody()?.string()}")
                    onResultado("Error al cerrar las sesiones: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Responses.LogoutResponse>, t: Throwable) {
                Log.e("LOGOUT_ALL", "Error de red: ${t.message}")

                if (intentos < 6) {
                    // Reintentar
                    Log.d("LOGOUT_ALL", "Intento ${intentos + 1} fallido. Reintentando...")
                    Handler(Looper.getMainLooper()).postDelayed({
                        cerrarSesionTodos(intentos + 1, onResultado)
                    }, 7000)
                } else {
                    // Máximo de intentos alcanzado
                    val mensaje = recibirMensajeDeError(t) + " Intentos agotados."
                    Log.e("LOGOUT_ALL", mensaje)
                    tokenDatabaseManager?.borrarTokens()
                    onResultado(mensaje)
                }
            }
        })
}

//EJEMPLO DE USO
/*
cerrarSesionTodos(0) { mensaje ->
    Log.d("LOGOUT_ALL", mensaje)
}
*/
