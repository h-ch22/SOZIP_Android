package com.eje.sozip.userManagement.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCard
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Money
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.eje.sozip.ui.theme.SOZIPColorPalette
import com.eje.sozip.ui.theme.SOZIPTheme
import com.eje.sozip.ui.theme.accent

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun AddAccountView(){
    val bank = listOf("NH농협", "KB국민은행", "신한은행", "우리은행", "하나은행", "IBK기업은행", "SC제일은행", "씨티은행", "KDB산업은행", "SBI저축은행", "새마을은행", "대구은행", "광주은행", "우체국", "신협", "전북은행", "경남은행", "부산은행", "수협", "제주은행", "저축은행", "산림조합", "토스뱅크", "케이뱅크", "카카오뱅크", "HSBC", "중국공산", "JP모간", "도이치", "BNP파리바", "BOA", "중국건설")
    val brokerage = listOf("토스증권", "키움", "KB증권", "미래에셋대우", "삼성", "NH투자", "유안타", "대신", "한국투자", "신한금융투자", "유진투자", "한화투자", "DB금융투자", "하나금융", "하이투자", "현대차증권", "신영", "이베스트", "교보", "메리츠증권", "KTB투자", "SK", "부국", "케이프투자", "한국포스증권", "카카오페이증권")
    val navController = rememberNavController()
    val selectedBank = remember {
        mutableStateOf("")
    }

    SOZIPTheme {
        NavHost(navController = navController, startDestination = "AddAccountView" ) {
            composable(route = "AddAccountDetailsView"){
                AddAccountDetailsView(selectedBank.value)
            }
            composable(route = "AddAccountView") {
                Surface(modifier = Modifier.fillMaxSize(), color = SOZIPColorPalette.current.background) {
                    Scaffold(topBar = {
                        TopAppBar(
                            title = {
                                Text(
                                    text = "계좌 추가",
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
                                Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(imageVector = Icons.Default.AddCard, contentDescription = null, tint = accent, modifier = Modifier.size(50.dp))
                                    Text(text = "먼저 은행 또는 증권사를 선택하세요.", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = accent)
                                    Spacer(modifier = Modifier.height(20.dp))

                                    LazyColumn{
                                        stickyHeader {
                                            Surface(Modifier.fillMaxWidth()) {
                                                Text(text = "은행")
                                            }
                                        }

                                        items(bank.size){
                                            TextButton(onClick = {
                                                selectedBank.value = bank[it]
                                                navController.navigate("AddAccountDetailsView"){
                                                    popUpTo("AddAccountView"){
                                                        inclusive = false
                                                    }
                                                }
                                            }, modifier = Modifier.fillMaxWidth()) {
                                                Text(text = bank[it], color = SOZIPColorPalette.current.txtColor, textAlign = TextAlign.Start)
                                                Spacer(modifier = Modifier.weight(1f))
                                            }
                                        }

                                        stickyHeader {
                                            Surface(Modifier.fillMaxWidth()) {
                                                Text(text = "증권사")
                                            }
                                        }

                                        items(brokerage.size){
                                            TextButton(onClick = {
                                                selectedBank.value = brokerage[it]
                                                navController.navigate("AddAccountDetailsView"){
                                                    popUpTo("AddAccountView"){
                                                        inclusive = false
                                                    }
                                                }
                                            }, modifier = Modifier.fillMaxWidth()) {
                                                Text(text = brokerage[it], color = SOZIPColorPalette.current.txtColor, textAlign = TextAlign.Start)
                                                Spacer(modifier = Modifier.weight(1f))
                                            }
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
fun AddAccountView_previews(){
    AddAccountView()
}