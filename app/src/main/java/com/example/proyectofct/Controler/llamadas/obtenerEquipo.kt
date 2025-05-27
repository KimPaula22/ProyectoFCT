package com.example.proyectofct.Controler.llamadas

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
                    refrescarToken(0) { _ ->
                        val nuevoToken = tokenDatabaseManager?.getAccessToken()
                        if (!nuevoToken.isNullOrEmpty()) {
                            obtenerEquipo(equipoId, context, callback, navController)
                        } else {
                            mostrarSesionCaducadaDialog(context, navController)
                            callback(null)
                        }
                    }
                } else {
                    Log.e("Equipo", "Error en la respuesta: ${response.code()}")
                    callback(null)
                }
            }

            override fun onFailure(call: Call<Responses.EquipoResponse>, t: Throwable) {
                Log.e("Equipo", "Error de red: ${t.localizedMessage}")
                if (intento < 3) {
                    Handler(Looper.getMainLooper()).postDelayed({
                        obtenerEquipo(equipoId, context, callback, navController, intento + 1)
                    }, 7000)
                } else {
                    Toast.makeText(context, "No se pudo conectar con el servidor", Toast.LENGTH_LONG).show()
                    callback(null)
                }
            }
        })
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
