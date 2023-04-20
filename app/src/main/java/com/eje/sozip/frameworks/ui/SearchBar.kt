package com.eje.sozip.frameworks.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eje.sozip.ui.theme.SOZIPColorPalette
import com.eje.sozip.ui.theme.accent
import com.eje.sozip.ui.theme.gray
import com.eje.sozip.ui.theme.red
import com.eje.sozip.ui.theme.white

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    state : MutableState<TextFieldValue>,
    placeHolder : String
){
    TextField(
        value = state.value,
        onValueChange = {
            state.value = it
        },
        leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null) },
        placeholder = { Text(text = placeHolder) },
        colors = TextFieldDefaults.textFieldColors(
            cursorColor = accent,
            errorCursorColor = red,
            errorLeadingIconColor = red,
            disabledPlaceholderColor = gray,
            disabledIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            focusedLabelColor = accent,
            focusedLeadingIconColor = gray,
            textColor = SOZIPColorPalette.current.txtColor,
            disabledTextColor = gray,
            containerColor = gray.copy(alpha = 0.5f),
            unfocusedLabelColor = gray,
            unfocusedLeadingIconColor = gray,
            unfocusedIndicatorColor = Color.Transparent,
            unfocusedSupportingTextColor = gray,
            selectionColors = TextSelectionColors(
                handleColor = accent,
                backgroundColor = accent.copy(alpha = 0.5f)
            )
        ),
        singleLine = true,
        shape = RoundedCornerShape(200.dp),
        modifier = Modifier.fillMaxWidth()
    )
}

@Preview
@Composable
fun SearchBar_preview(){
    SearchBar(state = remember {
        mutableStateOf(TextFieldValue(""))
    }, placeHolder = "원하는 소집을 검색해보세요!")
}