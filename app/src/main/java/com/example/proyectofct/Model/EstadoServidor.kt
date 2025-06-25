package com.example.proyectofct.Model

/**
 * Estado del servidor
 * @param esValido Indica si la respuesta es válida
 * @param mensaje Mensaje descriptivo del estado
 */
data class EstadoServidor(
    val esValido: Boolean? = null,
    val mensaje: String
)
