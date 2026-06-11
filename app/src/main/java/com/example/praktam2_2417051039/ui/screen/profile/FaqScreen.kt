package com.example.praktam2_2417051039.ui.screen.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.praktam2_2417051039.ui.theme.Background
import com.example.praktam2_2417051039.ui.theme.Navy
import com.example.praktam2_2417051039.ui.theme.SoftBlue
import com.example.praktam2_2417051039.ui.theme.Surface
import com.example.praktam2_2417051039.ui.theme.TextDark
import com.example.praktam2_2417051039.ui.theme.TextMuted

private data class FaqItem(val question: String, val answer: String)

private val faqList = listOf(
    FaqItem(
        "Apa itu Deadline Risk Analyzer?",
        "Deadline Risk Analyzer adalah aplikasi untuk membantu kamu memantau dan menganalisis risiko keterlambatan tugas kuliah. Aplikasi ini menghitung skor risiko berdasarkan sisa hari, tingkat kesulitan, estimasi jam, dan progress pengerjaanmu."
    ),
    FaqItem(
        "Bagaimana cara menambah tugas baru?",
        "Tekan tombol '+' di halaman Beranda, lalu isi detail tugas seperti judul, mata kuliah, deadline, tingkat kesulitan, dan estimasi jam pengerjaan. Klik 'Simpan Tugas' setelah selesai."
    ),
    FaqItem(
        "Apa arti Risk Score dan Survival Score?",
        "Risk Score menunjukkan seberapa berisiko tugas tersebut terlambat diselesaikan (0-100%, makin tinggi makin berbahaya). Survival Score adalah kebalikannya — menunjukkan peluang kamu menyelesaikan tugas tepat waktu."
    ),
    FaqItem(
        "Bagaimana cara menghitung Risk Score?",
        "Risk Score dihitung berdasarkan kombinasi: sisa hari deadline, tingkat kesulitan (1-5), estimasi jam pengerjaan, dan progress saat ini. Semakin sedikit waktu dan semakin tinggi kesulitan, maka risk score akan semakin tinggi."
    ),
    FaqItem(
        "Apa itu level risiko HIGH, MEDIUM, LOW?",
        "HIGH (merah): Risk Score ≥ 70%, perlu perhatian segera. MEDIUM (kuning): Risk Score 40-69%, perlu dipantau. LOW (hijau): Risk Score < 40%, tugas masih aman."
    ),
    FaqItem(
        "Bagaimana cara mengubah progress tugas?",
        "Buka detail tugas, lalu geser slider Progress di bagian bawah. Progress bisa juga diubah saat mengedit tugas melalui tombol Edit."
    ),
    FaqItem(
        "Bagaimana cara menandai tugas sebagai selesai?",
        "Di halaman Beranda atau Riwayat, tekan ikon centang pada kartu tugas. Tugas yang sudah selesai akan berpindah ke tab Selesai di halaman Riwayat."
    ),
    FaqItem(
        "Bagaimana cara mengganti foto profil?",
        "Buka halaman Profil, tekan ikon edit (pensil oranye), lalu masukkan URL gambar langsung di field 'URL Foto Profil'. Gunakan URL dari imgur (contoh: https://i.imgur.com/xxx.jpg)."
    ),
    FaqItem(
        "Apakah data tugas tersimpan permanen?",
        "Saat ini data disimpan sementara selama sesi aplikasi berjalan. Jika aplikasi ditutup, data akan kembali ke kondisi awal. Fitur penyimpanan permanen akan hadir di versi berikutnya."
    ),
    FaqItem(
        "Bagaimana cara menambah kolaborator tugas?",
        "Saat membuat atau mengedit tugas, scroll ke bawah ke bagian 'Kolaborasi Tim', masukkan email rekan, lalu tekan tombol '+' untuk menambahkan."
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FaqScreen(onBack: () -> Unit) {
    Scaffold(
        containerColor = Background,
        topBar = {
            TopAppBar(
                title = { Text("Bantuan & FAQ", color = Color.White, fontWeight = FontWeight.ExtraBold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Navy)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 18.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = SoftBlue)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Pertanyaan Umum", fontWeight = FontWeight.ExtraBold, color = Navy, fontSize = 16.sp)
                    Spacer(Modifier.height(4.dp))
                    Text("Temukan jawaban atas pertanyaan yang sering ditanyakan seputar penggunaan aplikasi.", color = TextMuted, style = MaterialTheme.typography.bodySmall)
                }
            }

            Spacer(Modifier.height(4.dp))

            faqList.forEachIndexed { index, item ->
                FaqCard(item = item)
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun FaqCard(item: FaqItem) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    item.question,
                    modifier = Modifier.weight(1f).padding(end = 8.dp),
                    color = TextDark,
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.bodyMedium
                )
                IconButton(
                    onClick = { expanded = !expanded },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = null,
                        tint = Navy
                    )
                }
            }
            if (expanded) {
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = SoftBlue)
                Text(
                    item.answer,
                    color = TextMuted,
                    style = MaterialTheme.typography.bodySmall,
                    lineHeight = 20.sp
                )
            }
        }
    }
}