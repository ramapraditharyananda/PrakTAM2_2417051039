package com.example.praktam2_2417051039.ui.screen.profile

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.praktam2_2417051039.data.model.Productivity
import com.example.praktam2_2417051039.data.model.RiskLevel
import com.example.praktam2_2417051039.viewmodel.UserViewModel
import com.example.praktam2_2417051039.ui.theme.Background
import com.example.praktam2_2417051039.ui.theme.Danger
import com.example.praktam2_2417051039.ui.theme.Navy
import com.example.praktam2_2417051039.ui.theme.SoftBlue
import com.example.praktam2_2417051039.ui.theme.Surface
import com.example.praktam2_2417051039.ui.theme.TextDark
import com.example.praktam2_2417051039.ui.theme.TextMuted
import com.example.praktam2_2417051039.ui.theme.Warning

@Composable
fun ProfileScreen(
    tasks: List<Productivity>,
    padding: PaddingValues,
    userViewModel: UserViewModel,
    onFaq: () -> Unit,
    onAbout: () -> Unit,
    onLogout: () -> Unit
) {
    val highRisk = tasks.count { it.riskLevel == RiskLevel.HIGH }
    val average = if (tasks.isEmpty()) 0 else tasks.sumOf { it.progress } / tasks.size

    var editMode by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }
    var showSavedDialog by remember { mutableStateOf(false) }

    if (editMode) {
        EditProfileScreen(
            padding = padding,
            currentName = userViewModel.name,
            currentEmail = userViewModel.email,
            currentPhotoUrl = userViewModel.photoUrl,
            currentPassword = userViewModel.password,
            onBack = { editMode = false },
            onSave = { newName, newEmail, newPhotoUrl, newPassword ->
                userViewModel.updateProfile(newName, newEmail, newPhotoUrl, newPassword)
                editMode = false
                showSavedDialog = true
            }
        )
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Background)
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 18.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(contentAlignment = Alignment.BottomEnd) {
                ProfilePhoto(
                    photoUrl = userViewModel.photoUrl,
                    imageVersion = userViewModel.imageVersion,
                    size = 118
                )
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Warning)
                        .clickable { editMode = true },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit profil", tint = Color.White, modifier = Modifier.size(18.dp))
                }
            }
            Spacer(Modifier.height(14.dp))
            Text(userViewModel.name, color = TextDark, style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold))
            Text(userViewModel.email, color = TextMuted, style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.height(26.dp))
            ProfileSectionTitle("Ringkasan Deadline")
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                MiniProfileStat("Total", tasks.size.toString(), Modifier.weight(1f))
                MiniProfileStat("Progress", "$average%", Modifier.weight(1f))
                MiniProfileStat("Risiko", highRisk.toString(), Modifier.weight(1f))
            }
            Spacer(Modifier.height(26.dp))
            ProfileSectionTitle("Informasi & Panduan")
            ProfileInfoCard(Icons.AutoMirrored.Filled.Help, "Bantuan & FAQ", "Panduan penggunaan aplikasi", onClick = onFaq)
            Spacer(Modifier.height(10.dp))
            ProfileInfoCard(Icons.Default.Info, "Tentang", "Aplikasi analisis risiko keterlambatan tugas", onClick = onAbout)
            Spacer(Modifier.height(26.dp))
            ProfileSectionTitle("Akun")
            ProfileInfoCard(
                icon = Icons.AutoMirrored.Filled.Logout,
                title = "Keluar",
                subtitle = "Logout dari akun aplikasi",
                titleColor = Danger,
                onClick = { showLogoutDialog = true }
            )
            Spacer(Modifier.height(18.dp))
        }
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Keluar", fontWeight = FontWeight.Bold) },
            text = { Text("Apakah kamu yakin ingin keluar dari akun?") },
            confirmButton = {
                Button(
                    onClick = { showLogoutDialog = false; onLogout() },
                    colors = ButtonDefaults.buttonColors(containerColor = Danger)
                ) { Text("Keluar") }
            },
            dismissButton = { TextButton(onClick = { showLogoutDialog = false }) { Text("Batal") } }
        )
    }

    if (showSavedDialog) {
        AlertDialog(
            onDismissRequest = { showSavedDialog = false },
            title = { Text("Berhasil", fontWeight = FontWeight.Bold) },
            text = { Text("Perubahan profil berhasil disimpan.") },
            confirmButton = { Button(onClick = { showSavedDialog = false }) { Text("Oke") } }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    padding: PaddingValues,
    currentName: String,
    currentEmail: String,
    currentPhotoUrl: String,
    currentPassword: String,
    onBack: () -> Unit,
    onSave: (String, String, String, String) -> Unit
) {
    var name by remember { mutableStateOf(currentName) }
    var email by remember { mutableStateOf(currentEmail) }
    var photoUrl by remember { mutableStateOf(currentPhotoUrl) }
    var oldPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var oldVisible by remember { mutableStateOf(false) }
    var newVisible by remember { mutableStateOf(false) }
    var confirmVisible by remember { mutableStateOf(false) }
    var errorText by remember { mutableStateOf("") }

    val profileValid = name.isNotBlank() && email.isNotBlank()
    val passwordEmpty = oldPassword.isBlank() && newPassword.isBlank() && confirmPassword.isBlank()

    Scaffold(
        containerColor = Background,
        topBar = {
            TopAppBar(
                title = { Text("Edit Akun", color = Color.White, fontWeight = FontWeight.ExtraBold) },
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
                .padding(padding)
                .padding(top = innerPadding.calculateTopPadding())
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 18.dp, vertical = 18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    Text("Foto Profil", color = TextDark, fontWeight = FontWeight.Bold)
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        ProfilePhoto(photoUrl = photoUrl, imageVersion = photoUrl.hashCode(), size = 96)
                    }
                    OutlinedTextField(
                        value = photoUrl,
                        onValueChange = { photoUrl = it },
                        modifier = Modifier.fillMaxWidth(),
                        leadingIcon = { Icon(Icons.Default.Image, contentDescription = null) },
                        label = { Text("URL Foto Profil") },
                        placeholder = { Text("Masukkan link foto dari internet") },
                        shape = RoundedCornerShape(14.dp),
                        singleLine = true
                    )
                    Text("Gunakan URL gambar langsung. Contoh: https://i.imgur.com/xxx.jpg", color = TextMuted, fontSize = 11.sp)
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    Text("Informasi Akun", color = TextDark, fontWeight = FontWeight.Bold)
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        modifier = Modifier.fillMaxWidth(),
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                        label = { Text("Nama Lengkap") },
                        shape = RoundedCornerShape(14.dp),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Email") },
                        shape = RoundedCornerShape(14.dp),
                        singleLine = true
                    )
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    Text("Ubah Password", color = TextDark, fontWeight = FontWeight.Bold)
                    PasswordField(
                        value = oldPassword,
                        onValueChange = { oldPassword = it },
                        label = "Password Saat Ini",
                        visible = oldVisible,
                        onVisibleChange = { oldVisible = !oldVisible }
                    )
                    PasswordField(
                        value = newPassword,
                        onValueChange = { newPassword = it },
                        label = "Password Baru",
                        visible = newVisible,
                        onVisibleChange = { newVisible = !newVisible }
                    )
                    PasswordField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        label = "Konfirmasi Password Baru",
                        visible = confirmVisible,
                        onVisibleChange = { confirmVisible = !confirmVisible }
                    )
                    if (errorText.isNotBlank()) Text(errorText, color = Danger, fontSize = 12.sp)
                }
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedButton(
                    onClick = onBack,
                    modifier = Modifier.weight(1f).height(52.dp),
                    shape = RoundedCornerShape(14.dp)
                ) { Text("Batal") }
                Button(
                    onClick = {
                        errorText = when {
                            !profileValid -> "Nama dan email wajib diisi."
                            !passwordEmpty && oldPassword != currentPassword -> "Password saat ini tidak sesuai."
                            !passwordEmpty && newPassword.length < 6 -> "Password baru minimal 6 karakter."
                            !passwordEmpty && newPassword != confirmPassword -> "Konfirmasi password tidak sama."
                            else -> ""
                        }
                        if (errorText.isBlank()) onSave(name, email, photoUrl, if (passwordEmpty) "" else newPassword)
                    },
                    enabled = profileValid,
                    modifier = Modifier.weight(1f).height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Navy)
                ) { Text("Simpan") }
            }
            Spacer(Modifier.height(18.dp))
        }
    }
}

@Composable
fun PasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    visible: Boolean,
    onVisibleChange: () -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
        trailingIcon = {
            IconButton(onClick = onVisibleChange) {
                Icon(if (visible) Icons.Default.VisibilityOff else Icons.Default.Visibility, contentDescription = null)
            }
        },
        label = { Text(label) },
        shape = RoundedCornerShape(14.dp),
        singleLine = true,
        visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation()
    )
}

@Composable
fun ProfilePhoto(photoUrl: String, imageVersion: Int, size: Int) {
    val imageUrl = normalizeProfileImageUrl(photoUrl)
    val context = LocalContext.current
    val cacheKey = "$imageUrl-$imageVersion"

    Box(
        modifier = Modifier
            .size(size.dp)
            .clip(CircleShape)
            .background(SoftBlue)
            .border(2.dp, Navy, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        if (imageUrl.isNotBlank()) {
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(context)
                    .data(imageUrl)
                    .crossfade(true)
                    .allowHardware(false)
                    .memoryCacheKey(cacheKey)
                    .diskCacheKey(cacheKey)
                    .memoryCachePolicy(CachePolicy.ENABLED)
                    .diskCachePolicy(CachePolicy.ENABLED)
                    .networkCachePolicy(CachePolicy.ENABLED)
                    .addHeader("User-Agent", "Mozilla/5.0")
                    .addHeader("Referer", "https://imgur.com/")
                    .listener(
                        onStart = { Log.d("COIL", "Loading: $imageUrl") },
                        onSuccess = { _, _ -> Log.d("COIL", "Success: $imageUrl") },
                        onError = { _, result -> Log.e("COIL", "Error: $imageUrl | ${result.throwable}") }
                    )
                    .build(),
                contentDescription = "Foto profil",
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            ) {
                when (painter.state) {
                    is AsyncImagePainter.State.Loading -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(
                                modifier = Modifier.size((size / 3).dp),
                                strokeWidth = 2.dp,
                                color = Navy
                            )
                        }
                    }
                    is AsyncImagePainter.State.Error -> {
                        Icon(Icons.Default.Person, contentDescription = null, tint = Navy, modifier = Modifier.size((size / 2).dp))
                    }
                    else -> SubcomposeAsyncImageContent()
                }
            }
        } else {
            Icon(Icons.Default.Person, contentDescription = null, tint = Navy, modifier = Modifier.size((size / 2).dp))
        }
    }
}

fun normalizeProfileImageUrl(url: String): String {
    val trimmed = url.trim()
    if (trimmed.isBlank()) return ""
    val cleanUrl = trimmed.replace("http://", "https://")

    val driveFilePattern = Regex("drive\\.google\\.com/file/d/([^/]+)")
    val driveFileMatch = driveFilePattern.find(cleanUrl)
    if (driveFileMatch != null) {
        val id = driveFileMatch.groupValues[1]
        return "https://drive.google.com/uc?export=view&id=$id"
    }

    val driveIdPattern = Regex("[?&]id=([^&]+)")
    val driveIdMatch = driveIdPattern.find(cleanUrl)
    if (cleanUrl.contains("drive.google.com") && driveIdMatch != null) {
        val id = driveIdMatch.groupValues[1]
        return "https://drive.google.com/uc?export=view&id=$id"
    }

    if (cleanUrl.contains("i.imgur.com")) {
        return cleanUrl
    }

    val imgurPagePattern = Regex("https://imgur\\.com/([A-Za-z0-9]+)$")
    val imgurPageMatch = imgurPagePattern.find(cleanUrl)
    if (imgurPageMatch != null) {
        return "https://i.imgur.com/${imgurPageMatch.groupValues[1]}.jpg"
    }

    return cleanUrl
}

@Composable
fun ProfileSectionTitle(title: String) {
    Text(
        text = title,
        color = TextMuted,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
    )
}

@Composable
fun ProfileInfoCard(
    icon: ImageVector,
    title: String,
    subtitle: String,
    titleColor: Color = TextDark,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(44.dp).clip(CircleShape).background(SoftBlue),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = Navy, modifier = Modifier.size(22.dp))
            }
            Spacer(Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, color = titleColor, style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold))
                Text(subtitle, color = TextMuted, style = MaterialTheme.typography.bodySmall)
            }
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = TextMuted)
        }
    }
}

@Composable
fun MiniProfileStat(title: String, value: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(title, color = TextMuted, fontSize = 12.sp)
            Text(value, color = Navy, fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
        }
    }
}