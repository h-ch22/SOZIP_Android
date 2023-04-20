package com.eje.sozip.frameworks.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.eje.sozip.frameworks.models.BottomNavigationItem
import com.eje.sozip.frameworks.models.NavigationGraph
import com.eje.sozip.ui.theme.SOZIPColorPalette
import com.eje.sozip.ui.theme.SOZIPTheme
import com.eje.sozip.ui.theme.accent
import com.eje.sozip.ui.theme.gray
import com.eje.sozip.ui.theme.white
import kotlin.system.exitProcess

const val HOME = "HOME"
const val SOZIP_MAP = "SOZIP_MAP"
const val ADD_SOZIP = "ADD_SOZIP"
const val CHAT = "CHAT"
const val MORE = "MORE"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SOZIPTheme {
                MainScreenView()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenView(){
    val navController = rememberNavController()
    val items = listOf<BottomNavigationItem>(
        BottomNavigationItem.Home,
        BottomNavigationItem.SOZIPMap,
        BottomNavigationItem.Chat,
        BottomNavigationItem.More
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    BackHandler {
        Runtime.getRuntime().runFinalization()
    }

    Scaffold(
        bottomBar = {
            BottomAppBar(
            actions = {
                items.forEach { item ->
                    IconButton(onClick = {
                        navController.navigate(item.screenRoute){
                            navController.graph.startDestinationRoute?.let{
                                popUpTo(it)
                            }

                            launchSingleTop = true
                            restoreState = true
                        }
                    }) {
                        Icon(imageVector = item.icon, contentDescription = null, tint = if (currentRoute == item.screenRoute) accent else gray)
                    }
                }
            },
            floatingActionButton = {
                FloatingActionButton(onClick = {}, shape = CircleShape, containerColor = accent){
                    Icon(imageVector = Icons.Default.Add, contentDescription = null, tint = white)
                }
            },
            containerColor = SOZIPColorPalette.current.background
        )}
    ) {
        Box(modifier = Modifier.padding(it)){
            NavigationGraph(navController = navController)
        }
    }
}

@Preview
@Composable
fun MainActivity_previews(){
    MainActivity()
}