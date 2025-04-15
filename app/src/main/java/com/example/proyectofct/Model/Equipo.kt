package com.example.proyectofct.Model

data class Equipo(
    val id: Int,
    val nombreequipo: String,
    val estado: String,
    val cpu: String,
    val gpu: String,
    val ram: String,
    val rom: List<String>?,
    val dispositivos_pcie: List<String>?
)