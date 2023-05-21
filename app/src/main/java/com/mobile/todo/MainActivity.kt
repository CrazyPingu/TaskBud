package com.mobile.todo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText


class MainActivity : AppCompatActivity() {

    private lateinit var username: EditText
    private lateinit var signupButton: Button
    private lateinit var loginButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        username = findViewById(R.id.username)
        signupButton = findViewById(R.id.signup_button)
        loginButton = findViewById(R.id.login_button)

        // Redirect to Signup Activity
        signupButton.setOnClickListener {
            startActivity(Intent(this, Signup::class.java))
        }

        loginButton.setOnClickListener {
            startActivity(Intent(this, HomePage::class.java))
        }


    }
}