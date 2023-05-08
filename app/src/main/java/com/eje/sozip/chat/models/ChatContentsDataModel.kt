package com.eje.sozip.chat.models

import androidx.compose.ui.graphics.Color

data class ChatContentsDataModel(
    val rootDocId: String,
    val docId: String,
    val msg: String,
    val sender: String,
    val unread: Int,
    val time: String,
    val type: String,
    val imgIndex: Int?,
    val profile: String,
    val profile_BG: Color,
    val nickName: String,
    val url: List<String>?,
    val account: String?
)