package com.example.praktam2_2417051039.ui.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.praktam2_2417051039.data.model.Productivity
import com.example.praktam2_2417051039.ui.components.RiskChip
import com.example.praktam2_2417051039.ui.components.riskColor
import com.example.praktam2_2417051039.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    task: Productivity,
    onBack: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onProgressChange: (Int) -> Unit,
    readOnly: Boolean = false
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detail Tugas", fontWeight = FontWeight.Bold) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null) } },
                actions = {
                    if (!readOnly) {
                        IconButton(onClick = onEdit) { Icon(Icons.Default.Edit, contentDescription = null, tint = Accent) }
                        IconButton(onClick = onDelete) { Icon(Icons.Default.Delete, contentDescription = null, tint = Danger) }
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            Box {
                AsyncImage(
                    model = task.imageUrl,
                    contentDescription = task.judulTugas,
                    modifier = Modifier.fillMaxWidth().height(200.dp),
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(alpha = 0.5f))))
                )
                Column(modifier = Modifier.align(Alignment.BottomStart).padding(16.dp)) {
                    Text(task.judulTugas, color = Color.White, style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold))
                    Text(task.mataKuliah, color = Color.White.copy(alpha = 0.8f))
                }
            }

            Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
                    RiskChip(task.riskLevel)
                    Surface(shape = RoundedCornerShape(50.dp), color = MaterialTheme.colorScheme.surfaceVariant) {
                        Text("${task.hariTersisa} hari tersisa", modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp), style = MaterialTheme.typography.labelSmall)
                    }
                }

                Card(shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        InfoRow("📅 Deadline", task.deadline)
                        InfoRow("⏱️ Estimasi", "${task.estimasiJam} jam pengerjaan")
                        InfoRow("🔥 Kesulitan", "${task.tingkatKesulitan}/5")
                        InfoRow("📝 Deskripsi", task.deskripsi)
                        Spacer(Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                            Text("Progress", fontWeight = FontWeight.SemiBold)
                            Text("${task.progress}%", color = riskColor(task.riskLevel), fontWeight = FontWeight.Bold)
                        }
                        LinearProgressIndicator(
                            progress = { task.progress / 100f },
                            modifier = Modifier.fillMaxWidth().height(8.dp).clip(CircleShape),
                            color = riskColor(task.riskLevel)
                        )
                        if (!readOnly) {
                            Slider(value = task.progress.toFloat(), onValueChange = { onProgressChange(it.toInt()) }, valueRange = 0f..100f)
                        }
                    }
                }

                Card(shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("Analisis Risiko", fontWeight = FontWeight.Bold)
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                            AnalysisMiniCard("Risk Score", "${task.riskScore}%", riskColor(task.riskLevel), Modifier.weight(1f))
                            AnalysisMiniCard("Survival", "${task.survivalScore}%", Accent, Modifier.weight(1f))
                        }
                        InfoRow("💡 Rekomendasi", task.recommendation)
                    }
                }

                if (task.kolaborator.isNotEmpty()) {
                    Card(shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Text("👥 Kolaborator (${task.kolaborator.size})", fontWeight = FontWeight.Bold)
                            task.kolaborator.forEach { email ->
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                    Box(
                                        modifier = Modifier.size(34.dp).clip(CircleShape).background(AccentSoft),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(email.first().uppercaseChar().toString(), color = Accent, fontWeight = FontWeight.Bold)
                                    }
                                    Text(email, style = MaterialTheme.typography.bodyMedium)
                                }
                            }
                        }
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                    if (readOnly) {
                        Button(onClick = onBack, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp)) { Text("Kembali") }
                    } else {
                        OutlinedButton(onClick = onBack, modifier = Modifier.weight(1f), shape = RoundedCornerShape(14.dp)) { Text("Kembali") }
                        Button(onClick = onEdit, modifier = Modifier.weight(1f), shape = RoundedCornerShape(14.dp)) { Text("Edit Tugas") }
                    }
                }
                Spacer(Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(label, style = MaterialTheme.typography.labelSmall, color = TextMuted)
        Text(value, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
fun AnalysisMiniCard(title: String, value: String, color: Color, modifier: Modifier = Modifier) {
    Surface(modifier = modifier, shape = RoundedCornerShape(14.dp), color = color.copy(alpha = 0.12f)) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(title, color = TextMuted, style = MaterialTheme.typography.labelSmall)
            Text(value, color = color, fontWeight = FontWeight.ExtraBold, style = MaterialTheme.typography.titleMedium)
        }
    }
}
