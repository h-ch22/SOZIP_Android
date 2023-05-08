package com.eje.sozip.SOZIP.models

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eje.sozip.frameworks.helper.AES256Util
import com.eje.sozip.ui.theme.SOZIPColorPalette
import com.eje.sozip.ui.theme.SOZIPTheme
import com.eje.sozip.userManagement.helper.UserManagement

@Composable
fun SOZIPParticipantsListModel(participants : Map<String, String>, profiles : Map<String, String>){
    SOZIPTheme {
        Row (verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
            participants.forEach{
                val nickName = it.value
                val uid = it.key
                val profile = profiles.get(uid)
                val profile_split = profile?.split(",")

                Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                    Text( UserManagement.convertProfileToEmoji(profile_split?.get(0) ?: "chick"), modifier = Modifier
                        .drawBehind{
                            drawCircle(
                                color =  UserManagement.convertProfileBGToColor(
                                    profile_split?.get(1) ?: "bg_3"
                                )
                            )
                        }
                        .padding(5.dp), fontSize = 25.sp)

                    Text(AES256Util.decrypt(nickName), color = SOZIPColorPalette.current.txtColor, fontSize = 10.sp)

                }

                Spacer(modifier = Modifier.width(10.dp))

            }

            Modifier.width(20.dp)
        }

    }
}