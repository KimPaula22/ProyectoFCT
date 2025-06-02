package com.example.proyectofct.Model

import com.example.proyectofct.Model.componentes.*

//version nueva (refactoriza el nombre de la clase y elimina o comenta al antigua)
data class Equipo(
    var id: Int,
    var nombre: String,
    var descripcion: String = "",
    var fechaRegistro: String,
    var estado: String, //'Disponible','En uso', 'Prestado', 'Averiado','Otro', 'Desconocido'
    var ubicacion: Ubicacion,
    var placaBase: PlacaBase,
    var cpu: Cpu?= null,
    var gpu: Gpu?= null,
    var rams: ArrayList<Ram>? = emptyList<Ram>() as ArrayList<Ram>?,
    var roms: ArrayList<Rom>? = emptyList<Rom>() as ArrayList<Rom>?,
    var pcis: ArrayList<Pci>? = emptyList<Pci>() as ArrayList<Pci>?,
    var dispositivosIO: ArrayList<DispositivoIO>? = emptyList<DispositivoIO>() as ArrayList<DispositivoIO>?,
    var usuario: Usuario? = null
    )

//version anterior
/*data class Equipo(
    var nombre: String,
    var estado: String,

    var ram: String = "",
    var cpu: String = "",
    var gpu: String = "",
    var ubicacion: String
)*/