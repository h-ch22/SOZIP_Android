package com.eje.sozip.userManagement.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eje.sozip.R
import com.eje.sozip.ui.theme.SOZIPColorPalette
import com.eje.sozip.ui.theme.SOZIPTheme
import com.eje.sozip.ui.theme.accent

@Composable
fun SignInView(){
    SOZIPTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = SOZIPColorPalette.current.background
        ){
            Column(modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center){
                Spacer(modifier = Modifier.weight(1f))

                Image(
                    painter = painterResource(id = R.drawable.appstore),
                    contentDescription = "SOZIP Logo",
                    modifier = Modifier
                        .width(100.dp)
                        .height(100.dp)
                        .shadow(
                            elevation = 8.dp,
                            shape = RoundedCornerShape(10.dp),
                            clip = true
                        )
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "소집 : SOZIP",
                    modifier = Modifier,
                    color = SOZIPColorPalette.current.txtColor,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )


            }
        }
    }
}

@Preview
@Composable
fun SignInView_preview(){
    SignInView()
}