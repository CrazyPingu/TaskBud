package com.mobile.todo

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import android.widget.TextView
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import java.util.*

class Utils {
    companion object {
        private val PERMISSION = arrayOf(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        )

        fun askPermission(activity: Activity) {
            ActivityCompat.requestPermissions(activity, PERMISSION, 1)
        }

        fun checkPermission(context: Context): Boolean {
            for (permission in PERMISSION) {
                if (context.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                    return false
                }
            }
            return true
        }


        // Gps functions
        fun getCurrentLocation(
            fusedLocationClient: FusedLocationProviderClient,
            gpsTextView: TextView, context: Context
        ) {
            if (!checkPermission(context)) {
                return
            }
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        // Case gps is enabled
                        gpsTextView.text =
                            getCityName(location.latitude, location.longitude, context)
                    } else {
                        // Case gps is disabled
                        startLocationUpdates(fusedLocationClient, gpsTextView, context)
                    }
                }
        }


        private fun startLocationUpdates(
            fusedLocationClient: FusedLocationProviderClient,
            gpsTextView: TextView, context: Context
        ) {
            val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000).build()
            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    val location = locationResult.lastLocation
                    if (location != null) {
                        getCityName(location.latitude, location.longitude, context).let {
                            gpsTextView.text = it
                        }
                    }
                    fusedLocationClient.removeLocationUpdates(this)
                }
            }

            if (checkPermission(context)) {
                fusedLocationClient.requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    Looper.getMainLooper()
                )
            }
        }

        private fun getCityName(latitude: Double, longitude: Double, context: Context): String {
            val geocoder = Geocoder(context, Locale.getDefault())
            val addressList = geocoder.getFromLocation(latitude, longitude, 1)
            return if (addressList != null && addressList.isNotEmpty()) {
                addressList[0].locality
            } else {
                "ERROR"
            }
        }

        fun isGpsEnabled(context: Context): Boolean {
            val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        }
    }
}