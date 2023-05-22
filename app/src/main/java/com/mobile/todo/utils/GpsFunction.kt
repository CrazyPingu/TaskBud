package com.mobile.todo.utils

import android.app.Activity
import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.media.audiofx.Visualizer.MeasurementPeakRms
import android.os.Looper
import android.widget.TextView
import com.google.android.gms.location.*
import java.util.*


class GpsFunction {
    companion object {
        fun getCurrentLocation(
            fusedLocationClient: FusedLocationProviderClient,
            gpsTextView: TextView, context: Context
        ) {
            if (!Permission.checkLocationPermission(context)) {
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
            val locationRequest =
                LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000).build()
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

            if (Permission.checkLocationPermission(context)) {
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
            val locationManager =
                context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        }
    }
}