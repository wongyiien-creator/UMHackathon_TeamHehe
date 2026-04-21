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
import com.example.umhackathon.R
import com.example.umhackathon.model.DataPayload
import com.google.gson.Gson
import kotlinx.coroutines.launch

class saveDataController: AppCompatActivity() {
    private lateinit var upload: Button
    private lateinit var back: Button
    private lateinit var dataInput: EditText
    private lateinit var nameInput: EditText


    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.savedatapage)

        upload = findViewById(R.id.upload)
        dataInput = findViewById(R.id.input)
        nameInput = findViewById(R.id.nameInput)
        back = findViewById(R.id.back)



        userId = intent.getStringExtra("user_id") ?: ""

        upload.setOnClickListener {
            uploadData()
        }

        back.setOnClickListener {
            handleBack()
        }
    }

    private fun uploadData() {
        val dataName = nameInput.text.toString()
        val userInput = dataInput.text.toString()
        val charCount = userInput.length

        if (userInput.isEmpty() || dataName.isEmpty()) {
            Toast.makeText(this, "Please fill up all the input!", Toast.LENGTH_SHORT).show()
            return
        }

        if (charCount > 10000) {
            Toast.makeText(this, "Data have exceed limit!", Toast.LENGTH_SHORT).show()
            return
        }

        val payload = DataPayload(dataName, userInput)

        lifecycleScope.launch {
            try {
                val response = apiClient.apiService.saveData(userId, payload)
                Toast.makeText(this@saveDataController, "Data Saved!", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Log.e("SAVE_ERROR", e.message.toString())
            }
        }
    }

    private fun handleBack(){
        val intent = Intent(this@saveDataController, homePageController::class.java)
        intent.putExtra("user_id", userId)
        startActivity(intent)
    }
}
