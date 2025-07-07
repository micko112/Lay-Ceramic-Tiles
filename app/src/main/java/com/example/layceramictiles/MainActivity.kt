package com.example.layceramictiles

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.layceramictiles.View.ScreenCalculateViewModel
import com.example.layceramictiles.View.ScreenMaterialsViewModel
import com.example.layceramictiles.components.SharedDataHolder
import com.example.layceramictiles.ui.theme.LayCeramicTilesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LayCeramicTilesTheme {

                myAppNavigation()
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    Greeting(
//                        name = "Android",
//                        modifier = Modifier.padding(innerPadding)
//                    )
//                }
            }
        }
    }
}
@Composable
fun myAppNavigation(){
    val viewModelMat: ScreenMaterialsViewModel = viewModel()
    val viewModelCal: ScreenCalculateViewModel = viewModel()
    val navController = rememberNavController()
    NavHost(navController=navController,
        startDestination = "Screen1"){
        composable ("Screen1"){
            Screen1(onContinueClick = {
                navController.navigate("ScreenCalculate")
            })
        }
        composable("ScreenCalculate") {
            ScreenCalculate(
                viewModel = viewModelCal,
                onNextClick = {
                    navController.navigate("ScreenMaterials")
                },

            )
        }
        composable("ScreenMaterials") {
            
            ScreenMaterials( wallTileArea = SharedDataHolder.wallTilesArea,
                floorTileArea = SharedDataHolder.floorTilesArea,
                viewModel = viewModelMat,
                onPreviousClick = {
                    navController.navigate("ScreenCalculate")
                }
            )
        }
    }

//var showScreen1 by rememberSaveable { mutableStateOf(true) }
//    Surface {
//        if(showScreen1){
//            Screen1(onContinueClick = {showScreen1=false})
//        }
//        else {
//            ScreenCalculate()
//        }
//    }
}

@Preview
@Composable
private fun view() {
    ScreenCalculate()
}
