package com.eje.sozip.userManagement.ui

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.eje.sozip.frameworks.helper.DataStoreUtil
import com.eje.sozip.frameworks.models.OnStartScreens
import com.eje.sozip.frameworks.ui.StartActivity
import com.eje.sozip.ui.theme.SOZIPColorPalette
import com.eje.sozip.ui.theme.SOZIPTheme
import com.eje.sozip.ui.theme.accent
import com.eje.sozip.ui.theme.gray
import com.eje.sozip.ui.theme.red
import com.eje.sozip.ui.theme.white
import com.eje.sozip.userManagement.helper.UserManagement
import com.eje.sozip.userManagement.models.ChangePasswordResultModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPasswordView(){
    val auth = FirebaseAuth.getInstance()
    val currentPassword = remember {
        mutableStateOf("")
    }

    val newPassword = remember{
        mutableStateOf("")
    }
    
    val checkPassword = remember{
        mutableStateOf("")
    }

    val alertModel = remember{
        mutableStateOf(ChangePasswordResultModel.ERROR)
    }

    val alertTitleMap = mapOf(ChangePasswordResultModel.ERROR to "오류", ChangePasswordResultModel.PASSWORD_DOES_NOT_MATCH to "비밀번호 불일치", ChangePasswordResultModel.WEAK_PASSWORD to "안전하지 않은 비밀번호", ChangePasswordResultModel.WRONG_PASSWORD to "비밀번호 불일치")
    val alertContentsMap = mapOf(ChangePasswordResultModel.ERROR to "요청하신 작업을 처리하는 중 문제가 발생했습니다.\n정상 로그인 여부, 네트워크 상태를 확인하거나 나중에 다시 시도해주세요.",
                                ChangePasswordResultModel.PASSWORD_DOES_NOT_MATCH to "비밀번호가 일치하지 않습니다.",
                                ChangePasswordResultModel.WRONG_PASSWORD to "비밀번호가 일치하지 않습니다.",
                                ChangePasswordResultModel.WEAK_PASSWORD to "보안을 위해 6자리 이상의 비밀번호를 설정해주세요.")
    val showFailAlert = remember{
        mutableStateOf(false)
    }

    val showSuccessAlert = remember{
        mutableStateOf(false)
    }

    val showDialog = remember{
        mutableStateOf(false)
    }

    val context = LocalContext.current
    val dataStoreUtil = DataStoreUtil(context)
    val coroutineScope = rememberCoroutineScope()

    val helper = UserManagement()

    SOZIPTheme {
        Surface(color = SOZIPColorPalette.current.background, modifier = Modifier.fillMaxSize()) {
            Scaffold(topBar = {
                TopAppBar (
                    title = {
                        Text(text = "비밀번호 변경", color = SOZIPColorPalette.current.txtColor)
                    },
                    navigationIcon = {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null, tint = accent)
                        }
                    },
                    colors = TopAppBarDefaults.smallTopAppBarColors(
                        containerColor = SOZIPColorPalette.current.background,
                        titleContentColor = SOZIPColorPalette.current.txtColor
                    )
                )
            }, content = {
                Column(
                    Modifier
                        .fillMaxSize()
                        .background(SOZIPColorPalette.current.background)
                        .padding(it)) {
                    Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(imageVector = Icons.Default.Lock, contentDescription = null, tint = SOZIPColorPalette.current.txtColor, modifier = Modifier.size(20.dp))
                        Text(text = "${auth.currentUser?.email}\n계정의 비밀번호를 변경합니다.", color = SOZIPColorPalette.current.txtColor, fontSize = 18.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                        Spacer(modifier = Modifier.height(10.dp))

                        Text(text = "계속하려면 현재 비밀번호를 입력하고 진행하십시오.", color = gray, fontSize = 10.sp)

                        Spacer(modifier = Modifier.height(20.dp))

                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = currentPassword.value,
                            onValueChange = { textVal : String -> currentPassword.value = textVal },
                            label = { androidx.compose.material3.Text("비밀번호") },
                            placeholder = { androidx.compose.material3.Text("비밀번호") } ,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            leadingIcon = {
                                androidx.compose.material3.Icon(
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

                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = newPassword.value,
                            onValueChange = { textVal : String -> newPassword.value = textVal },
                            label = { androidx.compose.material3.Text("새 비밀번호") },
                            placeholder = { androidx.compose.material3.Text("새 비밀번호") } ,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            leadingIcon = {
                                androidx.compose.material3.Icon(
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

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(text = "보안을 위해 6자 이상의 비밀번호를 설정해주세요.", color = gray, fontSize = 10.sp)

                        Spacer(modifier = Modifier.height(20.dp))

                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = checkPassword.value,
                            onValueChange = { textVal : String -> checkPassword.value = textVal },
                            label = { androidx.compose.material3.Text("한번 더") },
                            placeholder = { androidx.compose.material3.Text("한번 더") } ,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            leadingIcon = {
                                androidx.compose.material3.Icon(
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

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(text = "비밀번호 변경 시 자동으로 로그아웃됩니다.", color = gray, fontSize = 10.sp)

                        AnimatedVisibility(visible = currentPassword.value != "" && newPassword.value.count() >= 6 && newPassword.value == checkPassword.value) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally){
                                Spacer(modifier = Modifier.weight(1f))

                                Button(onClick = {
                                    showDialog.value = true

                                    if(checkPassword.value != newPassword.value){
                                        alertModel.value = ChangePasswordResultModel.PASSWORD_DOES_NOT_MATCH
                                        showFailAlert.value = true
                                        showDialog.value = false
                                    }

                                     else if(newPassword.value.count() < 6){
                                        alertModel.value = ChangePasswordResultModel.WEAK_PASSWORD
                                        showFailAlert.value = true
                                        showDialog.value = false
                                    }

                                    else{
                                        helper.updatePassword(newPassword.value, currentPassword.value){
                                            if(it == ChangePasswordResultModel.SUCCESS){
                                                showDialog.value = false
                                                alertModel.value = ChangePasswordResultModel.SUCCESS
                                                showSuccessAlert.value = true
                                            } else{
                                                showDialog.value = false
                                                alertModel.value = it
                                                showFailAlert.value = true
                                            }
                                        }
                                    }
                                },
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    enabled = !currentPassword.value.isEmpty() && !checkPassword.value.isEmpty() && !newPassword.value.isEmpty() && newPassword.value == checkPassword.value && newPassword.value.count() >= 6,
                                    contentPadding = PaddingValues(20.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = accent, disabledContainerColor = gray
                                    ),
                                    elevation = ButtonDefaults.buttonElevation(5.dp, disabledElevation = 5.dp)
                                ) {
                                    Row{
                                        androidx.compose.material3.Text("비밀번호 변경하기", color = white)
                                        androidx.compose.material3.Icon(imageVector = Icons.Default.ChevronRight, contentDescription = null, tint = white)
                                    }
                                }
                            }
                        }

                        if (showDialog.value) {
                            Dialog(
                                onDismissRequest = { showDialog.value = false },
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

                        if(showSuccessAlert.value){
                            AlertDialog(
                                onDismissRequest = { showSuccessAlert.value = false },

                                confirmButton = {
                                    TextButton(onClick = {
                                        showSuccessAlert.value = false
                                        coroutineScope.launch{
                                            dataStoreUtil.clearDataStore()
                                        }

                                        context.startActivity(
                                            Intent(
                                            context, StartActivity :: class.java)
                                        )
                                    }){
                                        androidx.compose.material3.Text("확인", color = accent, fontWeight = FontWeight.Bold)
                                    }
                                },
                                title = {
                                    androidx.compose.material3.Text("비밀번호 변경 완료")
                                },
                                text = {
                                    androidx.compose.material3.Text("비밀번호가 변경되었어요!")
                                },
                                icon = {
                                    androidx.compose.material3.Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null)
                                }
                            )
                        }

                        if(showFailAlert.value){
                            AlertDialog(
                                onDismissRequest = { showFailAlert.value = false },

                                confirmButton = {
                                    TextButton(onClick = {
                                        showFailAlert.value = false
                                    }){
                                        androidx.compose.material3.Text("확인", color = accent, fontWeight = FontWeight.Bold)
                                    }
                                },
                                title = {
                                    androidx.compose.material3.Text(alertTitleMap.get(alertModel.value)!!)
                                },
                                text = {
                                    androidx.compose.material3.Text(alertContentsMap.get(alertModel.value)!!)
                                },
                                icon = {
                                    androidx.compose.material3.Icon(imageVector = Icons.Default.Cancel, contentDescription = null)
                                }
                            )
                        }
                    }
                }
            })
        }
    }
}

@Preview
@Composable
fun EditPasswordView_previews(){
    EditPasswordView()
}