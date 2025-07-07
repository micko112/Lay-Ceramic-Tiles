package com.example.layceramictiles.View

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class ScreenCalculateViewModel : ViewModel() {
    var lengthB = MutableStateFlow("")
    var widthA = MutableStateFlow("")
    var heightH = MutableStateFlow("")
    var results = MutableStateFlow(listOf<String?>(null, null, null, null, null, null))
}