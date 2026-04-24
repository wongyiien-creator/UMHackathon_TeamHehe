package com.example.umhackathon.API

import com.example.umhackathon.model.*
import retrofit2.http.*

interface ApiService {

    @POST("login")
    suspend fun login(
        @Body loginInput: LoginInput
    ): LoginResponse

    @POST("signup")
    suspend fun signup(
        @Body signupInput: SignupInput
    ): SignUpResponse

    @POST("save_data/{user_id}")
    suspend fun saveData(
        @Path("user_id") userId: String,
        @Body payload: DataPayload
    ): DataPayloadResponse

    @POST("get_data/{user_id}")
    suspend fun getData(
        @Path("user_id") userId: String
    ): List<Data>?

    @POST("analysis")
    suspend fun getAnalysis(
        @Body request: AnalysisRequest
    ): AnalysisResponse

    @POST("comparison")
    suspend fun compareMarketingMix(
        @Body request: ComparisonRequest
    ): ComparisonResponse
}