package com.mobile.todo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.mobile.todo.database.dataset.User
import com.mobile.todo.database.AppDatabase
import com.mobile.todo.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class Signup : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var userDb: AppDatabase
    private lateinit var loginButton: Button
    private lateinit var signupButton: Button
    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var confirmPasswordEditText: EditText
    private lateinit var GPS: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        loginButton = findViewById(R.id.login_button)
        signupButton = findViewById(R.id.signup_button)
        usernameEditText = findViewById(R.id.username)
        passwordEditText = findViewById(R.id.password)
        confirmPasswordEditText = findViewById(R.id.confirm_password)

        binding = ActivityMainBinding.inflate(layoutInflater)
        userDb = AppDatabase.getDatabase(this)

        if (Utils.checkPermission(this)) {
            //GPS = Utils.getGPS(this)
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
            val user = User(username, password, GPS)
            GlobalScope.launch(Dispatchers.IO) {
                userDb.userDao().insertUser(user)
            }
        }
    }
}