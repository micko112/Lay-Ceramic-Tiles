package com.example.layceramictiles.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun InputField(
    value: String,
    prefix: String,
    unit: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    BasicTextField(
        value = value,
        onValueChange = { newValue ->
            if (newValue.isEmpty() || newValue.matches(Regex("^\\d*\\.?\\d*\$"))) {
                onValueChange(newValue)
            }
        },
        visualTransformation = PrefixUnitTransformation(prefix, unit),
        textStyle = TextStyle(
            color = Color.Black,
            textAlign = TextAlign.Center,
        ),
        cursorBrush = SolidColor(Color.Black),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = modifier
            .width(IntrinsicSize.Max)
            .defaultMinSize(100.dp)
            .widthIn(max = 170.dp)
            .height(30.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color.LightGray)
            .padding(horizontal = 8.dp)
            .padding(vertical = 8.dp)

            .fillMaxWidth()
            .fillMaxHeight()
    )
}

class PrefixUnitTransformation(private val prefix: String, private val unit: String) : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val transformedText = if (text.text.isEmpty()) {
            "$prefix" + "  " + "$unit"
        } else {
            "$prefix${text.text}$unit"
        }

        return TransformedText(
            text = AnnotatedString(transformedText),
            offsetMapping = object : OffsetMapping {
                override fun originalToTransformed(offset: Int): Int {
                    return offset + prefix.length
                }

                override fun transformedToOriginal(offset: Int): Int {
                    return (offset - prefix.length).coerceIn(0, text.length)
                }
            }
        )
    }
}