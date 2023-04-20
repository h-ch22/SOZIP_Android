package com.eje.sozip.SOZIP.models

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eje.sozip.ui.theme.SOZIPTheme
import com.eje.sozip.ui.theme.accent
import com.eje.sozip.ui.theme.white

@Composable
fun CategoryListModel(category : String, selected : Boolean){
    val animatedBorderColor = animateColorAsState(
        targetValue = if (selected) accent else Color.Transparent,
        animationSpec = tween(200, 0, LinearEasing)
    )

    val animatedContainerColor = animateColorAsState(
        targetValue = if (selected) white else accent,
        animationSpec = tween(200, 0, LinearEasing)
    )

    SOZIPTheme {
        Card(
            modifier = Modifier
                .wrapContentSize()
                .border(shape = RoundedCornerShape(10.dp), width = 1.dp, color = accent),
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(containerColor = animatedBorderColor.value)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .wrapContentSize(),
            ) {
                Text(
                    text = category,
                    color =  animatedContainerColor.value,
                    modifier = Modifier.padding(horizontal = 15.dp, vertical = 10.dp),
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Preview
@Composable
fun CategoryListModel_preview(){
    CategoryListModel("치킨", false)
}