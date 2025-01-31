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
import com.example.exemplosimplesdecompose.data.Coordinates
import com.example.exemplosimplesdecompose.data.GasStation
import org.json.JSONObject
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.nio.charset.StandardCharsets

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListofGasStations(navController: NavHostController, posto:String) {
    val context= LocalContext.current
   // val gasES = GasStation("Posto na Espanha", Coordenadas(41.40338, 2.17403))
    val gasNY = GasStation("Posto em NY", Coordinates(40.7128, -74.0060))
    val gasN= GasStation(posto,Coordinates(41.40338, 2.17403))

   // val gasES = getGasStation(context)
   // saveGasStation(context,gasN)

   // val gasES = getGasStationSerializable(context)
    //saveGasStationSerializable(context,gasN)

    val gasES = getGasStationJSON(context)
    saveGasStationJSON(context,gasN)

    val postosComp = listOf(gasN, gasES, gasNY)
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
                        val gmmIntentUri = Uri.parse("geo:${item.coord.lat},${item.coord.lgt}")
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
                            text = item.name,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
        }
    }
}
fun saveGasStation(context: Context, gasStation: GasStation){
    Log.v("PDM25","Salvando o posto")
    val sharedFileName="lastGasStation"
    var sp: SharedPreferences = context.getSharedPreferences(sharedFileName, Context.MODE_PRIVATE)
    var editor = sp.edit()
    editor.putString("nomeDoPosto",gasStation.name)
    editor.putString("latitude",gasStation.coord.lat.toString())
    editor.putString("latitude",gasStation.coord.lgt.toString())
    editor.apply()
}
fun getGasStation(context: Context):GasStation{
    val sharedFileName="lastGasStation"
    var sp: SharedPreferences = context.getSharedPreferences(sharedFileName, Context.MODE_PRIVATE)
    var gasStation = GasStation("Posto na Espanha", Coordinates(41.40338, 2.17403))
    if(sp!=null) {
        gasStation.name = sp.getString("nomeDoPosto", "").toString()
    }
    return gasStation
}
fun saveGasStationSerializable(context: Context, gasStation: GasStation){
    Log.v("PDM25","Salvando o posto serializado")
    var dt= ByteArrayOutputStream()
    var oos = ObjectOutputStream(dt);
    oos.writeObject(gasStation);
    val sharedFileName="lastGasStationSer"
    var sp: SharedPreferences = context.getSharedPreferences(sharedFileName, Context.MODE_PRIVATE)
    var editor = sp.edit()
    var aux= dt.toString(StandardCharsets.UTF_16.name())
    Log.v("PDM25",aux)
    editor.putString("posto1",aux)
    editor.apply()
}
fun getGasStationSerializable(context: Context):GasStation{
    val sharedFileName="lastGasStationSer"
    var sp: SharedPreferences = context.getSharedPreferences(sharedFileName, Context.MODE_PRIVATE)
    var gasStation = GasStation("PostoS", Coordinates(41.40338, 2.17403))
    var aux: String
    if(sp!=null) {
        //Lendo bytes serializados
        aux = sp.getString("posto1", "").toString()
        Log.v("PDM25",aux)

        if(aux.length>=2) {
            //Convertendo em objeto
            var bis: ByteArrayInputStream
            bis = ByteArrayInputStream(aux.toByteArray(Charsets.UTF_16))
            var obi: ObjectInputStream
            obi = ObjectInputStream(bis)

            //lendo
            gasStation = obi.readObject() as GasStation
        }
    }
    return gasStation
}

fun gasStationToJson(gasStation: GasStation): JSONObject {
    return JSONObject().apply {
        put("name", gasStation.name)
        put("lat", gasStation.coord.lat)
        put("lgt", gasStation.coord.lgt)
    }
}
fun jsonToGasStation(json: JSONObject?): GasStation {
    //Caso o json seja inválido existe um valor default (ternário a seguir)
    val name = json?.optString("name", "") ?: ""
    val lat = json?.optDouble("lat", 0.0) ?: 0.0
    val lgt = json?.optDouble("lgt", 0.0) ?: 0.0
    return GasStation(name, Coordinates(lat, lgt))
}
fun stringToJson_Safe(jsonString: String): JSONObject? {
    return try {
        JSONObject(jsonString)
    } catch (e: Exception) {
        null // Retorna null se a string não for um JSON válido
    }
}

fun saveGasStationJSON(context: Context, gasStation: GasStation){
    Log.v("PDM25","Salvando o posto em JSON")
    val sharedFileName="lastGasStationJSON"
    var sp: SharedPreferences = context.getSharedPreferences(sharedFileName, Context.MODE_PRIVATE)
    var editor = sp.edit()
    val jsonObject = gasStationToJson(gasStation)
    Log.v("PDM",": "+jsonObject);
    editor.putString("gasJSON",jsonObject.toString())
    editor.apply()
}
fun getGasStationJSON(context: Context):GasStation{
    val sharedFileName="lastGasStationJSON"
    var sp: SharedPreferences = context.getSharedPreferences(sharedFileName, Context.MODE_PRIVATE)
    lateinit var gasStation:GasStation
    var aux: String
    if(sp!=null) {
        //Lendo bytes serializados
        aux = sp.getString("gasJSON", "").toString()
        Log.v("PDM25",aux)
        //lendo
        gasStation= jsonToGasStation(stringToJson_Safe(aux))

    }
    return gasStation
}

//Sugestão de métodos a serem usados para a versão final
fun getListOfGasStation(context: Context):List<GasStation>{
    val gasStation1 = GasStation("Posto na Espanha", Coordinates(41.40338, 2.17403))
    val gasStation2 = GasStation("Posto em NY", Coordinates(40.7128, -74.0060))
    val gasStation3= GasStation("Posto em Fortaleza",Coordinates(41.40338, 2.17403))

    return listOf(gasStation1, gasStation2, gasStation3)
}
fun addGasStation(context: Context, gasStation: GasStation){

}