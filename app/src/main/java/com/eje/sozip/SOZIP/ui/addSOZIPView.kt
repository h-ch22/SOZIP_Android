package com.eje.sozip.SOZIP.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTimeFilled
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.ChangeCircle
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.FoodBank
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.PedalBike
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.eje.sozip.SOZIP.helper.SOZIPHelper
import com.eje.sozip.SOZIP.models.SOZIPPackagingTypeModel
import com.eje.sozip.frameworks.helper.AES256Util
import com.eje.sozip.frameworks.ui.MainActivity
import com.eje.sozip.frameworks.ui.TimePickerDialog
import com.eje.sozip.ui.theme.SOZIPColorPalette
import com.eje.sozip.ui.theme.SOZIPTheme
import com.eje.sozip.ui.theme.SOZIP_BG_1
import com.eje.sozip.ui.theme.SOZIP_BG_2
import com.eje.sozip.ui.theme.SOZIP_BG_3
import com.eje.sozip.ui.theme.SOZIP_BG_4
import com.eje.sozip.ui.theme.SOZIP_BG_5
import com.eje.sozip.ui.theme.accent
import com.eje.sozip.ui.theme.black
import com.eje.sozip.ui.theme.gray
import com.eje.sozip.ui.theme.red
import com.eje.sozip.ui.theme.white
import com.eje.sozip.userManagement.helper.UserManagement
import com.eje.sozip.userManagement.models.AccountDataModel
import com.eje.sozip.userManagement.ui.AddAccountDetailsView
import com.eje.sozip.userManagement.ui.AddAccountView
import com.fresh.materiallinkpreview.parsing.OpenGraphMetaDataProvider
import com.fresh.materiallinkpreview.ui.CardLinkPreview
import com.fresh.materiallinkpreview.ui.CardLinkPreviewProperties
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraAnimation
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapView
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.MarkerIcons
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import kotlin.concurrent.thread

fun getAccountInfo() : SnapshotStateList<AccountDataModel>?{
    return if(UserManagement.accountInfo == null){
        null
    } else if(UserManagement.accountInfo!!.isEmpty()){
        mutableStateListOf()
    } else{
        val accounts : SnapshotStateList<AccountDataModel> = mutableStateListOf()

        for(account in UserManagement.accountInfo!!){
            accounts.add(account)
        }

        accounts
    }
}

fun geoCode(addr : String, completion : ((LatLng?) -> Unit)){
    try{
        val reader : BufferedReader
        val builder = StringBuilder()
        val query = "https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode?query=" + URLEncoder.encode(addr, "UTF-8")
        val url = URL(query)
        val conn = url.openConnection() as HttpURLConnection

        if(conn != null){
            conn.connectTimeout = 5000
            conn.readTimeout = 5000
            conn.requestMethod = "GET"
            conn.setRequestProperty("X-NCP-APIGW-API-KEY-ID", "5hwhaoqcww")
            conn.setRequestProperty("X-NCP-APIGW-API-KEY", "TGN1tlIGOd3yivaLSREYc2pcQQijuwO2TngYiWY8")
            conn.doInput = true

            val response = conn.responseCode

            if(response == 200){
                reader = BufferedReader(InputStreamReader(conn.inputStream))

                var line : String? = null

                while (reader.readLine().also { line = it } != null) {
                    builder.append(
                        line + "\n"
                    )
                }

                var firstIndex : Int
                var lastIndex : Int

                firstIndex = builder.indexOf("\"x\":\"")
                lastIndex = builder.indexOf("\",\"y\":")
                val x: String = builder.substring(firstIndex + 5, lastIndex)

                firstIndex = builder.indexOf("\"y\":\"")
                lastIndex = builder.indexOf("\",\"distance\":")
                val y: String = builder.substring(firstIndex + 5, lastIndex)

                reader.close()
                conn.disconnect()

                completion(LatLng(y.toDouble(), x.toDouble()))
            } else{
                Log.d("addSOZIPView", conn.errorStream.toString())
                completion(null)
            }
        }

    } catch(e : Exception){
        e.printStackTrace()
        completion(null)
    }

}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun addSOZIPView(){
    val storeName = remember {
        mutableStateOf("")
    }

    val endDate = remember{
        mutableStateOf("")
    }

    val endTime = remember{
        mutableStateOf("")
    }

    val deliverURL = remember {
        mutableStateOf("")
    }

    val maxCount = remember{
        mutableStateOf(4)
    }

    val selectedCategory = remember{
        mutableStateOf(0)
    }

    val selectedAccount = remember{
        mutableStateOf<AccountDataModel?>(null)
    }

    val deliverType = remember{
        mutableStateOf(SOZIPPackagingTypeModel.DELIVERY)
    }

    val locationDescription = remember{
        mutableStateOf("")
    }

    val location = remember{
        mutableStateOf("")
    }

    val address = remember{
        mutableStateOf("")
    }

    val showMap = remember{
        mutableStateOf(false)
    }

    var showDialog by remember { mutableStateOf(false) }
    var showSuccessAlert by remember { mutableStateOf(false) }
    var showFailAlert by remember { mutableStateOf(false) }

    val userManagement = UserManagement()
    val navController = rememberNavController()
    val showDatePicker = remember{ mutableStateOf(false) }
    val showTimePicker = remember{ mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = System.currentTimeMillis())
    val timePickerState = rememberTimePickerState()
    val categoryList = remember {
        mutableStateListOf<String>("한식", "분식", "카페/디저트", "돈까스/회/일식", "치킨", "피자", "아시안/양식", "중식", "족발/보쌈", "야식", "찜/탕", "도시락", "패스트푸드")
    }
    val scrollState = rememberScrollState()
    val selectedColor = remember { mutableStateOf(0) }
    val colors = remember{ mutableStateOf(listOf(SOZIP_BG_1, SOZIP_BG_2, SOZIP_BG_3, SOZIP_BG_4, SOZIP_BG_5))}
    val postCodeSearchLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()){
        when(it.resultCode){
            Activity.RESULT_OK -> {
                val addr = it.data?.getStringExtra("data")
                address.value = addr ?: ""
                Log.d("addSOZIPView", addr ?: "")

                thread{
                    geoCode(addr ?: "") {
                        if (it != null) {
                            location.value = "${it.longitude}, ${it.latitude}"
                            showMap.value = true
                        } else{
                            showMap.value = false
                        }
                    }
                }
            }
        }
    }
    val activity = LocalContext.current as MainActivity
    val context = LocalContext.current
    val lifeCycleOwner = LocalLifecycleOwner.current
    val coroutineScope = rememberCoroutineScope()
    val helper = SOZIPHelper()
    var accountList = remember{
        getAccountInfo()
    }

    SOZIPTheme {
        NavHost(navController = navController, startDestination = "addSOZIPView" ) {
            composable(route = "AddAccountView") {
                AddAccountView()
            }

            composable(route = "addSOZIPView") {
                Surface(modifier = Modifier
                    .fillMaxSize(), color = SOZIPColorPalette.current.background) {
                    LaunchedEffect(key1 = true){
                        val formatter = SimpleDateFormat("yy/MM/dd")
                        endDate.value = formatter.format(Date())

                        val timeFormatter = SimpleDateFormat("kk:mm")
                        endTime.value = timeFormatter.format(Date())

                        userManagement.getAccountInfo {

                        }
                    }

                    Column(verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
                        .padding(20.dp)
                        .verticalScroll(scrollState)){
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Text(text = "소집 만들기", fontWeight = FontWeight.ExtraBold, fontSize = 18.sp, color = SOZIPColorPalette.current.txtColor)
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(modifier = Modifier.fillMaxWidth()) {
                            Text(text = "아래 정보를 입력해주세요.", fontWeight = FontWeight.ExtraBold, fontSize = 15.sp, color = SOZIPColorPalette.current.txtColor)
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Row(modifier = Modifier.wrapContentSize(), verticalAlignment = Alignment.CenterVertically){
                            OutlinedTextField(
                                value = storeName.value,
                                onValueChange = { textVal : String -> storeName.value = textVal },
                                label = { Text("업체명") },
                                placeholder = { Text("업체명") } ,
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.FoodBank,
                                        contentDescription = null
                                    )
                                },
                                colors = OutlinedTextFieldDefaults.colors(
                                    cursorColor = accent,
                                    focusedBorderColor = accent,
                                    errorCursorColor = red,
                                    errorLeadingIconColor = red,
                                    disabledPlaceholderColor = gray,
                                    focusedTextColor = accent,
                                    focusedLabelColor = accent,
                                    focusedLeadingIconColor = accent,
                                    disabledTextColor = gray,
                                    unfocusedLabelColor = SOZIPColorPalette.current.txtColor,
                                    unfocusedLeadingIconColor = SOZIPColorPalette.current.txtColor,
                                    unfocusedSupportingTextColor = SOZIPColorPalette.current.txtColor,
                                    selectionColors = TextSelectionColors(handleColor = accent, backgroundColor = accent.copy(alpha = 0.5f))
                                ),
                                maxLines = 1,
                                singleLine = true,
                                modifier = Modifier.weight(0.8f)
                            )

                            Spacer(modifier = Modifier.weight(0.05f))

                            Text(text = "시키실 분!", color = SOZIPColorPalette.current.txtColor, modifier = Modifier.weight(0.3f))
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        if(address.value.isEmpty()){
                            Button(onClick = {
                                postCodeSearchLauncher.launch(Intent(activity, AddressSelectView :: class.java))
                            },
                                modifier = Modifier
                                    .fillMaxWidth(),
                                contentPadding = PaddingValues(20.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = SOZIPColorPalette.current.btnColor, disabledContainerColor = gray
                                ),
                                shape = RoundedCornerShape(15.dp),
                                elevation = ButtonDefaults.buttonElevation(5.dp, disabledElevation = 5.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically){
                                    Icon(imageVector = Icons.Default.LocationOn, contentDescription = null, tint = SOZIPColorPalette.current.txtColor)

                                    Text("소집 장소", color = SOZIPColorPalette.current.txtColor)
                                }
                            }
                        } else{
                            Button(onClick = {
                                postCodeSearchLauncher.launch(Intent(activity, AddressSelectView :: class.java))
                            },
                                modifier = Modifier
                                    .fillMaxWidth(),
                                contentPadding = PaddingValues(20.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = accent, disabledContainerColor = gray
                                ),
                                shape = RoundedCornerShape(15.dp),
                                elevation = ButtonDefaults.buttonElevation(5.dp, disabledElevation = 5.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically){
                                    Icon(imageVector = Icons.Default.LocationOn, contentDescription = null, tint = white)

                                    Text("${address.value}\n다시 설정하려면 누르세요.", color = white)
                                }
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            if(showMap.value){
                                val mapView = remember{
                                    MapView(context).apply {
                                        getMapAsync{naverMap ->
                                            val coordAsString = location.value.split(", ")

                                            if(coordAsString.size == 2){
                                                val coord = LatLng(coordAsString[1].toDouble(), coordAsString[0].toDouble())

                                                val camUpdate = CameraUpdate.scrollTo(coord).animate(
                                                    CameraAnimation.Easing)
                                                naverMap.moveCamera(camUpdate)

                                                val marker = Marker()
                                                marker.position = coord
                                                marker.icon = MarkerIcons.BLACK
                                                marker.iconTintColor = (accent).toArgb()
                                                marker.captionText = "소집 장소"
                                                marker.captionColor = (accent).toArgb()
                                                marker.map = naverMap
                                                marker.subCaptionText = locationDescription.value
                                                marker.subCaptionColor = black.toArgb()
                                            }
                                        }
                                    }
                                }

                                val lifecycleObserver = remember {
                                    LifecycleEventObserver { source, event ->
                                        coroutineScope.launch {
                                            when (event) {
                                                Lifecycle.Event.ON_CREATE -> mapView.onCreate(Bundle())
                                                Lifecycle.Event.ON_START -> mapView.onStart()
                                                Lifecycle.Event.ON_RESUME -> mapView.onResume()
                                                Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                                                Lifecycle.Event.ON_STOP -> mapView.onStop()
                                                Lifecycle.Event.ON_DESTROY -> mapView.onDestroy()
                                                else -> mapView.onPause()
                                            }
                                        }
                                    }
                                }

                                DisposableEffect(true) {
                                    lifeCycleOwner.lifecycle.addObserver(lifecycleObserver)
                                    onDispose {
                                        lifeCycleOwner.lifecycle.removeObserver(lifecycleObserver)
                                    }
                                }

                                AndroidView(factory = {mapView}, modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp))
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            OutlinedTextField(
                                value = locationDescription.value,
                                onValueChange = { textVal : String -> locationDescription.value = textVal },
                                modifier = Modifier
                                    .fillMaxWidth(),
                                label = { Text("설명을 입력해주세요! (예 : 새빛관 앞)") },
                                placeholder = { Text("장소 설명") } ,
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.LocationOn,
                                        contentDescription = null
                                    )
                                },
                                colors = OutlinedTextFieldDefaults.colors(
                                    cursorColor = accent,
                                    focusedBorderColor = accent,
                                    errorCursorColor = red,
                                    errorLeadingIconColor = red,
                                    disabledPlaceholderColor = gray,
                                    focusedTextColor = accent,
                                    focusedLabelColor = accent,
                                    focusedLeadingIconColor = accent,
                                    disabledTextColor = gray,
                                    unfocusedLabelColor = SOZIPColorPalette.current.txtColor,
                                    unfocusedLeadingIconColor = SOZIPColorPalette.current.txtColor,
                                    unfocusedSupportingTextColor = SOZIPColorPalette.current.txtColor,
                                    selectionColors = TextSelectionColors(handleColor = accent, backgroundColor = accent.copy(alpha = 0.5f))

                                ),
                                maxLines = 1,
                                singleLine = true
                            )
                        }


                        Spacer(modifier = Modifier.height(20.dp))

                        Surface(modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(5.dp)
                            .shadow(5.dp),
                            color= SOZIPColorPalette.current.btnColor,
                            shape = RoundedCornerShape(size = 30f),
                            content = {
                                Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(5.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(10.dp)) {
                                        Icon(imageVector = Icons.Default.AccessTimeFilled, contentDescription = null, tint = SOZIPColorPalette.current.txtColor)
                                        Spacer(modifier = Modifier.width(5.dp))
                                        Text(text = "마감 날짜 및 시간", color = SOZIPColorPalette.current.txtColor)
                                    }

                                    Row(verticalAlignment = Alignment.CenterVertically){
                                        TextButton(onClick = { showDatePicker.value = true }) {
                                            Text(text = endDate.value, color = accent)
                                        }

                                        TextButton(onClick = { showTimePicker.value = true }) {
                                            Text(text = endTime.value, color = accent)
                                        }
                                    }

                                }
                            })

                        Spacer(modifier = Modifier.height(20.dp))

                        OutlinedTextField(
                            value = deliverURL.value,
                            onValueChange = { textVal : String -> deliverURL.value = textVal },
                            modifier = Modifier
                                .fillMaxWidth(),
                            label = { Text("배달앱 URL (선택)") },
                            placeholder = { Text("배달앱 URL (선택)") } ,
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Link,
                                    contentDescription = null
                                )
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                cursorColor = accent,
                                focusedBorderColor = accent,
                                errorCursorColor = red,
                                errorLeadingIconColor = red,
                                disabledPlaceholderColor = gray,
                                focusedTextColor = accent,
                                focusedLabelColor = accent,
                                focusedLeadingIconColor = accent,
                                disabledTextColor = gray,
                                unfocusedLabelColor = SOZIPColorPalette.current.txtColor,
                                unfocusedLeadingIconColor = SOZIPColorPalette.current.txtColor,
                                unfocusedSupportingTextColor = SOZIPColorPalette.current.txtColor,
                                selectionColors = TextSelectionColors(handleColor = accent, backgroundColor = accent.copy(alpha = 0.5f))

                            ),
                            maxLines = 1,
                            singleLine = true
                        )

//                        if(deliverURL.value.contains("http://") || deliverURL.value.contains("https://")){
//                            Spacer(modifier = Modifier.height(10.dp))
//
//                            val OGResult = OpenGraphMetaDataProvider().startFetchingMetadata(URL(deliverURL.value))
//
//                            CardLinkPreview(openGraphMetaData = OGResult, cardLinkPreviewProperties =
//                            CardLinkPreviewProperties.Builder(
//                                drawWithCardOutline = false
//                            ).build())
//                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Row(horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically, modifier = Modifier.wrapContentHeight()) {
                            Text("소집 최대 참여 인원 : ${maxCount.value}명", color = SOZIPColorPalette.current.txtColor, fontWeight = FontWeight.Bold)

                            Spacer(modifier = Modifier.weight(1f))

                            IconButton(onClick = {
                                if(maxCount.value > 1){
                                    maxCount.value -= 1
                                }
                            }, enabled = maxCount.value > 1 ) {
                                androidx.compose.material.Icon(imageVector = Icons.Default.Remove, contentDescription = null, tint = if(maxCount.value > 1) accent else{gray})
                            }

                            IconButton(onClick = {
                                if(maxCount.value < 4){
                                    maxCount.value += 1
                                }
                            }, enabled = maxCount.value < 4 ) {
                                androidx.compose.material.Icon(imageVector = Icons.Default.Add, contentDescription = null, tint = if(maxCount.value < 4) accent else{gray})
                            }
                        }

                        Spacer(modifier = Modifier.height(5.dp))

                        Row(horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.Top){
                            Text("고객님을 제외한 ${maxCount.value} 명의 소집 멤버가 참여하면 소집이 자동으로 종료됩니다.", fontSize = 10.sp, color = gray)

                            Spacer(modifier = Modifier.weight(1f))
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Row(horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically){
                            Text("태그 선택", color = SOZIPColorPalette.current.txtColor, fontWeight = FontWeight.Bold)

                            Spacer(modifier = Modifier.weight(1f))
                        }

                        Spacer(modifier = Modifier.height(5.dp))

                        Row(horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically){
                            Text("소집 멤버들이 더 쉽게 소집을 찾을 수 있어요!", fontSize = 10.sp, color = gray)

                            Spacer(modifier = Modifier.weight(1f))
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        LazyRow(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()){
                            items(items = categoryList){
                                FilterChip(selected = selectedCategory.value == categoryList.indexOf(it), onClick = {
                                    selectedCategory.value = categoryList.indexOf(it)}, leadingIcon = {
                                    if(selectedCategory.value == categoryList.indexOf(it)){
                                        Icon(imageVector = Icons.Default.Check, contentDescription = null, modifier = Modifier.size(FilterChipDefaults.IconSize))
                                    }
                                }, label = {
                                    Text(it)
                                }, colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = accent.copy(0.7f),
                                    selectedLabelColor = white,
                                    selectedLeadingIconColor = white
                                ))

                                Spacer(modifier = Modifier.width(10.dp))
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Row(horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically){
                            Text("정산 계좌", color = SOZIPColorPalette.current.txtColor, fontWeight = FontWeight.Bold)

                            Spacer(modifier = Modifier.weight(1f))

                            IconButton(onClick = {
                                showDialog = true

                                userManagement.getAccountInfo {
                                    if(it){
                                        showDialog = false
                                        accountList = getAccountInfo()
                                    } else{
                                        showDialog = false
                                    }
                                }
                            }) {
                                Icon(imageVector = Icons.Default.ChangeCircle, contentDescription = null, tint = accent)
                            }

                            if(accountList != null && accountList!!.size > 0){
                                IconButton(onClick = {
                                    navController.navigate("AddAccountView"){
                                        popUpTo("addSOZIPView"){
                                            inclusive = false
                                        }
                                    }
                                }) {
                                    Icon(imageVector = Icons.Default.AddCircle, contentDescription = null, tint = accent)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(5.dp))

                        Row(horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically){
                            Text("선택한 계좌 정보가 소집 멤버들에게 표시됩니다.", fontSize = 10.sp, color = gray)

                            Spacer(modifier = Modifier.weight(1f))
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        if(accountList == null || accountList!!.isEmpty()){
                            Button(onClick = {
                                navController.navigate("AddAccountView"){
                                    popUpTo("addSOZIPView"){
                                        inclusive = false
                                    }
                                }
                            },
                                modifier = Modifier
                                    .width(250.dp)
                                    .height(120.dp)
                                    .shadow(5.dp),
                                shape = RoundedCornerShape(15.dp),
                                contentPadding = PaddingValues(20.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = accent, disabledContainerColor = gray
                                ),
                                elevation = ButtonDefaults.buttonElevation(5.dp, disabledElevation = 5.dp)
                            ) {
                                Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(Icons.Default.AddCircle, contentDescription = null, tint = white.copy(0.5f))
                                    Text(text = "계좌 추가", color = white)
                                }
                            }
                        } else{
                            selectedAccount.value = accountList!![0]

                            HorizontalPager(pageCount = accountList!!.size, modifier = Modifier.wrapContentHeight()){page ->
                                Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier
                                        .size(width = 250.dp, height = 120.dp)
                                        .clip(RoundedCornerShape(15.dp))
                                        .background(if (selectedAccount.value == accountList!![page]) accent else gray)) {
                                    Row(modifier = Modifier.padding(10.dp)) {
                                        Spacer(modifier = Modifier.weight(1f))

                                        if(selectedAccount.value == accountList!![page]){
                                            Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint=white, modifier = Modifier.size(15.dp))
                                        }
                                    }

                                    Text(AES256Util.decrypt(accountList!![page].bank), color = white, fontSize = 10.sp)

                                    Spacer(modifier = Modifier.height(5.dp))

                                    Text(AES256Util.decrypt(accountList!![page].accountNumber), color = white, fontWeight = FontWeight.Bold)

                                    Spacer(modifier = Modifier.height(5.dp))

                                    Text(AES256Util.decrypt(accountList!![page].name), color = white, fontSize = 10.sp)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Row(horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically){
                            Text("주문 방식 선택", color = SOZIPColorPalette.current.txtColor, fontWeight = FontWeight.Bold)

                            Spacer(modifier = Modifier.weight(1f))
                        }

                        Spacer(modifier = Modifier.height(5.dp))

                        Row {
                            Button(onClick = { deliverType.value = SOZIPPackagingTypeModel.DELIVERY },
                                shape = RoundedCornerShape(15.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if(deliverType.value == SOZIPPackagingTypeModel.DELIVERY) accent else SOZIPColorPalette.current.btnColor
                                )
                            ) {
                                Column(modifier = Modifier.padding(20.dp)) {
                                    Row(horizontalArrangement = Arrangement.End) {
                                        Icon(imageVector = if(deliverType.value == SOZIPPackagingTypeModel.DELIVERY) Icons.Default.CheckCircle else Icons.Outlined.Circle, contentDescription = null, tint = if(deliverType.value == SOZIPPackagingTypeModel.DELIVERY) white else SOZIPColorPalette.current.txtColor)
                                    }

                                    Spacer(modifier = Modifier.height(10.dp))

                                    Row(verticalAlignment = Alignment.CenterVertically){
                                        Icon(imageVector = Icons.Default.PedalBike, contentDescription = null, tint = if(deliverType.value == SOZIPPackagingTypeModel.DELIVERY) white else SOZIPColorPalette.current.txtColor)
                                        Text(text = "배달", color = if(deliverType.value == SOZIPPackagingTypeModel.DELIVERY) white else SOZIPColorPalette.current.txtColor)
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.width(10.dp))

                            Button(onClick = { deliverType.value = SOZIPPackagingTypeModel.TAKE_OUT },
                                shape = RoundedCornerShape(15.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if(deliverType.value == SOZIPPackagingTypeModel.TAKE_OUT) accent else SOZIPColorPalette.current.btnColor
                                )
                            ) {
                                Column(modifier = Modifier.padding(20.dp)) {
                                    Row(horizontalArrangement = Arrangement.End) {
                                        Icon(imageVector = if(deliverType.value == SOZIPPackagingTypeModel.TAKE_OUT) Icons.Default.CheckCircle else Icons.Outlined.Circle, contentDescription = null, tint = if(deliverType.value == SOZIPPackagingTypeModel.TAKE_OUT) white else SOZIPColorPalette.current.txtColor)
                                    }

                                    Spacer(modifier = Modifier.height(10.dp))

                                    Row(verticalAlignment = Alignment.CenterVertically){
                                        Icon(imageVector = Icons.Default.Fastfood, contentDescription = null, tint = if(deliverType.value == SOZIPPackagingTypeModel.TAKE_OUT) white else SOZIPColorPalette.current.txtColor)
                                        Text(text = "포장", color = if(deliverType.value == SOZIPPackagingTypeModel.TAKE_OUT) white else SOZIPColorPalette.current.txtColor)
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Row(horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically){
                            Text("소집 색상", color = SOZIPColorPalette.current.txtColor, fontWeight = FontWeight.Bold)

                            Spacer(modifier = Modifier.weight(1f))
                        }

                        Spacer(modifier = Modifier.height(5.dp))

                        Row(horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically){
                            Text("선택한 색상이 목록과 채팅에 표시됩니다.", fontSize = 10.sp, color = gray)

                            Spacer(modifier = Modifier.weight(1f))
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically){
                            for(i in 0 until 5){
                                Button(onClick = { selectedColor.value = i }, shape = CircleShape, colors = ButtonDefaults.buttonColors(
                                    containerColor = colors.value[i]
                                ), modifier = Modifier.size(50.dp), contentPadding = PaddingValues(1.dp)
                                ) {
                                    if(selectedColor.value == i){
                                        Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = white)
                                    }
                                }

                                if(i != 4){
                                    Spacer(modifier = Modifier.weight(0.25f))
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Button(onClick = {
                            showDialog = true
                            val location_split = location.value.split(", ")

                            helper.addSOZIP(
                                "${storeName.value} 시키실 분!",
                                "${location_split[1]}, ${location_split[0]}",
                                address.value,
                                locationDescription.value,
                                endDate.value,
                                endTime.value,
                                colors.value[selectedColor.value],
                                AES256Util.encrypt("${selectedAccount.value?.bank} ${selectedAccount.value?.name} ${selectedAccount.value?.accountNumber}"),
                                deliverURL.value,
                                categoryList[selectedCategory.value],
                                maxCount.value,
                                deliverType.value
                            ){
                                if(it){
                                    showDialog = false
                                    showSuccessAlert = true
                                } else{
                                    showDialog = false
                                    showFailAlert = true
                                }
                            }
                        },
                            modifier = Modifier
                                .fillMaxWidth(),
                            enabled = !storeName.value.isEmpty() &&
                                    !endDate.value.isEmpty() &&
                                    !endTime.value.isEmpty() &&
                                    selectedAccount.value != null &&
                                    !location.value.isEmpty() &&
                                    !locationDescription.value.isEmpty() &&
                                    !address.value.isEmpty(),
                            contentPadding = PaddingValues(20.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = accent, disabledContainerColor = gray
                            ),
                            elevation = ButtonDefaults.buttonElevation(5.dp, disabledElevation = 5.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically){
                                Text("소집 업로드", color = white)
                                Icon(imageVector = Icons.Default.ChevronRight, contentDescription = null, tint = white)
                            }
                        }

                        if(showDatePicker.value){
                            DatePickerDialog(
                                onDismissRequest = {
                                    showDatePicker.value = false
                                },
                                confirmButton = {
                                    TextButton(
                                        onClick = {
                                            val formatter = SimpleDateFormat("yy/MM/dd")
                                            val calendar = Calendar.getInstance()
                                            calendar.timeInMillis = datePickerState.selectedDateMillis!!
                                            endDate.value = formatter.format(calendar.time)
                                            showDatePicker.value = false
                                        }
                                    ){
                                        Text("확인", color = accent)
                                    }
                                },
                                dismissButton = {
                                    TextButton(
                                        onClick = {
                                            showDatePicker.value = false
                                        }
                                    ){
                                        Text("취소", color = accent)
                                    }
                                },

                                tonalElevation = 5.dp) {
                                DatePicker(state = datePickerState, colors = DatePickerDefaults.colors(
                                    containerColor = SOZIPColorPalette.current.background,
                                    titleContentColor = SOZIPColorPalette.current.txtColor,
                                    weekdayContentColor = SOZIPColorPalette.current.txtColor,
                                    todayContentColor = accent,
                                    selectedDayContentColor = white,
                                    selectedYearContentColor = white,
                                    todayDateBorderColor = accent,
                                    selectedYearContainerColor = accent,
                                    selectedDayContainerColor = accent
                                ))
                            }
                        }

                        if(showTimePicker.value){
                            TimePickerDialog(onCancel = {
                                showTimePicker.value = false

                            }, onConfirm = {
                                val calendar = Calendar.getInstance()
                                calendar.set(Calendar.HOUR_OF_DAY, timePickerState.hour)
                                calendar.set(Calendar.MINUTE, timePickerState.minute)

                                val formatter = SimpleDateFormat("kk:mm")
                                endTime.value = formatter.format(calendar.time)

                                showTimePicker.value = false
                            }, title="소집 시간 선택", toggle = {

                            }) {
                                TimePicker(state = timePickerState, colors = TimePickerDefaults.colors(
                                    clockDialSelectedContentColor = white,
                                    clockDialUnselectedContentColor = gray,
                                    selectorColor = accent
                                ))
                            }
                        }

                        if (showDialog) {
                            Dialog(
                                onDismissRequest = { showDialog = false },
                                DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
                            ) {
                                Box(
                                    contentAlignment= Center,
                                    modifier = Modifier
                                        .size(100.dp)
                                        .background(
                                            SOZIPColorPalette.current.background.copy(alpha = 0.7f),
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                ) {
                                    CircularProgressIndicator(color = accent)
                                }
                            }
                        }

                        if(showSuccessAlert){
                            AlertDialog(
                                onDismissRequest = { showSuccessAlert = false },

                                confirmButton = {
                                    TextButton(onClick = {
                                        showSuccessAlert = false
                                    }){
                                        Text("확인", color = accent, fontWeight = FontWeight.Bold)
                                    }
                                },
                                title = {
                                    Text("업로드 완료")
                                },
                                text = {
                                    Text("소집이 추가되었어요!")
                                },
                                icon = {
                                    Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null)
                                }
                            )
                        }

                        if(showFailAlert){
                            AlertDialog(
                                onDismissRequest = { showFailAlert = false },

                                confirmButton = {
                                    TextButton(onClick = {
                                        showFailAlert = false
                                    }){
                                        Text("확인", color = accent, fontWeight = FontWeight.Bold)
                                    }
                                },
                                title = {
                                    Text("오류")
                                },
                                text = {
                                    Text("요청하신 작업을 처리하는 중 문제가 발생했습니다.\n정상 로그인 여부, 네트워크 상태를 확인하거나 나중에 다시 시도해주세요.")
                                },
                                icon = {
                                    Icon(imageVector = Icons.Default.Cancel, contentDescription = null)
                                }
                            )
                        }
                    }
                }
            }
        }


    }
}

@Preview
@Composable
fun addSOZIPView_preview(){
    addSOZIPView()
}