package com.eje.sozip.frameworks.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eje.sozip.ui.theme.SOZIPColorPalette
import com.eje.sozip.ui.theme.gray

@Composable
fun SOZIPTextField(
    text : String,
    icon : ImageVector,
    onTextChanged : (String) -> Unit,
    isPassword: Boolean = false,
    keyboardType : KeyboardType = KeyboardType.Text,
    placeHolder : String
){
    BasicTextField(
        value = text,
        onValueChange = onTextChanged,
        modifier = Modifier
            .fillMaxWidth()
            .background(SOZIPColorPalette.current.btnColor)
            .shadow(
            elevation = 8.dp,
            shape = RoundedCornerShape(5.dp),
            clip = true)
            .padding(10.dp),
            decorationBox = { innerTextField ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ){
                Icon(
                    modifier = Modifier.size(12.dp),
                    imageVector = icon,
                    contentDescription = null,
                    tint = SOZIPColorPalette.current.txtColor
                )

                if(text.isEmpty()){
                    Text(placeHolder, color = gray)
                }

                innerTextField()
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    )
}

@Preview
@Composable
fun SOZIPTextField_preview(){
    var text by remember{ mutableStateOf("") }
    SOZIPTextField(
        text = "",
        icon = Icons.Default.AlternateEmail,
        onTextChanged = {text = it},
        keyboardType = KeyboardType.Email,
        placeHolder = "E-Mail"
    )
}