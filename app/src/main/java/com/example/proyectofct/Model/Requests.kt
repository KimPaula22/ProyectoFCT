package com.example.proyectofct.Model

class Requests {
    data class LoginRequest(
        val email: String,
        val contrasena: String
    )

    data class RefreshRequest(
        val refreshToken: String
    )


}