package com.example.exemplosimplesdecompose.data

import java.io.Serializable

data class Posto(
    var nome: String,
    val coordenadas: Coordenadas
): Serializable {
    // Construtor secundário com coordenadas de Fortaleza
    constructor(nome: String) : this(nome, Coordenadas(41.40338, 2.17403))
}