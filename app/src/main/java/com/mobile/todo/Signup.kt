package com.mobile.todo

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
import com.mobile.todo.database.dataset.UserBadge
import com.mobile.todo.utils.Constant
import com.mobile.todo.utils.Permission
import com.mobile.todo.utils.GpsFunction
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class Signup : AppCompatActivity() {

    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var confirmPasswordEditText: EditText

    private lateinit var gpsTextView: TextView
    private lateinit var gpsBR: GpsBR

    private var profilePicImage: Uri? = null

    private lateinit var fusedLocationClient: FusedLocationProviderClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        Constant.setTheme(this)

        usernameEditText = findViewById(R.id.username)
        passwordEditText = findViewById(R.id.password)
        confirmPasswordEditText = findViewById(R.id.confirm_password)
        gpsTextView = findViewById(R.id.gps)
        val profilePic = findViewById<ImageView>(R.id.profile_pic)

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
            } else if (gpsTextView.text.equals(this.resources.getString(R.string.gps_permission_denied))) {
                Permission.openSettings(this)
            }
        }

        // Redirect to Login Activity
        findViewById<Button>(R.id.login_button).setOnClickListener {
            startActivity(Intent(this, Login::class.java))
        }

        // Signup button
        findViewById<Button>(R.id.signup_button).setOnClickListener {
            writeData()
        }

        // Redirect to Camera Activity
        profilePic.setOnClickListener {
            if (Permission.checkCameraPermission(this)) {
                redirectToCamera()
            } else {
                Permission.askCameraPermission(this)
            }
        }
    }

    private fun redirectToCamera() {
        intent = Intent(this, Camera::class.java)
        if (profilePicImage != null) {
            intent.putExtra("profilePic", profilePicImage)
        }
        Camera.PAGE_TO_RETURN = Signup::class
        startActivity(intent)

    }

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

            GlobalScope.launch {
                if (AppDatabase.getDatabase(this@Signup).userDao().getUser(username) == null) {
                    val user =
                        User(
                            username,
                            password,
                            if (gpsTextView.text.equals(this@Signup.resources.getString(R.string.gps)) || gpsTextView.text.equals(
                                    this@Signup.resources.getString(R.string.gps_permission_denied)
                                ) || gpsTextView.text.equals(
                                    this@Signup.resources.getString(R.string.loading_city)
                                )
                            ) {
                                "Unknown"
                            } else {
                                gpsTextView.text.toString()
                            },
                            if (profilePicImage == null) {
                                // Set default profile pic
                                Constant.DEFAULT_PROFILE_PIC
                            } else {
                                profilePicImage!!
                            }
                        )
                    AppDatabase.getDatabase(this@Signup).userDao().insertUser(user)

                    val userId = AppDatabase.getDatabase(this@Signup).userDao().getUser(username, password).id
                    // Add all the badge
                    val badges = AppDatabase.getDatabase(this@Signup).badgeDao().getAllBadge()

                    for(badge in badges){
                        AppDatabase.getDatabase(this@Signup).userBadgeDao().
                        insertBadge(UserBadge(false, userId, badge.name))
                    }

                    // Redirect to login
                    startActivity(Intent(this@Signup, Login::class.java))
                } else {
                    runOnUiThread {
                        Toast.makeText(this@Signup, "Username already exists", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
    }


    private fun startTracking() {
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
                    redirectToCamera()
                } else {
                    // Permission denied, show the dialog
                    Permission.showDialog(this)
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}