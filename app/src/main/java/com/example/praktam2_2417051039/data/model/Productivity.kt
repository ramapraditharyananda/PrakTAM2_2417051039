package com.example.praktam2_2417051039.data.model

import java.util.UUID


data class Productivity(
    val id: String = UUID.randomUUID().toString(),
    val judulTugas: String,
    val deadline: String,
    val hariTersisa: Int,
    val imageUrl: String,
    val deskripsi: String = "",
    val progress: Int = 0,
    val mataKuliah: String = "",
    val tingkatKesulitan: Int = 3,
    val estimasiJam: Int = 2,
    val kolaborator: List<String> = emptyList(),
    val statusSelesai: Boolean = false
) {
    val riskScore: Int
        get() {
            if (statusSelesai) return 0
            val deadlineScore = when {
                hariTersisa <= 0 -> 45
                hariTersisa == 1 -> 38
                hariTersisa <= 3 -> 28
                hariTersisa <= 7 -> 18
                else -> 8
            }
            val progressScore = ((100 - progress.coerceIn(0, 100)) * 0.35).toInt()
            val difficultyScore = tingkatKesulitan.coerceIn(1, 5) * 4
            val workloadScore = estimasiJam.coerceAtLeast(0).coerceAtMost(12) * 2
            return (deadlineScore + progressScore + difficultyScore + workloadScore).coerceIn(0, 100)
        }

    val survivalScore: Int
        get() = if (statusSelesai) 100 else (100 - riskScore).coerceIn(0, 100)

    val riskLevel: RiskLevel
        get() = when {
            statusSelesai -> RiskLevel.SAFE
            riskScore >= 75 -> RiskLevel.HIGH
            riskScore >= 45 -> RiskLevel.MEDIUM
            riskScore >= 20 -> RiskLevel.LOW
            else -> RiskLevel.SAFE
        }

    val recommendation: String
        get() = when (riskLevel) {
            RiskLevel.HIGH -> "Kerjakan sekarang minimal ${estimasiJam.coerceAtLeast(1)} jam agar tidak terlambat."
            RiskLevel.MEDIUM -> "Cicil hari ini dan naikkan progress minimal 20%."
            RiskLevel.LOW -> "Masih aman, tetap jadwalkan sesi belajar singkat."
            RiskLevel.SAFE -> if (statusSelesai) "Tugas sudah selesai." else "Risiko rendah, pertahankan progress."
        }
}

enum class RiskLevel(val label: String) {
    HIGH("Risiko Tinggi"),
    MEDIUM("Risiko Sedang"),
    LOW("Risiko Rendah"),
    SAFE("Aman")
}
