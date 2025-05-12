package com.example.proyectofct.Model.componentes

data class Ram (
    var id: Int,
    var ubicacion: Int?,
    var ubicacionEquipoId: Int?,
    var fechaRegistro: String?,
    var estado: String?, //'Operativo', 'Averiado','Sin instalar','Desconocido'
    var nota: String?,
    var nombreSerie: String?,
    var numeroSerie: String?,
    var capacidad: Int?,
    var frecuencia: Int?,
    var latencia: String?,
    var tipo: String?,
)