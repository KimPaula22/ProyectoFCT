package com.example.proyectofct.Controler

import com.example.proyectofct.Model.Equipo
import com.example.proyectofct.Model.Responses.*
import com.example.proyectofct.Model.Requests.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    // Ruta para el inicio de sesión
    @POST("login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    // Ruta para obtener los equipos
    @POST("equipos")
    fun getEquipos(@Header("Authorization") token: String): Call<List<Equipo>>


    // Ruta para obtener un equipo en particular
    @POST("equipo/{id}")
    fun getEquipo(@Body request: LoginRequest, @Path("id") id: Int): Call<EquipoResponse>

    // Ruta para refrescar el token
    @POST("refresh")
    fun refreshToken(@Body request: RefreshRequest): Call<RefreshResponse>

    // Ruta para componentes/{estado}
    @POST("componentes/{estado}")
    fun getComponentes(@Header("Authorization") authHeader: String,@Path("estado") estado: String): Call<List<Map<String, Any>>>


}


