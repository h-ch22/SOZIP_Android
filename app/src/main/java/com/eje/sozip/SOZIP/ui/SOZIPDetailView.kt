package com.eje.sozip.SOZIP.ui

import android.os.Bundle
import android.os.UserManager
import android.widget.CheckBox
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.PauseCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.eje.sozip.SOZIP.models.SOZIPDataModel
import com.eje.sozip.SOZIP.models.SOZIPListModel
import com.eje.sozip.ui.theme.SOZIPColorPalette
import com.eje.sozip.ui.theme.SOZIPTheme
import com.eje.sozip.ui.theme.SOZIP_BG_1
import com.eje.sozip.ui.theme.accent
import com.eje.sozip.ui.theme.gray
import com.eje.sozip.ui.theme.red
import com.eje.sozip.ui.theme.white
import com.eje.sozip.userManagement.helper.UserManagement
import com.naver.maps.map.MapView
import kotlinx.coroutines.launch
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SOZIPDetailView(data : SOZIPDataModel, showTopBar : Boolean = true){
    val acceptCancelLicense = remember {
        mutableStateOf(false)
    }

    val animatedCancelButtonColor = animateColorAsState(
        targetValue = if (acceptCancelLicense.value) red else gray,
        animationSpec = tween(200, 0, LinearEasing)
    )

    SOZIPTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = SOZIPColorPalette.current.background) {
            Scaffold(topBar = {
                if(showTopBar){
                    androidx.compose.material3.TopAppBar(
                        title = { Text(text = data.SOZIPName)},
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
                }

            }, content = {
                Column(modifier = Modifier
                    .padding(it)
                    .background(color = SOZIPColorPalette.current.background)
                    .fillMaxHeight(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Top) {
                    Column(modifier = Modifier.padding(20.dp)){
                        SOZIPListModel(data = data, modifier = Modifier, onClickStartSource = {})

                        val scrollState = rememberScrollState()

                        Column(
                            Modifier.verticalScroll(scrollState)
                        ){
                            Spacer(modifier = Modifier.height(10.dp))

                            Text("소집 장소", color = SOZIPColorPalette.current.txtColor, fontWeight = FontWeight.Bold, modifier = Modifier.padding(vertical = 20.dp))

                            SOZIPInsideMapView(data = data, modifier = Modifier.height(200.dp))

                            Spacer(modifier = Modifier.height(20.dp))

                            Text("참여자 정보", color = SOZIPColorPalette.current.txtColor, fontWeight = FontWeight.Bold, modifier = Modifier.padding(vertical = 20.dp))

                            if(data.participants?.keys?.size!! <= 1){
                                Text(text = "아직 이 소집에 참여자가 없어요!", color = gray)
                            } else{

                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            if(data.Manager == UserManagement.userInfo?.uid ?: ""){
                                Card(
                                    shape = RoundedCornerShape(15.dp),
                                    colors = CardDefaults.cardColors(containerColor = SOZIPColorPalette.current.btnColor),
                                    elevation = CardDefaults.cardElevation(
                                        defaultElevation = 3.dp
                                    )
                                ){
                                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(20.dp)) {
                                        Text(text = "소집을 취소하고 싶으신가요?", fontWeight = FontWeight.Bold)
                                        Spacer(modifier = Modifier.height(10.dp))

                                        Text(text = "소집 멤버들에게 돈을 받으셨다면, 돈을 돌려준 후 소집을 취소해주세요!\n정산하지 않을 경우 고객님의 계정이 정지되며, 법적 처벌을 받을 수 있습니다.", fontSize = 12.sp, textAlign = TextAlign.Center)

                                        Spacer(modifier = Modifier.height(5.dp))

                                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Start) {
                                            Checkbox(checked = acceptCancelLicense.value, onCheckedChange = {
                                                acceptCancelLicense.value = !acceptCancelLicense.value
                                            }, colors = CheckboxDefaults.colors(
                                                uncheckedColor = accent,
                                                checkedColor = accent
                                            ))
                                            Text("위 내용을 읽고 이해했으며, 비용 정산을 완료했습니다.", fontSize = 12.sp)

                                        }


                                    }
                                }

                                Spacer(modifier = Modifier.height(20.dp))


                                Button(onClick = {

                                },
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    enabled = acceptCancelLicense.value,
                                    contentPadding = PaddingValues(20.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = animatedCancelButtonColor.value, disabledContainerColor = animatedCancelButtonColor.value
                                    ),
                                    elevation = ButtonDefaults.buttonElevation(5.dp, disabledElevation = 5.dp)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically){
                                        androidx.compose.material3.Icon(imageVector = Icons.Default.Cancel, contentDescription = null, tint = white)

                                        Spacer(modifier = Modifier.width(5.dp))

                                        androidx.compose.material3.Text("소집 전체 취소하기", color = white)
                                    }
                                }

                                TextButton(onClick = { /*TODO*/ }, modifier = Modifier.fillMaxWidth()) {
                                    Row(verticalAlignment = Alignment.CenterVertically){
                                        androidx.compose.material3.Icon(imageVector = Icons.Default.PauseCircle, contentDescription = null, tint = accent)
                                        Spacer(modifier = Modifier.width(5.dp))

                                        androidx.compose.material3.Text("소집 일시 정지하기", color = accent)
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

@Preview
@Composable
fun SOZIPDetailView_preview(){
    SOZIPDetailView(data = SOZIPDataModel(
        docID = "", category = "치킨", firstCome = 3, SOZIPName = "테스트",
        currentPeople = 1, location = "", location_description = "너네집",
        time = Date(), Manager = "", participants = mapOf(), address = "전라북도 전주시",
        status = "", color = SOZIP_BG_1, account = "", profile = mapOf(), url = null
    )
    )
}