package com.example.praktam2_2417051039.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.praktam2_2417051039.data.model.Productivity
import com.example.praktam2_2417051039.data.model.RiskLevel
import com.example.praktam2_2417051039.ui.theme.*

@Composable
fun TaskCard(item: Productivity, onClick: () -> Unit, onToggle: (() -> Unit)? = null) {
    val animatedProgress by animateFloatAsState(
        targetValue = item.progress / 100f,
        animationSpec = tween(600),
        label = "progress"
    )
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Row(modifier = Modifier.padding(14.dp), horizontalArrangement = Arrangement.spacedBy(14.dp)) {
            Box {
                AsyncImage(
                    model = item.imageUrl,
                    contentDescription = item.judulTugas,
                    modifier = Modifier.size(80.dp).clip(RoundedCornerShape(16.dp)).background(SoftBlue),
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(riskColor(item.riskLevel))
                )
            }
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    item.judulTugas,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(item.mataKuliah, color = TextMuted, style = MaterialTheme.typography.bodySmall)
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                    Surface(shape = RoundedCornerShape(50.dp), color = riskColor(item.riskLevel).copy(alpha = 0.12f)) {
                        Text("Risk ${item.riskScore}%", color = riskColor(item.riskLevel), modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp), style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.SemiBold)
                    }
                    Surface(shape = RoundedCornerShape(50.dp), color = AccentSoft) {
                        Text("Survival ${item.survivalScore}%", color = Accent, modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp), style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.SemiBold)
                    }
                }
                if (item.kolaborator.isNotEmpty()) {
                    Text(
                        "👥 ${item.kolaborator.size} kolaborator",
                        color = Accent,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
                Spacer(Modifier.height(6.dp))
                Box(
                    modifier = Modifier.fillMaxWidth().height(6.dp).clip(CircleShape).background(SoftBlue)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(animatedProgress)
                            .height(6.dp)
                            .clip(CircleShape)
                            .background(Brush.horizontalGradient(listOf(Accent, CardGradientEnd)))
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        "${item.hariTersisa} hari lagi · ${item.estimasiJam} jam",
                        color = riskColor(item.riskLevel),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp
                    )
                    if (onToggle != null) {
                        FilledTonalButton(
                            onClick = onToggle,
                            modifier = Modifier.height(30.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp)
                        ) {
                            Text(if (item.statusSelesai) "Batal" else "Selesai", fontSize = 11.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SummaryCard(title: String, value: String, modifier: Modifier = Modifier, accent: Color = Accent) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(title, color = TextMuted, style = MaterialTheme.typography.labelSmall)
            Text(value, style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold, color = accent))
        }
    }
}

@Composable
fun RiskChip(level: RiskLevel) {
    Surface(
        shape = RoundedCornerShape(50.dp),
        color = riskColor(level).copy(alpha = 0.15f)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Box(Modifier.size(8.dp).clip(CircleShape).background(riskColor(level)))
            Text(level.label, color = riskColor(level), style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
fun LoadingState() {
    Box(modifier = Modifier.fillMaxWidth().padding(48.dp), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = Accent)
    }
}

@Composable
fun ErrorState(onRetry: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("⚠️ Data gagal dimuat", style = MaterialTheme.typography.bodyLarge)
        Button(onClick = onRetry) { Text("Coba Lagi") }
    }
}

@Composable
fun riskColor(level: RiskLevel): Color = when (level) {
    RiskLevel.HIGH -> Danger
    RiskLevel.MEDIUM -> Warning
    RiskLevel.LOW -> Accent
    RiskLevel.SAFE -> Success
}