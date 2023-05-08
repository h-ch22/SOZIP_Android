package com.eje.sozip.frameworks.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Switch
import androidx.compose.material.SwitchColors
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eje.sozip.ui.theme.SOZIPColorPalette
import com.eje.sozip.ui.theme.SOZIPTheme
import com.eje.sozip.ui.theme.accent
import com.eje.sozip.ui.theme.gray
import com.eje.sozip.ui.theme.white
import com.eje.sozip.userManagement.helper.UserManagement

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationView(){
    val helper = UserManagement()
    val receiveMarketing = remember {
        mutableStateOf(false)
    }

    val switchIcon : (@Composable () -> Unit)? = if(receiveMarketing.value){
        {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                modifier = Modifier.size(SwitchDefaults.IconSize),
                tint = accent
            )
        }
    } else{
        null
    }

    SOZIPTheme {
        LaunchedEffect(key1 = true){
            helper.getMarketingInfo {
                receiveMarketing.value = it
            }
        }

        Surface(modifier = Modifier.fillMaxSize(), color = SOZIPColorPalette.current.background) {
            Scaffold(topBar = {
                TopAppBar(
                    title = { Text(text = "알림", color = SOZIPColorPalette.current.txtColor)},
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

            },content={
                Column(verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
                    .padding(it)
                    .background(
                        SOZIPColorPalette.current.background
                    )
                    .fillMaxSize()) {
                    Column(modifier = Modifier.padding(horizontal = 20.dp)){
                        Row (verticalAlignment = Alignment.CenterVertically){
                            Text(text = "마케팅 알림 받기", fontSize = 15.sp, color = SOZIPColorPalette.current.txtColor)

                            Spacer(modifier = Modifier.weight(1f))

                            androidx.compose.material3.Switch(
                                checked = receiveMarketing.value,
                                onCheckedChange = {
                                    receiveMarketing.value = !receiveMarketing.value

                                    helper.updateMarketingInfo(receiveMarketing.value){
                                        if(it){
                                            helper.getMarketingInfo {
                                                receiveMarketing.value = it
                                            }
                                        } else{
                                            receiveMarketing.value = !receiveMarketing.value
                                        }
                                    }
                                },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = white,
                                    checkedTrackColor = accent,
                                    checkedBorderColor = accent,
                                    checkedIconColor = accent,
                                    uncheckedThumbColor = gray,
                                    uncheckedTrackColor = white
                                ),
                                thumbContent = switchIcon
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
fun NotificationView_previews(){
    NotificationView()
}