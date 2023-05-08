package com.eje.sozip.more.ui

import android.content.pm.PackageInfo
import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowCircleUp
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.eje.sozip.R
import com.eje.sozip.frameworks.helper.VersionManagement
import com.eje.sozip.ui.theme.SOZIPColorPalette
import com.eje.sozip.ui.theme.SOZIPTheme
import com.eje.sozip.ui.theme.accent
import com.eje.sozip.ui.theme.gray
import com.eje.sozip.ui.theme.green
import com.eje.sozip.ui.theme.red


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoView(){
    val navController = rememberNavController()
    var versionName: String = ""
    val latest = remember {
        mutableStateOf("")
    }

    val context = LocalContext.current
    val helper = VersionManagement()

    SOZIPTheme {
        LaunchedEffect(key1 = true){
            helper.getVersion {
                latest.value = if(it){
                    VersionManagement.version ?: "버전 정보를 불러올 수 없습니다."
                } else{
                    "버전 정보를 불러올 수 없습니다."
                }
            }
            versionName = try{
                val pInfo: PackageInfo =
                    context.getPackageManager().getPackageInfo(context.getPackageName(), 0)
                val version = pInfo.versionName
                version
            } catch(e : Exception){
                e.printStackTrace()
                "버전 정보를 불러올 수 없습니다."
            }

        }
        NavHost(navController = navController, startDestination = "InfoView" ){
            composable(route = "PDFView"){

            }

            composable(route = "InfoView"){
                Scaffold(topBar = {
                    TopAppBar (
                        title = {
                            androidx.compose.material.Text(text = "정보", color = SOZIPColorPalette.current.txtColor)
                        },
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
                    Surface(modifier = Modifier.fillMaxSize(), color = SOZIPColorPalette.current.background) {
                        Column(
                            Modifier
                                .padding(it)
                                .verticalScroll(rememberScrollState())) {
                            Column(verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(10.dp)) {
                                Row(modifier = Modifier
                                    .fillMaxWidth().shadow(5.dp)
                                    .height(150.dp)
                                    .background(
                                        color = SOZIPColorPalette.current.btnColor,
                                        shape = RoundedCornerShape(15.dp)
                                    ), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center){
                                    Image(painter = painterResource(id = R.drawable.appstore),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .width(100.dp)
                                            .height(100.dp)
                                            .shadow(
                                                elevation = 8.dp,
                                                shape = RoundedCornerShape(16.dp),
                                                clip = true
                                            ))

                                    Spacer(modifier = Modifier.width(10.dp))

                                    Column {
                                        Text(text = "소집 : SOZIP", fontSize = 25.sp, fontWeight = FontWeight.Bold, color = SOZIPColorPalette.current.txtColor)
                                        Text(text = "현재 버전 : $versionName", fontSize = 15.sp, color = SOZIPColorPalette.current.txtColor)
                                        Text(text = "최신 버전 : ${latest.value}", fontSize = 15.sp, color = SOZIPColorPalette.current.txtColor)
                                        
                                        if(latest.value != "" && latest.value != "버전 정보를 불러올 수 없습니다."){
                                            if(latest.value == versionName){
                                                Row(horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically) {
                                                    Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = green)

                                                    Spacer(modifier = Modifier.width(10.dp))

                                                    Text(text = "최신 버전입니다.", fontSize = 12.sp, color = green)
                                                }
                                            } else{
                                                Row(horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically) {
                                                    Icon(imageVector = Icons.Default.Close, contentDescription = null, tint = red)

                                                    Spacer(modifier = Modifier.width(5.dp))

                                                    Text(text = "최신 버전이 아닙니다.", fontSize = 12.sp, color = red)
                                                }
                                            }
                                        }
                                    }
                                }

                                if(latest.value != "" && latest.value != "버전 정보를 불러올 수 없습니다."){
                                    if(latest.value != versionName){
                                        Spacer(Modifier.height(20.dp))

                                        Button(onClick = { /*TODO*/ },
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = SOZIPColorPalette.current.btnColor
                                            ), elevation = ButtonDefaults.buttonElevation(5.dp),
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(80.dp),
                                            shape = RoundedCornerShape(15.dp)
                                        ) {
                                            Row(horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically) {
                                                Icon(imageVector = Icons.Default.ArrowCircleUp, contentDescription = null, tint = accent )
                                                Spacer(modifier = Modifier.width(10.dp))
                                                Text("업데이트", fontSize = 18.sp, color = SOZIPColorPalette.current.txtColor)
                                                Spacer(modifier = Modifier.weight(1f))
                                            }
                                        }
                                    }
                                }

                                Spacer(Modifier.height(20.dp))

                                Button(onClick = { /*TODO*/ },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = SOZIPColorPalette.current.btnColor
                                    ), elevation = ButtonDefaults.buttonElevation(5.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(80.dp),
                                    shape = RoundedCornerShape(15.dp)
                                ) {
                                    Row(horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically) {
                                        Image(painter = painterResource(id = R.drawable.ic_eula), contentDescription = null, modifier = Modifier.size(30.dp))
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Text("서비스 이용 약관 읽기", fontSize = 18.sp, color = SOZIPColorPalette.current.txtColor)
                                        Spacer(modifier = Modifier.weight(1f))
                                    }
                                }

                                Spacer(Modifier.height(20.dp))

                                Button(onClick = { /*TODO*/ },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = SOZIPColorPalette.current.btnColor
                                    ), elevation = ButtonDefaults.buttonElevation(5.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(80.dp),
                                    shape = RoundedCornerShape(15.dp)
                                ) {
                                    Row(horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically) {
                                        Image(painter = painterResource(id = R.drawable.ic_privacy), contentDescription = null, modifier = Modifier.size(30.dp))
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Text("개인정보 처리 방침 읽기", fontSize = 18.sp, color = SOZIPColorPalette.current.txtColor)
                                        Spacer(modifier = Modifier.weight(1f))
                                    }
                                }

                                Spacer(Modifier.height(20.dp))

                                Button(onClick = { /*TODO*/ },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = SOZIPColorPalette.current.btnColor
                                    ), elevation = ButtonDefaults.buttonElevation(5.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(80.dp),
                                    shape = RoundedCornerShape(15.dp)
                                ) {
                                    Row(horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically) {
                                        Image(painter = painterResource(id = R.drawable.ic_opensource), contentDescription = null, modifier = Modifier.size(30.dp))
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Text("오픈소스 라이센스 정보", fontSize = 18.sp, color = SOZIPColorPalette.current.txtColor)
                                        Spacer(modifier = Modifier.weight(1f))
                                    }
                                }

                                Spacer(Modifier.height(20.dp))

                                Button(onClick = { /*TODO*/ },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = SOZIPColorPalette.current.btnColor
                                    ), elevation = ButtonDefaults.buttonElevation(5.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(80.dp),
                                    shape = RoundedCornerShape(15.dp)
                                ) {
                                    Row(horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically) {
                                        Image(painter = painterResource(id = R.drawable.ic_community), contentDescription = null, modifier = Modifier.size(30.dp))
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Text("커뮤니티 가이드라인", fontSize = 18.sp, color = SOZIPColorPalette.current.txtColor)
                                        Spacer(modifier = Modifier.weight(1f))
                                    }
                                }

                                Spacer(Modifier.height(20.dp))

                                Button(onClick = { /*TODO*/ },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = SOZIPColorPalette.current.btnColor
                                    ), elevation = ButtonDefaults.buttonElevation(5.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(80.dp),
                                    shape = RoundedCornerShape(15.dp)
                                ) {
                                    Row(horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically) {
                                        Image(painter = painterResource(id = R.drawable.ic_ad), contentDescription = null, modifier = Modifier.size(30.dp))
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Text("맞춤형 광고 안내", fontSize = 18.sp, color = SOZIPColorPalette.current.txtColor)
                                        Spacer(modifier = Modifier.weight(1f))
                                    }
                                }

                                Spacer(Modifier.height(20.dp))

                                Button(onClick = { /*TODO*/ },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = SOZIPColorPalette.current.btnColor
                                    ), elevation = ButtonDefaults.buttonElevation(5.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(80.dp),
                                    shape = RoundedCornerShape(15.dp)
                                ) {
                                    Row(horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically) {
                                        Image(painter = painterResource(id = R.drawable.ic_gps_license), contentDescription = null, modifier = Modifier.size(30.dp))
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Text("위치기반서비스 이용약관", fontSize = 18.sp, color = SOZIPColorPalette.current.txtColor)
                                        Spacer(modifier = Modifier.weight(1f))
                                    }
                                }

                                Spacer(Modifier.height(20.dp))

                                Text(text = "© 2021-2023. eje All Rights Reserved.\n이제이 | 대표 : 문소정 | 사업자등록번호 : 763-33-00865",
                                    color = gray,
                                    fontSize = 10.sp,
                                    textAlign = TextAlign.Center)

                            }
                        }

                    }
                })

            }
        }

    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL)
@Composable
fun InfoView_previews(){
    InfoView()
}