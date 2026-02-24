package com.example.e_concalcmobile.ui.screens

import android.app.Activity
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
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import com.example.e_concalcmobile.api.ApiClient
import com.example.e_concalcmobile.api.GoogleLoginRequest
import com.example.e_concalcmobile.api.RegisterRequest
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
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit,
    onSkipLogin: () -> Unit = {}
) {
    val context = LocalContext.current
    val tokenManager = remember { TokenManager(context) }
    val scope = rememberCoroutineScope()

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var isGoogleLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var errorDetail by remember { mutableStateOf<String?>(null) }
    var showPassword by remember { mutableStateOf(false) }

    fun handleError(e: Exception) {
        when (e) {
            is ConnectException -> {
                errorMessage = "Tidak bisa terhubung ke server"
                errorDetail = "Pastikan server sudah dijalankan"
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

    fun doRegister() {
        when {
            username.isBlank() || password.isBlank() -> {
                errorMessage = "Username dan password harus diisi"
                errorDetail = null
                return
            }
            password.length < 6 -> {
                errorMessage = "Password minimal 6 karakter"
                errorDetail = null
                return
            }
            password != confirmPassword -> {
                errorMessage = "Password tidak cocok"
                errorDetail = null
                return
            }
        }
        isLoading = true
        errorMessage = null
        errorDetail = null
        scope.launch {
            try {
                val response = ApiClient.instance.register(
                    RegisterRequest(username, password, confirmPassword)
                )
                if (response.isSuccessful && response.body() != null) {
                    val body = response.body()!!
                    tokenManager.saveAuth(
                        body.token ?: "",
                        body.user?.id ?: 0,
                        body.user?.name ?: "",
                        body.user?.email ?: ""
                    )
                    onRegisterSuccess()
                } else {
                    when (response.code()) {
                        419 -> {
                            errorMessage = "Session expired"
                            errorDetail = "Restart server lalu coba lagi"
                        }
                        422 -> {
                            errorMessage = "Username sudah dipakai atau data tidak valid"
                            errorDetail = "Coba username lain"
                        }
                        500 -> {
                            errorMessage = "Server error"
                            errorDetail = "Coba lagi nanti"
                        }
                        else -> {
                            errorMessage = "Registrasi gagal (${response.code()})"
                            errorDetail = response.message()
                        }
                    }
                }
            } catch (e: Exception) {
                handleError(e)
            } finally {
                isLoading = false
            }
        }
    }

    fun doGoogleRegister() {
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
                    onRegisterSuccess()
                } else {
                    errorMessage = "Google signup gagal"
                    errorDetail = "Server menolak kredensial Google"
                }
            } catch (e: GetCredentialCancellationException) {
                Log.d("RegisterScreen", "Google cancelled")
            } catch (e: Exception) {
                Log.e("RegisterScreen", "Google error", e)
                errorMessage = "Google Sign-In gagal"
                errorDetail = e.localizedMessage
            } finally {
                isGoogleLoading = false
            }
        }
    }

    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedTextColor = Color.White,
        unfocusedTextColor = Color.White,
        focusedBorderColor = Color(0xFF00D4FF),
        unfocusedBorderColor = Color(0xFF334155),
        focusedLabelColor = Color(0xFF00D4FF),
        unfocusedLabelColor = Color(0xFF94a3b8),
        cursorColor = Color(0xFF00D4FF)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1a1a2e)),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF16213e))
        ) {
            Column(
                modifier = Modifier
                    .padding(32.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header
                Text(
                    "🖩 E-Concalc",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF00D4FF)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    "Buat akun untuk sinkronisasi riwayat",
                    fontSize = 14.sp,
                    color = Color(0xFF94a3b8),
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

                // ─── Username ───
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it; errorMessage = null },
                    label = { Text("Username") },
                    leadingIcon = { Icon(Icons.Default.Person, "Username", tint = Color(0xFF94a3b8)) },
                    singleLine = true,
                    colors = textFieldColors,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                // ─── Password ───
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it; errorMessage = null },
                    label = { Text("Password") },
                    leadingIcon = { Icon(Icons.Default.Lock, "Password", tint = Color(0xFF94a3b8)) },
                    trailingIcon = {
                        IconButton(onClick = { showPassword = !showPassword }) {
                            Icon(
                                if (showPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                "Toggle",
                                tint = Color(0xFF94a3b8)
                            )
                        }
                    },
                    singleLine = true,
                    visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    colors = textFieldColors,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                // ─── Confirm Password ───
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it; errorMessage = null },
                    label = { Text("Konfirmasi Password") },
                    leadingIcon = { Icon(Icons.Default.Lock, "Confirm", tint = Color(0xFF94a3b8)) },
                    singleLine = true,
                    visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    colors = textFieldColors,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // ─── Register Button ───
                Button(
                    onClick = { doRegister() },
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
                                Brush.linearGradient(listOf(Color(0xFF4FACFE), Color(0xFF00F2FE))),
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
                            Text("Daftar", fontWeight = FontWeight.SemiBold, color = Color(0xFF0F172A))
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

                // ─── Google Sign-Up Button ───
                OutlinedButton(
                    onClick = { doGoogleRegister() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        brush = Brush.linearGradient(listOf(Color(0xFF334155), Color(0xFF475569)))
                    ),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color(0xFF1e293b)
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
                        "Daftar dengan Google",
                        color = Color.White,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // ─── Login link ───
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text("Sudah punya akun? ", color = Color(0xFF94a3b8), fontSize = 13.sp)
                    Text(
                        "Masuk",
                        color = Color(0xFF00D4FF),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.clickable { onNavigateToLogin() }
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // ─── Skip login link ───
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
