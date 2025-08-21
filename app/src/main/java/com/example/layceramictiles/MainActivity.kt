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
import saveProjectToFirestore

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Referenca na Firestore
//        val db = Firebase.firestore
//
//        // Primer upisa u Firestore
//        val testData = hashMapOf(
//            "ime" to "Proba",
//            "vreme" to System.currentTimeMillis()
//        )
//
//        db.collection("testKolekcija")
//            .add(testData)
//            .addOnSuccessListener { documentReference ->
//                println("Uspešno dodato: ${documentReference.id}")
//            }
//            .addOnFailureListener { e ->
//                println("Greška pri dodavanju: $e")
//            }
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
    val viewModelCalc: ScreenCalculateViewModel = viewModel()
    val navController = rememberNavController()

    NavHost(navController=navController,
        startDestination = "Screen1"){
        composable ("Screen1"){
            Screen1(
                onContinueClick = { navController.navigate("ScreenCalculate") },
                viewModelCalc = viewModelCalc,
                viewModelMat  = viewModelMat,
                onOpenSaved = { /* file opened */ navController.navigate("ScreenCalculate") }
            )
        }
        composable("ScreenCalculate") {
            ScreenCalculate(
                viewModel = viewModelCalc,
                onNextClick = {
                    navController.navigate("ScreenMaterials")
                },

            )
        }
        composable("ScreenMaterials") {

            ScreenMaterials(
//                wallTileArea = SharedDataHolder.wallTilesArea,
//                floorTileArea = SharedDataHolder.floorTilesArea,
                viewModel = viewModelMat,
                viewModelCalc = viewModelCalc,
                onPreviousClick = {
                    navController.navigate("ScreenCalculate")
                },
                onSaveClick = { fileNameFromDialog ->
                    val name = SharedDataHolder.currentFileName ?: fileNameFromDialog
                    SharedDataHolder.currentFileName = name
                    saveProjectToFirestore(
                        fileName = name,
                        viewModelCalc = viewModelCalc,
                        viewModelMat = viewModelMat,
                        wallTileArea = SharedDataHolder.wallTilesArea,
                        floorTileArea = SharedDataHolder.floorTilesArea
                    )
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
