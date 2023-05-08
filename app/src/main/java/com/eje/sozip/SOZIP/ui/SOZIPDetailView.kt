package com.eje.sozip.SOZIP.ui

import android.os.Bundle
import android.os.UserManager
import android.util.Log
import android.widget.CheckBox
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.PauseCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.eje.sozip.SOZIP.helper.SOZIPHelper
import com.eje.sozip.SOZIP.models.ParticipateSOZIPResultModel
import com.eje.sozip.SOZIP.models.SOZIPDataModel
import com.eje.sozip.SOZIP.models.SOZIPListModel
import com.eje.sozip.SOZIP.models.SOZIPParticipantsListModel
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

    val animatedParticipateButtonColor = animateColorAsState(
        targetValue = if (!data.participants?.keys?.contains(
                UserManagement.userInfo?.uid ?: ""
            )!! && data.Manager != (UserManagement.userInfo?.uid
                ?: "") && data.currentPeople <= data.firstCome) accent else gray,
        animationSpec = tween(200, 0, LinearEasing)
    )

    val showProgress = remember{
        mutableStateOf(false)
    }

    val showFailAlert = remember{
        mutableStateOf(false)
    }

    val title_fails = remember{
        mutableStateMapOf<ParticipateSOZIPResultModel, String>(
            ParticipateSOZIPResultModel.ERROR to "소집에 참여하는 중 문제가 발생했습니다.\\n네트워크 상태를 확인하거나, 나중에 다시 시도하십시오.",
            ParticipateSOZIPResultModel.ALREADY_PARTICIPATED to "이미 참여 중인 소집이예요.",
            ParticipateSOZIPResultModel.ERROR_EXIT to "소집에서 나가는 중 오류가 발생했습니다.\\n네트워크 상태를 확인하거나, 나중에 다시 시도하십시오.",
            ParticipateSOZIPResultModel.ALREADY_EXIT to "이미 나온 소집",
            ParticipateSOZIPResultModel.LIMIT_PEOPLE to "허용 인원 초과"
        )
    }

    val contents_fails = remember{
        mutableStateMapOf<ParticipateSOZIPResultModel, String>(
            ParticipateSOZIPResultModel.ERROR to "오류",
            ParticipateSOZIPResultModel.ALREADY_PARTICIPATED to "이미 참여 중인 소집",
            ParticipateSOZIPResultModel.ERROR_EXIT to "나가기 오류",
            ParticipateSOZIPResultModel.ALREADY_EXIT to "이미 소집에서 나왔어요.",
            ParticipateSOZIPResultModel.LIMIT_PEOPLE to "소집 허용 인원을 초과했습니다."
        )
    }

    val results = remember{
        mutableStateOf<ParticipateSOZIPResultModel?>(null)
    }


    val showSuccessAlert = remember{
        mutableStateOf(false)
    }

    val helper = SOZIPHelper()

    SOZIPTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = SOZIPColorPalette.current.background) {
            Scaffold(topBar = {
                if(showTopBar){
                    androidx.compose.material3.TopAppBar(
                        title = { Text(text = data.SOZIPName, color = SOZIPColorPalette.current.txtColor)},
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


                            if(data.Manager == UserManagement.userInfo?.uid ?: "" ||
                                data.participants?.keys?.contains(UserManagement.userInfo?.uid ?: "") == true
                            ){
                                Text("참여자 정보", color = SOZIPColorPalette.current.txtColor, fontWeight = FontWeight.Bold, modifier = Modifier.padding(vertical = 20.dp))

                                if(data.participants?.keys?.size!! <= 1){
                                    Text(text = "아직 이 소집에 참여자가 없어요!", color = gray)
                                } else{
                                    data.profile?.let { it1 -> SOZIPParticipantsListModel(participants = data.participants, profiles = it1) }
                                }
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
                                        Text(text = "소집을 취소하고 싶으신가요?", fontWeight = FontWeight.Bold, color = SOZIPColorPalette.current.txtColor)
                                        Spacer(modifier = Modifier.height(10.dp))

                                        Text(text = "소집 멤버들에게 돈을 받으셨다면, 돈을 돌려준 후 소집을 취소해주세요!\n정산하지 않을 경우 고객님의 계정이 정지되며, 법적 처벌을 받을 수 있습니다.", fontSize = 12.sp, textAlign = TextAlign.Center, color = SOZIPColorPalette.current.txtColor)

                                        Spacer(modifier = Modifier.height(5.dp))

                                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Start) {
                                            Checkbox(checked = acceptCancelLicense.value, onCheckedChange = {
                                                acceptCancelLicense.value = !acceptCancelLicense.value
                                            }, colors = CheckboxDefaults.colors(
                                                uncheckedColor = accent,
                                                checkedColor = accent
                                            ))
                                            Text("위 내용을 읽고 이해했으며, 비용 정산을 완료했습니다.", fontSize = 12.sp, color = SOZIPColorPalette.current.txtColor)

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
                            } else{
                                if(data.participants?.keys?.contains(UserManagement.userInfo?.uid ?: "") == true){
                                    Text("이미 참여 중인 소집이예요", fontSize = 10.sp, color = gray)

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

                                            androidx.compose.material3.Text("소집 취소하기", color = white)
                                        }
                                    }

                                } else{
                                    Button(onClick = {
                                        showProgress.value = true

                                        helper.participate_SOZIP(data.docID, ""){
                                            if(it == ParticipateSOZIPResultModel.SUCCESS){
                                                showProgress.value = false
                                                showSuccessAlert.value = true
                                            } else{
                                                showProgress.value = false
                                                results.value = it
                                                showFailAlert.value = true
                                            }
                                        }
                                    },
                                        modifier = Modifier
                                            .fillMaxWidth(),
                                        enabled = !data.participants?.keys?.contains(
                                            UserManagement.userInfo?.uid ?: ""
                                        )!! && data.Manager != (UserManagement.userInfo?.uid
                                            ?: "") && data.currentPeople <= data.firstCome,
                                        contentPadding = PaddingValues(20.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = animatedParticipateButtonColor.value, disabledContainerColor = animatedParticipateButtonColor.value
                                        ),
                                        elevation = ButtonDefaults.buttonElevation(5.dp, disabledElevation = 5.dp)
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically){
                                            androidx.compose.material3.Text("소집 참여하기", color = white)

                                            Spacer(modifier = Modifier.width(5.dp))

                                            androidx.compose.material3.Icon(imageVector = Icons.Default.ChevronRight, contentDescription = null, tint = white)
                                        }
                                    }
                                }

                            }


                        }

                        if (showProgress.value) {
                            Dialog(
                                onDismissRequest = { showProgress.value = false },
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
                                    }){
                                        androidx.compose.material3.Text("확인", color = accent, fontWeight = FontWeight.Bold)
                                    }
                                },
                                title = {
                                    androidx.compose.material3.Text("참여 완료")
                                },
                                text = {
                                    androidx.compose.material3.Text("소집에 참여했어요!\n채팅으로 이동해서 소집을 진행해주세요!")
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
                                    title_fails.get(results.value)
                                        ?.let { it1 -> androidx.compose.material3.Text(it1) }
                                },
                                text = {
                                    contents_fails.get(results.value)?.let{it1 -> androidx.compose.material3.Text(it1)}
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
fun SOZIPDetailView_preview(){
    SOZIPDetailView(data = SOZIPDataModel(
        docID = "", category = "치킨", firstCome = 3, SOZIPName = "테스트",
        currentPeople = 1, location = "", location_description = "너네집",
        time = Date(), Manager = "", participants = mapOf(), address = "전라북도 전주시",
        status = "", color = SOZIP_BG_1, account = "", profile = mapOf(), url = null
    )
    )
}