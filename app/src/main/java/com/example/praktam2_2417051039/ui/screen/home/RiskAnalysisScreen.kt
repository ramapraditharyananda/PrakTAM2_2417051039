package com.example.praktam2_2417051039.ui.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.praktam2_2417051039.data.model.Productivity
import com.example.praktam2_2417051039.data.model.RiskLevel
import com.example.praktam2_2417051039.ui.components.RiskChip
import com.example.praktam2_2417051039.ui.components.SummaryCard
import com.example.praktam2_2417051039.ui.components.riskColor
import com.example.praktam2_2417051039.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RiskAnalysisScreen(tasks: List<Productivity>, onBack: () -> Unit) {
    val highRisk = tasks.count { it.riskLevel == RiskLevel.HIGH }
    val mediumRisk = tasks.count { it.riskLevel == RiskLevel.MEDIUM }
    val unfinished = tasks.count { !it.statusSelesai }
    val average = if (tasks.isEmpty()) 0 else tasks.sumOf { it.progress } / tasks.size
    val riskAverage = if (tasks.isEmpty()) 0 else tasks.sumOf { it.riskScore } / tasks.size
    val survivalAverage = if (tasks.isEmpty()) 100 else tasks.sumOf { it.survivalScore } / tasks.size
    val totalEstimate = tasks.filter { !it.statusSelesai }.sumOf { it.estimasiJam }
    val hardestTask = tasks.filter { !it.statusSelesai }.maxByOrNull { it.riskScore }
    val burnoutStatus = when {
        unfinished >= 5 && highRisk >= 2 -> "Risiko burnout tinggi"
        unfinished >= 4 || totalEstimate >= 12 -> "Beban tugas cukup padat"
        else -> "Beban tugas masih terkendali"
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Analisis Risiko", fontWeight = FontWeight.Bold) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null) } }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(Brush.linearGradient(listOf(Danger, Color(0xFFFF6B6B))))
                        .padding(18.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("⚠️ Ringkasan Risiko", color = Color.White, fontWeight = FontWeight.Bold)
                        Text("$highRisk tugas kritis · $mediumRisk sedang · $unfinished belum selesai", color = Color.White.copy(alpha = 0.85f), style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                    SummaryCard("Risk Avg", "$riskAverage%", Modifier.weight(1f), Danger)
                    SummaryCard("Survival", "$survivalAverage%", Modifier.weight(1f), Accent)
                }
            }
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                    SummaryCard("Risiko Tinggi", highRisk.toString(), Modifier.weight(1f), Danger)
                    SummaryCard("Belum Selesai", unfinished.toString(), Modifier.weight(1f), Warning)
                }
            }
            item { SummaryCard("Rata-rata Progress", "$average%", Modifier.fillMaxWidth(), Accent) }
            item {
                Card(shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = Surface), elevation = CardDefaults.cardElevation(2.dp)) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Burnout Detector", fontWeight = FontWeight.Bold)
                        Text(burnoutStatus, color = if (highRisk >= 2) Danger else Accent, fontWeight = FontWeight.SemiBold)
                        Text("Estimasi beban aktif: $totalEstimate jam", color = TextMuted, style = MaterialTheme.typography.bodySmall)
                        Text(hardestTask?.let { "Fokus pertama: ${it.judulTugas}" } ?: "Semua tugas aktif sudah aman.", color = TextMuted, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
            item { Text("Rekomendasi Prioritas", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold) }
            items(
                tasks.sortedWith(compareBy<Productivity> { it.statusSelesai }.thenByDescending { it.riskScore }.thenBy { it.hariTersisa }),
                key = { it.id }
            ) { task ->
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Surface),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Box(
                            modifier = Modifier.size(42.dp).clip(CircleShape).background(riskColor(task.riskLevel).copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                when (task.riskLevel) {
                                    RiskLevel.HIGH -> "🔴"
                                    RiskLevel.MEDIUM -> "🟡"
                                    RiskLevel.LOW -> "🔵"
                                    RiskLevel.SAFE -> "🟢"
                                },
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(task.judulTugas, fontWeight = FontWeight.SemiBold, maxLines = 1)
                            Text("${task.mataKuliah} · ${task.hariTersisa} hari · ${task.progress}% · ${task.estimasiJam} jam", color = TextMuted, style = MaterialTheme.typography.bodySmall)
                            Text("Risk ${task.riskScore}% · Survival ${task.survivalScore}%", color = riskColor(task.riskLevel), fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.labelSmall)
                            Text(task.recommendation, color = TextMuted, style = MaterialTheme.typography.bodySmall)
                            RiskChip(task.riskLevel)
                        }
                    }
                }
            }
            item { Spacer(Modifier.height(16.dp)) }
        }
    }
}
