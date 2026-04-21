package com.example.umhackathon.controller

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.umhackathon.API.apiClient
import com.example.umhackathon.R
import com.example.umhackathon.model.ComparisonRequest
import com.example.umhackathon.model.MarketingMixCompare
import kotlinx.coroutines.launch

class comparisonInputController: AppCompatActivity()  {
    private lateinit var back: Button
    private lateinit var start: Button

    private lateinit var product1: com.google.android.material.textfield.TextInputEditText
    private lateinit var price1: com.google.android.material.textfield.TextInputEditText
    private lateinit var place1: com.google.android.material.textfield.TextInputEditText
    private lateinit var promotion1: com.google.android.material.textfield.TextInputEditText
    private lateinit var product2: com.google.android.material.textfield.TextInputEditText
    private lateinit var price2: com.google.android.material.textfield.TextInputEditText
    private lateinit var place2: com.google.android.material.textfield.TextInputEditText
    private lateinit var promotion2: com.google.android.material.textfield.TextInputEditText
    private lateinit var industry: com.google.android.material.textfield.TextInputEditText
    private lateinit var region: com.google.android.material.textfield.TextInputEditText

    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.comparisoninputpage)

        product1 = findViewById(R.id.product1)
        price1 = findViewById(R.id.price1)
        place1 = findViewById(R.id.place1)
        promotion1 = findViewById(R.id.promotion1)
        product2 = findViewById(R.id.product2)
        place2 = findViewById(R.id.place2)
        price2 = findViewById(R.id.price2)
        promotion2 = findViewById(R.id.promotion2)
        industry = findViewById(R.id.industry)
        region = findViewById(R.id.region)
        back = findViewById(R.id.backButton)
        start = findViewById(R.id.startButton)

        userId = intent.getStringExtra("user_id") ?: ""

       start.setOnClickListener {
            handleStart()
        }

        back.setOnClickListener {
            handleBack()
        }
    }

    private fun handleBack(){
        val intent = Intent(this@comparisonInputController, homePageController::class.java)
        intent.putExtra("user_id", userId)
        startActivity(intent)
    }

    private fun handleStart(){
        val product_1 = product1.text.toString()
        val price_1 = price1.text.toString()
        val place_1 = place1.text.toString()
        val promotion_1 = promotion1.text.toString()
        val product_2 = product2.text.toString()
        val price_2 = price2.text.toString()
        val place_2 = place2.text.toString()
        val promotion_2 = promotion2.text.toString()
        val industry = industry.text.toString()
        val region = region.text.toString()

        if (product_1.isEmpty() || price_1.isEmpty() || place_1.isEmpty() || promotion_1.isEmpty() ||
            product_2.isEmpty() || price_2.isEmpty() || place_2.isEmpty() || promotion_2.isEmpty() ||
            industry.isEmpty() || region.isEmpty()) {

            val intent = Intent(this@comparisonInputController, comparisonController::class.java)
            intent.putExtra("user_id", userId)
            intent.putExtra("p1", product_1)
            intent.putExtra("pr1", price_1)
            intent.putExtra("pl1", place_1)
            intent.putExtra("pro1", promotion_1)


            intent.putExtra("p2", product_2)
            intent.putExtra("pr2", price_2)
            intent.putExtra("pl2", place_2)
            intent.putExtra("pro2", promotion_2)

            intent.putExtra("i", industry)
            intent.putExtra("r", region)

            startActivity(intent)
        }
    }

}

