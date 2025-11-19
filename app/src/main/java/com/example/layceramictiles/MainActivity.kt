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
import com.example.layceramictiles.navigation.Screen
import com.example.layceramictiles.ui.screens.Screen1
import com.example.layceramictiles.ui.screens.ScreenCalculate
import com.example.layceramictiles.ui.screens.ScreenMaterials
import com.example.layceramictiles.ui.theme.LayCeramicTilesTheme
import com.example.layceramictiles.view.ProjectViewModel
import com.example.layceramictiles.view.Screen1ViewModel

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
        startDestination = Screen.Screen1.route
    ) {

        composable(Screen.Screen1.route) {
            Screen1(
                viewModel = screen1ViewModel,
                onContinueClick = {
                    projectViewModel.resetState()
                    navController.navigate(Screen.ScreenCalculate.route)
                },
                onOpenSaved = { fileName ->
                    projectViewModel.loadProject(fileName) {
                        navController.navigate(Screen.ScreenCalculate.route)
                    }
                }
            )
        }
        composable(Screen.ScreenCalculate.route) {
            ScreenCalculate(
                viewModel = projectViewModel,
                onPreviousClick = {
                    navController.popBackStack()
                },
                onNextClick = {
                    navController.navigate(Screen.ScreenMaterials.route)
                }
            )
        }
        composable(Screen.ScreenMaterials.route) {

            ScreenMaterials(
                viewModel = projectViewModel,
                onPreviousClick = {
                    navController.popBackStack()
                },
                onSaveClick = { fileNameFromDialog ->
                    projectViewModel.saveProject(fileNameFromDialog)

                }
            )
        }
    }
}
