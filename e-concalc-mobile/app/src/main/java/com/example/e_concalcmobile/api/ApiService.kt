package com.example.e_concalcmobile.api

import retrofit2.Response
import retrofit2.http.*
import retrofit2.http.Query

// --- Request Models ---
data class LoginRequest(val username: String, val password: String)
data class RegisterRequest(val username: String, val password: String, val password_confirmation: String)
data class GoogleLoginRequest(val google_id: String, val name: String, val email: String)
data class HistoryRequest(val operasi: String, val tipe: String)

// --- Response Models ---
data class AuthResponse(val message: String, val user: UserData?, val token: String?)
data class UserData(val id: Int, val name: String, val email: String)
data class HistoryResponse(val data: List<HistoryItem>)
data class HistoryItem(val id: Int, val operasi: String, val tipe: String, val waktu: String)
data class MessageResponse(val message: String)

// --- API Interface ---
interface ApiService {

    @POST("api/register")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>

    @POST("api/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    @POST("api/login/google")
    suspend fun googleLogin(@Body request: GoogleLoginRequest): Response<AuthResponse>

    @POST("api/logout")
    suspend fun logout(@Header("Authorization") token: String): Response<MessageResponse>

    @GET("api/user")
    suspend fun getUser(@Header("Authorization") token: String): Response<UserData>

    @GET("api/history")
    suspend fun getHistory(@Header("Authorization") token: String, @Query("tipe") tipe: String? = null): Response<HistoryResponse>

    @POST("api/history")
    suspend fun saveHistory(@Header("Authorization") token: String, @Body request: HistoryRequest): Response<MessageResponse>

    @DELETE("api/history")
    suspend fun clearHistory(@Header("Authorization") token: String): Response<MessageResponse>

    @DELETE("api/history/{id}")
    suspend fun deleteHistory(@Header("Authorization") token: String, @Path("id") id: Int): Response<MessageResponse>
}
