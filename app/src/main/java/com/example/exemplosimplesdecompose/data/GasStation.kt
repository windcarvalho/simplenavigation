package com.example.exemplosimplesdecompose.data

import java.io.Serializable

data class GasStation(
    var name: String,
    val coord: Coordinates
): Serializable {
    // Construtor secundário com coordenadas de Fortaleza
    constructor(nome: String) : this(nome, Coordinates(41.40338, 2.17403))
}