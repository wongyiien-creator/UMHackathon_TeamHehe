package com.example.umhackathon.controller

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.umhackathon.R

class welcomePageController: AppCompatActivity() {
    private lateinit var loginButton: Button
    private lateinit var signupButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.welcomepage)

        Log.d("API_TEST", "done")

        // Bind views
        loginButton = findViewById(R.id.loginButton)
        signupButton = findViewById(R.id.signupButton)

        loginButton.setOnClickListener {
            handleLogin()
        }

        signupButton.setOnClickListener {
            handleSignUp()
        }
    }

    private fun handleLogin() {
        val intent = Intent(this@welcomePageController, loginPageController::class.java)
        startActivity(intent)
    }

    private fun handleSignUp() {
        val intent = Intent(this@welcomePageController, signupPageController::class.java)
        startActivity(intent)
    }

}