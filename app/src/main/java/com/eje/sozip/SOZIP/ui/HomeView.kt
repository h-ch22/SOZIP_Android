package com.eje.sozip.SOZIP.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SelectableChipColors
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.eje.sozip.SOZIP.helper.SOZIPHelper
import com.eje.sozip.SOZIP.models.CategoryListModel
import com.eje.sozip.SOZIP.models.HomeNavigationGraph
import com.eje.sozip.SOZIP.models.SOZIPDataModel
import com.eje.sozip.SOZIP.models.SOZIPListModel
import com.eje.sozip.SOZIP.models.TimingType
import com.eje.sozip.frameworks.helper.AES256Util
import com.eje.sozip.frameworks.models.OnStartScreens
import com.eje.sozip.frameworks.ui.NotificationView
import com.eje.sozip.frameworks.ui.SearchBar
import com.eje.sozip.ui.theme.SOZIPColorPalette
import com.eje.sozip.ui.theme.SOZIPTheme
import com.eje.sozip.ui.theme.accent
import com.eje.sozip.ui.theme.gray
import com.eje.sozip.ui.theme.white
import com.eje.sozip.userManagement.helper.UserManagement
import java.text.SimpleDateFormat
import java.util.Date

fun checkTimes() : TimingType{
    val pattern = "HH"
    val format = SimpleDateFormat(pattern)

    val currentAsString=format.format(Date())
    val current = format.parse(currentAsString)
    val morning_start = format.parse("09")
    val afternoon_start = format.parse("12")
    val night_start = format.parse("17")

    if(current.before(morning_start)){
        return TimingType.NIGHT
    } else if(current.before(afternoon_start)){
        return TimingType.MORNING
    } else if(current.before(night_start)){
        return TimingType.AFTERNOON
    }

    return TimingType.MORNING
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@SuppressLint("RememberReturnType")
@Composable
fun HomeView(){
    var timing by remember{
        mutableStateOf(TimingType.MORNING)
    }

    val helper = SOZIPHelper()
    var showAlert by remember{
        mutableStateOf(false)
    }

    val searchText = remember{
        mutableStateOf("")
    }

    var selectedIndex by remember{
        mutableStateOf(SOZIPDataModel())
    }

    var selectedTag by remember{
        mutableStateOf(-1)
    }

    val navController = rememberNavController()

    SOZIPTheme {
        LaunchedEffect(key1 = true){
            helper.getSOZIP {
                if(!it){
                    showAlert = true
                }
            }
        }

        NavHost(navController = navController, startDestination = "Home"){
            composable(route = "SOZIPDetailView"){
                SOZIPDetailView(selectedIndex, parent = navController)
            }

            composable(route = "NotificationView"){
                NotificationView(navController)
            }

            composable(route = "Home"){
                Surface(modifier = Modifier.fillMaxSize(), color = SOZIPColorPalette.current.background) {
                    Column(verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(horizontal = 20.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "소집 : SOZIP", fontWeight = FontWeight.Bold, color = SOZIPColorPalette.current.txtColor)

                            Spacer(modifier = Modifier.weight(1f))

                            IconButton(onClick = {
                                navController.navigate("NotificationView"){
                                    popUpTo("Home"){
                                        inclusive = false
                                    }
                                }
                            }) {
                                Icon(imageVector = Icons.Default.Notifications, contentDescription = null, tint = SOZIPColorPalette.current.txtColor)
                            }
                        }

                        LaunchedEffect(key1 = true){
                            timing = checkTimes()
                        }

                        Row(verticalAlignment = Alignment.CenterVertically){
                            when(timing){
                                TimingType.MORNING -> Text("좋은 아침이예요,\n${AES256Util.decrypt(UserManagement.userInfo?.name)}님!")
                                TimingType.AFTERNOON -> Text("좋은 오후예요,\n${AES256Util.decrypt(UserManagement.userInfo?.name)}님!")
                                TimingType.NIGHT -> Text("좋은 밤이예요,\n${AES256Util.decrypt(UserManagement.userInfo?.name)}님!")
                            }

                            Spacer(modifier = Modifier.weight(1f))
                        }

                        Spacer(modifier = Modifier.height(5.dp))

                        val searchedText = searchText.value

                        androidx.compose.material3.SearchBar(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight(),
                            query = searchText.value,
                            onQueryChange = {searchText.value = it},
                            onSearch = {},
                            active = false,
                            onActiveChange = {

                            },
                            colors = SearchBarDefaults.colors(
                                containerColor = SOZIPColorPalette.current.btnColor.copy(0.8f)
                            ),
                            placeholder = {Text("원하는 소집을 검색해보세요!")},
                            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) }
                        ) {

                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        if(!SOZIPHelper.categoryList.isEmpty()){
                            LazyRow(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()){
                                item{
                                    FilterChip(selected = selectedTag == -1, onClick = { selectedTag = -1 }, leadingIcon = {
                                        if(selectedTag == -1){
                                            Icon(imageVector = Icons.Default.Check, contentDescription = null, modifier = Modifier.size(FilterChipDefaults.IconSize))
                                        }
                                    }, label = {
                                        Text("전체")
                                    }, colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = accent.copy(0.7f),
                                        selectedLabelColor = white,
                                        selectedLeadingIconColor = white
                                    ))
                                }

                                items(items = SOZIPHelper.categoryList){
                                    Spacer(modifier = Modifier.width(10.dp))

                                    FilterChip(selected = selectedTag > -1 && it == SOZIPHelper.categoryList[selectedTag], onClick = {
                                        selectedTag = SOZIPHelper.categoryList.indexOf(it)
                                        SOZIPHelper.SOZIPList.filter {
                                            it.category == SOZIPHelper.categoryList[selectedTag]
                                        }}, leadingIcon = {
                                        if(selectedTag > -1 && it == SOZIPHelper.categoryList[selectedTag]){
                                            Icon(imageVector = Icons.Default.Check, contentDescription = null, modifier = Modifier.size(FilterChipDefaults.IconSize))
                                        }
                                    }, label = {
                                        Text(it)
                                    }, colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = accent.copy(0.7f),
                                        selectedLabelColor = white,
                                        selectedLeadingIconColor = white
                                    ))

                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        if(SOZIPHelper.SOZIPList.isEmpty()){
                            Text(text = "지금은 진행 중인 소집이 없어요.", color = gray, fontSize = 12.sp)
                            Spacer(modifier = Modifier.height(5.dp))
                            TextButton(onClick = {

                            }) {
                                Text("지금 소집을 만들어보세요!", color = accent, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        } else{
                            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)){
                                if(selectedTag > -1){
                                    items(items = SOZIPHelper.SOZIPList.filter{
                                        (it.SOZIPName.contains(searchedText, ignoreCase = true) && it.category == SOZIPHelper.categoryList[selectedTag])
                                    }, key = {
                                        it.docID
                                    }){
                                        SOZIPListModel(data = it,
                                            modifier = Modifier
                                                .animateItemPlacement()){
                                            selectedIndex = it
                                            navController.navigate("SOZIPDetailView"){
                                                popUpTo("Home"){
                                                    inclusive = false
                                                }
                                            }
                                        }

                                    }
                                } else{
                                    itemsIndexed(key = { index, item ->
                                        item.docID
                                    }, items = SOZIPHelper.SOZIPList.filter{
                                        it.SOZIPName.contains(searchedText, ignoreCase = true)
                                    }){ index, item ->
                                        SOZIPListModel(
                                            data = item,
                                            modifier = Modifier
                                                .animateItemPlacement()){
                                            selectedIndex = item
                                            navController.navigate("SOZIPDetailView"){
                                                popUpTo("Home"){
                                                    inclusive = false
                                                }
                                            }
                                        }
                                    }
                                }

                            }
                        }
                    }

                    if(showAlert){
                        AlertDialog(
                            onDismissRequest = {  },
                            confirmButton = {
                                TextButton(onClick = {
                                    showAlert = false
                                }){
                                    Text("확인", color = accent, fontWeight = FontWeight.Bold)
                                }
                            },
                            title = {
                                Text("오류")
                            },
                            text = {
                                Text("소집 목록을 불러오는 중 문제가 발생하였습니다.\n정상 로그인 여부 및 네트워크 상태를 확인하거나 나중에 다시 시도해주세요.")
                            },
                            icon = {
                                Icon(imageVector = Icons.Default.Warning, contentDescription = null)
                            }
                        )
                    }
                }
            }
        }


    }
}

@Preview
@Composable
fun HomeView_preview(){
    HomeView()
}