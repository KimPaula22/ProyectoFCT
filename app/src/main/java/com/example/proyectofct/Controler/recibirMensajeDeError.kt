package com.example.proyectofct.Controler

import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLHandshakeException
import com.google.gson.JsonParseException
import com.google.gson.stream.MalformedJsonException

fun recibirMensajeDeError(t: Throwable): String {
    return when (t) {
        is UnknownHostException -> "No se pudo conectar al servidor. Verifica tu conexión a internet."
        is SocketTimeoutException -> "Tiempo de espera agotado. El servidor no respondió a tiempo.Por favor, inténtalo de nuevo"
        is IOException -> "Error de red. Asegúrate de estar conectado a internet."
        is SSLHandshakeException -> "Fallo en la seguridad de la conexión. Intenta más tarde."
        is JsonParseException, is MalformedJsonException -> "Error procesando la respuesta del servidor."
        is IllegalStateException -> "Se recibió una respuesta inesperada del servidor.Por favor, inténtalo de nuevo"
        else -> "Ocurrió un error inesperado: ${t.localizedMessage ?: "Sin mensaje"}"
    }
}