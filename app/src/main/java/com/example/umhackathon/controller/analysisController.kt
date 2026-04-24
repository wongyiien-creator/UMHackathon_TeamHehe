package com.example.umhackathon.controller

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.umhackathon.API.apiClient
import com.example.umhackathon.R
import com.example.umhackathon.model.AnalysisRequest
import kotlinx.coroutines.launch

class analysisController: AppCompatActivity()  {
    private lateinit var back: Button
    private lateinit var retry: Button
    private lateinit var whiteSpaces: TextView
    private lateinit var sentimentCorrelation: TextView
    private lateinit var hiddenTruth: TextView
    private lateinit var forecastedDirection: TextView
    private lateinit var product: TextView
    private lateinit var price: TextView
    private lateinit var place: TextView
    private lateinit var promotion: TextView
    private lateinit var forecastedChallenges: TextView
    private lateinit var precaution: TextView
    private lateinit var sales: TextView
    private lateinit var revenue: TextView

    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.analysispage)

        whiteSpaces = findViewById(R.id.whitespaces)
        sentimentCorrelation = findViewById(R.id.sentimentCorrelation)
        hiddenTruth = findViewById(R.id.hiddenTruth)
        forecastedDirection = findViewById(R.id.forecastedDirection)
        product = findViewById(R.id.product)
        place = findViewById(R.id.place)
        price = findViewById(R.id.price)
        promotion = findViewById(R.id.promotion)
        forecastedChallenges = findViewById(R.id.forecastedChallenges)
        precaution = findViewById(R.id.precaution)
        sales = findViewById(R.id.forecastedSales)
        revenue = findViewById(R.id.estimate)
        back = findViewById(R.id.backButton)
        retry = findViewById(R.id.retryButton)


        userId = intent.getStringExtra("user_id") ?: ""

        startAnalysis()

        back.setOnClickListener {
            handleBack()
        }

        retry.setOnClickListener {
            handleRetry()
        }
    }

    private fun startAnalysis(){
        lifecycleScope.launch {
            try {
                Log.d("API_TEST","start analysis")
                val request = AnalysisRequest(userId)
                val analysis = apiClient.apiService.getAnalysis(request)

                //Market Analysis
                whiteSpaces.text = analysis.market_analysis.white_spaces.joinToString("\n\n") {
                    "${it.opportunity}: ${it.explanation}"
                }

                sentimentCorrelation.text = "${analysis.market_analysis.sentiment_correlation.observation}\n\nExplaination:\n${analysis.market_analysis.sentiment_correlation.explanation}"

                hiddenTruth.text = analysis.market_analysis.hidden_truth

                forecastedDirection.text = "${analysis.market_analysis.forecasted_direction.prediction}\n\nExplaination:\n${analysis.market_analysis.forecasted_direction.explanation}"

                //Marketing Mix
                product.text = "${analysis.marketing_mix_strategy.product.strategy}\n\nExplaination:\n${analysis.marketing_mix_strategy.product.explanation}"
                price.text = "${analysis.marketing_mix_strategy.price.strategy}\n\nExplaination:\n${analysis.marketing_mix_strategy.price.explanation}"
                place.text = "${analysis.marketing_mix_strategy.place.strategy}\n\nExplaination:\n${analysis.marketing_mix_strategy.place.explanation}"
                promotion.text = "${analysis.marketing_mix_strategy.promotion.strategy}\n\nExplaination:\n${analysis.marketing_mix_strategy.promotion.explanation}"

                //Forecasted Challenges
                forecastedChallenges.text = analysis.forecasted_challenges.joinToString("\n• ", prefix = "• ")

                //Precaution Plan
                precaution.text = analysis.precaution_plan.joinToString("\n• ", prefix = "• ")

                //Impact
                sales.text = analysis.impact_metrics.forecasted_sales
                revenue.text = analysis.impact_metrics.revenue_growth_estimate

            }

            catch (e: Exception) {
                Log.e("API_ERROR", "Failed to fetch analysis", e)
                whiteSpaces.text = "Error loading data. Please try again."
                sentimentCorrelation.text = "Error loading data. Please try again."
                hiddenTruth.text = "Error loading data. Please try again."
                forecastedDirection.text = "Error loading data. Please try again."

                //Marketing Mix
                product.text = "Error loading data. Please try again."
                price.text = "Error loading data. Please try again."
                place.text = "Error loading data. Please try again."
                promotion.text = "Error loading data. Please try again."

                //Forecast
                forecastedChallenges.text = "Error loading data. Please try again."

                //Precation
                precaution.text = "Error loading data. Please try again."

                //Impact
                sales.text = "Error loading data. Please try again."
                revenue.text = "Error loading data. Please try again."

            }

            catch (e: retrofit2.HttpException) {
                if (e.code() == 422) {
                    Log.e("API_ERROR", "Sanity Check Failed: AI Hallucinated")
                    Toast.makeText(
                        this@analysisController,
                        "The AI was a bit too optimistic! Please try again later!.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
            }
    }
    private fun handleBack(){
        val intent = Intent(this@analysisController, homePageController::class.java)
        intent.putExtra("user_id", userId)
        startActivity(intent)
    }

    private fun handleRetry(){
        whiteSpaces.text = "Loading..."
        sentimentCorrelation.text = "Loading..."
        hiddenTruth.text = "Loading..."
        forecastedDirection.text = "Loading..."
        product.text = "Loading..."
        price.text = "Loading..."
        place.text = "Loading..."
        promotion.text = "Loading..."
        forecastedChallenges.text = "Loading..."
        precaution.text = "Loading..."
        sales.text = "Loading..."
        revenue.text = "Loading..."

        startAnalysis()
    }
}