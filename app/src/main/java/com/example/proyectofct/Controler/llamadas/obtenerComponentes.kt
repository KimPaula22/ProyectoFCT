package com.example.proyectofct.Controler.llamadas

import android.app.AlertDialog
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.navigation.NavController
import com.example.proyectofct.Controler.RetrofitClient
import com.example.proyectofct.MainActivity.Companion.componentes
import com.example.proyectofct.MainActivity.Companion.tokenDatabaseManager
import com.example.proyectofct.Model.Responses
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Traduce un componente del servidor a un objeto Pair con el numero del tipo del componente y el objeto traducido.
 * @param componente El componente a traducir.
 * @return Un Pair con el numero del tipo del componente y el objeto traducido, o null si no se puede traducir.
 */
fun obtenerComponentes(
    context: Context,
    callback: (List<Pair<Int, Any>>?) -> Unit, // Cambiado a List<Any> para devolver los objetos ya traducidos
    navController: NavController,
    estado: String,
    intento: Int = 0
) {
    val accessToken = tokenDatabaseManager?.getAccessToken()
    Log.d("Token", "Token obtenido: $accessToken")
    val authHeader = "Bearer $accessToken"

    RetrofitClient.instance.getComponentes(authHeader, estado).enqueue(object :
        Callback<List<Map<String, Any>>> {

        override fun onResponse(
            call: Call<List<Map<String, Any>>>,
            response: Response<List<Map<String, Any>>>
        ) {
            if (response.isSuccessful) {
                val componentes = response.body()
                val componentesTraducidos = mutableListOf<Pair<Int, Any>>() // lista mutable de pares

                componentes?.forEach { componente ->
                    val casted = componente as? Map<String, Any>
                    val traduccion = casted?.let { traducirComponente(it) }
                    traduccion?.let { componentesTraducidos.add(it) }
                }

                callback(componentesTraducidos)
            } else if (response.code() == 403) {
                Log.d("Componentes", "Token inválido. Intentando refrescar...")
                refrescarToken(0) { _ ->
                    val nuevoToken = tokenDatabaseManager?.getAccessToken()
                    if (!nuevoToken.isNullOrEmpty()) {
                        Log.d("Componentes", "Reintentando con nuevo token: $nuevoToken")
                        obtenerComponentes(context, callback, navController, estado)
                    } else {
                        mostrarSesionCaducadaDialog(context, navController)
                        callback(null)
                    }
                }
            } else {
                Log.e("Componentes", "Error HTTP ${response.code()}: ${response.message()}")
                mostrarErrorDialog(context, "No se pudieron obtener los componentes. Por favor, inténtalo de nuevo más tarde. (Código: ${response.code()})")
                callback(null)
            }
        }

        override fun onFailure(call: Call<List<Map<String, Any>>>, t: Throwable) {
            if (intento < 6) {
                Log.d("Componentes", "Error de red o en la llamada: ${t.message}")
                Handler(Looper.getMainLooper()).postDelayed({
                    Log.d("Componentes", "Reintentando...")
                    obtenerComponentes(context, callback, navController, estado, intento + 1)
                }, 7000)
            } else {
                Log.e("Componentes", "Error de red o en la llamada: ${t.message}")
                mostrarErrorDialog(context, "No se pudo conectar con el servidor. Por favor, revisa tu conexión a internet e inténtalo de nuevo.")
                callback(null)
            }
        }
    })
}

// EJEMPLO DE USO obtenerComponentes Y traducirComponenetes
/*

obtenerComponentes(this, { componentesResponse ->
    if (componentesResponse != null) {
        // Agregar los componentes a la lista
        componentesResponse.forEach { componente ->
            componentes.add(componente)
        }

        // Log después de haber agregado los componentes
        Log.d("Componentes", "Lista de componentes en MainActivity: ${componentes.size}")

        // Traducir y formatear cada componente
        componentes.forEach { componente ->
            val componentetraducido = traducirComponente(componente)
            if (componentetraducido != null) {
                Log.d("Componentes", "Componente traducido y formateado: ${componentetraducido.first} ${componentetraducido.second}")
            }
        }
    } else {
        Log.d("Componentes", "No se pudieron obtener los componentes.")
    }
}, navController, "Operativo")

 */