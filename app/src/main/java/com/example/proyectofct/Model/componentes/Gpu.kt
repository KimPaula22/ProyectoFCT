package com.example.proyectofct.Model.componentes

data class Gpu (
    var id: Int,
    var ubicacionId: Int?,
    var ubicacionEquipoId: Int?,
    var fechaRegistro: String?,
    var estado: String?, //'Operativo', 'Averiado','Sin instalar','Desconocido'
    var nota: String?,
    var nombreSerie: String?,
    var numeroSerie: String?,
    var modelo: String?,
    var memoriaVram: Int?,
    var tipoMemoria: String?,
    var consumoWatts: Int?,
)