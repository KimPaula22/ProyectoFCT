package com.example.proyectofct.Model.componentes

data class DispositivoIO (
    var id: Int,
    var ubicacion: Int?,
    var ubicacionEquipoId: Int?,
    var fechaRegistro: String?,
    var estado: String?, //'Operativo', 'Averiado','Sin instalar','Desconocido'
    var nota: String?,
    var marca: String?,
    var modelo: String?,
    var tipo: String?, //'Entrada', 'Salida', 'Entrada/Salida'
    var descripcion: String?,
)