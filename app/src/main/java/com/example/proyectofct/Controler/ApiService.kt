package com.example.proyectofct.Controler

import com.example.proyectofct.Model.Responses.*
import com.example.proyectofct.Model.Requests.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    // Ruta para el inicio de sesión
    @POST("login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    // Ruta para obtener los equipos
    @POST("feed")
    fun getEquipos(@Body request: LoginRequest): Call<FeedResponse>

    // Ruta para obtener un equipo en particular
    @POST("feed/{id}")
    fun getEquipo(@Body request: LoginRequest, @Path("id") id: Int): Call<EquipoResponse>
}


