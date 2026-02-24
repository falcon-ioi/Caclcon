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
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.e_concalcmobile.utils.HistoryManager
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConverterScreen() {
    val context = LocalContext.current
    val historyManager = remember { HistoryManager(context) }
    val scope = rememberCoroutineScope()

    var inputValue by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }
    var selectedCategoryIdx by remember { mutableStateOf(0) }
    var fromUnitIdx by remember { mutableStateOf(0) }
    var toUnitIdx by remember { mutableStateOf(1) }
    var showCategoryPicker by remember { mutableStateOf(false) }
    var showFromPicker by remember { mutableStateOf(false) }
    var showToPicker by remember { mutableStateOf(false) }
    var showHistory by remember { mutableStateOf(false) }
    var history by remember { mutableStateOf(listOf<CalculationHistory>()) }

    val categories = remember { getConverterCategories() }
    val currentCategory = categories[selectedCategoryIdx]

    // Load history on start
    LaunchedEffect(Unit) {
        history = historyManager.loadHistory()
    }

    fun convert(): String {
        val value = inputValue.toDoubleOrNull() ?: 0.0
        if (value == 0.0) return "0"

        val fromFactor = currentCategory.units[fromUnitIdx].factor
        val toFactor = currentCategory.units[toUnitIdx].factor

        // Special handling for temperature
        if (currentCategory.name == "Temperature") {
            val fromUnit = currentCategory.units[fromUnitIdx].symbol
            val toUnit = currentCategory.units[toUnitIdx].symbol
            val convertedResult = convertTemperature(value, fromUnit, toUnit)
            return formatConverterResult(convertedResult)
        } else {
            val converted = value * fromFactor / toFactor
            return formatConverterResult(converted)
        }
    }

    LaunchedEffect(inputValue, fromUnitIdx, toUnitIdx, selectedCategoryIdx) {
        result = convert()
    }

    // Save conversion to history
    fun saveConversion() {
        val value = inputValue.toDoubleOrNull() ?: return
        if (value == 0.0) return
        val fromSymbol = currentCategory.units[fromUnitIdx].symbol
        val toSymbol = currentCategory.units[toUnitIdx].symbol
        val expr = "$inputValue $fromSymbol → $result $toSymbol"
        val resultVal = "$result $toSymbol"
        val newItem = CalculationHistory(expr, resultVal)
        history = listOf(newItem) + history.take(49)
        historyManager.saveHistory(history)

        scope.launch {
            historyManager.saveAndSync("$inputValue $fromSymbol", "$result $toSymbol", "conv")
        }
    }

    // Pickers
    if (showCategoryPicker) {
        ConverterPickerSheet(
            title = "Select Category",
            items = categories.map { "${it.icon}  ${it.name}" },
            selectedIndex = selectedCategoryIdx,
            onSelect = {
                selectedCategoryIdx = it
                fromUnitIdx = 0
                toUnitIdx = if (categories[it].units.size > 1) 1 else 0
                showCategoryPicker = false
            },
            onDismiss = { showCategoryPicker = false }
        )
    }

    if (showFromPicker) {
        ConverterPickerSheet(
            title = "From Unit",
            items = currentCategory.units.map { "${it.symbol} — ${it.name}" },
            selectedIndex = fromUnitIdx,
            onSelect = { fromUnitIdx = it; showFromPicker = false },
            onDismiss = { showFromPicker = false }
        )
    }

    if (showToPicker) {
        ConverterPickerSheet(
            title = "To Unit",
            items = currentCategory.units.map { "${it.symbol} — ${it.name}" },
            selectedIndex = toUnitIdx,
            onSelect = { toUnitIdx = it; showToPicker = false },
            onDismiss = { showToPicker = false }
        )
    }

    if (showHistory) {
        ConverterHistorySheet(
            history = history,
            onDismiss = { showHistory = false },
            onClear = {
                history = emptyList()
                historyManager.clearHistory()
                scope.launch { historyManager.clearHistoryFromApi() }
            },
            onSelect = { item ->
                showHistory = false
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F172A))
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Unit Converter",
                    color = Color(0xFF38BDF8),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "${categories.size} categories available",
                    color = Color(0xFF64748B),
                    fontSize = 12.sp
                )
            }
            IconButton(
                onClick = { showHistory = true },
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    Icons.Default.History,
                    contentDescription = "History",
                    tint = Color(0xFF64748B),
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Category Selector
        Card(
            onClick = { showCategoryPicker = true },
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(currentCategory.icon, fontSize = 28.sp)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("Category", color = Color(0xFF64748B), fontSize = 11.sp)
                        Text(
                            currentCategory.name,
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Text("▼", color = Color(0xFF64748B))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Input Value
        OutlinedTextField(
            value = inputValue,
            onValueChange = { inputValue = it },
            label = { Text("Value", color = Color(0xFF64748B)) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF38BDF8),
                unfocusedBorderColor = Color(0xFF334155),
                focusedTextColor = Color.White,
                unfocusedTextColor = Color(0xFFE2E8F0),
                cursorColor = Color(0xFF38BDF8)
            ),
            shape = RoundedCornerShape(14.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // From Unit
        UnitSelectorCard(
            label = "From",
            unit = currentCategory.units[fromUnitIdx],
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
                color = Color(0xFF1E3A5F),
                modifier = Modifier.size(44.dp),
                onClick = {
                    val temp = fromUnitIdx
                    fromUnitIdx = toUnitIdx
                    toUnitIdx = temp
                }
            ) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    Icon(
                        Icons.Default.SwapVert,
                        contentDescription = "Swap",
                        tint = Color(0xFF38BDF8),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // To Unit
        UnitSelectorCard(
            label = "To",
            unit = currentCategory.units[toUnitIdx],
            onClick = { showToPicker = true }
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Convert button
        Button(
            onClick = { saveConversion() },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            shape = RoundedCornerShape(14.dp),
            contentPadding = PaddingValues(0.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.horizontalGradient(listOf(Color(0xFF0EA5E9), Color(0xFF38BDF8))),
                        RoundedCornerShape(14.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Save to History",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Result Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "${inputValue.ifEmpty { "0" }} ${currentCategory.units[fromUnitIdx].symbol}",
                    color = Color(0xFF94A3B8),
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text("=", color = Color(0xFF475569), fontSize = 14.sp)
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "$result ${currentCategory.units[toUnitIdx].symbol}",
                    color = Color(0xFF38BDF8),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(100.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UnitSelectorCard(
    label: String,
    unit: UnitItem,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(label, color = Color(0xFF64748B), fontSize = 11.sp, fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    unit.name,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    unit.symbol,
                    color = Color(0xFF64748B),
                    fontSize = 12.sp
                )
            }
            Text("▼", color = Color(0xFF64748B), fontSize = 16.sp)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ConverterPickerSheet(
    title: String,
    items: List<String>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF1E293B),
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                title,
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(modifier = Modifier.heightIn(max = 380.dp)) {
                items(items.size) { index ->
                    val isSelected = index == selectedIndex
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                if (isSelected) Color(0xFF1E3A5F) else Color.Transparent,
                                RoundedCornerShape(10.dp)
                            )
                            .clickable { onSelect(index) }
                            .padding(horizontal = 14.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            items[index],
                            color = if (isSelected) Color(0xFF38BDF8) else Color(0xFFE2E8F0),
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                        if (isSelected) {
                            Text("✓", color = Color(0xFF38BDF8), fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ConverterHistorySheet(
    history: List<CalculationHistory>,
    onDismiss: () -> Unit,
    onClear: () -> Unit,
    onSelect: (CalculationHistory) -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF1E293B),
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Conversion History",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                if (history.isNotEmpty()) {
                    IconButton(onClick = onClear) {
                        Icon(Icons.Default.Delete, "Clear", tint = Color(0xFFEF4444))
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (history.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("📐", fontSize = 40.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("No conversion history", color = Color(0xFF64748B), fontSize = 16.sp)
                    }
                }
            } else {
                LazyColumn(modifier = Modifier.heightIn(max = 400.dp)) {
                    items(history) { item ->
                        Card(
                            onClick = { onSelect(item) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(item.expression, color = Color(0xFF94A3B8), fontSize = 14.sp)
                                Text(
                                    "= ${item.result}",
                                    color = Color(0xFF38BDF8),
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

// ==================== Data Models ====================

data class UnitItem(val name: String, val symbol: String, val factor: Double)
data class ConverterCategory(val name: String, val icon: String, val units: List<UnitItem>)

private fun formatConverterResult(value: Double): String {
    if (value.isNaN() || value.isInfinite()) return "0"
    return if (value == value.toLong().toDouble() && kotlin.math.abs(value) < 1e15) {
        String.format("%,d", value.toLong())
    } else if (kotlin.math.abs(value) >= 1000) {
        String.format("%,.4f", value)
    } else if (kotlin.math.abs(value) >= 1) {
        String.format("%.6f", value).trimEnd('0').trimEnd('.')
    } else {
        String.format("%.10f", value).trimEnd('0').trimEnd('.')
    }
}

private fun convertTemperature(value: Double, from: String, to: String): Double {
    // Convert to Celsius first
    val celsius = when (from) {
        "°C" -> value
        "°F" -> (value - 32) * 5.0 / 9.0
        "K" -> value - 273.15
        else -> value
    }
    // Convert from Celsius to target
    return when (to) {
        "°C" -> celsius
        "°F" -> celsius * 9.0 / 5.0 + 32
        "K" -> celsius + 273.15
        else -> celsius
    }
}

private fun getConverterCategories(): List<ConverterCategory> = listOf(
    ConverterCategory("Length", "📏", listOf(
        UnitItem("Kilometer", "km", 1000.0),
        UnitItem("Meter", "m", 1.0),
        UnitItem("Centimeter", "cm", 0.01),
        UnitItem("Millimeter", "mm", 0.001),
        UnitItem("Micrometer", "μm", 1e-6),
        UnitItem("Nanometer", "nm", 1e-9),
        UnitItem("Mile", "mi", 1609.344),
        UnitItem("Yard", "yd", 0.9144),
        UnitItem("Foot", "ft", 0.3048),
        UnitItem("Inch", "in", 0.0254)
    )),
    ConverterCategory("Weight", "⚖️", listOf(
        UnitItem("Tonne", "t", 1000.0),
        UnitItem("Kilogram", "kg", 1.0),
        UnitItem("Gram", "g", 0.001),
        UnitItem("Milligram", "mg", 1e-6),
        UnitItem("Pound", "lb", 0.453592),
        UnitItem("Ounce", "oz", 0.0283495)
    )),
    ConverterCategory("Temperature", "🌡️", listOf(
        UnitItem("Celsius", "°C", 1.0),
        UnitItem("Fahrenheit", "°F", 1.0),
        UnitItem("Kelvin", "K", 1.0)
    )),
    ConverterCategory("Area", "📐", listOf(
        UnitItem("Square Kilometer", "km²", 1e6),
        UnitItem("Square Meter", "m²", 1.0),
        UnitItem("Square Centimeter", "cm²", 1e-4),
        UnitItem("Hectare", "ha", 1e4),
        UnitItem("Acre", "ac", 4046.86),
        UnitItem("Square Foot", "ft²", 0.092903),
        UnitItem("Square Inch", "in²", 6.4516e-4)
    )),
    ConverterCategory("Volume", "🧊", listOf(
        UnitItem("Cubic Meter", "m³", 1000.0),
        UnitItem("Liter", "L", 1.0),
        UnitItem("Milliliter", "mL", 0.001),
        UnitItem("Gallon (US)", "gal", 3.78541),
        UnitItem("Quart", "qt", 0.946353),
        UnitItem("Cup", "cup", 0.236588),
        UnitItem("Fluid Ounce", "fl oz", 0.0295735),
        UnitItem("Cubic Centimeter", "cm³", 0.001)
    )),
    ConverterCategory("Speed", "🚀", listOf(
        UnitItem("Meter/second", "m/s", 1.0),
        UnitItem("Kilometer/hour", "km/h", 0.277778),
        UnitItem("Mile/hour", "mph", 0.44704),
        UnitItem("Knot", "kn", 0.514444),
        UnitItem("Foot/second", "ft/s", 0.3048)
    )),
    ConverterCategory("Data", "💾", listOf(
        UnitItem("Bit", "bit", 1.0),
        UnitItem("Byte", "B", 8.0),
        UnitItem("Kilobyte", "KB", 8192.0),
        UnitItem("Megabyte", "MB", 8388608.0),
        UnitItem("Gigabyte", "GB", 8589934592.0),
        UnitItem("Terabyte", "TB", 8.796093022208e12)
    )),
    ConverterCategory("Time", "⏱️", listOf(
        UnitItem("Nanosecond", "ns", 1e-9),
        UnitItem("Microsecond", "μs", 1e-6),
        UnitItem("Millisecond", "ms", 0.001),
        UnitItem("Second", "s", 1.0),
        UnitItem("Minute", "min", 60.0),
        UnitItem("Hour", "h", 3600.0),
        UnitItem("Day", "day", 86400.0),
        UnitItem("Week", "week", 604800.0),
        UnitItem("Month", "month", 2629746.0),
        UnitItem("Year", "year", 31557600.0)
    )),
    ConverterCategory("Pressure", "🔧", listOf(
        UnitItem("Pascal", "Pa", 1.0),
        UnitItem("Kilopascal", "kPa", 1000.0),
        UnitItem("Bar", "bar", 100000.0),
        UnitItem("PSI", "psi", 6894.76),
        UnitItem("Atmosphere", "atm", 101325.0),
        UnitItem("mmHg", "mmHg", 133.322)
    )),
    ConverterCategory("Energy", "⚡", listOf(
        UnitItem("Joule", "J", 1.0),
        UnitItem("Kilojoule", "kJ", 1000.0),
        UnitItem("Calorie", "cal", 4.184),
        UnitItem("Kilocalorie", "kcal", 4184.0),
        UnitItem("Kilowatt-hour", "kWh", 3600000.0),
        UnitItem("BTU", "BTU", 1055.06),
        UnitItem("Electronvolt", "eV", 1.602176634e-19)
    )),
    ConverterCategory("Force", "💪", listOf(
        UnitItem("Newton", "N", 1.0),
        UnitItem("Kilonewton", "kN", 1000.0),
        UnitItem("Pound-force", "lbf", 4.44822),
        UnitItem("Dyne", "dyn", 1e-5),
        UnitItem("Kilogram-force", "kgf", 9.80665)
    )),
    ConverterCategory("Angle", "📐", listOf(
        UnitItem("Degree", "°", 1.0),
        UnitItem("Radian", "rad", 57.2958),
        UnitItem("Gradian", "grad", 0.9),
        UnitItem("Arcminute", "arcmin", 1.0 / 60.0),
        UnitItem("Arcsecond", "arcsec", 1.0 / 3600.0),
        UnitItem("Revolution", "rev", 360.0)
    )),
    ConverterCategory("Frequency", "📡", listOf(
        UnitItem("Hertz", "Hz", 1.0),
        UnitItem("Kilohertz", "kHz", 1000.0),
        UnitItem("Megahertz", "MHz", 1e6),
        UnitItem("Gigahertz", "GHz", 1e9),
        UnitItem("RPM", "rpm", 1.0 / 60.0)
    )),
    ConverterCategory("Power", "🔌", listOf(
        UnitItem("Watt", "W", 1.0),
        UnitItem("Kilowatt", "kW", 1000.0),
        UnitItem("Megawatt", "MW", 1e6),
        UnitItem("Horsepower", "hp", 745.7),
        UnitItem("BTU/hour", "BTU/h", 0.293071)
    ))
)
