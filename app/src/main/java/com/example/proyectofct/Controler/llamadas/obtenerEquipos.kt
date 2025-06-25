package com.example.proyectofct.Controler.llamadas

import android.app.AlertDialog
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.navigation.NavController
import com.example.proyectofct.Controler.RetrofitClient
import com.example.proyectofct.MainActivity.Companion.tokenDatabaseManager
import com.example.proyectofct.Model.Equipo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

fun obtenerEquipos(
    context: Context,
    callback: (List<Equipo>?) -> Unit,
    navController: NavController,
    intento: Int = 0
) {
    var accessToken = tokenDatabaseManager?.getAccessToken()
    val authHeader = "Bearer $accessToken"

    RetrofitClient.instance.getEquipos(authHeader).enqueue(object : Callback<List<Equipo>> {
        override fun onResponse(call: Call<List<Equipo>>, response: Response<List<Equipo>>) {
            if (response.isSuccessful) {
                val equipos = response.body()
                //Formatear todas las fechas del equupo y sus componentes
                equipos?.forEach { equipo ->
                    equipo.fechaRegistro = formatearFecha(equipo.fechaRegistro)
                    equipo.placaBase.fechaRegistro = formatearFecha(equipo.placaBase.fechaRegistro)
                    equipo.cpu?.fechaRegistro = formatearFecha(equipo.cpu?.fechaRegistro)
                    equipo.gpu?.fechaRegistro = formatearFecha(equipo.gpu?.fechaRegistro)

                    equipo.rams?.forEach { ram ->
                        ram.fechaRegistro = formatearFecha(ram.fechaRegistro)
                    }

                    equipo.roms?.forEach { rom ->
                        rom.fechaRegistro = formatearFecha(rom.fechaRegistro)
                    }

                    equipo.pcis?.forEach { pci ->
                        pci.fechaRegistro = formatearFecha(pci.fechaRegistro)
                    }

                    equipo.dispositivosIO?.forEach { dispositivoIO ->
                        dispositivoIO.fechaRegistro = formatearFecha(dispositivoIO.fechaRegistro)
                    }
                }


                callback(equipos)
            } else if (response.code() == 403) {
                // Token inválido, intentar refrescar
                Log.d("Equipos", "Token inválido. Intentando refrescar...")
                refrescarToken(0) { mensaje ->
                    val nuevoToken = tokenDatabaseManager?.getAccessToken()
                    if (nuevoToken != null && nuevoToken.isNotEmpty()) {
                        // Reintenta con nuevo token
                        Log.d("Equipos", "Reintentando con nuevo token: $nuevoToken")
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

        override fun onFailure(call: Call<List<Equipo>>, t: Throwable) {
            Log.e("Equipos", "Error de red o en la llamada: ${t.message}")
            if (intento < 6) {
                Log.d("Equipos", "Intento $intento fallido. Reintentando...")
                Handler(Looper.getMainLooper()).postDelayed({
                obtenerEquipos(context, callback, navController, intento + 1)}, 7000)
            } else {
                Log.e("Equipos", "Error de red o en la llamada: ${t.message}")
                Toast.makeText(context, "No se pudo conectar con el servidor", Toast.LENGTH_LONG).show()
                callback(null)
            }
        }
    })
}

fun mostrarSesionCaducadaDialog(context: Context, navController: NavController) {
    AlertDialog.Builder(context)
        .setTitle("Sesión Caducada")
        .setMessage("Tu sesión ha expirado. Por favor, inicia sesión nuevamente.")
        .setCancelable(false)
        .setPositiveButton("Aceptar") { _, _ ->
            tokenDatabaseManager?.borrarTokens()
            navController.navigate("login") {
                popUpTo(0) { inclusive = true } // Limpia el backstack
            }
        }
        .show()

}

// EJEMPLO DE USO
/*
obtenerEquipos(this, { equipos ->
    if (equipos != null) {
        Log.d("Equipos", "Equipos obtenidos: $equipos")
        MainActivity.equipos.clear()
        MainActivity.equipos.addAll(equipos)
    } else {
        Log.d("Equipos", "No se pudieron obtener los equipos.")
    }
}, navController,0)
*/