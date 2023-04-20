package com.eje.sozip.userManagement.ui

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Password
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.eje.sozip.frameworks.models.OnStartScreens
import com.eje.sozip.ui.theme.SOZIPColorPalette
import com.eje.sozip.ui.theme.SOZIPTheme
import com.eje.sozip.ui.theme.accent
import com.eje.sozip.ui.theme.gray
import com.eje.sozip.ui.theme.green
import com.eje.sozip.ui.theme.red
import com.eje.sozip.ui.theme.white
import com.eje.sozip.userManagement.models.AdditionalUserInfoViewModel
import com.eje.sozip.userManagement.models.UserInfoModel
import com.google.firebase.auth.AdditionalUserInfo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdditionalUserInfoView(
    name : String,
    nickName : String,
    phoneNumber : String,
    studentNo : String,
    acceptMarketingInfo : Boolean
){
    val viewModel = AdditionalUserInfoViewModel()

    val email = remember{
        mutableStateOf("")
    }

    val password = remember{
        mutableStateOf("")
    }

    val checkPassword = remember{
        mutableStateOf("")
    }

    val emailStatus = remember{
        mutableStateOf("")
    }

    val navController = rememberNavController()

    SOZIPTheme {
        NavHost(navController = navController, startDestination = "AdditionalInfoView") {
            composable(route = "InProgressView") {
                InProgressSignUpView(
                    userInfo = UserInfoModel(
                        name = name,
                        nickName = nickName,
                        phone = phoneNumber,
                        studentNo = studentNo,
                        email = email.value,
                        school = emailStatus.value
                    ),
                    isAcceptMarketing = acceptMarketingInfo,
                    password = password.value
                )
            }

            composable(route = "AdditionalInfoView"){
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = SOZIPColorPalette.current.background
                ){
                    Column(verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(20.dp)) {
                        Text(text="추가 정보를 입력해주세요.", color = SOZIPColorPalette.current.txtColor, fontWeight = FontWeight.Bold)

                        Spacer(modifier = Modifier.height(20.dp))

                        TextField(
                            value = email.value,
                            onValueChange = { textVal : String ->
                                email.value = textVal

                                if(!textVal.isEmpty()){
                                    if(textVal.contains("@")){
                                        val result = viewModel.getSchoolName(textVal)

                                        if(result == null){
                                            emailStatus.value = "지원 대상 학교가 아닙니다."
                                        } else{
                                            emailStatus.value = result
                                        }

                                    } else{
                                        emailStatus.value = "올바른 형식의 E-Mail을 입력해주세요."
                                    }
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .shadow(5.dp),
                            label = { Text("대학 E-Mail") },
                            placeholder = { Text("대학 E-Mail") } ,
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
                                selectionColors = TextSelectionColors(handleColor = accent, backgroundColor = accent.copy(alpha = 0.5f))
                            ),
                            maxLines = 1,
                            singleLine = true
                        )

                        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                            AnimatedVisibility(visible = !email.value.isEmpty()) {
                                Row(verticalAlignment = Alignment.CenterVertically){
                                    Icon(imageVector = if(emailStatus.value.contains("대학교")) Icons.Default.Check else Icons.Default.Cancel,
                                        contentDescription = null,
                                        tint = if(emailStatus.value.contains("대학교")) green else red)

                                    Spacer(modifier = Modifier.width(5.dp))

                                    Text(text = if(emailStatus.value.contains("대학교")) "감지된 학교 : ${emailStatus.value}" else "지원 대상 학교가 아닙니다.",
                                        fontSize = 10.sp,
                                        color = if(emailStatus.value.contains("대학교")) green else red )
                                }

                            }

                            Spacer(modifier = Modifier.weight(1f))

                            Button(onClick = {

                            }, colors = ButtonDefaults.buttonColors(
                                containerColor = SOZIPColorPalette.current.background
                            )) {
                                Text("지원 대상 학교 보기", fontSize = 10.sp, color = accent, textAlign = TextAlign.End)
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        AnimatedVisibility(visible = emailStatus.value.contains("대학교")) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally){
                                TextField(
                                    value = password.value,
                                    onValueChange = { textVal : String ->
                                        password.value = textVal
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .shadow(5.dp),
                                    label = { Text("비밀번호") },
                                    placeholder = { Text("비밀번호") } ,
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
                                        selectionColors = TextSelectionColors(handleColor = accent, backgroundColor = accent.copy(alpha = 0.5f))
                                    ),
                                    maxLines = 1,
                                    singleLine = true,
                                    visualTransformation = PasswordVisualTransformation()
                                )

                                Spacer(modifier = Modifier.height(5.dp))

                                Text("보안을 위해 6자리 이상의 비밀번호를 설정해주세요.", fontSize = 12.sp, color = gray)

                                Spacer(modifier = Modifier.height(20.dp))

                                TextField(
                                    value = checkPassword.value,
                                    onValueChange = { textVal : String ->
                                        checkPassword.value = textVal
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .shadow(5.dp),
                                    label = { Text("한번 더") },
                                    placeholder = { Text("한번 더") } ,
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
                                        selectionColors = TextSelectionColors(handleColor = accent, backgroundColor = accent.copy(alpha = 0.5f))
                                    ),
                                    maxLines = 1,
                                    singleLine = true,
                                    visualTransformation = PasswordVisualTransformation()
                                )
                            }

                        }

                        AnimatedVisibility(visible = !email.value.isEmpty() && password.value == checkPassword.value && emailStatus.value.contains("대학교") && password.value.length > 5) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally){
                                Spacer(modifier = Modifier.weight(1f))

                                Button(onClick = {
                                    navController.navigate(OnStartScreens.inProgressView){
                                        popUpTo(OnStartScreens.additionalInfoView){
                                            inclusive = true
                                        }
                                    }
                                },
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
                                        Text("가입하기", color = white)
                                        Icon(imageVector = Icons.Default.ChevronRight, contentDescription = null, tint = white)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

    }
}

@Preview
@Composable
fun AdditionalUserInfoView_preview(){
    AdditionalUserInfoView(
        name = "a",
        nickName = "a",
        phoneNumber = "a",
        studentNo = "a",
        acceptMarketingInfo = true
    )
}