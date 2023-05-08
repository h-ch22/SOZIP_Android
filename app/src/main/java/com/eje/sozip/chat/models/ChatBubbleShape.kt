package com.eje.sozip.chat.models

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eje.sozip.frameworks.helper.AES256Util
import com.eje.sozip.ui.theme.SOZIP_BG_3
import com.eje.sozip.ui.theme.gray
import com.eje.sozip.ui.theme.white
import com.eje.sozip.userManagement.helper.UserManagement
import java.text.SimpleDateFormat

fun convertTime(time : String) : String{
    val dateFormatter = SimpleDateFormat("yy/MM/dd kk:mm:ss.SSSS")
    val formattedDate = dateFormatter.parse(time)

    val dateFormat = SimpleDateFormat("kk:mm")
    return dateFormat.format(formattedDate)
}

@Composable
fun ChatBubbleShape(data : ChatContentsDataModel, SOZIPData : ChatListDataModel, modifier : Modifier) {
    val isMyMsg = data.sender == UserManagement.userInfo?.uid
//    val isMyMsg = false
    Column(Modifier
        .fillMaxWidth().wrapContentHeight(), verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.Start) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if(!isMyMsg){
                Text(text = UserManagement.convertProfileToEmoji(data.profile), modifier = Modifier
                    .padding(5.dp)
                    .drawBehind {
                        drawCircle(
                            color = data.profile_BG
                        )
                    }, fontSize = 25.sp)

                Text(text = data.nickName, color = gray, fontSize = 10.sp)
            }
        }

        Row(horizontalArrangement = Arrangement.Start) {
            if(isMyMsg){
                Spacer(modifier = Modifier.weight(1f))

                Text(text = convertTime(data.time), color = gray, fontSize = 10.sp)

                Spacer(modifier = Modifier.width(5.dp))
            }

            if(data.type == "text"){
                Column(
                    modifier = Modifier
                        .background(
                            color = if (isMyMsg) SOZIPData.color else gray,
                            shape = RoundedCornerShape(4.dp, 4.dp, 0.dp, 4.dp)
                        )
                        .wrapContentWidth()
                        .padding(5.dp)
                ) {
                    Text(AES256Util.decrypt(data.msg), color = white)
                }
                Column(
                    modifier = Modifier
                        .background(
                            color = if (isMyMsg) SOZIPData.color else gray,
                            shape = ChatBubbleEdgeShape(10, isMyMsg)
                        )
                        .width(8.dp)
                        .fillMaxHeight()
                        .padding(5.dp)
                ) {
                }
            } else if(data.type == "participate"){
                if(isMyMsg){
                    Column(
                        modifier = Modifier
                            .background(
                                color = if (isMyMsg) SOZIPData.color else gray,
                                shape = RoundedCornerShape(4.dp, 4.dp, 0.dp, 4.dp)
                            )
                            .wrapContentWidth()
                            .padding(5.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "\uD83C\uDF89", fontSize = 32.sp)
                        Text(AES256Util.decrypt(data.msg), color = white)
                        Spacer(Modifier.height(5.dp))
                        Text("메뉴를 결정한 후 정산을 완료해주세요!", fontSize = 10.sp, color = white)
                    }
                    Column(
                        modifier = Modifier
                            .background(
                                color = if (isMyMsg) SOZIPData.color else gray,
                                shape = ChatBubbleEdgeShape(10, isMyMsg)
                            )
                            .width(8.dp)
                            .fillMaxHeight()
                            .padding(5.dp)
                    ) {
                    }
                } else{
                    Column(
                        modifier = Modifier
                            .background(
                                color = if (isMyMsg) SOZIPData.color else gray,
                                shape = ChatBubbleEdgeShape(10, isMyMsg)
                            )
                            .width(8.dp)
                            .fillMaxHeight()
                            .padding(5.dp)
                    ) {
                    }

                    Column(
                        modifier = Modifier
                            .background(
                                color = if (isMyMsg) SOZIPData.color else gray,
                                shape = RoundedCornerShape(4.dp, 4.dp, 0.dp, 4.dp)
                            )
                            .wrapContentWidth()
                            .padding(5.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "\uD83C\uDF89", fontSize = 32.sp)
                        Text(AES256Util.decrypt(data.msg), color = white)
                        Spacer(Modifier.height(5.dp))
                        Text("메뉴를 결정한 후 정산을 완료해주세요!", fontSize = 10.sp, color = white)
                    }

                }

            }

            if(!isMyMsg){
                Text(text = convertTime(data.time), color = gray, fontSize = 10.sp)
            }

        }
    }

}

@Preview
@Composable
fun ChatBubbleShape_previews(){
    ChatBubbleShape(data = ChatContentsDataModel(rootDocId = "", docId = "", msg = "", sender = "", unread = 0, time = "23/05/07 09:41:00.0000", type = "participate", account = null, imgIndex = null, nickName = "Changjin", profile = "chick", profile_BG = SOZIP_BG_3, url = emptyList()),
        SOZIPData = ChatListDataModel(docId = "", SOZIPName = "", color = SOZIP_BG_3, currentPeople = 0, last_msg_time = "", last_msg = "", manager = "", participants = emptyMap(), profiles = emptyMap(), status = ""),
        modifier = Modifier
    )
}
