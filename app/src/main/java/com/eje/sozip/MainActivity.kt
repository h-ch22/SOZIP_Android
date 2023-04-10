package com.eje.sozip

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eje.sozip.ui.theme.SOZIPColorPalette
import com.eje.sozip.ui.theme.SOZIPTheme
import com.eje.sozip.ui.theme.accent
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SOZIPTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = SOZIPColorPalette.current.background
                ) {
                    splash()
                }
            }
        }

        if(auth.currentUser?.uid == null){

        } else{

        }
    }
}

@Composable
fun splash(){
    Column(modifier = Modifier.padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center){
        Spacer(modifier = Modifier.weight(1f))

        Image(
            painter = painterResource(id = R.drawable.appstore),
            contentDescription = "SOZIP Logo",
            modifier = Modifier
                .width(180.dp)
                .height(180.dp)
                .shadow(elevation = 8.dp,
                    shape = RoundedCornerShape(16.dp),
                    clip = true
                )
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "소집 : SOZIP",
            modifier = Modifier,
            color = SOZIPColorPalette.current.txtColor,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.weight(1f))

        CircularProgressIndicator(modifier = Modifier,
                                color = accent)
    }
}


@Preview(showBackground = true)
@Composable
fun splashPreview() {
    SOZIPTheme {
        splash()
    }
}