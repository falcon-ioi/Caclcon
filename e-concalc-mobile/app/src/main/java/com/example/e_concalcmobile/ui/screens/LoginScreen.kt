package com.example.e_concalcmobile.ui.screens

import android.app.Activity
import android.content.Intent
import android.provider.Settings
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import com.example.e_concalcmobile.api.ApiClient
import com.example.e_concalcmobile.api.GoogleLoginRequest
import com.example.e_concalcmobile.api.LoginRequest
import com.example.e_concalcmobile.utils.TokenManager
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kotlinx.coroutines.launch
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.security.MessageDigest
import java.util.UUID

private const val GOOGLE_WEB_CLIENT_ID = "432651801928-taug1h6lsinh6glvh0mtia7pj6l69s5i.apps.googleusercontent.com"

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onSkipLogin: () -> Unit
) {
    val context = LocalContext.current
    val tokenManager = remember { TokenManager(context) }
    val scope = rememberCoroutineScope()

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var isGoogleLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var errorDetail by remember { mutableStateOf<String?>(null) }
    var showPassword by remember { mutableStateOf(false) }

    fun handleLoginError(e: Exception) {
        when (e) {
            is ConnectException -> {
                errorMessage = "Tidak bisa terhubung ke server"
                errorDetail = "Pastikan php artisan serve sudah dijalankan"
            }
            is SocketTimeoutException -> {
                errorMessage = "Koneksi timeout"
                errorDetail = "Server terlalu lama merespons"
            }
            is UnknownHostException -> {
                errorMessage = "Server tidak ditemukan"
                errorDetail = "Periksa koneksi internet"
            }
            else -> {
                errorMessage = "Terjadi kesalahan"
                errorDetail = e.localizedMessage
            }
        }
    }

    fun doLogin() {
        if (username.isBlank() || password.isBlank()) {
            errorMessage = "Username dan password harus diisi"
            errorDetail = null
            return
        }
        isLoading = true
        errorMessage = null
        errorDetail = null
        scope.launch {
            try {
                val response = ApiClient.instance.login(LoginRequest(username, password))
                if (response.isSuccessful && response.body() != null) {
                    val body = response.body()!!
                    tokenManager.saveAuth(
                        body.token ?: "",
                        body.user?.id ?: 0,
                        body.user?.name ?: "",
                        body.user?.email ?: ""
                    )
                    onLoginSuccess()
                } else {
                    when (response.code()) {
                        401, 422 -> {
                            errorMessage = "Username atau password salah"
                            errorDetail = null
                        }
                        500 -> {
                            errorMessage = "Server error"
                            errorDetail = "Coba lagi nanti"
                        }
                        else -> {
                            errorMessage = "Login gagal (${response.code()})"
                            errorDetail = response.message()
                        }
                    }
                }
            } catch (e: Exception) {
                handleLoginError(e)
            } finally {
                isLoading = false
            }
        }
    }

    fun doGoogleLogin() {
        isGoogleLoading = true
        errorMessage = null
        errorDetail = null
        scope.launch {
            try {
                val credentialManager = CredentialManager.create(context)
                val rawNonce = UUID.randomUUID().toString()
                val md = MessageDigest.getInstance("SHA-256")
                val hashedNonce = md.digest(rawNonce.toByteArray())
                    .joinToString("") { "%02x".format(it) }

                val googleIdOption = GetGoogleIdOption.Builder()
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(GOOGLE_WEB_CLIENT_ID)
                    .setNonce(hashedNonce)
                    .build()

                val request = GetCredentialRequest.Builder()
                    .addCredentialOption(googleIdOption)
                    .build()

                val activity = context as Activity

                // Clear cached credential to force full account picker
                credentialManager.clearCredentialState(ClearCredentialStateRequest())

                val result = credentialManager.getCredential(
                    request = request,
                    context = activity
                )

                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(result.credential.data)
                val googleId = googleIdTokenCredential.id
                val displayName = googleIdTokenCredential.displayName ?: "User"
                val profileEmail = googleIdTokenCredential.id

                val response = ApiClient.instance.googleLogin(
                    GoogleLoginRequest(
                        google_id = googleId,
                        name = displayName,
                        email = profileEmail
                    )
                )

                if (response.isSuccessful && response.body() != null) {
                    val body = response.body()!!
                    tokenManager.saveAuth(
                        body.token ?: "",
                        body.user?.id ?: 0,
                        body.user?.name ?: "",
                        body.user?.email ?: ""
                    )
                    onLoginSuccess()
                } else {
                    errorMessage = "Google login gagal"
                    errorDetail = "Server menolak kredensial Google"
                }
            } catch (e: GetCredentialCancellationException) {
                Log.d("LoginScreen", "Google sign-in cancelled")
            } catch (e: Exception) {
                Log.e("LoginScreen", "Google sign-in error", e)
                errorMessage = "Google Sign-In gagal"
                errorDetail = e.localizedMessage
            } finally {
                isGoogleLoading = false
            }
        }
    }

    fun openAddGoogleAccount() {
        try {
            val intent = Intent(Settings.ACTION_ADD_ACCOUNT).apply {
                putExtra(Settings.EXTRA_ACCOUNT_TYPES, arrayOf("com.google"))
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            errorMessage = "Tidak bisa membuka pengaturan akun"
            errorDetail = "Tambahkan akun Google di Settings > Accounts"
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F172A)),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))
        ) {
            Column(
                modifier = Modifier
                    .padding(28.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header
                Text(
                    "🖩 E-Concalc",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF38BDF8)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    "Masuk untuk sinkronisasi riwayat",
                    fontSize = 14.sp,
                    color = Color(0xFF94A3B8),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Error Message
                if (errorMessage != null) {
                    Surface(
                        color = Color(0x26EF4444),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Default.Warning,
                                    contentDescription = null,
                                    tint = Color(0xFFFCA5A5),
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    errorMessage!!,
                                    color = Color(0xFFFCA5A5),
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                            if (errorDetail != null) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    errorDetail!!,
                                    color = Color(0xFFFCA5A5).copy(alpha = 0.7f),
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // ─── Username Field ───
                OutlinedTextField(
                    value = username,
                    onValueChange = {
                        username = it
                        errorMessage = null
                    },
                    label = { Text("Username") },
                    leadingIcon = { Icon(Icons.Default.Person, "Username", tint = Color(0xFF94A3B8)) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = Color(0xFF38BDF8),
                        unfocusedBorderColor = Color(0xFF334155),
                        focusedLabelColor = Color(0xFF38BDF8),
                        unfocusedLabelColor = Color(0xFF94A3B8),
                        cursorColor = Color(0xFF38BDF8)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                // ─── Password Field ───
                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        errorMessage = null
                    },
                    label = { Text("Password") },
                    leadingIcon = { Icon(Icons.Default.Lock, "Password", tint = Color(0xFF94A3B8)) },
                    trailingIcon = {
                        IconButton(onClick = { showPassword = !showPassword }) {
                            Icon(
                                if (showPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                "Toggle password",
                                tint = Color(0xFF94A3B8)
                            )
                        }
                    },
                    singleLine = true,
                    visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = Color(0xFF38BDF8),
                        unfocusedBorderColor = Color(0xFF334155),
                        focusedLabelColor = Color(0xFF38BDF8),
                        unfocusedLabelColor = Color(0xFF94A3B8),
                        cursorColor = Color(0xFF38BDF8)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // ─── Login Button ───
                Button(
                    onClick = { doLogin() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    contentPadding = PaddingValues(0.dp),
                    enabled = !isLoading && !isGoogleLoading
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.linearGradient(listOf(Color(0xFF0EA5E9), Color(0xFF38BDF8))),
                                RoundedCornerShape(12.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = Color(0xFF0F172A),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text("Masuk", fontWeight = FontWeight.SemiBold, color = Color(0xFF0F172A))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // ─── Divider ───
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    HorizontalDivider(modifier = Modifier.weight(1f), color = Color(0xFF334155))
                    Text("  atau  ", color = Color(0xFF64748B), fontSize = 12.sp)
                    HorizontalDivider(modifier = Modifier.weight(1f), color = Color(0xFF334155))
                }

                Spacer(modifier = Modifier.height(18.dp))

                // ─── Google Sign-In Button ───
                OutlinedButton(
                    onClick = { doGoogleLogin() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        brush = Brush.linearGradient(listOf(Color(0xFF334155), Color(0xFF475569)))
                    ),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color(0xFF1E293B)
                    ),
                    enabled = !isGoogleLoading && !isLoading
                ) {
                    if (isGoogleLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                    }
                    Text("G", color = Color(0xFF4285F4), fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        "Masuk dengan Google",
                        color = Color.White,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // ─── Add Another Google Account Button ───
                TextButton(
                    onClick = { openAddGoogleAccount() },
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    Icon(
                        Icons.Default.PersonAdd,
                        contentDescription = null,
                        tint = Color(0xFF64748B),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Gunakan akun Google lain",
                        color = Color(0xFF64748B),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Normal
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // ─── Register link ───
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text("Belum punya akun? ", color = Color(0xFF94A3B8), fontSize = 13.sp)
                    Text(
                        "Daftar",
                        color = Color(0xFF38BDF8),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.clickable { onNavigateToRegister() }
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    "← Lanjutkan tanpa login",
                    color = Color(0xFF64748B),
                    fontSize = 12.sp,
                    modifier = Modifier.clickable { onSkipLogin() }
                )
            }
        }
    }
}
