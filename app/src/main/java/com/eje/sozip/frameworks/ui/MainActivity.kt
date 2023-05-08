package com.eje.sozip.frameworks.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.IconButton
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.eje.sozip.SOZIP.ui.addSOZIPView
import com.eje.sozip.frameworks.models.BottomNavigationItem
import com.eje.sozip.frameworks.models.NavigationGraph
import com.eje.sozip.ui.theme.SOZIPColorPalette
import com.eje.sozip.ui.theme.SOZIPTheme
import com.eje.sozip.ui.theme.accent
import com.eje.sozip.ui.theme.gray
import com.eje.sozip.ui.theme.white
import kotlinx.coroutines.launch
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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun MainScreenView(){
    val navController = rememberNavController()
    val items = listOf<BottomNavigationItem>(
        BottomNavigationItem.Home,
        BottomNavigationItem.SOZIPMap,
        BottomNavigationItem.Chat,
        BottomNavigationItem.More
    )
    val coroutineScope = rememberCoroutineScope()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val showBottomBar = remember{
        mutableStateOf(true)
    }
    val modalSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmStateChange = {
            showBottomBar.value = it == ModalBottomSheetValue.Hidden
            it != ModalBottomSheetValue.HalfExpanded
        },
        skipHalfExpanded = true
    )


    BackHandler {
        if(modalSheetState.isVisible){
            coroutineScope.launch {
                showBottomBar.value = true
                modalSheetState.hide()
            }
        } else{
            Runtime.getRuntime().runFinalization()
        }
    }

    Scaffold(
        bottomBar = {
            if(showBottomBar.value){
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
                        FloatingActionButton(onClick = {
                            coroutineScope.launch {
                                if(modalSheetState.isVisible){
                                    showBottomBar.value = true
                                    modalSheetState.hide()
                                } else{
                                    showBottomBar.value = false
                                    modalSheetState.show()
                                }
                            }
                        }, shape = RoundedCornerShape(15.dp), containerColor = accent){
                            Icon(imageVector = Icons.Default.Add, contentDescription = null, tint = white)
                        }
                    },
                    containerColor = SOZIPColorPalette.current.background
                )
            }}
    ) {
        Box(modifier = Modifier.padding(it)){
            NavigationGraph(navController = navController)
        }

        ModalBottomSheetLayout(sheetContent = {
            Surface(color = SOZIPColorPalette.current.background) {
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()) {
                    Row {
                        Spacer(modifier = Modifier.weight(1f))

                        IconButton(onClick = {
                            coroutineScope.launch{
                                showBottomBar.value = true
                                modalSheetState.hide()
                            }
                        }) {
                            Icon(imageVector = Icons.Default.Cancel, contentDescription = null, tint = gray)
                        }
                    }
                    addSOZIPView()
                }
            }

        },
            sheetState = modalSheetState,
            modifier = Modifier.fillMaxSize(),
            sheetShape = RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp)
        ) {

        }
    }
}

@Preview
@Composable
fun MainActivity_previews(){
    MainActivity()
}