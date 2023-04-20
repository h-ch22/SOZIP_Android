package com.eje.sozip.sozipMap.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Warning
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.eje.sozip.SOZIP.helper.SOZIPHelper
import com.eje.sozip.SOZIP.models.SOZIPDataModel
import com.eje.sozip.SOZIP.ui.SOZIPDetailView
import com.eje.sozip.frameworks.ui.MainActivity
import com.eje.sozip.frameworks.ui.ModalDialog
import com.eje.sozip.ui.theme.SOZIPColorPalette
import com.eje.sozip.ui.theme.SOZIPTheme
import com.eje.sozip.ui.theme.SOZIP_BG_1
import com.eje.sozip.ui.theme.accent
import com.eje.sozip.ui.theme.black
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.common.internal.service.Common
import com.google.android.gms.location.LocationServices
import com.naver.maps.geometry.Coord
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraAnimation
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.FusedLocationSource
import com.naver.maps.map.util.MarkerIcons
import kotlinx.coroutines.launch
import java.util.concurrent.Executor

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun SOZIPMapView(){
    val context = LocalContext.current
    val lifeCycleOwner = LocalLifecycleOwner.current
    val coroutineScope = rememberCoroutineScope()
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    val showDialog = remember {
        mutableStateOf(false)
    }

    var showAlert by remember{
        mutableStateOf(false)
    }

    var selectedData by remember{
        mutableStateOf(SOZIPDataModel())
    }

    val permissions = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()){
        if(it){

        } else{

        }
    }

    lateinit var locationSource : FusedLocationSource
    val LOCATION_PERMISSION_REQUEST_CODE = 1000

    SOZIPTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = SOZIPColorPalette.current.background) {
            var coord = LatLng(37.541, 126.986)

            if (permissions.all {
                    ContextCompat.checkSelfPermission(
                        context,
                        it
                    ) == PackageManager.PERMISSION_DENIED
                }
            ){
                showAlert = true
            }

            locationSource = FusedLocationSource(context as MainActivity, 1000)

            fusedLocationClient.lastLocation.addOnSuccessListener {
                    it -> it.also{
                    coord = LatLng(it.latitude, it.longitude)
                }
            }


            val mapView = remember{
                var mapView : NaverMap?

                MapView(context).apply {
                    getMapAsync{naverMap ->
                        mapView = naverMap
                        val camUpdate = CameraUpdate.scrollTo(coord).animate(CameraAnimation.Easing)
                        naverMap.moveCamera(camUpdate)
                        naverMap.isIndoorEnabled = true
                        naverMap.uiSettings.isCompassEnabled = true
                        naverMap.uiSettings.isIndoorLevelPickerEnabled = true
                        naverMap.uiSettings.isLocationButtonEnabled = true
                        naverMap.uiSettings.isScaleBarEnabled = true
                        naverMap.uiSettings.isZoomControlEnabled = true

                        if(permissions.all {
                                ContextCompat.checkSelfPermission(
                                    context,
                                    it
                                ) == PackageManager.PERMISSION_DENIED
                            }){
                            naverMap.locationTrackingMode = LocationTrackingMode.None
                        } else{
                            naverMap.locationTrackingMode = LocationTrackingMode.Face
                        }

                        naverMap.locationSource = locationSource

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
                                        showDialog.value = true
                                        selectedData = it
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
                    onDismissRequest = {  },
                    confirmButton = {
                        TextButton(onClick = {
                            showAlert = false
                            checkAndRequestLocationPermissions(
                                context,
                                permissions,
                                launcherMultiplePermissions
                            )
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

            if(showDialog.value){
                ModalDialog(onDismissRequest = { showDialog.value = false }, modifier = Modifier.fillMaxWidth()) {
                    SOZIPDetailView(data = selectedData, showTopBar = false)
                }
            }
        }
    }
}

@Preview
@Composable
fun SOZIPMapView_preview(){
    SOZIPMapView()
}