package com.eje.sozip.userManagement.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
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
import com.eje.sozip.ui.theme.red
import com.eje.sozip.ui.theme.white
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpView(){
    val title = remember{
        mutableStateOf("반가워요!")
    }

    val name = remember{
        mutableStateOf("")
    }

    val nickName = remember{
        mutableStateOf("")
    }

    val phoneNumber = remember{
        mutableStateOf("")
    }

    val studentNo = remember{
        mutableStateOf("")
    }

    val isLicenseAccepted = remember{
        mutableStateOf(false)
    }

    val isPrivacyLicenseAccepted = remember{
        mutableStateOf(false)
    }

    val isMarketingLicenseAccepted = remember{
        mutableStateOf(false)
    }

    val showView = remember{
        mutableStateOf(false)
    }

    val navController = rememberNavController()

    SOZIPTheme{
        NavHost(navController = navController, startDestination = "SignUpView"){
            composable(route = "AdditionalInfoView"){
                AdditionalUserInfoView(
                    name = name.value,
                    nickName = nickName.value,
                    phoneNumber = phoneNumber.value,
                    studentNo = studentNo.value,
                    acceptMarketingInfo = isMarketingLicenseAccepted.value
                )
            }

            composable(route = "SignUpView"){
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = SOZIPColorPalette.current.background
                ){
                    Scaffold(topBar = {
                        androidx.compose.material3.TopAppBar(
                            title = { androidx.compose.material.Text(text = "회원가입", color = SOZIPColorPalette.current.txtColor) },
                            navigationIcon = {
                                IconButton(onClick = { /*TODO*/ }) {
                                    androidx.compose.material.Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null, tint = accent)
                                }
                            },
                            colors = TopAppBarDefaults.smallTopAppBarColors(
                                containerColor = SOZIPColorPalette.current.background,
                                titleContentColor = SOZIPColorPalette.current.txtColor
                            )
                        )
                    }, content = {
                        Column(modifier = Modifier.fillMaxSize().background(SOZIPColorPalette.current.background).padding(it)) {
                            Column(modifier = Modifier
                                .padding(20.dp)
                                .verticalScroll(rememberScrollState()),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Top){

                                AnimatedVisibility(visible = true){
                                    Text(text = title.value,
                                        color = SOZIPColorPalette.current.txtColor,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 24.sp)
                                }

                                LaunchedEffect(Unit){
                                    delay(1.seconds)
                                    title.value = "이름을 입력해주세요."
                                    showView.value = true
                                }

                                Spacer(modifier = Modifier.height(40.dp))

                                AnimatedVisibility(visible = showView.value){

                                    OutlinedTextField(
                                        modifier = Modifier.fillMaxWidth(),
                                        value = name.value,
                                        onValueChange = { textVal : String -> name.value = textVal },
                                        label = { Text("이름") },
                                        placeholder = { Text("이름") } ,
                                        leadingIcon = {
                                            Icon(
                                                imageVector = Icons.Default.AccountCircle,
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
                                }

                                Spacer(modifier = Modifier.height(20.dp))

                                AnimatedVisibility(visible = name.value != ""){
                                    title.value = "닉네임을 입력해주세요."

                                    OutlinedTextField(
                                        modifier = Modifier.fillMaxWidth(),
                                        value = nickName.value,
                                        onValueChange = { textVal : String -> nickName.value = textVal },
                                        label = { Text("닉네임") },
                                        placeholder = { Text("닉네임") } ,
                                        leadingIcon = {
                                            Icon(
                                                imageVector = Icons.Default.Person,
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
                                }

                                Spacer(modifier = Modifier.height(20.dp))

                                AnimatedVisibility(visible = nickName.value != ""){
                                    title.value = "휴대폰 번호를 입력해주세요."

                                    OutlinedTextField(
                                        modifier = Modifier.fillMaxWidth(),
                                        value = phoneNumber.value,
                                        onValueChange = { textVal : String -> phoneNumber.value = textVal },
                                        label = { Text("휴대폰 번호") },
                                        placeholder = { Text("휴대폰 번호") } ,
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                        leadingIcon = {
                                            Icon(
                                                imageVector = Icons.Default.Phone,
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
                                }

                                Spacer(modifier = Modifier.height(20.dp))

                                AnimatedVisibility(visible = phoneNumber.value != "") {
                                    title.value = "학번을 입력해주세요."

                                    OutlinedTextField(
                                        modifier = Modifier.fillMaxWidth(),
                                        value = studentNo.value,
                                        onValueChange = { textVal : String -> studentNo.value = textVal },
                                        label = { Text("학번") },
                                        placeholder = { Text("학번") } ,
                                        leadingIcon = {
                                            Icon(
                                                imageVector = Icons.Default.Badge,
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
                                }

                                Spacer(modifier = Modifier.height(20.dp))


                                AnimatedVisibility(visible = studentNo.value != "") {
                                    title.value = "이용약관을 읽어주세요."

                                    Column(horizontalAlignment = Alignment.Start){
                                        Row(horizontalArrangement = Arrangement.Start){
                                            Text(text = "이용약관을 읽어주세요.", color= SOZIPColorPalette.current.txtColor, fontWeight = FontWeight.Bold)
                                        }

                                        Spacer(modifier = Modifier.height(5.dp))

                                        Row(horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically){
                                            Checkbox(checked = isLicenseAccepted.value, onCheckedChange = {isChecked -> isLicenseAccepted.value = isChecked}, colors = CheckboxDefaults.colors(
                                                uncheckedColor = accent,
                                                checkedColor = accent
                                            ))
                                            Text(text = "서비스 이용약관 (필수)", color = SOZIPColorPalette.current.txtColor, fontSize = 12.sp)

                                            Spacer(modifier = Modifier.weight(1f))

                                            Button(onClick = {  }, colors = ButtonDefaults.buttonColors(containerColor = SOZIPColorPalette.current.background)) {
                                                Text("읽기", color= accent, fontSize = 12.sp)
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(5.dp))

                                        Row(horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically){
                                            Checkbox(checked = isPrivacyLicenseAccepted.value, onCheckedChange = {isChecked -> isPrivacyLicenseAccepted.value = isChecked}, colors = CheckboxDefaults.colors(
                                                uncheckedColor = accent,
                                                checkedColor = accent
                                            ))
                                            Text(text = "개인정보 수집 및 이용방침 (필수)", color = SOZIPColorPalette.current.txtColor, fontSize = 12.sp)

                                            Spacer(modifier = Modifier.weight(1f))

                                            Button(onClick = {  }, colors = ButtonDefaults.buttonColors(containerColor = SOZIPColorPalette.current.background)) {
                                                Text("읽기", color= accent, fontSize = 12.sp)
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(5.dp))

                                        Row(horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically){
                                            Checkbox(checked = isMarketingLicenseAccepted.value, onCheckedChange = {isChecked -> isMarketingLicenseAccepted.value = isChecked}, colors = CheckboxDefaults.colors(
                                                uncheckedColor = accent,
                                                checkedColor = accent
                                            ))
                                            Text(text = "마케팅 정보 및 알림 수신 (선택)", color = SOZIPColorPalette.current.txtColor, fontSize = 12.sp)

                                            Spacer(modifier = Modifier.weight(1f))

                                            Button(onClick = {  }, colors = ButtonDefaults.buttonColors(containerColor = SOZIPColorPalette.current.background)) {
                                                Text("읽기", color= accent, fontSize = 12.sp)
                                            }
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(20.dp))

                                AnimatedVisibility(visible = isLicenseAccepted.value && isPrivacyLicenseAccepted.value) {
                                    title.value = "다음 단계로 이동해주세요!"

                                    Button(onClick = {
                                        navController.navigate(OnStartScreens.additionalInfoView){
                                            popUpTo(OnStartScreens.SignInView){
                                                inclusive = true
                                            }
                                        }
                                    },
                                        modifier = Modifier
                                            .fillMaxWidth(),
                                        enabled = !name.value.isEmpty() && !nickName.value.isEmpty() && !studentNo.value.isEmpty() && !phoneNumber.value.isEmpty() && isLicenseAccepted.value && isPrivacyLicenseAccepted.value,
                                        contentPadding = PaddingValues(20.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = accent, disabledContainerColor = gray
                                        ),
                                        elevation = ButtonDefaults.buttonElevation(5.dp, disabledElevation = 5.dp)
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically){
                                            Text("다음 단계로", color = white)
                                            Icon(imageVector = Icons.Default.ChevronRight, contentDescription = null, tint = white)
                                        }
                                    }
                                }
                            }
                        }

                    })

                }
            }
        }

    }
}

@Preview
@Composable
fun SignUpView_preview(){
    SignUpView()
}

