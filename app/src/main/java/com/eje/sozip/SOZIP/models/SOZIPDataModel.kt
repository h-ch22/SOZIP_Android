package com.eje.sozip.SOZIP.models

import androidx.compose.ui.graphics.Color
import java.util.Date

data class SOZIPDataModel(
    val docID : String = "",
    val category : String = "",
    val firstCome : Int = 0,
    val SOZIPName : String = "",
    val currentPeople : Int = 0,
    val location_description : String = "",
    val time : Date? = null,
    val Manager : String = "",
    val participants : Map<String, String> = emptyMap(),
    val location : String = "",
    val address : String = "",
    val status : String = "",
    val color : Color? = null,
    val account : String = "",
    val profile : Map<String, String>? = null,
    val url : String? = null,
    val type : SOZIPPackagingTypeModel = SOZIPPackagingTypeModel.DELIVERY
)
