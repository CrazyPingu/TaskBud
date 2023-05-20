package com.mobile.todo.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.widget.Toast

class GpsBR : BroadcastReceiver(){

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == LocationManager.PROVIDERS_CHANGED_ACTION) {
            val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

            if (gpsEnabled) {
                // GPS is enabled
                // Do something here, such as notifying the user or triggering a specific action
                Toast.makeText(context, "GPS is enabled", Toast.LENGTH_SHORT).show()
            }
        }
    }
}