package com.example.praktam2_2417051039.model

import com.example.praktam2_2417051039.data.model.ProductivityResponse
import com.example.praktam2_2417051039.data.model.Productivity

object ProductivitySource {
    val dummyProductivity = listOf(
        Productivity(
            judulTugas = "Pemrograman Website",
            deadline = "10 Juni 2026",
            hariTersisa = 1,
            imageUrl = "https://i.ibb.co.com/mFBy1wHg/productivity1.png"
        ),
        Productivity(
            judulTugas = "Basis Data",
            deadline = "12 Juni 2026",
            hariTersisa = 3,
            imageUrl = "https://i.ibb.co.com/VW2hw7BV/productivity2.png"
        ),
        Productivity(
            judulTugas = "Kalkulus",
            deadline = "15 Juni 2026",
            hariTersisa = 5,
            imageUrl = "https://i.ibb.co.com/LF5Xkvq/productivity3.png"
        ),
        Productivity(
            judulTugas = "Matematika Diskrit",
            deadline = "18 Juni 2026",
            hariTersisa = 7,
            imageUrl = "https://i.ibb.co.com/vCCWXwN6/productivity4.png"
        ),
        Productivity(
            judulTugas = "Logika",
            deadline = "20 Juni 2026",
            hariTersisa = 10,
            imageUrl = "https://i.ibb.co.com/gZsf5rgK/productivity5.png"
        )
    )

    fun fromResponse(response: ProductivityResponse): Productivity {
        return Productivity(
            judulTugas = response.judulTugas,
            deadline = response.deadline,
            hariTersisa = response.hariTersisa,
            imageUrl = response.imageUrl
        )
    }
}