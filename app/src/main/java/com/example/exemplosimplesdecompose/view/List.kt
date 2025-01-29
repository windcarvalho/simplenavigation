package com.example.exemplosimplesdecompose.view

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.exemplosimplesdecompose.data.Coordenadas
import com.example.exemplosimplesdecompose.data.Posto

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaDePostos(navController: NavHostController, nomeDoPosto: String) {
    val context= LocalContext.current
    val postoES = Posto("Posto na Espanha", Coordenadas(41.40338, 2.17403))
    val postoNY = Posto("Posto em NY", Coordenadas(40.7128, -74.0060))
    val postoN= Posto("$nomeDoPosto")


    val postos = listOf(
        "$nomeDoPosto",
        "Outro posto",
        "Mais um posto"
    )
    val postosComp = listOf(postoN, postoES, postoNY)
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lista de Postos") }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(postosComp) { item ->
                Card(
                    onClick = {

                        //Abrir Mapa
                        // Cria o Intent para abrir o Google Maps
                        val gmmIntentUri = Uri.parse("geo:${item.coordenadas.latitude},${item.coordenadas.longitude}")
                        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri).apply {
                            setPackage("com.google.android.apps.maps") // Garante que o Maps será usado
                        }
                        context.startActivity(mapIntent)



                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                ) {
                    Box(Modifier.fillMaxSize()) {
                        Text(
                            text = item.nome,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
        }
    }
}