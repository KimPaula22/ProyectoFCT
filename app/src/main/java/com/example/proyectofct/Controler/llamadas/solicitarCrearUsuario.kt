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
 * Realiza una llamada a la API para crear un nuevo usuario.
 *
 * @param context El contexto de la aplicación.
 * @param navController El controlador de navegación para manejar la navegación.
 * @param usuarioRequest El objeto que contiene los datos del usuario a crear.
 * @param rol El rol del usuario a crear (por ejemplo, "usuario" o "administrador").
 * @param callback Callback que se llama con el resultado de la operación.
 * @param intento Número de reintentos en caso de fallos (poner 0 significa 7 intentos, si se pone 1, serán 6 intentos, etc.)
 */
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
                Toast.makeText(context, "No se pudo conectar con el servidor", Toast.LENGTH_LONG).show()
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