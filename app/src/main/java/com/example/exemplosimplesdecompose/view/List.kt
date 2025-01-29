package com.example.exemplosimplesdecompose.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
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
import org.json.JSONObject
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.nio.charset.StandardCharsets

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaDePostos(navController: NavHostController, posto:String) {
    val context= LocalContext.current
    val postoES = Posto("Posto na Espanha", Coordenadas(41.40338, 2.17403))
    val postoNY = Posto("Posto em NY", Coordenadas(40.7128, -74.0060))
    val postoN= Posto(posto,Coordenadas(41.40338, 2.17403))

    //val postoES = getGasStation(context)
    //saveGasStation(context,postoN)

   // val postoES = getGasStationSerializable(context)
   // saveGasStationSerializable(context,postoN)

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
fun saveGasStation(context: Context, posto: Posto){
    Log.v("PDM25","Salvando o posto")
    val sharedFileName="lastGasStation"
    var sp: SharedPreferences = context.getSharedPreferences(sharedFileName, Context.MODE_PRIVATE)
    var editor = sp.edit()
    editor.putString("nomeDoPosto",posto.nome)
    editor.putString("latitude",posto.coordenadas.latitude.toString())
    editor.putString("latitude",posto.coordenadas.longitude.toString())
    editor.apply()
}
fun getGasStation(context: Context):Posto{
    val sharedFileName="lastGasStation"
    var sp: SharedPreferences = context.getSharedPreferences(sharedFileName, Context.MODE_PRIVATE)
    var posto = Posto("Posto na Espanha", Coordenadas(41.40338, 2.17403))
    if(sp!=null) {
        posto.nome = sp.getString("nomeDoPosto", "").toString()
    }
    return posto
}
fun saveGasStationSerializable(context: Context, posto: Posto){
    Log.v("PDM25","Salvando o posto serializado")
    var dt= ByteArrayOutputStream()
    var oos = ObjectOutputStream(dt);
    oos.writeObject(posto);
    val sharedFileName="lastGasStationSer"
    var sp: SharedPreferences = context.getSharedPreferences(sharedFileName, Context.MODE_PRIVATE)
    var editor = sp.edit()
    var aux= dt.toString(StandardCharsets.ISO_8859_1.name())
    Log.v("PDM25",aux)
    editor.putString("posto1",aux)
    editor.apply()
}
fun getGasStationSerializable(context: Context):Posto{
    val sharedFileName="lastGasStationSer"
    var sp: SharedPreferences = context.getSharedPreferences(sharedFileName, Context.MODE_PRIVATE)
    var posto = Posto("PostoS", Coordenadas(41.40338, 2.17403))
    var aux: String
    if(sp!=null) {
        //Lendo bytes serializados
        aux = sp.getString("posto1", "").toString()
        Log.v("PDM25",aux)

        //Convertendo em objeto
        var bis: ByteArrayInputStream
        bis = ByteArrayInputStream(aux.toByteArray(Charsets.ISO_8859_1))
        var obi: ObjectInputStream
        obi = ObjectInputStream(bis)

        //lendo
        posto=obi.readObject() as Posto

    }
    return posto
}

fun postoToJson(posto: Posto): JSONObject {
    return JSONObject().apply {
        put("nome", posto.nome)
        put("latitude", posto.coordenadas.latitude)
        put("longitude", posto.coordenadas.longitude)
    }
}

fun saveGasStationJSON(context: Context, posto: Posto){
    Log.v("PDM25","Salvando o posto em JSON")
    val sharedFileName="lastGasStationJSON"
    var sp: SharedPreferences = context.getSharedPreferences(sharedFileName, Context.MODE_PRIVATE)
    var editor = sp.edit()
    val jsonObject = postoToJson(posto)
    Log.v("PDM",": "+jsonObject);
    editor.putString("postoJSON",jsonObject.toString())
    editor.apply()
}
fun getGasStationJSON(context: Context):Posto{
    val sharedFileName="lastGasStationSer"
    var sp: SharedPreferences = context.getSharedPreferences(sharedFileName, Context.MODE_PRIVATE)
    var posto = Posto("PostoS", Coordenadas(41.40338, 2.17403))
    var aux: String
    if(sp!=null) {
        //Lendo bytes serializados
        aux = sp.getString("posto1", "").toString()
        Log.v("PDM25",aux)

        //Convertendo em objeto
        var bis: ByteArrayInputStream
        bis = ByteArrayInputStream(aux.toByteArray(Charsets.ISO_8859_1))
        var obi: ObjectInputStream
        obi = ObjectInputStream(bis)

        //lendo
        posto=obi.readObject() as Posto

    }
    return posto
}

fun getListOfGasStation(context: Context):List<Posto>{
    val posto1 = Posto("Posto na Espanha", Coordenadas(41.40338, 2.17403))
    val posto2 = Posto("Posto em NY", Coordenadas(40.7128, -74.0060))
    val posto3= Posto("Posto em Fortaleza",Coordenadas(41.40338, 2.17403))

    return listOf(posto1, posto2, posto3)
}
fun addGasStation(context: Context, posto: Posto){

}