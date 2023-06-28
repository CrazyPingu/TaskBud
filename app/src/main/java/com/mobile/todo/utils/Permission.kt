package com.mobile.todo.utils

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.mobile.todo.BuildConfig

class Permission {
    companion object {
        private var isDialogShown: Boolean = false
        const val CAMERA_PERMISSION_CODE = 1
        const val LOCATION_PERMISSION_CODE = 2

        private val LOCATION_PERMISSION = arrayOf(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
        )

        private val CAMERA_PERMISSION = arrayOf(
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        )


        fun askCameraPermission(activity: Activity, showDialog: Boolean = false) {
            if (!checkPermission(activity, CAMERA_PERMISSION, showDialog)) {
                ActivityCompat.requestPermissions(activity, CAMERA_PERMISSION, CAMERA_PERMISSION_CODE)
            }
        }

        fun askLocationPermission(activity: Activity, showDialog: Boolean = false) {
            if (!checkPermission(activity, LOCATION_PERMISSION, showDialog)) {
                ActivityCompat.requestPermissions(activity, LOCATION_PERMISSION, LOCATION_PERMISSION_CODE)
            }
        }

        fun checkCameraPermission(context: Context, showDialog: Boolean = false): Boolean {
            return checkPermission(context, CAMERA_PERMISSION, showDialog)
        }

        fun checkLocationPermission(context: Context, showDialog: Boolean = false): Boolean {
            return checkPermission(context, LOCATION_PERMISSION, showDialog)
        }

        private fun checkPermission(
            context: Context,
            PERMISSION: Array<String>,
            showDialog: Boolean
        ): Boolean {
            for (permission in PERMISSION) {
                if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    if (showDialog) {
                        showDialog(context)
                    }
                    return false
                }
            }
            return true
        }

        private var dialog: AlertDialog? = null

        fun showDialog(context: Context) {
            if (isDialogShown) return

            dialog = AlertDialog.Builder(context)
                .setTitle("Permissions Required")
                .setMessage("Please grant all the required permissions and restart the app or else the app won't work properly.")
                .setPositiveButton("OK") { dialog, _ ->
                    openSettings(context)
                    dialog.dismiss()
                }
                .setOnDismissListener {
                    isDialogShown = false
                }
                .create()
            dialog?.show()

            isDialogShown = true
        }

        fun openSettings(context: Context) {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null)
            intent.data = uri
            ContextCompat.startActivity(context, intent, null)
        }
    }
}