package com.mobile.todo

import android.app.Activity
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.hardware.camera2.CameraCharacteristics
import android.location.*
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.mobile.todo.broadcast.GpsBR
import com.mobile.todo.database.dataset.User
import com.mobile.todo.database.AppDatabase
import com.mobile.todo.databinding.ActivityMainBinding
import kotlinx.coroutines.DelicateCoroutinesApi
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
    private lateinit var profilePic: ImageView

    private lateinit var gpsTextView: TextView
    private lateinit var gpsBR: GpsBR

    private var profilePicImage : Uri? = null

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

        binding = ActivityMainBinding.inflate(layoutInflater)
        userDb = AppDatabase.getDatabase(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if (Utils.checkPermission(this) && Utils.isGpsEnabled(this)) {
            Utils.getCurrentLocation(fusedLocationClient, gpsTextView, this)
        } else if (!Utils.checkPermission(this)) {
            Utils.askPermission(this)
        }

        if(intent.hasExtra("profilePic")) {
            profilePicImage = intent.getParcelableExtra<Uri>("profilePic")!!
            profilePic.setImageURI(profilePicImage)
        }else{
            profilePicImage = null
        }

        // Redirect to Login Activity
        loginButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        signupButton.setOnClickListener {
            writeData()
        }

        profilePic.setOnClickListener {
            intent = Intent(this, Camera::class.java)
            if(profilePicImage != null) {
                intent.putExtra("profilePic", profilePicImage)
            }
            startActivity(intent)
        }


        // Register broadcast receiver to listen to GPS status
        gpsBR = GpsBR(fusedLocationClient, gpsTextView)
        registerReceiver(gpsBR, IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION))
    }


    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(gpsBR)
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun writeData() {
        val username = usernameEditText.text.toString()
        val password = passwordEditText.text.toString()
        val confirmPassword = confirmPasswordEditText.text.toString()

        if (password == confirmPassword && username.isNotEmpty() && password.isNotEmpty()) {
            val user = User(username, password, gpsTextView.text.toString())
            GlobalScope.launch(Dispatchers.IO) {
                userDb.userDao().insertUser(user)
            }
        } else if (password != confirmPassword) {
            Toast.makeText(
                this,
                "Password and Confirm Password must be the same",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            Toast.makeText(this, "Username and Password must not be empty", Toast.LENGTH_SHORT)
                .show()
        }
    }
}