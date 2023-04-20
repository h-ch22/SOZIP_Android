package com.eje.sozip.SOZIP.ui

import android.os.Bundle
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.eje.sozip.SOZIP.models.SOZIPDataModel
import com.eje.sozip.ui.theme.SOZIPTheme
import com.eje.sozip.ui.theme.SOZIP_BG_1
import com.eje.sozip.ui.theme.black
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraAnimation
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapView
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.MarkerIcons
import kotlinx.coroutines.launch

@Composable
fun SOZIPInsideMapView(data : SOZIPDataModel, modifier : Modifier){
    val context = LocalContext.current
    val lifeCycleOwner = LocalLifecycleOwner.current
    val coroutineScope = rememberCoroutineScope()

    SOZIPTheme {
        val mapView = remember{
            MapView(context).apply {
                getMapAsync{naverMap ->
                    val coordAsString = data.location.split(", ")
                    val coord = LatLng(coordAsString[0].toDouble(), coordAsString[1].toDouble())

                    val camUpdate = CameraUpdate.scrollTo(coord).animate(CameraAnimation.Easing)
                    naverMap.moveCamera(camUpdate)

                    val marker = Marker()
                    marker.position = coord
                    marker.icon = MarkerIcons.BLACK
                    marker.iconTintColor = (data.color ?: SOZIP_BG_1).toArgb()
                    marker.captionText = "소집 장소"
                    marker.captionColor = (data.color ?: SOZIP_BG_1).toArgb()
                    marker.map = naverMap
                    marker.subCaptionText = data.location_description
                    marker.subCaptionColor = black.toArgb()
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
                        else -> mapView.onPause()
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

        AndroidView(factory = {mapView}, modifier = Modifier.fillMaxWidth().height(200.dp))
    }
}