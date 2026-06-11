package com.example.praktam2_2417051039.model

import com.example.praktam2_2417051039.data.model.Productivity
import com.example.praktam2_2417051039.data.model.ProductivityResponse

object ProductivitySource {
    val dummyProductivity = listOf(
        Productivity(judulTugas = "Pemrograman Website", deadline = "10 Juni 2026", hariTersisa = 1, imageUrl = "https://images.unsplash.com/photo-1498050108023-c5249f4df085?w=800", deskripsi = "Membuat aplikasi web dengan fitur CRUD dan validasi form.", progress = 80, mataKuliah = "Pemrograman Web", tingkatKesulitan = 5, estimasiJam = 4),
        Productivity(judulTugas = "Basis Data", deadline = "12 Juni 2026", hariTersisa = 3, imageUrl = "https://images.unsplash.com/photo-1544383835-bda2bc66a55d?w=800", deskripsi = "Mendesain ERD, normalisasi, dan query database relasional.", progress = 55, mataKuliah = "Sistem Basis Data", tingkatKesulitan = 4, estimasiJam = 5),
        Productivity(judulTugas = "Kalkulus", deadline = "15 Juni 2026", hariTersisa = 5, imageUrl = "https://images.unsplash.com/photo-1635070041078-e363dbe005cb?w=800", deskripsi = "Menyelesaikan latihan integral dan diferensial.", progress = 40, mataKuliah = "Kalkulus II", tingkatKesulitan = 5, estimasiJam = 6),
        Productivity(judulTugas = "Matematika Diskrit", deadline = "18 Juni 2026", hariTersisa = 7, imageUrl = "https://images.unsplash.com/photo-1509228627152-72ae9ae6848d?w=800", deskripsi = "Mempelajari teori graf dan logika proposisi.", progress = 30, mataKuliah = "Matematika Diskrit", tingkatKesulitan = 4, estimasiJam = 4),
        Productivity(judulTugas = "Logika Informatika", deadline = "20 Juni 2026", hariTersisa = 10, imageUrl = "https://images.unsplash.com/photo-1453733190371-0a9bedd82893?w=800", deskripsi = "Membuat rangkuman logika predikat dan pembuktian.", progress = 15, mataKuliah = "Logika Informatika", tingkatKesulitan = 3, estimasiJam = 3)
    )

    fun fromResponse(response: ProductivityResponse): Productivity {
        return Productivity(
            judulTugas = response.judulTugas,
            deadline = response.deadline,
            hariTersisa = response.hariTersisa,
            imageUrl = response.imageUrl,
            deskripsi = response.deskripsi,
            progress = response.progress,
            mataKuliah = response.mataKuliah,
            tingkatKesulitan = response.tingkatKesulitan,
            estimasiJam = response.estimasiJam
        )
    }
}
