package com.mobile.todo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class Signup : AppCompatActivity() {

    private var loginButton : Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        loginButton = findViewById(R.id.login_button)

        // Redirect to Login Activity
        loginButton?.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}