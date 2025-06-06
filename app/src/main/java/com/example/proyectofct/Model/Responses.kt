package com.example.proyectofct.Model

import androidx.collection.ObjectList
import com.example.proyectofct.Model.Equipo
import com.example.proyectofct.Model.componentes.Cpu
import com.example.proyectofct.Model.componentes.DispositivoIO
import com.example.proyectofct.Model.componentes.Gpu
import com.example.proyectofct.Model.componentes.Pci
import com.example.proyectofct.Model.componentes.PlacaBase
import com.example.proyectofct.Model.componentes.Ram
import com.example.proyectofct.Model.componentes.Rom

class Responses {
    data class LoginResponse(
        val accessToken: String,
        val refreshToken: String
    )


    data class EquipoResponse(
        val id: Int,
        val nombre: String?,
        val tipo: String,
        val descripcion: String? = "",
        val fechaRegistro: String,
        val estado: String,
        val ubicacion: Ubicacion?,
        val placaBase: PlacaBase?,
        val cpu: Cpu?,
        val gpu: Gpu?,
        val rams: List<Ram>?,
        val roms: List<Rom>?,
        val pcis: List<Pci>?,
        val dispositivosIO: List<DispositivoIO>?
    )


    data class RefreshResponse(
        val accessToken: String,
    )

}