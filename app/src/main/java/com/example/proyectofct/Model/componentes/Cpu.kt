package com.example.proyectofct.Model.componentes

data class Cpu(
    var id: Int,
    var ubicacion: Int?,
    var ubicacionEquipoId: Int?,
    var fechaRegistro: String?,
    var estado: String?, //'Operativo', 'Averiado','Sin instalar','Desconocido'
    var nota: String?,
    var nombreSerie: String?,
    var numeroSerieCpu: String?,
    var modelo: String?,
    var arquitectura: String?,
    var frecuenciaGhz: Double?,
    var nucleos: Int?,
    var hilos: Int?,
    var tdpWatts: Int?,
    var socket: String?
)