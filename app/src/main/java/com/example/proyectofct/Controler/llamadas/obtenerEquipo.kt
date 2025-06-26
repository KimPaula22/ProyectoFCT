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
import com.example.proyectofct.Model.Responses
import com.example.proyectofct.Model.Ubicacion
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
/** * Realiza una llamada a la API para obtener un equipo específico por su ID.
 *
 * @param equipoId El ID del equipo a obtener.
 * @param context El contexto de la aplicación.
 * @param callback Callback que se llama con el resultado de la operación.
 * @param navController El controlador de navegación para manejar la navegación.
 * @param intento Número de reintentos en caso de fallos (poner 0 significa 7 intentos, si se pone 1, serán 6 intentos, etc.)
 */
fun obtenerEquipo(
    equipoId: Int,
    context: Context,
    callback: (Equipo?) -> Unit,
    navController: NavController,
    intento: Int = 0
) {
    val accessToken = tokenDatabaseManager?.getAccessToken()
    val authHeader = "Bearer $accessToken"

    RetrofitClient.instance.getEquipo(authHeader, equipoId)
        .enqueue(object : Callback<Responses.EquipoResponse> {
            override fun onResponse(
                call: Call<Responses.EquipoResponse>,
                response: Response<Responses.EquipoResponse>
            ) {
                if (response.isSuccessful) {
                    val equipoResponse = response.body()

                    val equipo = (equipoResponse?.placaBase)?.let {
                        val equipo = equipoResponse.descripcion?.let { it1 ->
                            Equipo(
                                id = equipoResponse.id,
                                nombre = equipoResponse.nombre ?: "Equipo $equipoResponse.id",
                                tipo = equipoResponse.tipo,
                                descripcion = it1,
                                fechaRegistro = formatearFecha(equipoResponse.fechaRegistro),
                                estado = equipoResponse.estado,
                                ubicacion = equipoResponse.ubicacion
                                    ?: Ubicacion(-1, "Desconocida", "Sin ubicación"),
                                placaBase = equipoResponse.placaBase.apply {
                                    fechaRegistro = formatearFecha(fechaRegistro)
                                },
                                cpu = equipoResponse.cpu?.apply {
                                    fechaRegistro = formatearFecha(fechaRegistro)
                                },
                                gpu = equipoResponse.gpu?.apply {
                                    fechaRegistro = formatearFecha(fechaRegistro)
                                },
                                rams = ArrayList(
                                    equipoResponse.rams?.onEach {
                                        it.fechaRegistro = formatearFecha(it.fechaRegistro)
                                    } ?: emptyList()
                                ),
                                roms = ArrayList(
                                    equipoResponse.roms?.onEach {
                                        it.fechaRegistro = formatearFecha(it.fechaRegistro)
                                    } ?: emptyList()
                                ),
                                pcis = ArrayList(
                                    equipoResponse.pcis?.onEach {
                                        it.fechaRegistro = formatearFecha(it.fechaRegistro)
                                    } ?: emptyList()
                                ),
                                dispositivosIO = ArrayList(
                                    equipoResponse.dispositivosIO?.onEach {
                                        it.fechaRegistro = formatearFecha(it.fechaRegistro)
                                    } ?: emptyList()
                                )
                            )
                        }
                        equipo
                    }

                    callback(equipo)

                } else if (response.code() == 403) {
                    Log.d("Equipo", "Token inválido. Intentando refrescar...")
                    refrescarToken(0) { _ ->
                        val nuevoToken = tokenDatabaseManager?.getAccessToken()
                        if (!nuevoToken.isNullOrEmpty()) {
                            Log.d("Equipo", "Reintentando con nuevo token: $nuevoToken")
                            obtenerEquipo(equipoId, context, callback, navController)
                        } else {
                            mostrarSesionCaducadaDialog(context, navController)
                            callback(null)
                        }
                    }
                } else {
                    Log.e("Equipo", "Error en la respuesta: ${response.code()}")
                    mostrarErrorDialog(context, "No se pudo obtener la información del equipo. Por favor, inténtalo de nuevo más tarde. (Código: ${response.code()})")
                    callback(null)
                }
            }

            override fun onFailure(call: Call<Responses.EquipoResponse>, t: Throwable) {
                Log.e("Equipo", "Error de red: ${t.localizedMessage}")
                if (intento < 6) {
                    Log.d("Equipo", "Intento $intento fallido. Reintentando...")
                    Handler(Looper.getMainLooper()).postDelayed({
                        obtenerEquipo(equipoId, context, callback, navController, intento + 1)
                    }, 7000)
                } else {
                    Toast.makeText(context, "No se pudo conectar con el servidor", Toast.LENGTH_LONG).show()
                    mostrarErrorDialog(context, "No se pudo conectar con el servidor. Por favor, revisa tu conexión a internet e inténtalo de nuevo.")
                    callback(null)
                }
            }
        })
}

fun mostrarErrorDialog(context: Context, mensaje: String) {
    AlertDialog.Builder(context)
        .setTitle("Error")
        .setMessage(mensaje)
        .setCancelable(false)
        .setPositiveButton("Aceptar", null)
        .show()
}

/*
obtenerEquipo(
equipoId = 1, this, { equipo ->
    if (equipo != null) {
        Log.d("Equipo", "Equipo obtenido: $equipo")
    } else {
        Log.d("Equipo", "No se pudo obtener el equipo.")
    }
}, navController,0
)
*/
