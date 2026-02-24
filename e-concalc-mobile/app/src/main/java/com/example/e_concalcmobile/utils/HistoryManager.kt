package com.example.e_concalcmobile.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.e_concalcmobile.api.ApiClient
import com.example.e_concalcmobile.api.HistoryRequest
import com.example.e_concalcmobile.api.HistoryItem
import com.example.e_concalcmobile.ui.screens.CalculationHistory
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HistoryManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("ecalc_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()
    private val ratesKey = "cached_rates"
    private val tokenManager = TokenManager(context)

    // --- Per-user history key ---
    private fun historyKey(): String {
        val userId = tokenManager.getUserId()
        return if (userId > 0) "history_user_$userId" else "history_guest"
    }

    // --- Local History (SharedPreferences) ---
    fun saveHistory(history: List<CalculationHistory>) {
        val json = gson.toJson(history)
        prefs.edit().putString(historyKey(), json).apply()
    }

    fun loadHistory(): List<CalculationHistory> {
        val json = prefs.getString(historyKey(), null) ?: return emptyList()
        val type = object : TypeToken<List<CalculationHistory>>() {}.type
        return try {
            gson.fromJson(json, type)
        } catch (e: Exception) {
            emptyList()
        }
    }

    // --- Combined Save: local + API ---
    suspend fun saveAndSync(expression: String, result: String, tipe: String) {
        // Save locally
        val current = loadHistory()
        val newItem = CalculationHistory(expression, result)
        val updated = listOf(newItem) + current.take(99)
        saveHistory(updated)

        // Sync to API if logged in
        if (tokenManager.isLoggedIn()) {
            val operasi = "$expression = $result"
            saveHistoryToApi(operasi, tipe)
        }
    }

    // --- API Sync ---
    private suspend fun saveHistoryToApi(operasi: String, tipe: String) {
        val token = tokenManager.getBearerToken() ?: return
        try {
            withContext(Dispatchers.IO) {
                ApiClient.instance.saveHistory(token, HistoryRequest(operasi, tipe))
            }
        } catch (e: Exception) {
            // Silently fail - local history is still saved
        }
    }

    suspend fun loadHistoryFromApi(tipe: String? = null): List<HistoryItem> {
        val token = tokenManager.getBearerToken() ?: return emptyList()
        return try {
            withContext(Dispatchers.IO) {
                val response = ApiClient.instance.getHistory(token, tipe)
                if (response.isSuccessful) {
                    response.body()?.data ?: emptyList()
                } else {
                    emptyList()
                }
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun clearHistoryFromApi() {
        val token = tokenManager.getBearerToken() ?: return
        try {
            withContext(Dispatchers.IO) {
                ApiClient.instance.clearHistory(token)
            }
        } catch (e: Exception) {
            // Silently fail
        }
    }

    fun isLoggedIn(): Boolean = tokenManager.isLoggedIn()

    // --- Clear Local History ---
    fun clearHistory() {
        prefs.edit().remove(historyKey()).apply()
    }

    // --- Clear ALL local history (for logout) ---
    fun clearAllUserHistory() {
        val editor = prefs.edit()
        prefs.all.keys.filter { it.startsWith("history_") }.forEach {
            editor.remove(it)
        }
        editor.apply()
    }

    // --- Cached Rates ---
    fun saveCachedRates(ratesJson: String) {
        prefs.edit().putString(ratesKey, ratesJson).apply()
    }

    fun getCachedRates(): String? {
        return prefs.getString(ratesKey, null)
    }

    fun saveRates(rates: Map<String, Double>) {
        val json = gson.toJson(rates)
        prefs.edit()
            .putString(ratesKey, json)
            .putLong("rates_timestamp", System.currentTimeMillis())
            .apply()
    }

    fun loadRates(): Pair<Map<String, Double>?, Long> {
        val json = prefs.getString(ratesKey, null)
        val timestamp = prefs.getLong("rates_timestamp", 0L)
        if (json == null) return Pair(null, 0L)
        return try {
            val type = object : TypeToken<Map<String, Double>>() {}.type
            val rates: Map<String, Double> = gson.fromJson(json, type)
            Pair(rates, timestamp)
        } catch (e: Exception) {
            Pair(null, 0L)
        }
    }
}
