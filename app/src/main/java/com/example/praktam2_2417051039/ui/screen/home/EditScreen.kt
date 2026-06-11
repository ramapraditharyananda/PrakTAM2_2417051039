package com.example.praktam2_2417051039.ui.screen.home

import android.app.DatePickerDialog
import android.util.Patterns
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.praktam2_2417051039.data.model.Productivity
import com.example.praktam2_2417051039.ui.theme.Accent
import com.example.praktam2_2417051039.ui.theme.Danger
import com.example.praktam2_2417051039.ui.theme.TextMuted
import java.util.Calendar
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditScreen(existing: Productivity?, onBack: () -> Unit, onSave: (Productivity) -> Unit) {
    val context = LocalContext.current
    var title by remember { mutableStateOf(existing?.judulTugas ?: "") }
    var subject by remember { mutableStateOf(existing?.mataKuliah ?: "") }
    var deadline by remember { mutableStateOf(existing?.deadline ?: "") }
    var days by remember { mutableStateOf((existing?.hariTersisa ?: 1).toString()) }
    var description by remember { mutableStateOf(existing?.deskripsi ?: "") }
    var imageUrl by remember { mutableStateOf(existing?.imageUrl ?: "") }
    var progress by remember { mutableStateOf(existing?.progress ?: 0) }
    var difficulty by remember { mutableStateOf(existing?.tingkatKesulitan ?: 3) }
    var estimate by remember { mutableStateOf((existing?.estimasiJam ?: 2).toString()) }
    var kolaborator by remember { mutableStateOf(existing?.kolaborator ?: emptyList()) }
    var emailInput by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf("") }

    val calendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            deadline = "%02d-%02d-%04d".format(dayOfMonth, month + 1, year)
            val today = Calendar.getInstance()
            val selected = Calendar.getInstance().apply { set(year, month, dayOfMonth) }
            val diff = ((selected.timeInMillis - today.timeInMillis) / (1000 * 60 * 60 * 24)).toInt()
            days = diff.coerceAtLeast(0).toString()
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )
    datePickerDialog.datePicker.minDate = calendar.timeInMillis

    fun addEmail() {
        val email = emailInput.trim()
        when {
            email.isBlank() -> emailError = "Email tidak boleh kosong"
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> emailError = "Format email tidak valid"
            kolaborator.contains(email) -> emailError = "Email sudah ditambahkan"
            else -> {
                kolaborator = kolaborator + email
                emailInput = ""
                emailError = ""
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (existing == null) "Tambah Tugas" else "Edit Tugas", fontWeight = FontWeight.Bold) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null) } }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(18.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Informasi Tugas", fontWeight = FontWeight.SemiBold, color = TextMuted)
            OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Judul tugas") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp))
            OutlinedTextField(value = subject, onValueChange = { subject = it }, label = { Text("Mata kuliah") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp))
            OutlinedTextField(
                value = deadline,
                onValueChange = {},
                readOnly = true,
                label = { Text("Deadline (DD-MM-YYYY)") },
                placeholder = { Text("Pilih tanggal deadline") },
                trailingIcon = { IconButton(onClick = { datePickerDialog.show() }) { Icon(Icons.Default.CalendarMonth, contentDescription = "Pilih tanggal", tint = Accent) } },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp)
            )
            OutlinedTextField(value = imageUrl, onValueChange = { imageUrl = it }, label = { Text("URL gambar") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp))
            OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Deskripsi") }, minLines = 3, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp))
            Text("Progress $progress%", fontWeight = FontWeight.SemiBold, color = TextMuted)
            Slider(value = progress.toFloat(), onValueChange = { progress = it.toInt() }, valueRange = 0f..100f)
            Text("Tingkat Kesulitan $difficulty/5", fontWeight = FontWeight.SemiBold, color = TextMuted)
            Slider(value = difficulty.toFloat(), onValueChange = { difficulty = it.toInt().coerceIn(1, 5) }, valueRange = 1f..5f, steps = 3)
            OutlinedTextField(
                value = estimate,
                onValueChange = { estimate = it.filter { char -> char.isDigit() } },
                label = { Text("Estimasi pengerjaan (jam)") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )
            val preview = Productivity(judulTugas = title.ifBlank { "Tugas Baru" }, mataKuliah = subject.ifBlank { "Umum" }, deadline = deadline.ifBlank { "Belum ditentukan" }, hariTersisa = days.toIntOrNull() ?: 0, imageUrl = imageUrl, deskripsi = description, progress = progress, tingkatKesulitan = difficulty, estimasiJam = estimate.toIntOrNull() ?: 0)
            Card(shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("Preview Analisis", fontWeight = FontWeight.Bold)
                    Text("Risk Score ${preview.riskScore}% · Survival ${preview.survivalScore}%", color = Accent, fontWeight = FontWeight.SemiBold)
                    Text(preview.recommendation, color = TextMuted, style = MaterialTheme.typography.bodySmall)
                }
            }
            HorizontalDivider()
            Text("Kolaborasi Tim", fontWeight = FontWeight.SemiBold, color = TextMuted)
            Text("Tambahkan email rekan untuk berkolaborasi pada tugas ini.", style = MaterialTheme.typography.bodySmall, color = TextMuted)
            Row(verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = emailInput,
                    onValueChange = { emailInput = it; emailError = "" },
                    label = { Text("Email rekan") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp),
                    isError = emailError.isNotEmpty(),
                    supportingText = if (emailError.isNotEmpty()) { { Text(emailError, color = Danger) } } else null,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    singleLine = true
                )
                IconButton(onClick = { addEmail() }, modifier = Modifier.padding(top = 4.dp)) { Icon(Icons.Default.Add, contentDescription = "Tambah email", tint = Accent) }
            }
            if (kolaborator.isNotEmpty()) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    kolaborator.forEach { email ->
                        Surface(shape = RoundedCornerShape(10.dp), color = MaterialTheme.colorScheme.surfaceVariant) {
                            Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                                Text("📧", modifier = Modifier.padding(end = 8.dp))
                                Text(email, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium)
                                IconButton(onClick = { kolaborator = kolaborator - email }, modifier = Modifier.size(24.dp)) { Icon(Icons.Default.Close, contentDescription = "Hapus", modifier = Modifier.size(16.dp), tint = TextMuted) }
                            }
                        }
                    }
                }
            }
            Spacer(Modifier.height(8.dp))
            Button(
                onClick = {
                    onSave(
                        Productivity(
                            id = existing?.id ?: UUID.randomUUID().toString(),
                            judulTugas = title.ifBlank { "Tugas Baru" },
                            mataKuliah = subject.ifBlank { "Umum" },
                            deadline = deadline.ifBlank { "Belum ditentukan" },
                            hariTersisa = days.toIntOrNull() ?: 0,
                            imageUrl = imageUrl,
                            deskripsi = description,
                            progress = progress,
                            tingkatKesulitan = difficulty,
                            estimasiJam = estimate.toIntOrNull() ?: 0,
                            statusSelesai = existing?.statusSelesai ?: false,
                            kolaborator = kolaborator
                        )
                    )
                },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(14.dp)
            ) { Text("Simpan Tugas", fontWeight = FontWeight.Bold) }
            Spacer(Modifier.height(24.dp))
        }
    }
}