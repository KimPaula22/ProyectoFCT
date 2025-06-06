package com.example.proyectofct.Controler

import com.example.proyectofct.Model.Equipo
import com.example.proyectofct.Model.Responses.*
import com.example.proyectofct.Model.Requests.*
import com.example.proyectofct.Model.Usuario
import retrofit2.Call
import retrofit2.Response
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
    fun getEquipo(@Header("Authorization") authHeader: String, @Path("id") id: Int): Call<EquipoResponse>

    // Ruta para refrescar el token
    @POST("refresh")
    fun refreshToken(@Body request: RefreshRequest): Call<RefreshResponse>

    // Ruta para componentes/{estado}
    @POST("componentes/{estado}")
    fun getComponentes(@Header("Authorization") authHeader: String,@Path("estado") estado: String): Call<List<Map<String, Any>>>

    @POST("nuevo/usuario/{rol}")
    fun crearUsuario(@Header("Authorization") authHeader: String, @Path("rol") rol: String, @Body usuarioRequest: UsuarioRequest): Call<MensajeResponse>

    @POST("usuarios/aceptar/{id}")
    fun aceptarUsuario(@Header("Authorization") authHeader: String, @Path("id") id: String): Call<MensajeResponse>

    @POST("usuarios/rechazar/{id}")
    fun rechazarUsuario(@Header("Authorization") authHeader: String, @Path("id") id: String): Call<MensajeResponse>

    @POST("/miusuario")
    fun getMisDatos(@Header("Authorization") token: String): Call<Usuario>
}


