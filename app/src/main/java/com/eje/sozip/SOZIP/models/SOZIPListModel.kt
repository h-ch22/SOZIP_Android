package com.eje.sozip.SOZIP.models

import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTimeFilled
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.PedalBike
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eje.sozip.ui.theme.SOZIPColorPalette
import com.eje.sozip.ui.theme.SOZIPTheme
import com.eje.sozip.ui.theme.SOZIP_BG_1
import com.eje.sozip.ui.theme.accent
import com.eje.sozip.ui.theme.blue
import com.eje.sozip.ui.theme.gray
import com.eje.sozip.ui.theme.white
import com.eje.sozip.userManagement.helper.UserManagement
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.concurrent.TimeUnit

fun convertDateToString(date : Date) : String{
    val dateFormat = SimpleDateFormat("MM.dd HH:mm")

    return dateFormat.format(date)
}

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SOZIPListModel(data : SOZIPDataModel, modifier : Modifier, onClickStartSource : () -> Unit){
    var timer by remember{
        mutableStateOf(data.time?.time?.minus(System.currentTimeMillis()) ?: Date().time)
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
        ){
            LaunchedEffect(key1 = timer){
                if(timer > 0){
                    delay(1000L)
                    timer = data.time?.time?.minus(System.currentTimeMillis()) ?: Date().time
                }
            }

            Column(
                modifier = Modifier
                    .padding(15.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Row (verticalAlignment = Alignment.CenterVertically){
                    Box(modifier = Modifier
                        .width(5.dp)
                        .height(25.dp)
                        .clip(RoundedCornerShape(5.dp))
                        .background(data.color ?: SOZIP_BG_1))

                    Spacer(modifier = Modifier.width(5.dp))

                    Icon(imageVector = if(data.type == SOZIPPackagingTypeModel.DELIVERY) Icons.Default.PedalBike else Icons.Default.Fastfood, contentDescription = null, tint = SOZIPColorPalette.current.txtColor)

                    Spacer(modifier = Modifier.width(5.dp))

                    Text(text = data.SOZIPName, fontWeight = FontWeight.Bold, color = SOZIPColorPalette.current.txtColor)

                    if(data.Manager == UserManagement.userInfo?.uid){
                        Spacer(modifier = Modifier.width(5.dp))

                        Text(text = "MY", color = white, fontSize = 10.sp, modifier = Modifier
                            .background(color = blue, shape = CircleShape)
                            .padding(5.dp))
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.End){
                        Icon(imageVector = Icons.Default.People, contentDescription = null, tint = SOZIPColorPalette.current.txtColor)

                        Spacer(modifier = Modifier.width(5.dp))

                        Text(text = "${data.currentPeople}/${data.firstCome}")
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center){
                    Icon(imageVector = Icons.Default.LocationOn, contentDescription = null, tint = gray)

                    Column(verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.Start) {
                        Text(text = data.location_description, color = gray, fontWeight = FontWeight.Bold, fontSize = 10.sp)
                        Text(text = data.address, color = gray, fontSize = 10.sp, lineHeight = 12.sp)
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.End){
                        Icon(imageVector = Icons.Default.AccessTimeFilled, contentDescription = null, tint = accent, modifier = Modifier
                            .width(15.dp)
                            .height(15.dp))

                        Spacer(modifier = Modifier.width(5.dp))

                        AnimatedContent(targetState = timer, transitionSpec = { fadeIn() with fadeOut() }) {
                            Text(text = String.format("%d:%d:%d", TimeUnit.MILLISECONDS.toHours(it),
                                TimeUnit.MILLISECONDS.toMinutes(it) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(it)),
                                TimeUnit.MILLISECONDS.toSeconds(it) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(it))), fontSize = 10.sp, color = accent)
                        }


                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center){
                    CategoryListModel(category = data.category, selected = false)

                    Spacer(modifier = Modifier.weight(1f))

                    Text(convertDateToString(data.time ?: Date()), fontSize = 10.sp, color = gray)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SOZIPListModel_preview(){
    SOZIPListModel(data = SOZIPDataModel(
        "", "치킨", 2, "테스트",
        1, "너네집", Date(), "",
        mapOf(), "", "전라북도 전주시 덕진구", "", SOZIP_BG_1,
        "", mapOf(), "", SOZIPPackagingTypeModel.DELIVERY
    ), modifier = Modifier, onClickStartSource = {})
}