package com.eje.sozip.chat.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.eje.sozip.SOZIP.models.SOZIPDataModel
import com.eje.sozip.chat.helper.ChatHelper
import com.eje.sozip.chat.models.ChatListDataModel
import com.eje.sozip.chat.models.ChatListModel
import com.eje.sozip.ui.theme.SOZIPColorPalette
import com.eje.sozip.ui.theme.SOZIPTheme

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ChatView(){
    val searchText = remember{
        mutableStateOf("")
    }

    var selectedIndex by remember{
        mutableStateOf(ChatListDataModel())
    }

    val helper = ChatHelper()

    val navController = rememberNavController()

    SOZIPTheme {
        LaunchedEffect(key1 = true){
            helper.getChatList {

            }
        }

        NavHost(navController = navController, startDestination = "ChatView"){
            composable(route = "ChatDetailView"){
                ChatDetailView(SOZIPData = selectedIndex)
            }

            composable(route = "ChatView"){
                Surface(modifier = Modifier.fillMaxSize(), color = SOZIPColorPalette.current.background) {

                    Column(verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
                        .padding(20.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "소집 : SOZIP", fontWeight = FontWeight.Bold, color = SOZIPColorPalette.current.txtColor)

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
                            placeholder = {Text("채팅 검색")},
                            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) }
                        ) {

                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        if(!ChatHelper.chatList.isEmpty()){
                            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)){
                                items(items = ChatHelper.chatList.filter{
                                    it.SOZIPName.contains(searchedText, ignoreCase = true) || it.last_msg.contains(searchedText, ignoreCase = true) || it.participants.values.contains(searchedText)
                                }){
                                    ChatListModel(
                                        data = it,
                                        modifier = Modifier
                                            .animateItemPlacement()
                                    ){
                                        selectedIndex = it
                                        navController.navigate("ChatDetailView"){
                                            popUpTo("ChatView"){
                                                inclusive = false
                                            }
                                        }
                                    }
                                }
                            }
                        }


                    }
                }
            }
        }


    }
}

@Preview
@Composable
fun ChatView_preview(){
    ChatView()
}