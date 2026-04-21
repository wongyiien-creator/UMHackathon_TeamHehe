package com.example.umhackathon.controller

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.umhackathon.API.apiClient
import kotlinx.coroutines.launch
import com.example.umhackathon.R
import com.example.umhackathon.model.SignupInput

class signupPageController:  AppCompatActivity()  {
    private lateinit var usernameField: EditText
    private lateinit var passwordField: EditText

    private lateinit var signupButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup)

        // Bind views
        usernameField = findViewById(R.id.usernameField)
        passwordField = findViewById(R.id.passwordField)
        signupButton = findViewById(R.id.signupButton)

        signupButton.setOnClickListener {
            handleLogin()
        }
    }

    private fun handleLogin() {
        val username = usernameField.text.toString()
        val password = passwordField.text.toString()

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this@signupPageController, "Empty Fields", Toast.LENGTH_SHORT).show()
            return
        }

        val request = SignupInput(username, password)

        lifecycleScope.launch {
            try {
                val success= apiClient.apiService.signup(request)

                if (success.error) {
                    Toast.makeText(this@signupPageController, "Login successful", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@signupPageController, homePageController::class.java)
                    intent.putExtra("user_id", success.user_id)
                    startActivity(intent)
                } else {
                    Toast.makeText(this@signupPageController, "Invalid credentials", Toast.LENGTH_SHORT).show()
                }

            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("LOGIN_ERROR", e.toString())
                Toast.makeText(this@signupPageController,
                    "Error: ${e.message}",
                    Toast.LENGTH_LONG).show()
            }
        }
    }
}