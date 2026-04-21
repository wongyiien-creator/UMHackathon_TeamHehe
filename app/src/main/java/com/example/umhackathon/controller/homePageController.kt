    package com.example.umhackathon.controller

    import android.content.Intent
    import android.os.Bundle
    import android.widget.Button
    import androidx.appcompat.app.AppCompatActivity
    import com.example.umhackathon.R

    class homePageController: AppCompatActivity() {
        private lateinit var analysisButton: Button
        private lateinit var comparisonButton: Button
        private lateinit var saveButton: Button
        private lateinit var logoutButton: Button
        private lateinit var userId: String

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.homepage)

            analysisButton = findViewById(R.id.btnAnalysis)
            comparisonButton = findViewById(R.id.btnComparison)
            saveButton = findViewById(R.id.btnAddData)
            logoutButton = findViewById(R.id.btnLogout)

            userId = intent.getStringExtra("user_id") ?: ""

            analysisButton.setOnClickListener {
                handleAnalysis()
            }

            comparisonButton.setOnClickListener {
                handleComparison()
            }

            saveButton.setOnClickListener {
                handleSave()
            }

            logoutButton.setOnClickListener {
                handleLogout()
            }
        }

        private fun handleAnalysis() {
            val intent = Intent(this@homePageController, analysisController::class.java)
            intent.putExtra("user_id", userId)
            startActivity(intent)
        }

        private fun handleComparison(){
            val intent = Intent(this@homePageController, comparisonInputController::class.java)
            intent.putExtra("user_id", userId)
            startActivity(intent)
        }

        private fun handleSave(){
            val intent = Intent(this@homePageController, saveDataController::class.java)
            intent.putExtra("user_id", userId)
            startActivity(intent)
        }

        private fun handleLogout(){
            val intent = Intent(this@homePageController, welcomePageController::class.java)
            startActivity(intent)
        }
    }