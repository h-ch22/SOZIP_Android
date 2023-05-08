package com.eje.sozip.sozipMap.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.IconButton
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.eje.sozip.SOZIP.helper.SOZIPHelper
import com.eje.sozip.SOZIP.models.SOZIPDataModel
import com.eje.sozip.SOZIP.ui.SOZIPDetailView
import com.eje.sozip.sozipMap.helper.LocationTrackingManager
import com.eje.sozip.ui.theme.SOZIPColorPalette
import com.eje.sozip.ui.theme.SOZIPTheme
import com.eje.sozip.ui.theme.SOZIP_BG_1
import com.eje.sozip.ui.theme.accent
import com.eje.sozip.ui.theme.black
import com.eje.sozip.ui.theme.gray
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraAnimation
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.MarkerIcons
import kotlinx.coroutines.launch

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterialApi::class)
@Composable
fun SOZIPMapView(){
    val context = LocalContext.current
    val lifeCycleOwner = LocalLifecycleOwner.current
    val coroutineScope = rememberCoroutineScope()

    var showAlert by remember{
        mutableStateOf(false)
    }

    var selectedData by remember{
        mutableStateOf(SOZIPDataModel())
    }

    val currentLocation by remember{mutableStateOf(LatLng(37.541, 126.986))}

    val modalSheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden,
        confirmStateChange = {it != ModalBottomSheetValue.HalfExpanded},
        skipHalfExpanded = true)

    val locationTrackingManager = LocationTrackingManager(context)

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ){
        if(it){
        }
    }


    BackHandler {
        if(modalSheetState.isVisible){
            coroutineScope.launch {
                modalSheetState.hide()
            }
        }
    }

    SOZIPTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = SOZIPColorPalette.current.background) {
            LaunchedEffect(key1 = true){
                val fineLocationPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                val coarseLocationPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)

                if (fineLocationPermission == PackageManager.PERMISSION_DENIED ||
                    coarseLocationPermission == PackageManager.PERMISSION_DENIED){
                    showAlert = true
                } else{

                }
            }

            val mapView = remember{
                var mapView : NaverMap?

                MapView(context).apply {
                    getMapAsync{naverMap ->
                        mapView = naverMap
                        val camUpdate = CameraUpdate.scrollTo(currentLocation).animate(CameraAnimation.Easing)
                        naverMap.moveCamera(camUpdate)
                        naverMap.isIndoorEnabled = true
                        naverMap.uiSettings.isCompassEnabled = true
                        naverMap.uiSettings.isIndoorLevelPickerEnabled = true
                        naverMap.uiSettings.isLocationButtonEnabled = true
                        naverMap.uiSettings.isScaleBarEnabled = true
                        naverMap.uiSettings.isZoomControlEnabled = true
                        naverMap.locationSource = locationTrackingManager
                        naverMap.locationTrackingMode = LocationTrackingMode.Follow

                        SOZIPHelper.SOZIPList.forEach{
                            val location = it.location.split(", ")

                            if(location != null && location.size == 2){
                                Marker().apply{
                                    position = LatLng(location[0].toDouble(), location[1].toDouble())
                                    icon = MarkerIcons.BLACK
                                    iconTintColor = (it.color ?: SOZIP_BG_1).toArgb()

                                    captionText = it.SOZIPName
                                    captionColor = (it.color ?: SOZIP_BG_1).toArgb()

                                    subCaptionText = it.location_description
                                    subCaptionColor = black.toArgb()

                                    isClickable = true

                                    this.setOnClickListener { o ->
                                        selectedData = it

                                        coroutineScope.launch {
                                            if(modalSheetState.isVisible){
                                                modalSheetState.hide()
                                            } else{
                                                modalSheetState.show()
                                            }
                                        }

                                        true
                                    }

                                    map = naverMap
                                }
                            }
                        }

                    }
                }
            }

            val lifecycleObserver = remember {
                LifecycleEventObserver { source, event ->
                    // CoroutineScope 안에서 호출해야 정상적으로 동작합니다.
                    coroutineScope.launch {
                        when (event) {
                            Lifecycle.Event.ON_CREATE -> mapView.onCreate(Bundle())
                            Lifecycle.Event.ON_START -> mapView.onStart()
                            Lifecycle.Event.ON_RESUME -> mapView.onResume()
                            Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                            Lifecycle.Event.ON_STOP -> mapView.onStop()
                            Lifecycle.Event.ON_DESTROY -> mapView.onDestroy()
                            else -> {}
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

            AndroidView(factory = {mapView})

            if(showAlert){
                AlertDialog(
                    onDismissRequest = { showAlert = false },

                    confirmButton = {
                        TextButton(onClick = {
                            showAlert = false
                            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                        }){
                            Text("확인", color = accent, fontWeight = FontWeight.Bold)
                        }
                    },
                    title = {
                        Text("권한 상승 필요")
                    },
                    text = {
                        Text("지도에 현재 위치를 표시하기 위해 위치 권한이 필요합니다.")
                    },
                    icon = {
                        Icon(imageVector = Icons.Default.LocationOn, contentDescription = null)
                    }
                )
            }

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
                                    Icon(imageVector = Icons.Default.Cancel, contentDescription = null, tint = gray)
                                }
                            }
                            SOZIPDetailView(selectedData, showTopBar = false)
                        }

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
}

@Preview
@Composable
fun SOZIPMapView_preview(){
    SOZIPMapView()
}