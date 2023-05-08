package com.eje.sozip.sozipMap.helper

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.core.content.PermissionChecker
import com.naver.maps.map.LocationSource

class LocationTrackingManager(
    private val context : Context
) : LocationSource, LocationListener{
    private val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager?
    private var listener : LocationSource.OnLocationChangedListener? = null

    override fun activate(p0: LocationSource.OnLocationChangedListener) {
        if(locationManager == null){
            return
        }

        if (PermissionChecker.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION)
            != PermissionChecker.PERMISSION_GRANTED
            && PermissionChecker.checkSelfPermission(context,
                Manifest.permission.ACCESS_COARSE_LOCATION)
            != PermissionChecker.PERMISSION_GRANTED) {
            return
        }

        this.listener = p0
        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER, 1000, 10f, this
        )
    }

    override fun deactivate() {
        if (locationManager == null) {
            return
        }

        listener = null
        locationManager.removeUpdates(this)
    }

    override fun onLocationChanged(p0: Location) {
        listener?.onLocationChanged(p0)
    }
}