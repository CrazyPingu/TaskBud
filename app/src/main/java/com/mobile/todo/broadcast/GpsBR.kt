package com.mobile.todo.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.widget.TextView
import com.google.android.gms.location.FusedLocationProviderClient
import com.mobile.todo.R
import com.mobile.todo.utils.GpsFunction
import com.mobile.todo.utils.Permission

class GpsBR(
    private var fusedLocationClient: FusedLocationProviderClient,
    private var gpsTextView: TextView,
) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == LocationManager.PROVIDERS_CHANGED_ACTION) {
            val locationManager =
                context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)


            if (Permission.checkLocationPermission(context, true) && gpsEnabled) {
                gpsTextView.text = context.getString(R.string.loading_city)
                GpsFunction.getCurrentLocation(fusedLocationClient, gpsTextView, context)
            }
        }
    }
}
