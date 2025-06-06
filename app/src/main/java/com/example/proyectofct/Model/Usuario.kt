package com.example.proyectofct.Model

data class Usuario(
    var id: Int,
    var nombre: String,
    var apellidos: String,
    var rol: String,
    var email: String,
    val aceptado: Boolean
)
