package com.example.praktam2_2417051039.data.repository

import com.example.praktam2_2417051039.data.api.RetrofitClient
import com.example.praktam2_2417051039.data.model.Productivity

class ProductivityRepository {
    suspend fun getProductivity(): List<Productivity> {
        return try {
            val response = RetrofitClient.instance.getProductivityList()
            response.map {
                Productivity(
                    judulTugas = it.judulTugas,
                    deadline = it.deadline,
                    hariTersisa = it.hariTersisa,
                    imageUrl = it.imageUrl
                )
            }
        } catch (e: Exception) {
            android.util.Log.e("ProductivityRepo", "Error: ${e.message}", e)
            emptyList()
        }
    }
}