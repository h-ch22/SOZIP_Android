package com.eje.sozip.chat.models

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eje.sozip.frameworks.helper.AES256Util
import com.eje.sozip.ui.theme.SOZIPColorPalette
import com.eje.sozip.ui.theme.SOZIPTheme
import com.eje.sozip.ui.theme.gray
import com.eje.sozip.userManagement.helper.UserManagement
import java.text.SimpleDateFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatListModel(data : ChatListDataModel, modifier : Modifier, onClickStartSource : () -> Unit){
    val UIDs = remember {
        mutableStateListOf<String>()
    }

    val last_msg_time = remember{
        mutableStateOf("")
    }

    SOZIPTheme {
        Card(
            shape = RoundedCornerShape(15.dp),
            colors = CardDefaults.cardColors(containerColor = SOZIPColorPalette.current.btnColor),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 3.dp
            ),
            onClick = {
                onClickStartSource()
            }
        ) {
            LaunchedEffect(key1 = true){
                if (data.profiles != null) {
                    for(key in data.profiles!!.keys){
                        UIDs.add(key)
                    }
                }

                val dateFormat = SimpleDateFormat("yy/MM/dd kk:mm:ss.SSSS")
                val last_msg_time_asDate = dateFormat.parse(AES256Util.decrypt(data.last_msg_time))

                val dateFormat_modify = SimpleDateFormat("MM.dd HH:mm")
                last_msg_time.value = dateFormat_modify.format(last_msg_time_asDate)
            }

            Column(modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(SOZIPColorPalette.current.btnColor)) {

                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.Start){
                    Row {
                        if (data.profiles != null) {
                            when(UIDs.size){
                                1 -> {
                                    val profile = data.profiles!!.getValue(UIDs[0])
                                    val profile_split = profile.split(",")
                                    val profile_icon = profile_split.get(0)
                                    val profile_bg = profile_split.get(1)

                                    Text(UserManagement.convertProfileToEmoji(profile_icon ?: "chick"), modifier = Modifier
                                        .drawBehind {
                                            drawRoundRect(
                                                color = UserManagement.convertProfileBGToColor(
                                                    profile_bg
                                                ),
                                                cornerRadius = CornerRadius(
                                                    x = 15.dp.toPx(),
                                                    15.dp.toPx()
                                                )
                                            )
                                        }
                                        .padding(5.dp), fontSize = 25.sp)
                                }

                                2 -> {
                                    for(i in 0..1){
                                        val profile = data.profiles!!.getValue(UIDs[i])
                                        val profile_split = profile.split(",")
                                        val profile_icon = profile_split.get(0)
                                        val profile_bg = profile_split.get(1)

                                        Text(UserManagement.convertProfileToEmoji(profile_icon ?: "chick"), modifier = Modifier
                                            .drawBehind {
                                                drawRoundRect(
                                                    color = UserManagement.convertProfileBGToColor(
                                                        profile_bg
                                                    ),
                                                    cornerRadius = CornerRadius(
                                                        x = 15.dp.toPx(),
                                                        15.dp.toPx()
                                                    )
                                                )
                                            }
                                            .padding(5.dp), fontSize = (12.5).sp)

                                        if(i < 1){
                                            Spacer(modifier = Modifier.width(5.dp))
                                        }
                                    }
                                }

                                3 -> {
                                    for(i in 0..2) {
                                            when (i) {
                                                0, 1 -> {
                                                    Row {
                                                        val profile =
                                                            data.profiles!!.getValue(UIDs[i])
                                                        val profile_split = profile.split(",")
                                                        val profile_icon = profile_split.get(0)
                                                        val profile_bg = profile_split.get(1)

                                                        Text(UserManagement.convertProfileToEmoji(
                                                            profile_icon
                                                        ), modifier = Modifier
                                                            .drawBehind {
                                                                drawRoundRect(
                                                                    color = UserManagement.convertProfileBGToColor(
                                                                        profile_bg
                                                                    ),
                                                                    cornerRadius = CornerRadius(
                                                                        x = 15.dp.toPx(),
                                                                        15.dp.toPx()
                                                                    )
                                                                )
                                                            }
                                                            .padding(5.dp), fontSize = (12.5).sp)

                                                        if (i < 1) {
                                                            Spacer(modifier = Modifier.width(5.dp))
                                                        }
                                                    }
                                                }

                                                2 -> {
                                                    val profile = data.profiles!!.getValue(UIDs[i])
                                                    val profile_split = profile.split(",")
                                                    val profile_icon = profile_split.get(0)
                                                    val profile_bg = profile_split.get(1)

                                                    Text(UserManagement.convertProfileToEmoji(
                                                        profile_icon ?: "chick"
                                                    ), modifier = Modifier
                                                        .drawBehind {
                                                            drawRoundRect(
                                                                color = UserManagement.convertProfileBGToColor(
                                                                    profile_bg ?: "bg_3"
                                                                ),
                                                                cornerRadius = CornerRadius(
                                                                    x = 15.dp.toPx(),
                                                                    15.dp.toPx()
                                                                )
                                                            )
                                                        }
                                                        .padding(5.dp), fontSize = (12.5).sp)
                                                }
                                            }
                                    }
                                }

                                4, 5 -> {
                                    Column {
                                        for(i in 0..3){
                                            when(i){
                                                0, 1 -> {
                                                    Row {
                                                        val profile = data.profiles!!.getValue(UIDs[i])
                                                        val profile_split = profile.split(",")
                                                        val profile_icon = profile_split?.get(0)
                                                        val profile_bg = profile_split?.get(1)

                                                        Text(UserManagement.convertProfileToEmoji(profile_icon ?: "chick"), modifier = Modifier
                                                            .drawBehind {
                                                                drawRoundRect(
                                                                    color = UserManagement.convertProfileBGToColor(
                                                                        profile_bg ?: "bg_3"
                                                                    ),
                                                                    cornerRadius = CornerRadius(
                                                                        x = 15.dp.toPx(),
                                                                        15.dp.toPx()
                                                                    )
                                                                )
                                                            }
                                                            .padding(5.dp), fontSize = (12.5).sp)

                                                        if(i < 1){
                                                            Spacer(modifier = Modifier.width(5.dp))
                                                        }
                                                    }
                                                }

                                                2, 3 -> {
                                                    val profile = data.profiles!!.getValue(UIDs[i])
                                                    val profile_split = profile.split(",")
                                                    val profile_icon = profile_split?.get(0)
                                                    val profile_bg = profile_split?.get(1)

                                                    Text(UserManagement.convertProfileToEmoji(profile_icon ?: "chick"), modifier = Modifier
                                                        .drawBehind {
                                                            drawRoundRect(
                                                                color = UserManagement.convertProfileBGToColor(
                                                                    profile_bg ?: "bg_3"
                                                                ),
                                                                cornerRadius = CornerRadius(
                                                                    x = 15.dp.toPx(),
                                                                    15.dp.toPx()
                                                                )
                                                            )
                                                        }
                                                        .padding(5.dp), fontSize = (12.5).sp)

                                                    if(i < 3){
                                                        Spacer(modifier = Modifier.width(5.dp))
                                                    }
                                                }
                                            }

                                        }
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Row(verticalAlignment = Alignment.CenterVertically){
                            Text(AES256Util.decrypt(data.SOZIPName), fontWeight = FontWeight.Bold, color = SOZIPColorPalette.current.txtColor)

                            Spacer(modifier = Modifier.width(10.dp))

                            Text(data.profiles?.size.toString(), fontSize = 10.sp, color = gray)

                            Spacer(modifier = Modifier.weight(1f))

                            Text(last_msg_time.value, color = gray, fontSize = 10.sp)
                        }
                    }

                    Column {
                        Row{
                            Text(AES256Util.decrypt(data.last_msg), color = gray, maxLines = 1, fontSize = 12.sp)

                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}