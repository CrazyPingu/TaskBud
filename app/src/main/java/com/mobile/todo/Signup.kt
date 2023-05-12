package com.mobile.todo

import android.Manifest
import com.google.android.gms.location.LocationRequest

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Intent
import android.content.pm.PackageManager
import android.location.*
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.AbsListView
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY
import com.mobile.todo.database.dataset.User
import com.mobile.todo.database.AppDatabase
import com.mobile.todo.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class Signup : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var userDb: AppDatabase
    private lateinit var loginButton: Button
    private lateinit var signupButton: Button
    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var confirmPasswordEditText: EditText

    private lateinit var gpsTextView: TextView

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        loginButton = findViewById(R.id.login_button)
        signupButton = findViewById(R.id.signup_button)
        usernameEditText = findViewById(R.id.username)
        passwordEditText = findViewById(R.id.password)
        confirmPasswordEditText = findViewById(R.id.confirm_password)
        gpsTextView = findViewById(R.id.gps)

        binding = ActivityMainBinding.inflate(layoutInflater)
        userDb = AppDatabase.getDatabase(this)

        if (Utils.checkPermission(this)) {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
            getCurrentLocation()
        } else {
            Utils.askPermission(this)
        }

        // Redirect to Login Activity
        loginButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        signupButton.setOnClickListener {
            writeData()
        }
    }

    private fun writeData() {
        val username = usernameEditText.text.toString()
        val password = passwordEditText.text.toString()
        val confirmPassword = confirmPasswordEditText.text.toString()

        if (password == confirmPassword && username.isNotEmpty() && password.isNotEmpty()) {
            val user = User(username, password, gpsTextView.text.toString())
            GlobalScope.launch(Dispatchers.IO) {
                userDb.userDao().insertUser(user)
            }
        }
    }

    private fun getCurrentLocation() {
        if (!Utils.checkPermission(this)) {
            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    // Case gps is enabled
                    gpsTextView.text = getCityName(location.latitude, location.longitude)
                }else{
                    // Case gps is disabled
                    gpsTextView.text = "Enable location"
                }
            }
                // Case random error
            .addOnFailureListener { e: Exception ->
                gpsTextView.text = "GPS not found"
            }
    }

    private fun getCityName(latitude: Double, longitude: Double): String {
        val geocoder = Geocoder(this, Locale.getDefault())
        val addressList = geocoder.getFromLocation(latitude, longitude, 1)
        return if (addressList != null && addressList.isNotEmpty()) {
            addressList[0].locality
        } else {
            "ERROR"
        }
    }
}