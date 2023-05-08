package com.eje.sozip.userManagement.ui

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.eje.sozip.R
import com.eje.sozip.frameworks.helper.AES256Util
import com.eje.sozip.ui.theme.SOZIPColorPalette
import com.eje.sozip.ui.theme.SOZIPTheme
import com.eje.sozip.ui.theme.accent
import com.eje.sozip.ui.theme.gray
import com.eje.sozip.userManagement.helper.UserManagement
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileView(){
    val auth = FirebaseAuth.getInstance()
    val navController = rememberNavController()

    SOZIPTheme {
        NavHost(navController = navController, startDestination = "ProfileView"){
            composable(route = "EditProfileView"){
                EditProfileView()
            }

            composable(route = "ProfileView"){
                Surface(color = SOZIPColorPalette.current.background, modifier = Modifier.fillMaxSize()) {
                    Scaffold(topBar = {
                        TopAppBar (
                            title = {
                                Text(text = "프로필 보기", color = SOZIPColorPalette.current.txtColor)
                            },
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
                    }, content = {
                        Column(
                            Modifier
                                .background(SOZIPColorPalette.current.background)
                                .padding(it)
                                .fillMaxSize()) {
                            Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(20.dp)) {
                                androidx.compose.material3.Text(UserManagement.convertProfileToEmoji(
                                    UserManagement.userInfo?.profile ?: "chick"
                                ), modifier = Modifier
                                    .drawBehind {
                                        drawCircle(
                                            color = UserManagement.convertProfileBGToColor(
                                                UserManagement.userInfo?.profile_bg ?: "bg_3"
                                            )
                                        )
                                    }
                                    .padding(5.dp), fontSize = 50.sp)

                                Spacer(modifier = Modifier.height(10.dp))

                                androidx.compose.material3.Text(
                                    AES256Util.decrypt(UserManagement.userInfo?.nickName),
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SOZIPColorPalette.current.txtColor
                                )

                                Spacer(modifier = Modifier.height(5.dp))

                                androidx.compose.material3.Text(
                                    auth.currentUser?.email ?: "",
                                    fontSize = 12.sp,
                                    color = gray
                                )

                                Spacer(modifier = Modifier.height(20.dp))

                                Button(onClick = {
                                    navController.navigate("EditProfileView"){
                                        popUpTo("ProfileView"){
                                            inclusive = false
                                        }
                                    }
                                },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = SOZIPColorPalette.current.btnColor
                                    ), elevation = ButtonDefaults.buttonElevation(5.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(80.dp)
                                        .shadow(10.dp),
                                    shape = RoundedCornerShape(15.dp)
                                ) {
                                    Row(horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically) {
                                        androidx.compose.material3.Text(UserManagement.convertProfileToEmoji(
                                            UserManagement.userInfo?.profile ?: "chick"
                                        ), modifier = Modifier
                                            .drawBehind {
                                                drawCircle(
                                                    color = UserManagement.convertProfileBGToColor(
                                                        UserManagement.userInfo?.profile_bg ?: "bg_3"
                                                    )
                                                )
                                            }
                                            .padding(5.dp), fontSize = 18.sp)
                                        Spacer(modifier = Modifier.width(10.dp))
                                        androidx.compose.material3.Text("프로필 정보 변경", fontSize = 18.sp, color = SOZIPColorPalette.current.txtColor)
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
                                        Icon(imageVector = Icons.Default.History, contentDescription = null, modifier = Modifier.size(30.dp), tint = SOZIPColorPalette.current.txtColor)
                                        Spacer(modifier = Modifier.width(10.dp))
                                        androidx.compose.material3.Text("이용 기록 보기", fontSize = 18.sp, color = SOZIPColorPalette.current.txtColor)
                                        Spacer(modifier = Modifier.weight(1f))
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