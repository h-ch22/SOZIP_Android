package com.eje.sozip.more.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.eje.sozip.R
import com.eje.sozip.frameworks.helper.AES256Util
import com.eje.sozip.ui.theme.SOZIPColorPalette
import com.eje.sozip.ui.theme.SOZIPTheme
import com.eje.sozip.ui.theme.gray
import com.eje.sozip.userManagement.helper.UserManagement
import com.eje.sozip.userManagement.ui.ProfileView

@Composable
fun MoreView(){
    val navController = rememberNavController()

    SOZIPTheme {
        NavHost(navController = navController, startDestination = "MoreView" ){
            composable(route = "ProfileView"){
                ProfileView()
            }

            composable(route = "FeedbackHubView"){
                FeedbackHubView()
            }

            composable(route = "InfoView"){
                InfoView()
            }

            composable(route = "MoreView"){
                Surface(modifier = Modifier.fillMaxSize(), color = SOZIPColorPalette.current.background) {
                    Column(verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.Start, modifier = Modifier.padding(20.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "소집 : SOZIP", fontWeight = FontWeight.Bold, color = SOZIPColorPalette.current.txtColor)
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Button(onClick = {
                            navController.navigate("ProfileView"){
                                popUpTo("MoreView"){
                                    inclusive = false
                                }
                            }
                        },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = SOZIPColorPalette.current.btnColor
                            ), elevation = ButtonDefaults.buttonElevation(5.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(80.dp),
                            shape = RoundedCornerShape(15.dp),
                            contentPadding = PaddingValues(start = 15.dp)
                        ) {
                            Row(horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically) {
                                Text( UserManagement.convertProfileToEmoji(UserManagement.userInfo?.profile ?: "chick"), modifier = Modifier
                                    .drawBehind {
                                        drawCircle(
                                            color = UserManagement.convertProfileBGToColor(
                                                UserManagement.userInfo?.profile_bg ?: "bg_3"
                                            )
                                        )
                                    }
                                    .padding(5.dp), fontSize = 25.sp)
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(AES256Util.decrypt(UserManagement.userInfo?.nickName), fontSize = 18.sp, fontWeight = FontWeight.Bold, color = SOZIPColorPalette.current.txtColor)
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Divider(modifier = Modifier.fillMaxWidth(), color = gray.copy(alpha = 0.5f))

                        Spacer(modifier = Modifier.height(10.dp))

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
                                Image(painter = painterResource(id = R.drawable.ic_notice), contentDescription = null, modifier = Modifier.size(30.dp))
                                Spacer(modifier = Modifier.width(10.dp))
                                Text("공지사항", fontSize = 18.sp, color = SOZIPColorPalette.current.txtColor)
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Button(onClick = {
                            navController.navigate("FeedbackHubView"){
                                popUpTo("MoreView"){
                                    inclusive = false
                                }
                            }
                        },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = SOZIPColorPalette.current.btnColor
                            ), elevation = ButtonDefaults.buttonElevation(5.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(80.dp),
                            shape = RoundedCornerShape(15.dp)
                        ) {
                            Row(horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically) {
                                Image(painter = painterResource(id = R.drawable.ic_feedback_hub), contentDescription = null, modifier = Modifier.size(30.dp))
                                Spacer(modifier = Modifier.width(10.dp))
                                Text("피드백 허브", fontSize = 18.sp, color = SOZIPColorPalette.current.txtColor)
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

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
                                Image(painter = painterResource(id = R.drawable.ic_counselor), contentDescription = null, modifier = Modifier.size(30.dp))
                                Spacer(modifier = Modifier.width(10.dp))
                                Text("소집 문의", fontSize = 18.sp, color = SOZIPColorPalette.current.txtColor)
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Button(onClick = {
                            navController.navigate("InfoView"){
                                popUpTo("MoreView"){
                                    inclusive = false
                                }
                            }
                        },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = SOZIPColorPalette.current.btnColor
                            ), elevation = ButtonDefaults.buttonElevation(5.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(80.dp),
                            shape = RoundedCornerShape(15.dp)
                        ) {
                            Row(horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically) {
                                Icon(imageVector = Icons.Default.Info, contentDescription = null, tint = SOZIPColorPalette.current.txtColor, modifier = Modifier.size(30.dp))
                                Spacer(modifier = Modifier.width(10.dp))
                                Text("정보", fontSize = 18.sp, color = SOZIPColorPalette.current.txtColor)
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }
        }

    }
}

@Preview
@Composable
fun MoreView_preview(){
    MoreView()
}