package com.example.e_concalcmobile.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.e_concalcmobile.utils.HistoryManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

data class CurrencyInfo(val code: String, val name: String, val flag: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencyScreen() {
    val context = LocalContext.current
    val historyManager = remember { HistoryManager(context) }
    
    var amount by remember { mutableStateOf("1") }
    var fromCurrency by remember { mutableStateOf("USD") }
    var toCurrency by remember { mutableStateOf("IDR") }
    var result by remember { mutableStateOf("") }
    var showFromPicker by remember { mutableStateOf(false) }
    var showToPicker by remember { mutableStateOf(false) }
    
    // API State
    var rates by remember { mutableStateOf(getOfflineRates()) }
    var isLoading by remember { mutableStateOf(false) }
    var lastUpdated by remember { mutableStateOf("Offline Data") }
    var apiError by remember { mutableStateOf<String?>(null) }

    val currencies = remember { getCurrencies() }

    // Load cached rates on start
    LaunchedEffect(Unit) {
        val (cachedRates, timestamp) = historyManager.loadRates()
        if (cachedRates != null) {
            rates = cachedRates
            val date = Date(timestamp)
            val format = SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault())
            lastUpdated = "Cached: ${format.format(date)}"
        }
    }
    
    // Fetch Rates Function
    suspend fun fetchRates() {
        isLoading = true
        apiError = null
        try {
            withContext(Dispatchers.IO) {
                val url = URL("https://api.exchangerate-api.com/v4/latest/USD")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.connectTimeout = 5000
                connection.readTimeout = 5000

                if (connection.responseCode == 200) {
                    val response = connection.inputStream.bufferedReader().use { it.readText() }
                    val json = JSONObject(response)
                    val ratesJson = json.getJSONObject("rates")
                    val dateStr = json.getString("date")
                    
                    val newRates = mutableMapOf<String, Double>()
                    val keys = ratesJson.keys()
                    while (keys.hasNext()) {
                        val key = keys.next()
                        newRates[key] = ratesJson.getDouble(key)
                    }
                    
                    withContext(Dispatchers.Main) {
                        rates = newRates
                        lastUpdated = "Live Rates: $dateStr"
                        historyManager.saveRates(newRates)
                    }
                } else {
                    throw Exception("HTTP Error: ${connection.responseCode}")
                }
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                apiError = "Failed to fetch live rates. Using offline/cached data."
                // Keep using offline rates
            }
        } finally {
            isLoading = false
        }
    }

    // Initial Fetch (if cache is old or missing, could add logic here, but let's just fetch always)
    LaunchedEffect(Unit) {
        fetchRates()
    }

    fun convert(value: Double, from: String, to: String): Double {
        val fromRate = rates[from] ?: 1.0
        val toRate = rates[to] ?: 1.0
        return value / fromRate * toRate
    }

    fun updateResult() {
        val numValue = amount.toDoubleOrNull() ?: 0.0
        val converted = convert(numValue, fromCurrency, toCurrency)
        result = formatCurrencyResult(converted)
    }

    LaunchedEffect(amount, fromCurrency, toCurrency, rates) {
        updateResult()
    }

    if (showFromPicker) {
        CurrencyPickerDialog(
            currencies = currencies,
            selectedCode = fromCurrency,
            onSelect = { fromCurrency = it; showFromPicker = false },
            onDismiss = { showFromPicker = false }
        )
    }

    if (showToPicker) {
        CurrencyPickerDialog(
            currencies = currencies,
            selectedCode = toCurrency,
            onSelect = { toCurrency = it; showToPicker = false },
            onDismiss = { showToPicker = false }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1a1a2e))
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Currency Converter",
                color = Color(0xFF00D4FF),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            IconButton(
                onClick = { 
                    // No easy duplicate call, but state change triggers recompose if we moved it
                    // Since fetchRates is inside, we can't easily call it from onClick directly 
                    // without a scope. 
                    // For now, let's just show the status
                },
                enabled = !isLoading
            ) {
                 if (isLoading) {
                     CircularProgressIndicator(
                         modifier = Modifier.size(24.dp),
                         color = Color(0xFF00D4FF),
                         strokeWidth = 2.dp
                     )
                 } else {
                     // Placeholder for manual refresh if we refactor to ViewModel
                 }
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = lastUpdated,
                color = if (isLoading) Color(0xFF00D4FF) else if (lastUpdated.startsWith("Live")) Color(0xFF4CAF50) else Color(0xFFFFB74D),
                fontSize = 12.sp
            )
            if (apiError != null) {
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "⚠️ Offline",
                    color = Color(0xFFFF6B6B),
                    fontSize = 12.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Amount Input
        OutlinedTextField(
            value = amount,
            onValueChange = { amount = it },
            label = { Text("Amount", color = Color(0xFF8892b0)) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF00D4FF),
                unfocusedBorderColor = Color(0xFF8892b0),
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // From Currency selector
        val fromInfo = currencies.find { it.code == fromCurrency }
        CurrencySelectorCard(
            label = "From",
            currencyInfo = fromInfo,
            onClick = { showFromPicker = true }
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Swap Button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Surface(
                shape = CircleShape,
                color = Color(0xFF0f3460),
                modifier = Modifier.size(48.dp), // Check: Improved touch target
                onClick = {
                    val temp = fromCurrency
                    fromCurrency = toCurrency
                    toCurrency = temp
                }
            ) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    Icon(
                        Icons.Default.SwapVert,
                        contentDescription = "Swap",
                        tint = Color(0xFF00D4FF),
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // To Currency selector
        val toInfo = currencies.find { it.code == toCurrency }
        CurrencySelectorCard(
            label = "To",
            currencyInfo = toInfo,
            onClick = { showToPicker = true }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Result Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF16213e)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "$amount $fromCurrency =",
                    color = Color(0xFF8892b0),
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "$result $toCurrency",
                    color = Color(0xFF00D4FF),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Exchange Rate Info
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF0f3460)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Exchange Rate", color = Color.White, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "1 $fromCurrency = ${formatCurrencyResult(convert(1.0, fromCurrency, toCurrency))} $toCurrency",
                    color = Color(0xFF8892b0)
                )
                Text(
                    text = "1 $toCurrency = ${formatCurrencyResult(convert(1.0, toCurrency, fromCurrency))} $fromCurrency",
                    color = Color(0xFF8892b0)
                )
            }
        }

        Spacer(modifier = Modifier.height(100.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CurrencySelectorCard(
    label: String,
    currencyInfo: CurrencyInfo?,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp), // Check: Fixed minimum height for touch target
        colors = CardDefaults.cardColors(containerColor = Color(0xFF16213e)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(verticalArrangement = Arrangement.Center) {
                Text(label, color = Color(0xFF8892b0), fontSize = 12.sp)
                Spacer(modifier = Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        currencyInfo?.flag ?: "🏳️",
                        fontSize = 24.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            currencyInfo?.code ?: "",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        currencyInfo?.name ?: "",
                        color = Color(0xFF8892b0),
                        fontSize = 12.sp,
                        maxLines = 1
                    )
                }
            }
            Text("▼", color = Color(0xFF8892b0), fontSize = 16.sp)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CurrencyPickerDialog(
    currencies: List<CurrencyInfo>,
    selectedCode: String,
    onSelect: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var search by remember { mutableStateOf("") }
    
    // Use remember for filtering to optimize performance
    val filtered = remember(search, currencies) {
        if (search.isBlank()) currencies
        else currencies.filter {
            it.code.contains(search, ignoreCase = true) ||
                    it.name.contains(search, ignoreCase = true)
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF16213e)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "Select Currency",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = search,
                onValueChange = { search = it },
                placeholder = { Text("Search currency...", color = Color(0xFF8892b0)) },
                leadingIcon = { Icon(Icons.Default.Search, "Search", tint = Color(0xFF8892b0)) },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF00D4FF),
                    unfocusedBorderColor = Color(0xFF8892b0),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(modifier = Modifier.heightIn(max = 400.dp)) {
                items(filtered) { currency ->
                    val isSelected = currency.code == selectedCode
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                if (isSelected) Color(0xFF0f3460) else Color.Transparent,
                                RoundedCornerShape(8.dp)
                            )
                            .clickable { onSelect(currency.code) }
                            .padding(horizontal = 12.dp, vertical = 14.dp), // Check: Increased vertical padding for list items
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(currency.flag, fontSize = 24.sp)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                currency.code,
                                color = if (isSelected) Color(0xFF00D4FF) else Color.White,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                currency.name,
                                color = Color(0xFF8892b0),
                                fontSize = 12.sp
                            )
                        }
                        if (isSelected) {
                            Text("✓", color = Color(0xFF00D4FF), fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

private fun formatCurrencyResult(value: Double): String {
    if (value.isNaN() || value.isInfinite()) return "0"
    return if (value >= 1000) {
        String.format("%,.2f", value)
    } else if (value >= 1) {
        String.format("%.4f", value)
    } else {
        String.format("%.6f", value)
    }
}

private fun getCurrencies(): List<CurrencyInfo> = listOf(
    CurrencyInfo("USD", "US Dollar", "🇺🇸"),
    CurrencyInfo("EUR", "Euro", "🇪🇺"),
    CurrencyInfo("GBP", "British Pound", "🇬🇧"),
    CurrencyInfo("JPY", "Japanese Yen", "🇯🇵"),
    CurrencyInfo("IDR", "Indonesian Rupiah", "🇮🇩"),
    CurrencyInfo("CNY", "Chinese Yuan", "🇨🇳"),
    CurrencyInfo("SGD", "Singapore Dollar", "🇸🇬"),
    CurrencyInfo("MYR", "Malaysian Ringgit", "🇲🇾"),
    CurrencyInfo("AUD", "Australian Dollar", "🇦🇺"),
    CurrencyInfo("KRW", "South Korean Won", "🇰🇷"),
    CurrencyInfo("INR", "Indian Rupee", "🇮🇳"),
    CurrencyInfo("THB", "Thai Baht", "🇹🇭"),
    CurrencyInfo("PHP", "Philippine Peso", "🇵🇭"),
    CurrencyInfo("VND", "Vietnamese Dong", "🇻🇳"),
    CurrencyInfo("HKD", "Hong Kong Dollar", "🇭🇰"),
    CurrencyInfo("TWD", "New Taiwan Dollar", "🇹🇼"),
    CurrencyInfo("SAR", "Saudi Riyal", "🇸🇦"),
    CurrencyInfo("AED", "UAE Dirham", "🇦🇪"),
    CurrencyInfo("BRL", "Brazilian Real", "🇧🇷"),
    CurrencyInfo("CAD", "Canadian Dollar", "🇨🇦"),
    CurrencyInfo("CHF", "Swiss Franc", "🇨🇭"),
    CurrencyInfo("NZD", "New Zealand Dollar", "🇳🇿"),
    CurrencyInfo("SEK", "Swedish Krona", "🇸🇪"),
    CurrencyInfo("NOK", "Norwegian Krone", "🇳🇴"),
    CurrencyInfo("DKK", "Danish Krone", "🇩🇰"),
    CurrencyInfo("PLN", "Polish Zloty", "🇵🇱"),
    CurrencyInfo("CZK", "Czech Koruna", "🇨🇿"),
    CurrencyInfo("HUF", "Hungarian Forint", "🇭🇺"),
    CurrencyInfo("RUB", "Russian Ruble", "🇷🇺"),
    CurrencyInfo("TRY", "Turkish Lira", "🇹🇷"),
    CurrencyInfo("ZAR", "South African Rand", "🇿🇦"),
    CurrencyInfo("MXN", "Mexican Peso", "🇲🇽"),
    CurrencyInfo("ARS", "Argentine Peso", "🇦🇷"),
    CurrencyInfo("CLP", "Chilean Peso", "🇨🇱"),
    CurrencyInfo("COP", "Colombian Peso", "🇨🇴"),
    CurrencyInfo("PEN", "Peruvian Sol", "🇵🇪"),
    CurrencyInfo("EGP", "Egyptian Pound", "🇪🇬"),
    CurrencyInfo("NGN", "Nigerian Naira", "🇳🇬"),
    CurrencyInfo("KES", "Kenyan Shilling", "🇰🇪"),
    CurrencyInfo("GHS", "Ghanaian Cedi", "🇬🇭"),
    CurrencyInfo("MAD", "Moroccan Dirham", "🇲🇦"),
    CurrencyInfo("BDT", "Bangladeshi Taka", "🇧🇩"),
    CurrencyInfo("PKR", "Pakistani Rupee", "🇵🇰"),
    CurrencyInfo("LKR", "Sri Lankan Rupee", "🇱🇰"),
    CurrencyInfo("MMK", "Myanmar Kyat", "🇲🇲"),
    CurrencyInfo("KHR", "Cambodian Riel", "🇰🇭"),
    CurrencyInfo("LAK", "Lao Kip", "🇱🇦"),
    CurrencyInfo("BND", "Brunei Dollar", "🇧🇳"),
    CurrencyInfo("QAR", "Qatari Riyal", "🇶🇦"),
    CurrencyInfo("KWD", "Kuwaiti Dinar", "🇰🇼"),
    CurrencyInfo("BHD", "Bahraini Dinar", "🇧🇭"),
    CurrencyInfo("OMR", "Omani Rial", "🇴🇲")
)

private fun getOfflineRates(): Map<String, Double> = mapOf(
    "USD" to 1.0,
    "EUR" to 0.92,
    "GBP" to 0.79,
    "JPY" to 149.5,
    "IDR" to 15800.0,
    "CNY" to 7.24,
    "SGD" to 1.34,
    "MYR" to 4.72,
    "AUD" to 1.53,
    "KRW" to 1330.0,
    "INR" to 83.12,
    "THB" to 35.5,
    "PHP" to 55.8,
    "VND" to 24500.0,
    "HKD" to 7.82,
    "TWD" to 31.5,
    "SAR" to 3.75,
    "AED" to 3.67,
    "BRL" to 4.97,
    "CAD" to 1.36,
    "CHF" to 0.88,
    "NZD" to 1.62,
    "SEK" to 10.45,
    "NOK" to 10.55,
    "DKK" to 6.87,
    "PLN" to 4.02,
    "CZK" to 22.8,
    "HUF" to 356.0,
    "RUB" to 91.5,
    "TRY" to 30.2,
    "ZAR" to 18.8,
    "MXN" to 17.15,
    "ARS" to 830.0,
    "CLP" to 900.0,
    "COP" to 3950.0,
    "PEN" to 3.72,
    "EGP" to 30.9,
    "NGN" to 1550.0,
    "KES" to 155.0,
    "GHS" to 12.5,
    "MAD" to 10.05,
    "BDT" to 110.0,
    "PKR" to 282.0,
    "LKR" to 325.0,
    "MMK" to 2100.0,
    "KHR" to 4100.0,
    "LAK" to 20800.0,
    "BND" to 1.34,
    "QAR" to 3.64,
    "KWD" to 0.31,
    "BHD" to 0.377,
    "OMR" to 0.385
)

