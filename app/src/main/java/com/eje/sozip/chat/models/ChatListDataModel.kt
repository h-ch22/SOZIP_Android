package com.eje.sozip.chat.models

import androidx.compose.ui.graphics.Color
import com.eje.sozip.ui.theme.SOZIP_BG_3

data class ChatListDataModel(
    var docId : String = "",
    var SOZIPName : String = "",
    var currentPeople : Int = 0,
    var last_msg : String = "",
    var participants : Map<String, String> = emptyMap(),
    var status : String = "",
    var profiles : Map<String, String>? = null,
    var color : Color = SOZIP_BG_3,
    var last_msg_time : String = "",
    var manager : String = ""
)
