package com.eje.sozip.more.ui

import android.graphics.drawable.Icon
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.KeyboardDoubleArrowUp
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.eje.sozip.R
import com.eje.sozip.more.models.FeedbackHubTypeModel
import com.eje.sozip.ui.theme.SOZIPColorPalette
import com.eje.sozip.ui.theme.SOZIPTheme
import com.eje.sozip.ui.theme.accent
import com.eje.sozip.ui.theme.gray
import com.eje.sozip.ui.theme.red
import com.eje.sozip.ui.theme.white

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedbackHubView(parent: NavHostController){
    var selectedType by remember{
        mutableStateOf<FeedbackHubTypeModel?>(null)
    }
    
    var title by remember{
        mutableStateOf("")
    }

    var contents by remember{
        mutableStateOf("")
    }

    SOZIPTheme {
        Scaffold(topBar = {
            TopAppBar (
                title = {
                    Text(text = "피드백 허브", color = SOZIPColorPalette.current.txtColor)
                },
                navigationIcon = {
                    androidx.compose.material.IconButton(onClick = { parent.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null, tint = accent)
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = SOZIPColorPalette.current.background,
                    titleContentColor = SOZIPColorPalette.current.txtColor
                )
            )
        }, content = {
            Surface(modifier = Modifier
                .fillMaxSize()
                .padding(it), color = SOZIPColorPalette.current.background){
                Column(modifier= Modifier
                    .padding(it)
                    .padding(20.dp), verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = painterResource(id = R.drawable.bg_feedback_hub),
                        contentDescription = null,
                        modifier = Modifier
                            .width(100.dp)
                            .height(100.dp)
                    )

                    Text(text = "고객님의 소집은 어떠셨나요?", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = SOZIPColorPalette.current.txtColor)

                    Spacer(modifier = Modifier.height(20.dp))

                    Row {
                        Spacer(modifier = Modifier.weight(1f))

                        Button(onClick = {
                            selectedType = FeedbackHubTypeModel.COMPLIMENT
                        },
                            shape= RoundedCornerShape(15.dp),
                            modifier = Modifier
                                .size(85.dp)
                                .padding(5.dp),
                            contentPadding = PaddingValues(1.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if(selectedType == FeedbackHubTypeModel.COMPLIMENT) accent
                                else SOZIPColorPalette.current.btnColor,
                                contentColor = if(selectedType == FeedbackHubTypeModel.COMPLIMENT) white else accent
                            ),
                            elevation = ButtonDefaults.buttonElevation(5.dp, disabledElevation = 5.dp)) {
                            Icon(imageVector = Icons.Default.Favorite, contentDescription = null, tint = if(selectedType == FeedbackHubTypeModel.COMPLIMENT) white else accent)
                        }

                        Spacer(modifier = Modifier.weight(1f))

                        Button(onClick = {
                            selectedType = FeedbackHubTypeModel.IMPROVE
                        },
                            shape= RoundedCornerShape(15.dp),
                            modifier = Modifier
                                .size(85.dp)
                                .padding(5.dp),
                            contentPadding = PaddingValues(1.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if(selectedType == FeedbackHubTypeModel.IMPROVE) accent
                                else SOZIPColorPalette.current.btnColor,
                                contentColor = if(selectedType == FeedbackHubTypeModel.IMPROVE) white else accent
                            ),
                            elevation = ButtonDefaults.buttonElevation(5.dp, disabledElevation = 5.dp)) {
                            Icon(imageVector = Icons.Default.KeyboardDoubleArrowUp, contentDescription = null, tint = if(selectedType == FeedbackHubTypeModel.IMPROVE) white else accent)
                        }

                        Spacer(modifier = Modifier.weight(1f))

                        Button(onClick = {
                            selectedType = FeedbackHubTypeModel.QUESTION
                        },
                            shape= RoundedCornerShape(15.dp),
                            modifier = Modifier
                                .size(85.dp)
                                .padding(5.dp),
                            contentPadding = PaddingValues(1.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if(selectedType == FeedbackHubTypeModel.QUESTION) accent
                                else SOZIPColorPalette.current.btnColor,
                                contentColor = if(selectedType == FeedbackHubTypeModel.QUESTION) white else accent
                            ),
                            elevation = ButtonDefaults.buttonElevation(5.dp, disabledElevation = 5.dp)) {
                            Icon(imageVector = Icons.Default.QuestionMark, contentDescription = null, tint = if(selectedType == FeedbackHubTypeModel.QUESTION) white else accent)
                        }

                        Spacer(modifier = Modifier.weight(1f))
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = title,
                        onValueChange = { textVal : String -> title = textVal },
                        label = { androidx.compose.material3.Text("피드백 제목") },
                        placeholder = { androidx.compose.material3.Text("피드백 제목") } ,
                        leadingIcon = {
                            androidx.compose.material3.Icon(
                                imageVector = Icons.Default.Favorite,
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

                    Spacer(modifier = Modifier.height(20.dp))

                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = contents,
                        onValueChange = { textVal : String -> contents = textVal },
                        label = { androidx.compose.material3.Text("고객님의 소집 경험을 들려주세요.") },
                        placeholder = { androidx.compose.material3.Text("피드백 내용") } ,
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
                        maxLines = 10
                    )


                    if(title != "" && contents != "" && selectedType != null){
                        Spacer(modifier = Modifier.height(20.dp))

                        Button(onClick = {

                        },
                            modifier = Modifier
                                .fillMaxWidth(),
                            contentPadding = PaddingValues(20.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = accent, disabledContainerColor = gray
                            ),
                            elevation = ButtonDefaults.buttonElevation(5.dp, disabledElevation = 5.dp)
                        ) {
                            Row{
                                androidx.compose.material3.Text("피드백 보내기", color = white)
                                androidx.compose.material3.Icon(
                                    imageVector = Icons.Default.ChevronRight,
                                    contentDescription = null,
                                    tint = white
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    TextButton(onClick = { /*TODO*/ }) {
                        Text(text = "보낸 피드백 확인하기", color = accent)
                    }
                }
            }
        })
    }
}

@Preview(showBackground = true, device = "id:pixel_7_pro")
@Composable
fun FeedbackHubView_previews(){
    FeedbackHubView(rememberNavController())
}