package com.example.proyectofct.Model

class Responses {
    data class LoginResponse(
        val mensaje: String,
        val rol: String
    )
    data class FeedResponse(
        val mensaje: String,
        val equipos: List<Equipo>
    )
    data class EquipoResponse(
        val mensaje: String,
        val equipo: Equipo
    )
}