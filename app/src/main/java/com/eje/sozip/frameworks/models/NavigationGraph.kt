package com.eje.sozip.frameworks.models

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.eje.sozip.chat.ui.ChatView
import com.eje.sozip.SOZIP.ui.HomeView
import com.eje.sozip.SOZIP.ui.addSOZIPView
import com.eje.sozip.more.ui.MoreView
import com.eje.sozip.sozipMap.ui.SOZIPMapView

@Composable
fun NavigationGraph(navController : NavHostController){
    NavHost(navController = navController, startDestination = BottomNavigationItem.Home.screenRoute){
        composable(BottomNavigationItem.Home.screenRoute){
            HomeView()
        }

        composable(BottomNavigationItem.SOZIPMap.screenRoute){
            SOZIPMapView()
        }

        composable(BottomNavigationItem.addSOZIP.screenRoute){
            addSOZIPView()
        }

        composable(BottomNavigationItem.Chat.screenRoute){
            ChatView()
        }

        composable(BottomNavigationItem.More.screenRoute){
            MoreView()
        }
    }
}