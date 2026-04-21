package com.example.umhackathon.controller

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.umhackathon.API.apiClient
import kotlinx.coroutines.launch
import com.example.umhackathon.R
import com.example.umhackathon.model.LoginInput

class loginPageController: AppCompatActivity() {
    private lateinit var usernameField: EditText
    private lateinit var passwordField: EditText
    private lateinit var loginButton: Button

    private lateinit var signUp: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        // Bind views
        usernameField = findViewById(R.id.usernameField)
        passwordField = findViewById(R.id.passwordField)
        loginButton = findViewById(R.id.loginButton)
        signUp = findViewById(R.id.signUp)

        loginButton.setOnClickListener {
            handleLogin()
        }

        signUp.setOnClickListener {
            handleSignUp()
        }
    }

    private fun handleLogin() {
        val username = usernameField.text.toString()
        val password = passwordField.text.toString()

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this@loginPageController, "Empty Fields!", Toast.LENGTH_SHORT).show()
            return
        }

        val request = LoginInput(username, password)

        lifecycleScope.launch {
            try {
                val success  = apiClient.apiService.login(request)

                if (success.error) {
                    Toast.makeText(this@loginPageController, "Login successful", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@loginPageController, homePageController::class.java)
                    intent.putExtra("user_id", success.user_id)
                    startActivity(intent)
                } else {
                    Toast.makeText(this@loginPageController, "Invalid credentials", Toast.LENGTH_SHORT).show()
                }

            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("LOGIN_ERROR", e.toString())
                Toast.makeText(this@loginPageController,
                    "Error: ${e.message}",
                    Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun handleSignUp(){
        val intent = Intent(this@loginPageController, signupPageController::class.java)
        startActivity(intent)
    }
}
