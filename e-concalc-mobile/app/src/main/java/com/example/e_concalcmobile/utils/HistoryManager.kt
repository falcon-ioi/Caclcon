package com.example.e_concalcmobile.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.e_concalcmobile.ui.screens.CalculationHistory
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class HistoryManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("e_calc_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()
    private val historyKey = "calculation_history"
    private val ratesKey = "currency_rates"
    private val ratesTimestampKey = "currency_rates_timestamp"

    fun saveHistory(history: List<CalculationHistory>) {
        val json = gson.toJson(history)
        prefs.edit().putString(historyKey, json).apply()
    }

    fun loadHistory(): List<CalculationHistory> {
        val json = prefs.getString(historyKey, null) ?: return emptyList()
        val type = object : TypeToken<List<CalculationHistory>>() {}.type
        return try {
            gson.fromJson(json, type)
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun saveRates(rates: Map<String, Double>) {
        val json = gson.toJson(rates)
        prefs.edit()
            .putString(ratesKey, json)
            .putLong(ratesTimestampKey, System.currentTimeMillis())
            .apply()
    }

    fun loadRates(): Pair<Map<String, Double>?, Long> {
        val json = prefs.getString(ratesKey, null)
        val timestamp = prefs.getLong(ratesTimestampKey, 0)
        
        if (json == null) return Pair(null, 0)
        
        val type = object : TypeToken<Map<String, Double>>() {}.type
        return try {
            val rates: Map<String, Double> = gson.fromJson(json, type)
            Pair(rates, timestamp)
        } catch (e: Exception) {
            Pair(null, 0)
        }
    }
    
    fun clearHistory() {
        prefs.edit().remove(historyKey).apply()
    }
}
