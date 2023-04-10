package com.eje.sozip.userManagement.ui

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.ArrowCircleRight
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Key
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.eje.sozip.R
import com.eje.sozip.frameworks.models.OnStartScreens
import com.eje.sozip.splash
import com.eje.sozip.ui.theme.SOZIPColorPalette
import com.eje.sozip.ui.theme.SOZIPTheme
import com.eje.sozip.ui.theme.accent
import com.eje.sozip.ui.theme.black
import com.eje.sozip.ui.theme.gray
import com.eje.sozip.ui.theme.red
import com.eje.sozip.ui.theme.white
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInView(){
    val email = remember{
        mutableStateOf("")
    }

    val password = remember{
        mutableStateOf("")
    }

    val navController = rememberNavController()

    SOZIPTheme {
        NavHost(navController = navController, startDestination = "SignInView"){
            composable(route = "SignUpView"){
                SignUpView()
            }

            composable(route = "SignInView"){
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


                        TextField(
                            value = email.value,
                            onValueChange = { textVal : String -> email.value = textVal },
                            modifier = Modifier
                                .fillMaxWidth()
                                .shadow(5.dp),
                            label = { Text("E-Mail") },
                            placeholder = { Text("E-Mail") } ,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.AlternateEmail,
                                    contentDescription = null
                                )
                            },
                            colors = TextFieldDefaults.textFieldColors(
                                cursorColor = accent,
                                errorCursorColor = red,
                                errorLeadingIconColor = red,
                                disabledPlaceholderColor = gray,
                                focusedIndicatorColor = accent,
                                focusedLabelColor = accent,
                                focusedLeadingIconColor = accent,
                                textColor = accent,
                                disabledTextColor = gray,
                                containerColor = SOZIPColorPalette.current.btnColor,
                                unfocusedLabelColor = SOZIPColorPalette.current.txtColor,
                                unfocusedLeadingIconColor = SOZIPColorPalette.current.txtColor,
                                unfocusedIndicatorColor = SOZIPColorPalette.current.txtColor,
                                unfocusedSupportingTextColor = SOZIPColorPalette.current.txtColor,
                                selectionColors = TextSelectionColors(handleColor = accent, backgroundColor = white)
                            ),
                            maxLines = 1,
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(20.dp))


                        TextField(
                            value = password.value,
                            onValueChange = { textVal : String -> password.value = textVal },
                            modifier = Modifier
                                .fillMaxWidth()
                                .shadow(5.dp),
                            label = { Text("비밀번호") },
                            placeholder = { Text("비밀번호") } ,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Key,
                                    contentDescription = null
                                )
                            },
                            colors = TextFieldDefaults.textFieldColors(
                                cursorColor = accent,
                                errorCursorColor = red,
                                errorLeadingIconColor = red,
                                disabledPlaceholderColor = gray,
                                focusedIndicatorColor = accent,
                                focusedLabelColor = accent,
                                focusedLeadingIconColor = accent,
                                textColor = accent,
                                disabledTextColor = gray,
                                containerColor = SOZIPColorPalette.current.btnColor,
                                unfocusedLabelColor = SOZIPColorPalette.current.txtColor,
                                unfocusedLeadingIconColor = SOZIPColorPalette.current.txtColor,
                                unfocusedIndicatorColor = SOZIPColorPalette.current.txtColor,
                                unfocusedSupportingTextColor = SOZIPColorPalette.current.txtColor,
                                selectionColors = TextSelectionColors(handleColor = accent, backgroundColor = white)

                            ),
                            visualTransformation = PasswordVisualTransformation(),
                            maxLines = 1,
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Button(onClick = {  },
                            modifier = Modifier
                                .fillMaxWidth(),
                            enabled = !email.value.isEmpty() && !password.value.isEmpty(),
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
                                    inclusive = true
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
                                    Text("처음 사용하시나요?", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                    Text("학교 인증 바로가기", fontSize = 12.sp)
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