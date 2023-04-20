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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.eje.sozip.R
import com.eje.sozip.frameworks.helper.AES256Util
import com.eje.sozip.frameworks.helper.DataStoreUtil
import com.eje.sozip.frameworks.models.OnStartScreens
import com.eje.sozip.ui.theme.SOZIPColorPalette
import com.eje.sozip.ui.theme.SOZIPTheme
import com.eje.sozip.ui.theme.accent
import com.eje.sozip.ui.theme.gray
import com.eje.sozip.userManagement.helper.UserManagement
import com.eje.sozip.userManagement.models.AuthResultModel
import com.eje.sozip.userManagement.models.UserInfoModel

@Composable
fun InProgressSignUpView(
    userInfo : UserInfoModel,
    isAcceptMarketing : Boolean,
    password : String
){
    val helper = UserManagement()
    val navController = rememberNavController()
    val authResultCode = remember{
        mutableStateOf(AuthResultModel.UNKNOWN)
    }
    val context = LocalContext.current
    val dataStore = DataStoreUtil(context)


    SOZIPTheme {
        LaunchedEffect(key1 = true){
            helper.signUp(userInfo = userInfo, isMarketingAccept = isAcceptMarketing, password = password){
                when(it){
                    AuthResultModel.SUCCESS -> {
                        navController.navigate(OnStartScreens.successView){
                            popUpTo(OnStartScreens.inProgressView){
                                inclusive = true
                            }
                        }
                    }

                    AuthResultModel.UNKNOWN -> {
                        authResultCode.value = AuthResultModel.UNKNOWN
                        navController.navigate(OnStartScreens.errorView){
                            popUpTo(OnStartScreens.inProgressView){
                                inclusive = true
                            }
                        }
                    }

                    AuthResultModel.EMAIL_EXCEPTION -> {
                        authResultCode.value = AuthResultModel.EMAIL_EXCEPTION
                        navController.navigate(OnStartScreens.errorView){
                            popUpTo(OnStartScreens.inProgressView){
                                inclusive = true
                            }
                        }
                    }

                    AuthResultModel.TOO_MANY_REQUESTS -> {
                        authResultCode.value = AuthResultModel.TOO_MANY_REQUESTS
                        navController.navigate(OnStartScreens.errorView){
                            popUpTo(OnStartScreens.inProgressView){
                                inclusive = true
                            }
                        }
                    }

                    AuthResultModel.WEAK_PASSWORD -> {
                        authResultCode.value = AuthResultModel.WEAK_PASSWORD
                        navController.navigate(OnStartScreens.errorView){
                            popUpTo(OnStartScreens.inProgressView){
                                inclusive = true
                            }
                        }
                    }

                    AuthResultModel.NETWORK_EXCEPTION -> {
                        authResultCode.value = AuthResultModel.NETWORK_EXCEPTION
                        navController.navigate(OnStartScreens.errorView){
                            popUpTo(OnStartScreens.inProgressView){
                                inclusive = true
                            }
                        }
                    }
                }
            }
        }

        NavHost(navController = navController, startDestination = "InProgressView") {
            composable(route = "AuthSuccessView") {
                LaunchedEffect(key1 = true){
                    dataStore.saveToDataStore(AES256Util.encrypt(userInfo.email), AES256Util.encrypt(password))
                }

                AuthSuccessView()
            }

            composable(route = "AuthErrorView"){
                AuthErrorView(code = authResultCode.value)
            }

            composable(route = "InProgressView"){
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = SOZIPColorPalette.current.background
                ){
                    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center, modifier = Modifier.padding(20.dp)) {
                        Spacer(modifier = Modifier.weight(1f))

                        Image(painter = painterResource(id = R.drawable.appstore),
                            contentDescription = null,
                            modifier = Modifier
                                .width(150.dp)
                                .height(150.dp)
                                .shadow(
                                    elevation = 8.dp,
                                    shape = RoundedCornerShape(16.dp),
                                    clip = true
                                ))

                        Text(text = "가입 처리 중...", fontSize = 24.sp, color = SOZIPColorPalette.current.txtColor, fontWeight = FontWeight.Bold)

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(text = "고객님의 가입 요청을 처리하고 있습니다.\n잠시 기다려주세요!", textAlign = TextAlign.Center, color = SOZIPColorPalette.current.txtColor)

                        Spacer(modifier = Modifier.weight(1f))

                        CircularProgressIndicator(color = accent)

                        Text(text = "네트워크 상태에 따라 최대 1분 정도 소요될 수 있어요.", color = gray, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}