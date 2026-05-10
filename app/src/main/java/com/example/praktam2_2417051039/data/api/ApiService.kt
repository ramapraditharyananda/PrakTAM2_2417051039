package com.example.praktam2_2417051039.data.api

import com.example.praktam2_2417051039.data.model.ProductivityResponse
import retrofit2.http.GET

interface ApiService {
    @GET("deadline_risk.json")
    suspend fun getProductivityList(): List<ProductivityResponse>
}