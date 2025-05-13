package com.example.proyectofct.Controler.llamadas

import android.util.Log
import com.example.proyectofct.Model.componentes.*
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*

fun traducirComponente(componente: Map<String, Any>): Pair<Int, Any>? {
    val gson = Gson()

    var resultado = when {
        "chipset" in componente -> Pair(0, gson.fromJson(gson.toJson(componente), PlacaBase::class.java))
        "nucleos" in componente -> Pair(1, gson.fromJson(gson.toJson(componente), Cpu::class.java))
        "memoriaVram" in componente -> Pair(2, gson.fromJson(gson.toJson(componente), Gpu::class.java))
        "latencia" in componente -> Pair(3, gson.fromJson(gson.toJson(componente), Ram::class.java))
        "tipo" in componente && "descripcion" in componente -> Pair(6, gson.fromJson(gson.toJson(componente), DispositivoIO::class.java))
        else -> null
    }

    if (resultado == null) {
        resultado = when {
            "capacidad" in componente && "tipo" !in componente -> Pair(4, gson.fromJson(gson.toJson(componente), Rom::class.java))
            else -> Pair(5, gson.fromJson(gson.toJson(componente), Pci::class.java))
        }
    }

    // Modificar la fecha antes de devolver el resultado
    val componenteConFechaFormateada = formatearFechaObjeto(resultado.second)

    return Pair(resultado.first, componenteConFechaFormateada)
}

//fun traducir componente de un componente de tipo any y no map
fun traducirComponente(componente: Any): Pair<Int, Any>? {
    val tipoTraducido = when (componente) {
        is PlacaBase -> 0
        is Cpu -> 1
        is Gpu -> 2
        is Ram -> 3
        is Rom -> 4
        is Pci -> 5
        is DispositivoIO -> 6
        else -> return null
    }

    return Pair(tipoTraducido, componente)
}






fun formatearFechaObjeto(objeto: Any): Any {
    // Usamos la reflexión para buscar el campo `fechaRegistro` y formatearlo
    val clazz = objeto::class
    val fechaRegistroPropiedad = clazz.members.find { it.name == "fechaRegistro" }

    // Verificamos si la propiedad existe y es de tipo String
    fechaRegistroPropiedad?.let {
        try {
            val valorFecha = it.call(objeto) as? String
            if (valorFecha != null) {
                // Formateamos la fecha al formato deseado
                val fechaFormateada = formatearFecha(valorFecha)
                if (fechaFormateada != null) {
                    // Asignamos la fecha formateada de nuevo al objeto
                    val propiedadFecha = clazz.members.find { m -> m.name == "fechaRegistro" } as? kotlin.reflect.KMutableProperty1<Any, String>
                    propiedadFecha?.set(objeto, fechaFormateada)
                } else {

                }
            } else { }
        } catch (e: Exception) {
            Log.e("Fecha", "Error al procesar fechaRegistro", e)
        }
    }

    return objeto
}

fun formatearFecha(fechaIso: String?): String {
    return try {
        val fecha = ZonedDateTime.parse(fechaIso ?: return "")
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy:HH:mm:ss")
        fecha.format(formatter)
    } catch (e: Exception) {
        ""
    }
}

