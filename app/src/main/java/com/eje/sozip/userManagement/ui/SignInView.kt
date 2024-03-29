package com.eje.sozip.userManagement.ui

import android.content.Intent
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.ArrowCircleRight
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.datastore.core.DataStore
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.eje.sozip.R
import com.eje.sozip.frameworks.helper.AES256Util
import com.eje.sozip.frameworks.helper.DataStoreUtil
import com.eje.sozip.frameworks.models.OnStartScreens
import com.eje.sozip.frameworks.ui.MainActivity
import com.eje.sozip.frameworks.ui.ProgressView
import com.eje.sozip.ui.theme.SOZIPColorPalette
import com.eje.sozip.ui.theme.SOZIPTheme
import com.eje.sozip.ui.theme.accent
import com.eje.sozip.ui.theme.gray
import com.eje.sozip.ui.theme.red
import com.eje.sozip.ui.theme.white
import com.eje.sozip.userManagement.helper.UserManagement
import com.eje.sozip.userManagement.models.AuthInfoModel
import kotlinx.coroutines.flow.collect
import java.util.prefs.Preferences

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInView(){
    val email = remember{
        mutableStateOf("")
    }

    val password = remember{
        mutableStateOf("")
    }

    var showDialog by remember{
        mutableStateOf(false)
    }

    var buttonDisable by remember{
        mutableStateOf(false)
    }

    var showProgress by remember{
        mutableStateOf(false)
    }

    var signedIn by remember{
        mutableStateOf(false)
    }

    var saveData by remember{
        mutableStateOf(true)
    }

    val helper = UserManagement()

    val context = LocalContext.current

    val navController = rememberNavController()

    val dataStoreUtil = DataStoreUtil(context)

    val authInfo = dataStoreUtil.getFromDataStore().collectAsState(initial = AuthInfoModel(email = "", password = ""))

    SOZIPTheme {
        NavHost(navController = navController, startDestination = "SignInView"){
            composable(route = "SignUpView"){
                SignUpView(navController)
            }

            composable(route = "SignInView"){
                LaunchedEffect(key1 = true){
                    Log.d("SignInView", "Launched")
                    if(AES256Util.decrypt(authInfo.value.email) != "" &&
                        AES256Util.decrypt(authInfo.value.password) != ""){
                        buttonDisable = true
                        showProgress = true

                        helper.signIn(email = AES256Util.decrypt(authInfo.value.email), password = AES256Util.decrypt(authInfo.value.password)){
                            if(it){
                                saveData = false
                                signedIn = true
                            } else{
                                showProgress = false
                                buttonDisable = false
                            }
                        }
                    }
                }

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

                        Spacer(modifier = Modifier.height(20.dp))


                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = email.value,
                            onValueChange = { textVal : String -> email.value = textVal },
                            label = { Text("E-Mail") },
                            placeholder = { Text("E-Mail") } ,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.AlternateEmail,
                                    contentDescription = null
                                )
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                cursorColor = accent,
                                focusedBorderColor = accent,
                                errorCursorColor = red,
                                errorLeadingIconColor = red,
                                disabledPlaceholderColor = gray,
                                focusedTextColor = accent,
                                focusedLabelColor = accent,
                                focusedLeadingIconColor = accent,
                                disabledTextColor = gray,
                                unfocusedLabelColor = SOZIPColorPalette.current.txtColor,
                                unfocusedLeadingIconColor = SOZIPColorPalette.current.txtColor,
                                unfocusedSupportingTextColor = SOZIPColorPalette.current.txtColor,
                                selectionColors = TextSelectionColors(handleColor = accent, backgroundColor = accent.copy(alpha = 0.5f))
                            ),
                            maxLines = 1,
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(20.dp))


                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = password.value,
                            onValueChange = { textVal : String -> password.value = textVal },
                            label = { Text("비밀번호") },
                            placeholder = { Text("비밀번호") } ,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Key,
                                    contentDescription = null
                                )
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                cursorColor = accent,
                                focusedBorderColor = accent,
                                errorCursorColor = red,
                                errorLeadingIconColor = red,
                                disabledPlaceholderColor = gray,
                                focusedTextColor = accent,
                                focusedLabelColor = accent,
                                focusedLeadingIconColor = accent,
                                disabledTextColor = gray,
                                unfocusedLabelColor = SOZIPColorPalette.current.txtColor,
                                unfocusedLeadingIconColor = SOZIPColorPalette.current.txtColor,
                                unfocusedSupportingTextColor = SOZIPColorPalette.current.txtColor,
                                selectionColors = TextSelectionColors(handleColor = accent, backgroundColor = accent.copy(alpha = 0.5f))

                            ),
                            visualTransformation = PasswordVisualTransformation(),
                            maxLines = 1,
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Button(onClick = {
                            buttonDisable = true
                            showProgress = true

                            helper.signIn(email = email.value, password = password.value){
                                if(it){
                                    signedIn = true
                                } else{
                                    showProgress = false
                                    buttonDisable = false
                                    showDialog = true
                                }
                            }
                        },
                            modifier = Modifier
                                .fillMaxWidth(),
                            enabled = !email.value.isEmpty() && !password.value.isEmpty() && !buttonDisable,
                            contentPadding = PaddingValues(20.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = accent, disabledContainerColor = gray
                            ),
                            elevation = ButtonDefaults.buttonElevation(5.dp, disabledElevation = 5.dp)
                        ) {
                            Row{
                                Text("로그인", color = white)
                                Icon(imageVector = Icons.Default.ChevronRight, contentDescription = null, tint = white)
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Button(onClick = {
                            navController.navigate(OnStartScreens.SignUpView){
                                popUpTo(OnStartScreens.SignInView){
                                    inclusive = false
                                }
                            }
                        },
                            modifier = Modifier
                                .padding(5.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = accent
                            ),
                            elevation = ButtonDefaults.buttonElevation(5.dp)
                        ){
                            Row(verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(5.dp)){
                                Column(verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.Start){
                                    Text("처음 사용하시나요?", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = white)
                                    Text("학교 인증 바로가기", fontSize = 12.sp, color = white)
                                }

                                Spacer(modifier = Modifier.width(60.dp))

                                Icon(imageVector = Icons.Default.ArrowCircleRight, contentDescription = null, tint = white)
                            }
                        }

                        Spacer(modifier = Modifier.weight(1f))

                        Text(text = "© 2021-2023. eje All Rights Reserved.\n이제이 | 대표 : 문소정 | 사업자등록번호 : 763-33-00865",
                            color = gray,
                            fontSize = 10.sp,
                            textAlign = TextAlign.Center)
                    }

                    if(showDialog){
                        AlertDialog(
                            onDismissRequest = {  },
                            confirmButton = {
                                TextButton(onClick = {
                                    showDialog = false
                                }){
                                    Text("확인", color = accent, fontWeight = FontWeight.Bold)
                                }
                            },
                            title = {
                                Text("오류")
                            },
                            text = {
                                Text("요청을 처리하는 중 문제가 발생했습니다.\n로그인 정보가 올바르지 않거나, 네트워크 상태를 확인한 후 다시 시도하십시오.")
                            },
                            icon = {
                                Icon(imageVector = Icons.Default.Warning, contentDescription = null)
                            }
                        )
                    }

                    if(showProgress){
                        Dialog(
                            onDismissRequest = { showDialog = false },
                            DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
                        ) {
                            Box(
                                contentAlignment= Alignment.Center,
                                modifier = Modifier
                                    .size(100.dp)
                                    .background(
                                        SOZIPColorPalette.current.background.copy(alpha = 0.7f),
                                        shape = RoundedCornerShape(8.dp)
                                    )
                            ) {
                                CircularProgressIndicator(color = accent)
                            }
                        }
                    }

                    if(signedIn){
                        LaunchedEffect(key1 = true){
                            if(saveData){
                                dataStoreUtil.saveToDataStore(AES256Util.encrypt(email.value), AES256Util.encrypt(password.value))
                            }

                            context.startActivity(Intent(context, MainActivity :: class.java))

                        }
                    }
                }
            }
        }


    }
}

@Preview
@Composable
fun SignInView_preview(){
    SignInView()
}