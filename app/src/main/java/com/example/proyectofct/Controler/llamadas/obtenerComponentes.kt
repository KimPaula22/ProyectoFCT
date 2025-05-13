package com.example.proyectofct.Controler.llamadas

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

fun obtenerComponentes(
    context: Context,
    callback: (List<Any>?) -> Unit, // Cambiado a List<Any> para devolver los objetos ya traducidos
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
                val componentesTraducidos = mutableListOf<Any>()

                componentes?.forEach { componente ->
                    val casted = componente as? Map<String, Any>
                    val traduccion = casted?.let { traducirComponente(it) }?.second
                    traduccion?.let { componentesTraducidos.add(it) }
                }

                callback(componentesTraducidos)
            } else if (response.code() == 403) {
                refrescarToken(0) { _ ->
                    val nuevoToken = tokenDatabaseManager?.getAccessToken()
                    if (!nuevoToken.isNullOrEmpty()) {
                        obtenerComponentes(context, callback, navController, estado)
                    } else {
                        mostrarSesionCaducadaDialog(context, navController)
                        callback(null)
                    }
                }
            } else {
                Log.e("Componentes", "Error HTTP ${response.code()}: ${response.message()}")
                callback(null)
            }
        }

        override fun onFailure(call: Call<List<Map<String, Any>>>, t: Throwable) {
            if (intento < 3) {
                Log.d("Componentes", "Error de red o en la llamada: ${t.message}")
                Handler(Looper.getMainLooper()).postDelayed({
                    obtenerComponentes(context, callback, navController, estado, intento + 1)
                }, 7000)
            } else {
                Log.e("Componentes", "Error de red o en la llamada: ${t.message}")
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