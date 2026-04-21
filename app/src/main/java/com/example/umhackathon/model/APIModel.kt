package com.example.umhackathon.model

data class DataPayload(
    val data_name: String,
    val content: Any
)

data class DataPayloadResponse(
    val data_id: String,
    val error: Boolean
)

data class LoginInput(
    val username: String,
    val password: String
)

data class LoginResponse(
    val user_id: String,
    val error: Boolean
)

data class SignupInput(
    val username: String,
    val password: String
)

data class SignUpResponse(
    val user_id: String,
    val error: Boolean
)

data class AnalysisResponse(
    val market_analysis: MarketAnalysis,
    val marketing_mix_strategy: MarketingMix,
    val forecasted_challenges: List<String>,
    val precaution_plan: List<String>,
    val impact_metrics: ImpactMetrics
)

data class MarketAnalysis(
    val white_spaces: List<WhiteSpace>,
    val sentiment_correlation: ObservationDetail,
    val hidden_truth: String,
    val forecasted_direction: PredictionDetail
)

data class WhiteSpace(
    val opportunity: String,
    val explanation: String
)

data class ObservationDetail(
    val observation: String,
    val explanation: String
)

data class PredictionDetail(
    val prediction: String,
    val explanation: String
)

data class MarketingMix(
    val product: StrategyDetail,
    val price: StrategyDetail,
    val place: StrategyDetail,
    val promotion: StrategyDetail
)

data class StrategyDetail(
    val strategy: String,
    val explanation: String
)

data class ImpactMetrics(
    val forecasted_sales: String,
    val revenue_growth_estimate: String
)

data class ComparisonResponse(
    val stress_test: StressTest,
    val comparison: ComparisonData,
    val final_decision: FinalDecision,
    val optimization_plan: List<String>
)

data class StressTest(
    val scenario: String,
    val plan_a_resilience: String,
    val plan_b_resilience: String
)

data class ComparisonData(
    val plan_a: ProsCons,
    val plan_b: ProsCons
)

data class ProsCons(
    val pros: List<String>,
    val cons: List<String>
)

data class FinalDecision(
    val chosen_plan: String,
    val reasoning: String,
    val roi_probability: String
)

data class Data(
    val data_id: String,
    val data_name: String,
    val content: Any
)

data class MarketingMixCompare(
    val product: String,
    val price: String,
    val place: String,
    val promotion: String
)

data class ComparisonRequest(
    val marketing_mix_1: MarketingMixCompare,
    val marketing_mix_2: MarketingMixCompare,
    val region: String,
    val industry: String
)