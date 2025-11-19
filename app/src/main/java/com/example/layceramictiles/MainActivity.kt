package com.example.layceramictiles

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.layceramictiles.view.ProjectViewModel
import com.example.layceramictiles.view.Screen1ViewModel
import com.example.layceramictiles.ui.theme.LayCeramicTilesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            LayCeramicTilesTheme {

                MyAppNavigation()
            }
        }
    }
}


@Composable
fun MyAppNavigation() {

    val projectViewModel: ProjectViewModel = viewModel()
    val screen1ViewModel: Screen1ViewModel = viewModel()
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "Screen1"
    ) {

        composable("Screen1") {
            Screen1(
                viewModel = screen1ViewModel,
                onContinueClick = {
                    projectViewModel.resetState()
                    navController.navigate("ScreenCalculate")
                },
                onOpenSaved = { fileName ->
                    projectViewModel.loadProject(fileName) {
                        navController.navigate("ScreenCalculate")
                    }
                }
            )
        }
        composable("ScreenCalculate") {
            ScreenCalculate(
                viewModel = projectViewModel,
                onNextClick = {
                    navController.navigate("ScreenMaterials")
                }
                )
        }
        composable("ScreenMaterials") {

            ScreenMaterials(
                viewModel = projectViewModel,
                onPreviousClick = {
                    navController.navigate("ScreenCalculate")
                },
                onSaveClick = { fileNameFromDialog ->
                    projectViewModel.saveProject(fileNameFromDialog)

                }
            )
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
//}

    }
}
