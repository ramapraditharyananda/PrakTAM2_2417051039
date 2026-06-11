package com.example.praktam2_2417051039.ui.screen.history

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.praktam2_2417051039.data.model.Productivity
import com.example.praktam2_2417051039.ui.components.TaskCard
import com.example.praktam2_2417051039.ui.theme.TextMuted

@Composable
fun HistoryScreen(
    tasks: List<Productivity>,
    padding: PaddingValues,
    onDetail: (String) -> Unit,
    onToggle: (String) -> Unit
) {
    val done = tasks.filter { it.statusSelesai }.sortedByDescending { it.deadline }

    Column(modifier = Modifier.fillMaxSize().padding(padding)) {
        Column(modifier = Modifier.padding(horizontal = 18.dp, vertical = 14.dp)) {
            Text("Riwayat Tugas", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold))
            Text(
                "${done.size} tugas selesai dari ${tasks.size} total",
                color = TextMuted,
                style = MaterialTheme.typography.bodySmall
            )
        }
        if (done.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxWidth().padding(48.dp)
            ) {
                Text("Belum ada tugas yang diselesaikan.", color = TextMuted)
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(horizontal = 18.dp, vertical = 4.dp)
            ) {
                items(done, key = { it.id }) { task ->
                    TaskCard(item = task, onClick = { onDetail(task.id) })
                }
                item { Spacer(Modifier.height(80.dp)) }
            }
        }
    }
}