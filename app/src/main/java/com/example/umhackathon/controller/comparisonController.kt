package com.example.umhackathon.controller

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.umhackathon.API.apiClient
import com.example.umhackathon.R
import com.example.umhackathon.model.ComparisonRequest
import com.example.umhackathon.model.MarketingMixCompare
import kotlinx.coroutines.launch

class comparisonController: AppCompatActivity() {
    private lateinit var back: Button
    private lateinit var retry: Button
    private lateinit var stressTest: TextView
    private lateinit var c_mix1: TextView
    private lateinit var c_mix2: TextView
    private lateinit var decision: TextView
    private lateinit var reason: TextView
    private lateinit var improve: TextView

    private lateinit var userId: String
    private lateinit var requestBody: ComparisonRequest

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.comparisonpage)

        Log.d("Test","comparison")
        stressTest = findViewById(R.id.stressTest)
        c_mix1 = findViewById(R.id.comparemix1)
        c_mix2 = findViewById(R.id.comparemix2)
        decision = findViewById(R.id.decision)
        reason = findViewById(R.id.reason)
        improve = findViewById(R.id.improve)
        retry = findViewById(R.id.retryButton)
        back = findViewById(R.id.backButton)

        userId = intent.getStringExtra("user_id") ?: ""

        val mix1 = MarketingMixCompare(
            product = intent.getStringExtra("p1") ?: "",
            price = intent.getStringExtra("pr1") ?: "",
            place = intent.getStringExtra("pl1") ?: "",
            promotion = intent.getStringExtra("pro1") ?: ""
        )

        val mix2 = MarketingMixCompare(
            product = intent.getStringExtra("p2") ?: "",
            price = intent.getStringExtra("pr2") ?: "",
            place = intent.getStringExtra("pl2") ?: "",
            promotion = intent.getStringExtra("pro2") ?: ""
        )

        requestBody = ComparisonRequest(
            mix1,
            mix2,
            intent.getStringExtra("r") ?: "",
            intent.getStringExtra("i") ?: ""
        )

        Log.d("API_TEST","start comparison")
        handleComparison(requestBody)

        back.setOnClickListener {
            handleBack()
        }

        retry.setOnClickListener {
            handleRetry()
        }
    }

    private fun handleComparison(request: ComparisonRequest) {
        lifecycleScope.launch {
            try {
                val response = apiClient.apiService.compareMarketingMix(requestBody)

                //Stress Test
                val st = response.stress_test
                stressTest.text = "Scenario: ${st.scenario}\n\n" +
                        "Plan A Resilience: ${st.plan_a_resilience}\n" +
                        "Plan B Resilience: ${st.plan_b_resilience}"

                //Pros & Cons
                val comp = response.comparison
                c_mix1.text = "PROS:\n• ${comp.plan_a.pros.joinToString("\n• ")}\n\n" +
                        "CONS:\n• ${comp.plan_a.cons.joinToString("\n• ")}"

                c_mix2.text = "PROS:\n• ${comp.plan_b.pros.joinToString("\n• ")}\n\n" +
                        "CONS:\n• ${comp.plan_b.cons.joinToString("\n• ")}"

                //Final Decision
                val f_decision = response.final_decision
                decision.text =
                    "${f_decision.chosen_plan} (ROI Probability: ${f_decision.roi_probability})"
                reason.text = f_decision.reasoning

                //Optimization Plan
                improve.text = response.optimization_plan.joinToString("\n• ", prefix = "• ")
            }
            catch (e: Exception) {
                Log.e("API_ERROR", "Comparison failed", e)
            }
        }
    }

    private fun handleBack() {
        val intent = Intent(this@comparisonController, homePageController::class.java)
        intent.putExtra("user_id", userId)
        startActivity(intent)
    }

    private fun handleRetry() {
        stressTest.text = "Loading..."
        c_mix1.text = "Loading..."
        c_mix2.text = "Loading..."
        decision.text = "Loading..."
        reason.text = "Loading..."
        improve.text = "Loading..."

        handleComparison(requestBody)
    }

}

