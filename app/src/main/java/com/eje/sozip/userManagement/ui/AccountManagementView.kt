package com.eje.sozip.userManagement.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.eje.sozip.ui.theme.SOZIPColorPalette
import com.eje.sozip.ui.theme.SOZIPTheme
import com.eje.sozip.ui.theme.accent
import com.eje.sozip.ui.theme.gray
import com.eje.sozip.ui.theme.red
import com.eje.sozip.userManagement.helper.UserManagement
import com.eje.sozip.userManagement.models.AccountDataModel
import com.eje.sozip.userManagement.models.AccountInfoListModel

fun getAccountInfo() : SnapshotStateList<AccountDataModel>?{
    return if(UserManagement.accountInfo == null){
        null
    } else if(UserManagement.accountInfo!!.isEmpty()){
        mutableStateListOf()
    } else{
        val accounts : SnapshotStateList<AccountDataModel> = mutableStateListOf()

        for(account in UserManagement.accountInfo!!){
            accounts.add(account)
        }

        accounts
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun AccountManagementView(isModalSheet : Boolean = false){
    val helper = UserManagement()
    val navController = rememberNavController()

    var accountList = remember{
        getAccountInfo()
    }

    val showProgress = remember {
        mutableStateOf(true)
    }

    val showErrorAlert = remember {
        mutableStateOf(false)
    }

    var showConfirmDialog by remember{
        mutableStateOf(false)
    }

    var showSuccessAlert by remember{
        mutableStateOf(false)
    }

    var selectedAccount by remember{
        mutableStateOf("")
    }

    SOZIPTheme {
        NavHost(navController = navController, startDestination = "AccountManagementView" ) {
            composable(route = "AddAccountView") {
                AddAccountView()
            }

            composable(route = "AccountManagementView"){
                Surface(modifier = Modifier.fillMaxSize(), color = SOZIPColorPalette.current.background) {
                    Scaffold(topBar = {
                        if (!isModalSheet) {
                            TopAppBar(
                                title = {
                                    Text(
                                        text = "계좌 관리",
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
                                ),
                                actions = {
                                    IconButton(onClick = {
                                        navController.navigate("AddAccountView"){
                                            popUpTo("AccountManagementView"){
                                                inclusive = false
                                            }
                                        }
                                    }) {
                                        Icon(imageVector = Icons.Default.Add, contentDescription = null, tint = red)
                                    }
                                }

                            )
                        }

                    }, content = {
                        Surface(color = SOZIPColorPalette.current.background, modifier = Modifier.fillMaxSize()) {
                            Column(modifier = Modifier.padding(it)) {
                                Column(
                                    modifier = Modifier
                                        .padding(20.dp)
                                        .fillMaxSize(),
                                    verticalArrangement = Arrangement.Top,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    LaunchedEffect(key1 = null){
                                        helper.getAccountInfo {
                                            if(!it){
                                                showProgress.value = false
                                                showErrorAlert.value = true
                                            } else{
                                                accountList = getAccountInfo()
                                                showProgress.value = false
                                            }
                                        }
                                    }

                                    if(accountList == null || accountList!!.isEmpty()){
                                        Text(text = "등록된 계좌 정보가 없습니다.", fontSize = 12.sp, color = gray)
                                    } else{
                                        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)){
                                            itemsIndexed(key = { index, item ->
                                                item.accountNumber
                                            }, items = accountList!!){ index, item ->
                                                AccountInfoListModel(account = item, modifier = Modifier.animateItemPlacement()) {
                                                    selectedAccount = "${item.bank} ${item.accountNumber} ${item.name}"
                                                    showConfirmDialog = true
                                                }
                                            }
                                        }
                                    }

                                }
                            }
                        }

                        if(showErrorAlert.value){
                            AlertDialog(
                                onDismissRequest = { showErrorAlert.value = false },

                                confirmButton = {
                                    TextButton(onClick = {
                                        showErrorAlert.value = false
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

                        if(showSuccessAlert){
                            AlertDialog(
                                onDismissRequest = { showSuccessAlert = false },

                                confirmButton = {
                                    TextButton(onClick = {
                                        showSuccessAlert = false
                                        helper.getAccountInfo {
                                            if(it){
                                                showProgress.value = false
                                                accountList = getAccountInfo()
                                            } else{
                                                showProgress.value = false
                                                showErrorAlert.value = true
                                            }

                                        }
                                    }){
                                        androidx.compose.material3.Text("확인", color = accent, fontWeight = FontWeight.Bold)
                                    }
                                },
                                title = {
                                    androidx.compose.material3.Text("계좌 제거 완료")
                                },
                                text = {
                                    androidx.compose.material3.Text("계좌가 제거되었어요!")
                                },
                                icon = {
                                    androidx.compose.material3.Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null)
                                }
                            )
                        }

                        if(showConfirmDialog){
                            AlertDialog(
                                onDismissRequest = { showConfirmDialog = false },

                                confirmButton = {
                                    TextButton(onClick = {
                                        showProgress.value = true
                                        helper.removeAccount(selectedAccount){
                                            if(it){
                                                showConfirmDialog = false
                                                showSuccessAlert = true
                                            } else{
                                                showErrorAlert.value = true
                                            }
                                        }
                                    }){
                                        androidx.compose.material3.Text("예", color = accent, fontWeight = FontWeight.Bold)
                                    }
                                },

                                dismissButton = {
                                    TextButton(onClick = {
                                        showConfirmDialog = false
                                    }){
                                        androidx.compose.material3.Text("아니오", color = accent, fontWeight = FontWeight.Bold)
                                    }
                                },
                                title = {
                                    androidx.compose.material3.Text("계좌 제거")
                                },
                                text = {
                                    androidx.compose.material3.Text("선택한 계좌를 제거할까요?")
                                },
                                icon = {
                                    androidx.compose.material3.Icon(imageVector = Icons.Default.Delete, contentDescription = null)
                                }
                            )
                        }

                        if(showProgress.value){
                            Dialog(
                                onDismissRequest = { },
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
                    })
                }

            }
        }
    }
}

@Preview
@Composable
fun AccountManagementView_previews(){
    AccountManagementView()
}