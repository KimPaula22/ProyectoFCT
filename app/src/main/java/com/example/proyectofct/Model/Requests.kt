package com.example.proyectofct.Model

class Requests {
    data class LoginRequest(
        val correo: String,
        val contrasenia: String
    )
}