package com.example.praktam2_2417051039.data.model

data class ProductivityResponse(
    val judulTugas: String,
    val deadline: String,
    val hariTersisa: Int,
    val imageUrl: String,
    val deskripsi: String = "",
    val progress: Int = 0,
    val mataKuliah: String = "",
    val tingkatKesulitan: Int = 3,
    val estimasiJam: Int = 2
)
