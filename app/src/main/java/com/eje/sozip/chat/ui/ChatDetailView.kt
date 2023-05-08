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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Key
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TextFieldDefaults.indicatorLine
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eje.sozip.SOZIP.models.SOZIPDataModel
import com.eje.sozip.chat.helper.ChatHelper
import com.eje.sozip.chat.models.ChatBubbleShape
import com.eje.sozip.chat.models.ChatListDataModel
import com.eje.sozip.frameworks.helper.AES256Util
import com.eje.sozip.ui.theme.SOZIPColorPalette
import com.eje.sozip.ui.theme.SOZIPTheme
import com.eje.sozip.ui.theme.accent
import com.eje.sozip.ui.theme.black
import com.eje.sozip.ui.theme.gray
import com.eje.sozip.ui.theme.red
import com.eje.sozip.ui.theme.white
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ChatDetailView(SOZIPData : ChatListDataModel){
    var msg = remember {
        mutableStateOf("")
    }

    val helper = ChatHelper()

    val interactionSource = remember { MutableInteractionSource() }
    val rotation = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()

    SOZIPTheme {
        LaunchedEffect(key1 = true){
            helper.getChatContents(SOZIPData){

            }
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
                Column(
                    Modifier
                        .background(SOZIPColorPalette.current.background)
                        .padding(it)
                        .fillMaxSize()) {
                    Column(verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(20.dp)) {
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.weight(1f)){
                            itemsIndexed(key = {index, item ->
                                item.docId
                            }, items = ChatHelper.chatContents){index, item ->
                                ChatBubbleShape(data = item, SOZIPData = SOZIPData, modifier = Modifier.animateItemPlacement())
                            }
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
//                            OutlinedTextField(
//                                modifier = Modifier
//                                    .weight(1f)
//                                    .height(30.dp),
//                                value = msg.value,
//                                onValueChange = { textVal : String -> msg.value = textVal },
//                                label = { androidx.compose.material3.Text("메시지를 입력하세요.") },
//                                placeholder = { androidx.compose.material3.Text("메시지") } ,
//                                colors = OutlinedTextFieldDefaults.colors(
//                                    cursorColor = accent,
//                                    focusedBorderColor = accent,
//                                    errorCursorColor = red,
//                                    errorLeadingIconColor = red,
//                                    disabledPlaceholderColor = gray,
//                                    focusedTextColor = accent,
//                                    focusedLabelColor = accent,
//                                    focusedLeadingIconColor = accent,
//                                    disabledTextColor = gray,
//                                    unfocusedLabelColor = SOZIPColorPalette.current.txtColor,
//                                    unfocusedLeadingIconColor = SOZIPColorPalette.current.txtColor,
//                                    unfocusedSupportingTextColor = SOZIPColorPalette.current.txtColor,
//                                    selectionColors = TextSelectionColors(handleColor = accent, backgroundColor = accent.copy(alpha = 0.5f))
//
//                                )
//                            )

                            BasicTextField(
                                value = msg.value,
                                onValueChange = { msg.value = it },
                                modifier = Modifier
                                    .weight(1f)
                                    .wrapContentHeight()
                                    .blur(30.dp),
                                singleLine = false,
                                interactionSource = interactionSource,
                                enabled = true,

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
                                    colors = TextFieldDefaults.colors(
                                        focusedTextColor = SOZIPColorPalette.current.txtColor,
                                        unfocusedTextColor = SOZIPColorPalette.current.txtColor,
                                        focusedContainerColor = SOZIPColorPalette.current.btnColor,
                                        unfocusedContainerColor = SOZIPColorPalette.current.btnColor,
                                        cursorColor = accent,
                                        selectionColors = TextSelectionColors(handleColor = accent, backgroundColor = accent.copy(0.5f)),
                                        focusedIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent,
                                        focusedLabelColor = gray,
                                        unfocusedLabelColor = gray,
                                        focusedPlaceholderColor = gray,
                                        unfocusedPlaceholderColor = gray
                                    )
                                )
                            }

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