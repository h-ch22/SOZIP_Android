package com.eje.sozip.frameworks.models

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.ui.graphics.vector.ImageVector
import com.eje.sozip.frameworks.ui.ADD_SOZIP
import com.eje.sozip.frameworks.ui.CHAT
import com.eje.sozip.frameworks.ui.HOME
import com.eje.sozip.frameworks.ui.MORE
import com.eje.sozip.frameworks.ui.SOZIP_MAP

sealed class BottomNavigationItem(
    val title : String, val icon : ImageVector, val screenRoute : String
){
    object Home : BottomNavigationItem(
        "홈", Icons.Default.Home, HOME
    )

    object SOZIPMap : BottomNavigationItem(
        "소집 맵", Icons.Default.Map, SOZIP_MAP
    )

    object addSOZIP : BottomNavigationItem(
        "소집 추가", Icons.Default.AddCircle, ADD_SOZIP
    )

    object Chat : BottomNavigationItem(
        "채팅", Icons.Default.ChatBubble, CHAT
    )

    object More : BottomNavigationItem(
        "더 보기", Icons.Default.MoreHoriz, MORE
    )
}
