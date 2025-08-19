package com.example.layceramictiles

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.layceramictiles.components.CustomButton

@Composable
fun Screen1(onContinueClick: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Naslov - svaka reč u posebnom redu
        Text(
            text = "LAY",
            fontSize = 60.sp,
            fontWeight = FontWeight.ExtraBold
        )
        Text(
            text = "CERAMIC",
            fontSize = 60.sp,
            fontWeight = FontWeight.ExtraBold
        )
        Text(
            text = "TILES",
            fontSize = 60.sp,
            fontWeight = FontWeight.ExtraBold
        )
        // Logo
        Image(
            painter = painterResource(id = R.drawable.ceramiclogo_removebg_preview),
            contentDescription = "Ceramic logo",
            modifier = Modifier
                .height(400.dp)
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(50.dp))

        // Dugme - veće i bliže sredini
        CustomButton("CALCULATE", onClick = onContinueClick)
    }
}
