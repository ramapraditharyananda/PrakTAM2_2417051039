package com.example.praktam2_2417051039.ui.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Analytics
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
import com.example.praktam2_2417051039.ui.components.*
import com.example.praktam2_2417051039.ui.theme.*

@Composable
fun HomeScreen(
    tasks: List<Productivity>,
    allTasks: List<Productivity>,
    isLoading: Boolean,
    padding: PaddingValues,
    onRetry: () -> Unit,
    onDetail: (String) -> Unit,
    onAdd: () -> Unit,
    onAnalysis: () -> Unit,
    onToggle: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(padding),
        contentPadding = PaddingValues(bottom = 100.dp)
    ) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp))
                    .background(Brush.linearGradient(listOf(Navy, Accent)))
                    .padding(24.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        "Deadline Risk Analyzer",
                        color = Color.White,
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.ExtraBold)
                    )
                    Spacer(Modifier.height(16.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        HomeSummaryCard("Total Tugas", allTasks.size.toString(), Modifier.weight(1f))
                        HomeSummaryCard("Selesai", allTasks.count { it.statusSelesai }.toString(), Modifier.weight(1f))
                    }
                }
            }
        }
        item {
            Row(
                modifier = Modifier.padding(horizontal = 18.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = onAnalysis,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(Icons.Default.Analytics, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("Analisis Risiko")
                }
                Button(
                    onClick = onAdd,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("Tambah Tugas")
                }
            }
        }
        item {
            Row(
                modifier = Modifier.padding(horizontal = 18.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Prioritas Tugas",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )
                val highCount = tasks.count { it.riskLevel.name == "HIGH" }
                if (highCount > 0) {
                    Surface(shape = RoundedCornerShape(50.dp), color = Danger.copy(alpha = 0.15f)) {
                        Text(
                            "$highCount risiko tinggi",
                            color = Danger,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
        when {
            isLoading -> item { LoadingState() }
            tasks.isEmpty() -> item {
                Box(Modifier.fillMaxWidth().padding(48.dp), contentAlignment = Alignment.Center) {
                    Text("Belum ada tugas aktif.", color = TextMuted)
                }
            }
            else -> items(
                tasks.sortedWith(compareBy<Productivity> { it.statusSelesai }.thenBy { it.hariTersisa }),
                key = { it.id }
            ) { task ->
                Box(Modifier.padding(horizontal = 18.dp, vertical = 6.dp)) {
                    TaskCard(item = task, onClick = { onDetail(task.id) }, onToggle = { onToggle(task.id) })
                }
            }
        }
    }
}

@Composable
fun HomeSummaryCard(title: String, value: String, modifier: Modifier = Modifier, valueColor: Color = Color.White) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = Color.White.copy(alpha = 0.2f)
    ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(title, color = Color.White.copy(alpha = 0.8f), style = MaterialTheme.typography.labelSmall)
            Text(value, color = valueColor, style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold))
        }
    }
}