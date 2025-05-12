package com.example.proyectofct.Controler.llamadas

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import androidx.navigation.NavController
import com.example.proyectofct.Controler.RetrofitClient
import com.example.proyectofct.MainActivity.Companion.tokenDatabaseManager
import com.example.proyectofct.Model.EquipoN
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

fun obtenerEquipos(
    context: Context,
    callback: (List<EquipoN>?) -> Unit,
    navController: NavController
) {
    var accessToken = tokenDatabaseManager?.getAccessToken()
    val authHeader = "Bearer $accessToken"

    RetrofitClient.instance.getEquipos(authHeader).enqueue(object : Callback<List<EquipoN>> {
        override fun onResponse(call: Call<List<EquipoN>>, response: Response<List<EquipoN>>) {
            if (response.isSuccessful) {
                val equipos = response.body()
                Log.d("Equipos", "Equipos recibidos: $equipos")
                callback(equipos)
            } else if (response.code() == 403) {
                // Token inválido, intentar refrescar
                refrescarToken(0) { mensaje ->
                    val nuevoToken = tokenDatabaseManager?.getAccessToken()
                    if (nuevoToken != null && nuevoToken.isNotEmpty()) {
                        // Reintenta con nuevo token
                        obtenerEquipos(context, callback, navController)
                    } else {
                        // No se pudo refrescar: sesión caducada
                        mostrarSesionCaducadaDialog(context, navController)
                        callback(null)
                    }
                }
            } else {
                Log.e("Equipos", "Error en la respuesta: ${response.code()} ${response.message()}")
                callback(null)
            }
        }

        override fun onFailure(call: Call<List<EquipoN>>, t: Throwable) {
            Log.e("Equipos", "Error de red o en la llamada: ${t.message}")
            callback(null)
        }
    })
}

fun mostrarSesionCaducadaDialog(context: Context, navController: NavController) {
    AlertDialog.Builder(context)
        .setTitle("Sesión Caducada")
        .setMessage("Tu sesión ha expirado. Por favor, inicia sesión nuevamente.")
        .setCancelable(false)
        .setPositiveButton("Aceptar") { _, _ ->
            navController.navigate("login") {
                popUpTo(0) { inclusive = true } // Limpia el backstack
            }
        }
        .show()

}

// EJEMPLO DE USO
/*
obtenerEquipos(context, { equipos ->
    if (equipos != null) {
        Log.d("Equipos", "Equipos obtenidos: $equipos")
        MainActivity.equipos.clear()
        MainActivity.equipos.addAll(equipos)
    } else {
        Log.d("Equipos", "No se pudieron obtener los equipos.")
    }
}, navController)
*/