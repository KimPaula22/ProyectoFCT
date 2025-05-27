package com.example.proyectofct.Model

class Requests {
    data class LoginRequest(
        val email: String,
        val contrasena: String
    )

    data class RefreshRequest(
        val refreshToken: String
    )

    data class UsuarioRequest(
        val email: String,
        val contrasena: String,
        val nombre: String,
        val apellido: String,
        val dni: String
    )

    data class MensajeResponse(
        val mensaje: String
    )


}