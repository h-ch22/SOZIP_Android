package com.eje.sozip.chat.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Report
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eje.sozip.SOZIP.helper.SOZIPHelper
import com.eje.sozip.SOZIP.models.SOZIPDataModel
import com.eje.sozip.SOZIP.models.SOZIPParticipantsListModel
import com.eje.sozip.SOZIP.ui.SOZIPDetailView
import com.eje.sozip.SOZIP.ui.SOZIPInsideMapView
import com.eje.sozip.chat.helper.ChatHelper
import com.eje.sozip.chat.models.ChatBubbleShape
import com.eje.sozip.chat.models.ChatListDataModel
import com.eje.sozip.frameworks.helper.AES256Util
import com.eje.sozip.ui.theme.SOZIPColorPalette
import com.eje.sozip.ui.theme.SOZIPTheme
import com.eje.sozip.ui.theme.accent
import com.eje.sozip.ui.theme.blue
import com.eje.sozip.ui.theme.btnColorAsDark
import com.eje.sozip.ui.theme.gray
import com.eje.sozip.ui.theme.green
import com.eje.sozip.ui.theme.red
import com.eje.sozip.ui.theme.white
import com.eje.sozip.userManagement.helper.UserManagement
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterialApi::class
)
@Composable
fun ChatDetailView(SOZIPData : ChatListDataModel){
    val msg = remember {
        mutableStateOf("")
    }

    val listState = rememberLazyListState()

    val helper = ChatHelper()
    val sozipHelper = SOZIPHelper()
    val config = LocalConfiguration.current
    val deviceHeight = config.screenHeightDp
    val coroutineScope = rememberCoroutineScope()

    val interactionSource = remember { MutableInteractionSource() }
    val rotation = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()
    val bottomSheetState = rememberBottomSheetScaffoldState()
    var data by remember{
        mutableStateOf<SOZIPDataModel?>(null)
    }

    var iconList = remember{
        mutableStateListOf(
            Icons.Default.PhotoCamera,
            Icons.Default.PhotoLibrary,
            Icons.Default.MonetizationOn,
            Icons.Default.Check,
            Icons.Default.Close,
            Icons.Default.Report
        )
    }

    var titleList = remember{
        mutableStateListOf(
            "사진 촬영", "이미지 선택", "계좌 정보 전송", "소집 완료", "소집 취소", "소집 신고"
        )
    }

    var tintList = remember{
        mutableStateListOf(
            blue, blue, accent, green, red, red
        )
    }


    val modalSheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden,
        confirmStateChange = {it != ModalBottomSheetValue.HalfExpanded},
        skipHalfExpanded = true)

    LaunchedEffect(key1 = true){
        sozipHelper.getSpecificSOZIP(id = SOZIPData.docId){
            data = it
        }
    }

    SOZIPTheme {
        ModalBottomSheetLayout(sheetContent = {
            if(modalSheetState.isVisible){
                Surface(modifier = Modifier.background(SOZIPColorPalette.current.background)) {
                    Column(modifier = Modifier.fillMaxWidth().background(SOZIPColorPalette.current.background)) {
                        Row(modifier = Modifier.padding(horizontal = 20.dp)){
                            Spacer(modifier = Modifier.weight(1f))

                            IconButton(onClick = {
                                coroutineScope.launch {
                                    modalSheetState.hide()
                                }
                            }) {
                                androidx.compose.material3.Icon(imageVector = Icons.Default.Cancel, contentDescription = null, tint = gray)
                            }
                        }
                        data?.let { SOZIPDetailView(it, showTopBar = false) }
                    }

                }
            }
        },
            sheetState = modalSheetState,
            modifier = Modifier.fillMaxSize(),
            sheetShape = RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp)
        ) {

        }

        Surface(color = SOZIPColorPalette.current.background, modifier = Modifier.fillMaxSize()) {
            Scaffold(topBar = {
                TopAppBar(
                    title = { Text(text = AES256Util.decrypt(SOZIPData.SOZIPName), color = SOZIPColorPalette.current.txtColor) },
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

            }, content ={
                val values = it

                BottomSheetScaffold(
                    scaffoldState = bottomSheetState,
                    sheetShape = RoundedCornerShape(topEnd = 15.dp, topStart = 15.dp),
                    sheetShadowElevation = 20.dp,
                    sheetContainerColor = SOZIPColorPalette.current.btnColor.copy(0.7f),
                    sheetPeekHeight = 100.dp,
                    sheetContent = {
                        SOZIPTheme {
                            Column(modifier = Modifier
                                .background(SOZIPColorPalette.current.btnColor.copy(0.7f))
                                .fillMaxWidth()
                                .heightIn(min = 100.dp, max = deviceHeight.dp)
                                .padding(horizontal = 20.dp, vertical = 10.dp), verticalArrangement = Arrangement.Top) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    BasicTextField(
                                        value = msg.value,
                                        onValueChange = { msg.value = it },
                                        modifier = Modifier
                                            .weight(0.95f)
                                            .wrapContentHeight(),
                                        singleLine = false,
                                        interactionSource = interactionSource,
                                        enabled = true,
                                        textStyle = TextStyle(
                                            color = SOZIPColorPalette.current.txtColor,
                                            fontSize = 12.sp,
                                            textAlign = TextAlign.Start
                                        )

                                    ) { innerTextField ->
                                        TextFieldDefaults.DecorationBox(
                                            value = msg.value,
                                            innerTextField = innerTextField,
                                            enabled = true,
                                            singleLine = false,
                                            placeholder = {
                                                Text(text = "메시지를 입력하세요.")
                                            },
                                            visualTransformation = VisualTransformation.None,
                                            interactionSource = interactionSource,
                                            contentPadding = TextFieldDefaults.textFieldWithoutLabelPadding(
                                                top = 0.dp,
                                                bottom = 0.dp
                                            ),
                                            shape = RoundedCornerShape(50.dp),
                                            colors = TextFieldDefaults.colors(
                                                focusedTextColor = SOZIPColorPalette.current.txtColor,
                                                unfocusedTextColor = SOZIPColorPalette.current.txtColor,
                                                focusedContainerColor = SOZIPColorPalette.current.txtFieldColor,
                                                unfocusedContainerColor = SOZIPColorPalette.current.txtFieldColor,
                                                cursorColor = accent,
                                                selectionColors = TextSelectionColors(handleColor = accent, backgroundColor = accent.copy(0.5f)),
                                                focusedIndicatorColor = Color.Transparent,
                                                unfocusedIndicatorColor = Color.Transparent,
                                                focusedLabelColor = gray,
                                                unfocusedLabelColor = gray,
                                                focusedPlaceholderColor = gray,
                                                unfocusedPlaceholderColor = gray,
                                                disabledPlaceholderColor = gray
                                            )
                                        )
                                    }

                                    Spacer(modifier = Modifier.weight(0.05f))

                                    Button(onClick = {
                                        scope.launch {
                                            rotation.animateTo(
                                                targetValue = 180f,
                                                animationSpec = tween(500, easing = LinearEasing)
                                            )
                                            rotation.animateTo(
                                                targetValue = 360f,
                                                animationSpec = tween(500, easing = LinearEasing)
                                            )
                                        }

                                        helper.sendPlainText(SOZIPData.docId, msg.value){
                                            if(it){
                                                msg.value = ""
                                            }
                                        }
                                    },
                                        modifier = Modifier.size(30.dp),
                                        shape = CircleShape,
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = SOZIPData.color,
                                            disabledContainerColor = gray,
                                            contentColor = white,
                                            disabledContentColor = white
                                        ),
                                        contentPadding = PaddingValues(1.dp),
                                        enabled = !msg.value.isEmpty()) {
                                        Icon(imageVector = Icons.Default.ArrowUpward, contentDescription = null, tint = white, modifier = Modifier.size(15.dp))
                                    }
                                }

                                Column(verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.verticalScroll(
                                    rememberScrollState())) {
                                    Spacer(modifier = Modifier.height(20.dp))

                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(text = "채팅 메뉴", color = SOZIPColorPalette.current.txtColor, fontWeight = FontWeight.Bold)

                                        Spacer(modifier = Modifier.weight(1f))
                                    }

                                    Spacer(modifier = Modifier.height(10.dp))
                                    Divider(modifier = Modifier.fillMaxWidth(), color = gray.copy(alpha = 0.5f))
                                    Spacer(modifier = Modifier.height(10.dp))

                                    LazyRow(horizontalArrangement = Arrangement.spacedBy(20.dp), verticalAlignment = Alignment.CenterVertically){
                                        itemsIndexed(key = {index, item ->
                                            item
                                        }, items = titleList){index, item ->
                                            if(index >= 3 && index < 5){
                                                if(SOZIPData.manager == UserManagement.userInfo?.uid){
                                                    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                                                        Button(onClick = { /*TODO*/ },
                                                            modifier = Modifier.size(50.dp),
                                                            shape = CircleShape,
                                                            contentPadding = PaddingValues(1.dp),
                                                            colors = ButtonDefaults.buttonColors(containerColor = SOZIPColorPalette.current.btnColor)
                                                        ) {
                                                            Icon(imageVector = iconList[index], contentDescription = null, tint = tintList[index])
                                                        }

                                                        Spacer(Modifier.height(5.dp))
                                                        Text(text = item, color = SOZIPColorPalette.current.txtColor, fontSize = 10.sp)
                                                    }
                                                }
                                            } else{
                                                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                                                    Button(onClick = { /*TODO*/ },
                                                        modifier = Modifier.size(50.dp),
                                                        shape = CircleShape,
                                                        contentPadding = PaddingValues(1.dp),
                                                        colors = ButtonDefaults.buttonColors(containerColor = SOZIPColorPalette.current.btnColor)
                                                    ) {
                                                        Icon(imageVector = iconList[index], contentDescription = null, tint = tintList[index])
                                                    }

                                                    Spacer(Modifier.height(5.dp))
                                                    Text(text = item, color = SOZIPColorPalette.current.txtColor, fontSize = 10.sp)
                                                }
                                            }

                                        }
                                    }

                                    if(data != null){
                                        Spacer(modifier = Modifier.height(20.dp))

                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(text = "소집 정보", color = SOZIPColorPalette.current.txtColor, fontWeight = FontWeight.Bold)

                                            Spacer(modifier = Modifier.weight(1f))

                                            TextButton(onClick = {
                                                coroutineScope.launch {
                                                    if(modalSheetState.isVisible){
                                                        modalSheetState.hide()
                                                    } else{
                                                        modalSheetState.show()
                                                    }
                                                }
                                            }) {
                                                Text(text = "자세히", fontSize = 10.sp, color = gray)
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(10.dp))
                                        Divider(modifier = Modifier.fillMaxWidth(), color = gray.copy(alpha = 0.5f))
                                        Spacer(modifier = Modifier.height(10.dp))

                                        SOZIPInsideMapView(data = data!!, modifier = Modifier)

                                        Spacer(modifier = Modifier.height(20.dp))

                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(text = "참여자 정보", color = SOZIPColorPalette.current.txtColor, fontWeight = FontWeight.Bold)

                                            Spacer(modifier = Modifier.weight(1f))
                                        }

                                        Spacer(modifier = Modifier.height(10.dp))
                                        Divider(modifier = Modifier.fillMaxWidth(), color = gray.copy(alpha = 0.5f))
                                        Spacer(modifier = Modifier.height(10.dp))

                                        data!!.profile?.let { SOZIPParticipantsListModel(participants = data!!.participants, profiles = it) }
                                    }
                                }
                            }
                        }
                    }
                ) {
                    SOZIPTheme {
                        LaunchedEffect(key1 = true){
                            helper.getChatContents(SOZIPData){

                            }

                            listState.animateScrollToItem(ChatHelper.chatContents.size)
                        }


                        Column(
                            Modifier
                                .background(SOZIPColorPalette.current.background)
                                .padding(it)
                                .fillMaxSize()) {
                            Column(verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(horizontal = 20.dp)) {
                                LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier
                                    .weight(1f)
                                    , state = listState){
                                    itemsIndexed(key = {index, item ->
                                        item.docId
                                    }, items = ChatHelper.chatContents){index, item ->
                                        ChatBubbleShape(data = item, SOZIPData = SOZIPData, modifier = Modifier.animateItemPlacement())
                                    }
                                }
                            }
                        }

                    }
                }
            })
    }


    }
}

@Preview
@Composable
fun ChatDetailView_previews(){
    ChatDetailView(SOZIPData = ChatListDataModel())
}