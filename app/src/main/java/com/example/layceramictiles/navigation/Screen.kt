package com.example.layceramictiles.navigation

sealed class Screen(val route: String){
    object Screen1: Screen("screen1")
    object ScreenCalculate : Screen("screen_calculate")
    object ScreenMaterials : Screen("screen_materials")
}