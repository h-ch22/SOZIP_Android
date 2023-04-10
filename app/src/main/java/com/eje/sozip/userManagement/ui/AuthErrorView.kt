package com.eje.sozip.userManagement.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.eje.sozip.frameworks.models.OnStartScreens
import com.eje.sozip.ui.theme.SOZIPColorPalette
import com.eje.sozip.ui.theme.accent
import com.eje.sozip.ui.theme.gray
import com.eje.sozip.ui.theme.red
import com.eje.sozip.ui.theme.white
import com.eje.sozip.userManagement.models.AuthResultModel

@Composable
fun AuthErrorView(code : AuthResultModel){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "ErrorView") {
        composable(route = "SignInView") {
            SignInView()
        }

        composable(route = "ErrorView"){
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = SOZIPColorPalette.current.background
            ){
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center, modifier = Modifier.padding(20.dp)) {
                    Spacer(modifier = Modifier.weight(1f))

                    Icon(imageVector = Icons.Default.Error, contentDescription = null, modifier = Modifier
                        .width(100.dp)
                        .height(100.dp), tint= red)
                    Text(text = "죄송합니다, 고객님.", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = SOZIPColorPalette.current.txtColor)

                    Spacer(modifier = Modifier.height(10.dp))

                    when(code){
                        AuthResultModel.EMAIL_EXCEPTION -> Text("이미 가입된 E-Mail입니다.\n다른 E-Mail을 사용해주세요.", fontSize = 12.sp, color = gray, textAlign = TextAlign.Center)
                        AuthResultModel.WEAK_PASSWORD -> Text("안전하지 않은 비밀번호입니다.\n보안을 위해 6자리 이상의 비밀번호를 사용해주세요.", fontSize = 12.sp, color = gray, textAlign = TextAlign.Center)
                        AuthResultModel.TOO_MANY_REQUESTS -> Text("동일한 IP주소에서 너무 많은 요청이 있었습니다.\n나중에 다시 시도해주세요.", fontSize = 12.sp, color = gray, textAlign = TextAlign.Center)
                        AuthResultModel.UNKNOWN -> Text("고객님의 요청을 처리하는 중 문제가 발생하였습니다.\n나중에 다시 시도해주세요.", fontSize = 12.sp, color = gray, textAlign = TextAlign.Center)
                        AuthResultModel.NETWORK_EXCEPTION -> Text("네트워크에 연결되어 있지 않습니다\n네트워크 상태를 확인한 후 다시 시도해주세요.", fontSize = 12.sp, color = gray, textAlign = TextAlign.Center)
                        else -> Text("")
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Button(onClick = {
                        navController.navigate(OnStartScreens.SignInView){
                            popUpTo(OnStartScreens.errorView){
                                inclusive = true
                            }
                        }
                    },
                        modifier = Modifier
                            .fillMaxWidth(),
                        contentPadding = PaddingValues(20.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = accent, disabledContainerColor = gray
                        ),
                        elevation = ButtonDefaults.buttonElevation(5.dp, disabledElevation = 5.dp)
                    ){
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center){
                            Icon(imageVector = Icons.Default.ChevronLeft, contentDescription = null, tint = white)
                            Text(text = "이전 페이지로", color = white)
                        }
                    }
                }
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun AuthErrorView_preview(){
    AuthErrorView(code = AuthResultModel.EMAIL_EXCEPTION)
}