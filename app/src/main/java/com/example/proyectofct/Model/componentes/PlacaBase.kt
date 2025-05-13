package com.example.proyectofct.Model.componentes

data class PlacaBase (
    var id: Int,
    var ubicacionId: Int?,
    var ubicacionEquipoId: Int?,
    var fechaRegistro: String?,
    var estado: String?, //'Operativo', 'Averiado','Sin instalar','Desconocido'
    var nota: String?,
    var nombreSerie: String?,
    var numeroSerie: String?,
    var modelo: String?,
    var tipoSocket: String?,
    var chipset: String?,
    var memoriaMaxRam: Int?,
    var ranurasRam: Int?,
    var ranurasPci: Int?,
    var ranurasSata: Int?,
    var ranurasM2: Int?,
)