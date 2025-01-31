package com.example.exemplosimplesdecompose

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.exemplosimplesdecompose.ui.theme.ExemploSimplesDeComposeTheme
import com.example.exemplosimplesdecompose.view.AlcoolGasolinaPreco
import com.example.exemplosimplesdecompose.view.ListofGasStations
import com.example.exemplosimplesdecompose.view.Welcome

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        var check= false
        check=loadConfig(this)
        setContent {
            ExemploSimplesDeComposeTheme {
                val navController: NavHostController = rememberNavController()
                NavHost(navController = navController, startDestination = "welcome") {
                    composable("welcome") { Welcome(navController) }
                   // composable("input") { InputView(navController) }
                    composable("mainalcgas") { AlcoolGasolinaPreco(navController,check) }
                    composable("listaDePostos/{posto}") { backStackEntry ->
                        val posto = backStackEntry.arguments?.getString("posto") ?: ""
                        ListofGasStations(navController, posto)
                    }

                }
            }
        }
    }


    fun loadConfig(context: Context):Boolean{
        val sharedFileName="config_Alc_ou_Gas"
        var sp: SharedPreferences = context.getSharedPreferences(sharedFileName, Context.MODE_PRIVATE)
        var is_75_checked=false
        if(sp!=null) {
            is_75_checked = sp.getBoolean("is_75_checked", false)
        }
        return is_75_checked
    }
}

