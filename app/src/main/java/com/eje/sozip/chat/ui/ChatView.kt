package com.eje.sozip.chat.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.eje.sozip.ui.theme.SOZIPColorPalette
import com.eje.sozip.ui.theme.SOZIPTheme

@Composable
fun ChatView(){
    SOZIPTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = SOZIPColorPalette.current.background) {
            Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Chat")
            }
        }
    }
}

@Preview
@Composable
fun ChatView_preview(){
    ChatView()
}