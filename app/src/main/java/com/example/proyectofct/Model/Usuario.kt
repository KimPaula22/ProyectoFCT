package com.example.proyectofct.Model

data class Usuario(
    var id: Int,
    var nombre: String,
    var email: String,
    val aprobado: Boolean = false
)