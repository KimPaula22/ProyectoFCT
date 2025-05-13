package com.example.proyectofct.Model

import androidx.collection.ObjectList
import com.example.proyectofct.Model.Equipo

class Responses {
    data class LoginResponse(
        val accessToken: String,
        val refreshToken: String
    )


    data class EquipoResponse(
        val mensaje: String,
        val equipo: Equipo
    )

    data class RefreshResponse(
        val accessToken: String,
    )

}