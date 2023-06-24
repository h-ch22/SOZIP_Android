package com.eje.sozip.userManagement.ui

import android.R.attr.text
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
import androidx.compose.material.icons.filled.AddCard
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Warning
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.eje.sozip.ui.theme.SOZIPColorPalette
import com.eje.sozip.ui.theme.accent
import com.eje.sozip.ui.theme.gray
import com.eje.sozip.ui.theme.red
import com.eje.sozip.ui.theme.white
import com.eje.sozip.userManagement.helper.UserManagement


fun getInitial(name: String) : String{
    val initial = listOf(
        "ㄱ", "ㄲ", "ㄴ", "ㄷ", "ㄸ",
        "ㄹ", "ㅁ", "ㅂ", "ㅃ", "ㅅ",
        "ㅆ", "ㅇ", "ㅈ", "ㅉ", "ㅊ",
        "ㅋ", "ㅌ", "ㅍ", "ㅎ"
    )

    var nameInitial = ""

    val chName: Char = name[0]

    for (n in name){
        if (n.code >= 0xAC00) {
            val uniVal = n.code - 0xAC00
            val cho = (uniVal - uniVal % 28) / 28 / 21
            nameInitial += initial[cho]
        } else{
            return name
        }
    }

    return nameInitial
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAccountDetailsView(bank: String){
    val accountNumber = remember {
        mutableStateOf("")
    }

    val name = remember{
        mutableStateOf("")
    }

    var showSuccessAlert by remember { mutableStateOf(false) }
    var showFailAlert by remember { mutableStateOf(false) }

    var buttonDisable by remember{
        mutableStateOf(false)
    }

    var showProgress by remember{
        mutableStateOf(false)
    }

    val helper = UserManagement()

    Surface(modifier = Modifier.fillMaxSize(), color = SOZIPColorPalette.current.background) {
        Scaffold(topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "계좌 상세 정보 입력",
                        color = SOZIPColorPalette.current.txtColor
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = null,
                            tint = accent
                        )
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = SOZIPColorPalette.current.background,
                    titleContentColor = SOZIPColorPalette.current.txtColor
                )
            )
        }, content = {
            Surface(
                color = SOZIPColorPalette.current.background,
                modifier = Modifier.fillMaxSize()
            ) {
                Column(modifier = Modifier.padding(it)) {
                    Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Top) {
                        Icon(
                            imageVector = Icons.Default.AddCard,
                            contentDescription = null,
                            tint = accent,
                            modifier = Modifier.size(50.dp)
                        )

                        Text(
                            text = "${bank}\n계좌 정보를 입력해주세요.",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = accent,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = accountNumber.value,
                            onValueChange = { textVal : String -> accountNumber.value = textVal },
                            label = { androidx.compose.material3.Text("계좌 번호") },
                            placeholder = { androidx.compose.material3.Text("계좌 번호") } ,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            leadingIcon = {
                                androidx.compose.material3.Icon(
                                    imageVector = Icons.Default.CreditCard,
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
                            value = name.value,
                            onValueChange = { textVal : String -> name.value = textVal },
                            label = { androidx.compose.material3.Text("예금주") },
                            placeholder = { androidx.compose.material3.Text("예금주") } ,
                            leadingIcon = {
                                androidx.compose.material3.Icon(
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

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(text = if(name.value.isEmpty()) "소집 멤버들에게 이름이 초성으로 표시됩니다." else "소집 멤버들에게 이름이 ${getInitial(name.value)}으로 표시됩니다.", fontSize = 12.sp, color = gray)

                        Spacer(modifier = Modifier.height(20.dp))

                        Button(onClick = {
                            buttonDisable = true
                            showProgress = true

                            helper.addAccountInfo(bank, accountNumber.value, name.value){
                                if(it){
                                    showProgress = false
                                    showSuccessAlert = true
                                } else{
                                    showProgress = false
                                    showFailAlert = true
                                }
                            }

                        },
                            modifier = Modifier
                                .fillMaxWidth(),
                            enabled = !name.value.isEmpty() && !accountNumber.value.isEmpty() && !buttonDisable,
                            contentPadding = PaddingValues(20.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = accent, disabledContainerColor = gray
                            ),
                            elevation = ButtonDefaults.buttonElevation(5.dp, disabledElevation = 5.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically){
                                androidx.compose.material3.Text("계좌 추가", color = white)
                                androidx.compose.material3.Icon(imageVector = Icons.Default.ChevronRight, contentDescription = null, tint = white)
                            }
                        }
                    }

                    if(showSuccessAlert){
                        AlertDialog(
                            onDismissRequest = {  },
                            confirmButton = {
                                TextButton(onClick = {
                                    showSuccessAlert = false
                                    buttonDisable = false
                                }){
                                    androidx.compose.material3.Text("확인", color = accent, fontWeight = FontWeight.Bold)
                                }
                            },
                            title = {
                                androidx.compose.material3.Text("계좌 추가 완료")
                            },
                            text = {
                                androidx.compose.material3.Text("계좌가 추가되었어요!")
                            },
                            icon = {
                                androidx.compose.material3.Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null)
                            }
                        )
                    }

                    if(showFailAlert){
                        AlertDialog(
                            onDismissRequest = {  },
                            confirmButton = {
                                TextButton(onClick = {
                                    showFailAlert = false
                                    buttonDisable = false
                                }){
                                    androidx.compose.material3.Text(
                                        "확인",
                                        color = accent,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            },
                            title = {
                                androidx.compose.material3.Text("오류")
                            },
                            text = {
                                androidx.compose.material3.Text("요청을 처리하는 중 문제가 발생했습니다.\n로그인 정보가 올바르지 않거나, 네트워크 상태를 확인한 후 다시 시도하십시오.")
                            },
                            icon = {
                                androidx.compose.material3.Icon(
                                    imageVector = Icons.Default.Warning,
                                    contentDescription = null
                                )
                            }
                        )
                    }

                    if(showProgress){
                        Dialog(
                            onDismissRequest = {  },
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
                }
            }
        })
    }
}

@Preview
@Composable
fun AddAccountDetailsView_previews(){
    AddAccountDetailsView(bank = "")
}
