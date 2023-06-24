package com.eje.sozip.userManagement.ui

import android.content.Intent
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.Output
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.eje.sozip.frameworks.helper.AES256Util
import com.eje.sozip.frameworks.helper.DataStoreUtil
import com.eje.sozip.frameworks.ui.StartActivity
import com.eje.sozip.ui.theme.SOZIPColorPalette
import com.eje.sozip.ui.theme.SOZIPTheme
import com.eje.sozip.ui.theme.SOZIP_BG_1
import com.eje.sozip.ui.theme.SOZIP_BG_2
import com.eje.sozip.ui.theme.SOZIP_BG_3
import com.eje.sozip.ui.theme.SOZIP_BG_4
import com.eje.sozip.ui.theme.SOZIP_BG_5
import com.eje.sozip.ui.theme.accent
import com.eje.sozip.ui.theme.gray
import com.eje.sozip.ui.theme.red
import com.eje.sozip.ui.theme.white
import com.eje.sozip.userManagement.helper.UserManagement
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileView() {
    val currentIcon = remember {
        mutableStateOf(UserManagement.userInfo?.profile)
    }

    val currentBackground = remember {
        mutableStateOf(UserManagement.userInfo?.profile_bg)
    }

    val iconList = remember {
        mutableStateListOf("\uD83D\uDC37", "\uD83D\uDC30", "\uD83D\uDC2F", "\uD83D\uDC35", "\uD83D\uDC25")
    }


    val bgList = remember{
        mutableStateListOf(SOZIP_BG_1, SOZIP_BG_2, SOZIP_BG_3, SOZIP_BG_4, SOZIP_BG_5)
    }

    val nickName = remember{
        mutableStateOf(AES256Util.decrypt(UserManagement.userInfo?.nickName))
    }

    val helper = UserManagement()

    var showDialog by remember { mutableStateOf(false) }
    var showSuccessAlert by remember { mutableStateOf(false) }
    var showFailAlert by remember { mutableStateOf(false) }
    var showSignOutAlert by remember { mutableStateOf(false) }
    var showSecessionAlert by remember { mutableStateOf(false) }
    var showSecessionCompleteAlert by remember{ mutableStateOf(false) }
    var showSecessionFailAlert by remember{ mutableStateOf(false) }
    val context = LocalContext.current
    val dataStoreUtil = DataStoreUtil(context)
    val coroutineScope = rememberCoroutineScope()
    val navController = rememberNavController()

    SOZIPTheme {
        NavHost(navController = navController, startDestination = "EditProfileView" ){
            composable("EditPasswordView"){
                EditPasswordView()
            }

            composable("UpdatePhoneView"){
                UpdatePhoneView()
            }

            composable("AccountManagementView"){
                AccountManagementView(false)
            }

            composable(route = "EditProfileView"){
                Surface(color = SOZIPColorPalette.current.background, modifier = Modifier.fillMaxSize()) {
                    Scaffold(topBar = {
                        TopAppBar (
                            title = {
                                Text(text = "프로필 정보 변경", color = SOZIPColorPalette.current.txtColor)
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
                                .background(SOZIPColorPalette.current.background)
                                .padding(it)
                                .fillMaxSize()) {
                            Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
                                .padding(20.dp)
                                .verticalScroll(
                                    rememberScrollState()
                                )) {
                                androidx.compose.material3.Text(UserManagement.convertProfileToEmoji(
                                    currentIcon.value ?: "chick"
                                ), modifier = Modifier
                                    .drawBehind {
                                        drawCircle(
                                            color = UserManagement.convertProfileBGToColor(
                                                currentBackground.value ?: "bg_3"
                                            )
                                        )
                                    }
                                    .padding(5.dp), fontSize = 70.sp)

                                Spacer(modifier = Modifier.height(10.dp))

                                Text(AES256Util.decrypt(UserManagement.userInfo?.name), color = SOZIPColorPalette.current.txtColor, fontSize = 18.sp, fontWeight = FontWeight.Bold)

                                Spacer(modifier = Modifier.height(20.dp))

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(text = "닉네임 변경", color = gray, fontSize = 10.sp)

                                    Spacer(modifier = Modifier.weight(1f))
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                OutlinedTextField(
                                    modifier = Modifier.fillMaxWidth(),
                                    value = nickName.value,
                                    onValueChange = { textVal : String -> nickName.value = textVal },
                                    label = { androidx.compose.material3.Text("닉네임") },
                                    placeholder = { androidx.compose.material3.Text("닉네임") } ,
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

                                Spacer(modifier = Modifier.height(20.dp))

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(text = "프로필 이미지 설정", color = gray, fontSize = 10.sp)

                                    Spacer(modifier = Modifier.weight(1f))
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                LazyRow(horizontalArrangement = Arrangement.spacedBy(20.dp), verticalAlignment = Alignment.CenterVertically){
                                    itemsIndexed(key = {index, item ->
                                        item
                                    }, items = iconList){index, item ->
                                        OutlinedButton(onClick = {
                                            currentIcon.value = UserManagement.convertEmojiToProfile(item)
                                        },
                                            modifier = Modifier.size(50.dp),
                                            border = BorderStroke(1.dp, if(UserManagement.convertProfileToEmoji(currentIcon.value ?: "chick") == item) accent else Color.Transparent),
                                            shape = CircleShape,
                                            contentPadding = PaddingValues(1.dp)
                                        ) {
                                            Text(text = item, fontSize = 35.sp)
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(20.dp))

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(text = "프로필 배경 설정", color = gray, fontSize = 10.sp)

                                    Spacer(modifier = Modifier.weight(1f))
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                LazyRow(horizontalArrangement = Arrangement.spacedBy(20.dp), verticalAlignment = Alignment.CenterVertically){
                                    itemsIndexed(key = {index, item ->
                                        UserManagement.convertColorToProfileBG(item)
                                    }, items = bgList){index, item ->
                                        Button(onClick = {
                                            currentBackground.value = UserManagement.convertColorToProfileBG(item)
                                        },
                                            modifier = Modifier.size(50.dp),
                                            shape = CircleShape,
                                            contentPadding = PaddingValues(1.dp),
                                            colors = ButtonDefaults.buttonColors(containerColor = item)
                                        ) {
                                            if(currentBackground.value == UserManagement.convertColorToProfileBG(item)){
                                                Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = white)
                                            }
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(20.dp))

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(text = "계정 정보 관리", color = gray, fontSize = 10.sp)

                                    Spacer(modifier = Modifier.weight(1f))
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                Button(onClick = {
                                    navController.navigate("EditPasswordView"){
                                        popUpTo("EditProfileView"){
                                            inclusive = false
                                        }
                                    }
                                },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = SOZIPColorPalette.current.btnColor
                                    ), elevation = ButtonDefaults.buttonElevation(5.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(60.dp),
                                    shape = RoundedCornerShape(15.dp),
                                    contentPadding = PaddingValues(start = 15.dp)
                                ) {
                                    Row(horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically) {
                                        Icon(imageVector = Icons.Default.Key, contentDescription = null, tint = SOZIPColorPalette.current.txtColor)
                                        Spacer(modifier = Modifier.width(5.dp))
                                        androidx.compose.material3.Text(
                                            "비밀번호 변경",
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = SOZIPColorPalette.current.txtColor
                                        )
                                        Spacer(modifier = Modifier.weight(1f))
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                Button(onClick = {
                                    navController.navigate("UpdatePhoneView"){
                                        popUpTo("EditProfileView"){
                                            inclusive = false
                                        }
                                    }
                                },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = SOZIPColorPalette.current.btnColor
                                    ), elevation = ButtonDefaults.buttonElevation(5.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(60.dp),
                                    shape = RoundedCornerShape(15.dp),
                                    contentPadding = PaddingValues(start = 15.dp)
                                ) {
                                    Row(horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically) {
                                        Icon(imageVector = Icons.Default.Phone, contentDescription = null, tint = SOZIPColorPalette.current.txtColor)
                                        Spacer(modifier = Modifier.width(5.dp))
                                        androidx.compose.material3.Text(
                                            "연락처 변경",
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = SOZIPColorPalette.current.txtColor
                                        )
                                        Spacer(modifier = Modifier.weight(1f))
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                Button(onClick = {
                                    navController.navigate("AccountManagementView"){
                                        popUpTo("EditProfileView"){
                                            inclusive = false
                                        }
                                    }
                                },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = SOZIPColorPalette.current.btnColor
                                    ), elevation = ButtonDefaults.buttonElevation(5.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(60.dp),
                                    shape = RoundedCornerShape(15.dp),
                                    contentPadding = PaddingValues(start = 15.dp)
                                ) {
                                    Row(horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically) {
                                        Icon(imageVector = Icons.Default.CreditCard, contentDescription = null, tint = SOZIPColorPalette.current.txtColor)
                                        Spacer(modifier = Modifier.width(5.dp))
                                        androidx.compose.material3.Text(
                                            "계좌 관리",
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = SOZIPColorPalette.current.txtColor
                                        )
                                        Spacer(modifier = Modifier.weight(1f))
                                    }
                                }

                                Spacer(modifier = Modifier.height(20.dp))

                                Button(onClick = {
                                    showDialog = true

                                    helper.updateProfile(
                                        nickName = nickName.value,
                                        profile = currentIcon.value ?: "chick",
                                        profile_bg = currentBackground.value ?: "bg_3"
                                    ){
                                        if(it){
                                            showDialog = false
                                            showSuccessAlert = true
                                        } else{
                                            showDialog = false
                                            showFailAlert = true
                                        }
                                    }
                                },
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    enabled = currentBackground.value != UserManagement.userInfo?.profile_bg || currentIcon.value != UserManagement.userInfo?.profile || nickName.value != AES256Util.decrypt(UserManagement.userInfo?.nickName),
                                    contentPadding = PaddingValues(20.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = accent, disabledContainerColor = gray
                                    ),
                                    elevation = ButtonDefaults.buttonElevation(5.dp, disabledElevation = 5.dp)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically){
                                        androidx.compose.material3.Text("프로필 변경하기", color = white)
                                        androidx.compose.material3.Icon(
                                            imageVector = Icons.Default.ChevronRight,
                                            contentDescription = null,
                                            tint = white
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(5.dp))

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    TextButton(onClick = { showSignOutAlert = true }) {
                                        Text(text = "로그아웃", color = gray, fontSize = 10.sp, textDecoration = TextDecoration.Underline)
                                    }

                                    Text(text = "또는", color = gray, fontSize = 10.sp)

                                    TextButton(onClick = { showSecessionAlert = true }) {
                                        Text(text = "회원 탈퇴", color = gray, fontSize = 10.sp, textDecoration = TextDecoration.Underline)
                                    }
                                }

                                if (showDialog) {
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

                                if(showSuccessAlert){
                                    AlertDialog(
                                        onDismissRequest = { showSuccessAlert = false },

                                        confirmButton = {
                                            TextButton(onClick = {
                                                showSuccessAlert = false
                                            }){
                                                androidx.compose.material3.Text("확인", color = accent, fontWeight = FontWeight.Bold)
                                            }
                                        },
                                        title = {
                                            androidx.compose.material3.Text("프로필 변경 완료")
                                        },
                                        text = {
                                            androidx.compose.material3.Text("프로필이 변경되었어요!")
                                        },
                                        icon = {
                                            androidx.compose.material3.Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null)
                                        }
                                    )
                                }

                                if(showFailAlert){
                                    AlertDialog(
                                        onDismissRequest = { showFailAlert = false },

                                        confirmButton = {
                                            TextButton(onClick = {
                                                showFailAlert = false
                                            }){
                                                androidx.compose.material3.Text("확인", color = accent, fontWeight = FontWeight.Bold)
                                            }
                                        },
                                        title = {
                                            androidx.compose.material3.Text("오류")
                                        },
                                        text = {
                                            androidx.compose.material3.Text("요청하신 작업을 처리하는 중 문제가 발생했습니다.\n정상 로그인 여부, 네트워크 상태를 확인하거나 나중에 다시 시도해주세요.")
                                        },
                                        icon = {
                                            androidx.compose.material3.Icon(imageVector = Icons.Default.Cancel, contentDescription = null)
                                        }
                                    )
                                }

                                if(showSignOutAlert){
                                    AlertDialog(
                                        onDismissRequest = { showSignOutAlert = false },

                                        confirmButton = {
                                            TextButton(onClick = {
                                                showSignOutAlert = false
                                                showDialog = true

                                                helper.signOut {
                                                    if(it){
                                                        showDialog = false
                                                        coroutineScope.launch{
                                                            dataStoreUtil.clearDataStore()
                                                        }

                                                        context.startActivity(Intent(
                                                            context, StartActivity :: class.java)
                                                        )

                                                    } else{
                                                        showDialog = false
                                                        showFailAlert = true
                                                    }
                                                }
                                            }){
                                                androidx.compose.material3.Text("예", color = accent, fontWeight = FontWeight.Bold)
                                            }
                                        },
                                        dismissButton = {
                                            TextButton(onClick = {
                                                showSignOutAlert = false
                                            }){
                                                Text(text = "아니오", color = accent, fontWeight = FontWeight.Bold)
                                            }
                                        },
                                        title = {
                                            androidx.compose.material3.Text("로그아웃")
                                        },
                                        text = {
                                            androidx.compose.material3.Text("로그아웃 시 자동 로그인이 해제되며, 다시 로그인하셔야 합니다.\n계속 하시겠습니까?")
                                        },
                                        icon = {
                                            androidx.compose.material3.Icon(imageVector = Icons.Default.Output, contentDescription = null)
                                        }
                                    )
                                }

                                if(showSecessionAlert){
                                    AlertDialog(
                                        onDismissRequest = { showSecessionAlert = false },

                                        confirmButton = {
                                            TextButton(onClick = {
                                                showSecessionAlert = false
                                                showDialog = true

                                                helper.secession {
                                                    if(it){
                                                        showDialog = false
                                                        showSecessionCompleteAlert = true

                                                    } else{
                                                        showDialog = false
                                                        showSecessionFailAlert = true
                                                    }
                                                }
                                            }){
                                                androidx.compose.material3.Text("예", color = accent, fontWeight = FontWeight.Bold)
                                            }
                                        },
                                        dismissButton = {
                                            TextButton(onClick = {
                                                showSecessionAlert = false
                                            }){
                                                Text(text = "아니오", color = accent, fontWeight = FontWeight.Bold)
                                            }
                                        },
                                        title = {
                                            androidx.compose.material3.Text("회원 탈퇴 안내")
                                        },
                                        text = {
                                            androidx.compose.material3.Text("회원 탈퇴 시 모든 가입 정보가 제거되며, 복구할 수 없습니다.\n계속 하시겠습니까?")
                                        },
                                        icon = {
                                            androidx.compose.material3.Icon(imageVector = Icons.Default.Output, contentDescription = null)
                                        }
                                    )
                                }

                                if(showSecessionCompleteAlert){
                                    AlertDialog(
                                        onDismissRequest = {
                                            showSecessionCompleteAlert = false
                                            coroutineScope.launch{
                                                dataStoreUtil.clearDataStore()
                                            }

                                            context.startActivity(Intent(
                                                context, StartActivity :: class.java)
                                            )
                                        },

                                        confirmButton = {
                                            TextButton(onClick = {
                                                showSecessionCompleteAlert = false
                                                coroutineScope.launch{
                                                    dataStoreUtil.clearDataStore()
                                                }

                                                context.startActivity(Intent(
                                                    context, StartActivity :: class.java)
                                                )
                                            }){
                                                androidx.compose.material3.Text("확인", color = accent, fontWeight = FontWeight.Bold)
                                            }
                                        },
                                        title = {
                                            androidx.compose.material3.Text("감사 인사")
                                        },
                                        text = {
                                            androidx.compose.material3.Text("회원 탈퇴가 완료되었습니다.\n더 나은 서비스로 다시 만날 수 있도록 노력하겠습니다.\n그 동안 서비스를 이용해주셔서 감사합니다.")
                                        },
                                        icon = {
                                            androidx.compose.material3.Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null)
                                        }
                                    )
                                }

                                if(showSecessionFailAlert){
                                    AlertDialog(
                                        onDismissRequest = { showSecessionFailAlert = false },

                                        confirmButton = {
                                            TextButton(onClick = {
                                                showSecessionFailAlert = false
                                            }){
                                                androidx.compose.material3.Text("확인", color = accent, fontWeight = FontWeight.Bold)
                                            }
                                        },
                                        title = {
                                            androidx.compose.material3.Text("오류")
                                        },
                                        text = {
                                            androidx.compose.material3.Text("요청하신 작업을 처리하는 중 문제가 발생했습니다.\n이제이 고객센터로 문의해주세요.")
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

    }
}

@Preview
@Composable
fun EditProfileView_previews(){
    EditProfileView()
}