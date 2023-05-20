package com.mobile.todo.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.widget.TextView
import com.google.android.gms.location.FusedLocationProviderClient
import com.mobile.todo.Utils

class GpsBR(
    private var fusedLocationClient: FusedLocationProviderClient,
    private var gpsTextView: TextView,
) : BroadcastReceiver() {


    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == LocationManager.PROVIDERS_CHANGED_ACTION) {
            val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

            if (gpsEnabled) {
                gpsTextView.text = "Loading city ..."
                Utils.getCurrentLocation(fusedLocationClient, gpsTextView, context)
            }
        }
    }
}
