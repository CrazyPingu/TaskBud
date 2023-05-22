package com.mobile.todo

import android.content.ContentResolver
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.*
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.mobile.todo.broadcast.GpsBR
import com.mobile.todo.database.AppDatabase
import com.mobile.todo.database.dataset.User
import android.provider.Settings
import android.util.Log
import com.mobile.todo.utils.Permission
import com.mobile.todo.utils.GpsFunction
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*


class Signup : AppCompatActivity() {

    private lateinit var userDb: AppDatabase
    private lateinit var loginButton: Button
    private lateinit var signupButton: Button
    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var confirmPasswordEditText: EditText
    private lateinit var profilePic: ImageView

    private lateinit var gpsTextView: TextView
    private lateinit var gpsBR: GpsBR

    private var profilePicImage: Uri? = null

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
        profilePic = findViewById(R.id.profile_pic)

        userDb = AppDatabase.getDatabase(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if (Permission.checkLocationPermission(this)) {
            startTracking()
        } else {
            Permission.askLocationPermission(this)
        }

        if (intent.hasExtra("profilePic")) {
            profilePicImage = intent.getParcelableExtra<Uri>("profilePic")!!
            profilePic.setImageURI(profilePicImage)
        } else {
            profilePicImage = null
        }

        gpsTextView.setOnClickListener {
            if (gpsTextView.text.equals(this.resources.getString(R.string.gps))) {
                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            } else if(gpsTextView.text.equals(this.resources.getString(R.string.gps_permission_denied))) {
                Permission.openSettings(this)
            }
        }

        // Redirect to Login Activity
        loginButton.setOnClickListener {
            startActivity(Intent(this, Login::class.java))
        }

        signupButton.setOnClickListener {
            writeData()
        }

        profilePic.setOnClickListener {
            if (Permission.checkCameraPermission(this)) {
                intent = Intent(this, Camera::class.java)
                if (profilePicImage != null) {
                    intent.putExtra("profilePic", profilePicImage)
                }
                startActivity(intent)
            } else {
                Permission.askCameraPermission(this)
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun writeData() {
        val username = usernameEditText.text.toString()
        val password = passwordEditText.text.toString()
        val confirmPassword = confirmPasswordEditText.text.toString()
        if (password != confirmPassword) {
            Toast.makeText(
                this,
                "Password and Confirm Password must be the same",
                Toast.LENGTH_SHORT
            ).show()
        } else if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Username and Password must not be empty", Toast.LENGTH_SHORT)
                .show()
        } else {
            val user =
                User(
                    username,
                    password,
                    if (gpsTextView.text.equals(this.resources.getString(R.string.gps)) || gpsTextView.text.equals(
                            this.resources.getString(R.string.gps_permission_denied))) {
                        "Unknown"
                    } else {
                        gpsTextView.text.toString()
                    },
                    if (profilePicImage == null) {
                        // Set default profile pic
                        Uri.parse(
                            ContentResolver.SCHEME_ANDROID_RESOURCE
                                    + "://" + this.resources
                                .getResourcePackageName(R.drawable.default_profile_pic)
                                    + '/' + this.resources.getResourceTypeName(R.drawable.default_profile_pic)
                                    + '/' + this.resources.getResourceEntryName(R.drawable.default_profile_pic)
                        ).toString()
                    } else {
                        profilePicImage.toString()
                    }
                )
            GlobalScope.launch(Dispatchers.IO) {
                userDb.userDao().insertUser(user)
            }
        }
    }


    private fun startTracking(){
        if (GpsFunction.isGpsEnabled(this)) {
            GpsFunction.getCurrentLocation(fusedLocationClient, gpsTextView, this)
        } else {
            gpsBR = GpsBR(fusedLocationClient, gpsTextView)
            registerReceiver(gpsBR, IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION))
        }
    }

    override fun onDestroy() {
        if (::gpsBR.isInitialized) {
            unregisterReceiver(gpsBR)
        }
        super.onDestroy()
    }

    override fun onBackPressed() {
        startActivity(Intent(this, Login::class.java))
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            Permission.LOCATION_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Start the receiver in case the user give permission
                    startTracking()
                } else {
                    // Change the text to ask the permission
                    gpsTextView.text = this.resources.getString(R.string.gps_permission_denied)
                    // Permission denied, show the dialog
                    Permission.showDialog(this)
                }
            }
            Permission.CAMERA_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted, send to camera activity
                    intent = Intent(this, Camera::class.java)
                    if (profilePicImage != null) {
                        intent.putExtra("profilePic", profilePicImage)
                    }
                    startActivity(intent)
                } else {
                    // Permission denied, show the dialog
                    Permission.showDialog(this)
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}