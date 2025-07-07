package com.example.layceramictiles.View

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class ScreenMaterialsViewModel : ViewModel() {
    var tileWallWidth = MutableStateFlow("")
    var tileWallLength = MutableStateFlow("")
    var tileFloorWidth = MutableStateFlow("")
    var tileFloorLength = MutableStateFlow("")
    var wallGroutWidth = MutableStateFlow("")
    var floorGroutWidth = MutableStateFlow("")

    var results = MutableStateFlow(listOf<String?>(null, null))
}