package com.example.praktam2_2417051039.data.repository

import com.example.praktam2_2417051039.data.api.RetrofitClient
import com.example.praktam2_2417051039.data.model.Productivity
import com.example.praktam2_2417051039.model.DeadlineDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProductivityRepository(
    private val apiService: com.example.praktam2_2417051039.data.api.ApiService = RetrofitClient.apiService
) {
    private val localItems = mutableListOf<Productivity>()
    private var loaded = false

    // Selalu return Result.success — jika internet mati atau API error,
    // fallback ke data dummy lokal agar aplikasi tetap bisa digunakan.
    suspend fun getProductivity(): Result<List<Productivity>> = withContext(Dispatchers.IO) {
        if (loaded) return@withContext Result.success(localItems.toList())
        try {
            val response = apiService.getProductivityList()
            val items = response.map(DeadlineDataSource::fromResponse).ifEmpty { DeadlineDataSource.dummyProductivity }
            localItems.clear()
            localItems.addAll(items)
        } catch (_: Exception) {
            // Tidak ada internet atau API error → pakai data dummy, tidak tampilkan error
            if (localItems.isEmpty()) {
                localItems.addAll(DeadlineDataSource.dummyProductivity)
            }
        }
        loaded = true
        Result.success(localItems.toList())
    }

    fun addProductivity(item: Productivity): List<Productivity> {
        localItems.add(0, item)
        return localItems.toList()
    }

    fun updateProductivity(item: Productivity): List<Productivity> {
        val index = localItems.indexOfFirst { it.id == item.id }
        if (index >= 0) localItems[index] = item
        return localItems.toList()
    }

    fun deleteProductivity(id: String): List<Productivity> {
        localItems.removeAll { it.id == id }
        return localItems.toList()
    }
}
