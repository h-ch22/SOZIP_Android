package com.eje.sozip.frameworks.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.eje.sozip.R
import com.eje.sozip.frameworks.helper.AES256Util
import com.eje.sozip.frameworks.helper.DataStoreUtil
import com.eje.sozip.frameworks.models.OnStartScreens
import com.eje.sozip.frameworks.models.SplashViewModel
import com.eje.sozip.ui.theme.SOZIPColorPalette
import com.eje.sozip.ui.theme.SOZIPTheme
import com.eje.sozip.ui.theme.accent
import com.eje.sozip.userManagement.helper.UserManagement
import com.eje.sozip.userManagement.models.AuthInfoModel
import com.eje.sozip.userManagement.ui.SignInView

class StartActivity : ComponentActivity() {
    private val viewModel : SplashViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()

            SOZIPTheme {
                NavHost(navController = navController, startDestination = "Splash"){
                    composable(route = "Splash"){
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = SOZIPColorPalette.current.background
                        ){
                            splash(navController, viewModel)
                        }
                    }

                    composable(route = "SignInView"){
                        SignInView()
                    }
                }
            }
        }
    }
}

@Composable
fun splash(navController : NavController, viewModel: SplashViewModel){
    val context = LocalContext.current

    LaunchedEffect(key1 = true){
        if(!viewModel.hasSignedIn.value){
            navController.navigate(OnStartScreens.SignInView){
                popUpTo(OnStartScreens.Splash){
                    inclusive = true
                }
            }
        } else{
            context.startActivity(Intent(context, MainActivity :: class.java))
        }
    }

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
                .shadow(
                    elevation = 8.dp,
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

