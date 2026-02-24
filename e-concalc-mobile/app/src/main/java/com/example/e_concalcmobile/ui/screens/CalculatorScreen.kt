package com.example.e_concalcmobile.ui.screens

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.e_concalcmobile.utils.HistoryManager
import kotlinx.coroutines.launch
import kotlin.math.*

data class CalculationHistory(
    val expression: String,
    val result: String,
    val timestamp: Long = System.currentTimeMillis()
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculatorScreen() {
    val context = LocalContext.current
    val historyManager = remember { HistoryManager(context) }
    val scope = rememberCoroutineScope()

    var expression by remember { mutableStateOf("") }
    var displayResult by remember { mutableStateOf("0") }
    var memory by remember { mutableStateOf(0.0) }
    var isSecondMode by remember { mutableStateOf(false) }
    var isDegreeMode by remember { mutableStateOf(true) }
    var showHistory by remember { mutableStateOf(false) }
    var history by remember { mutableStateOf(listOf<CalculationHistory>()) }
    var justCalculated by remember { mutableStateOf(false) }

    // Load history on start
    LaunchedEffect(Unit) {
        history = historyManager.loadHistory()
    }

    // Save history locally when it changes
    LaunchedEffect(history) {
        if (history.isNotEmpty()) {
            historyManager.saveHistory(history)
        } else {
            historyManager.clearHistory()
        }
    }

    fun vibrate() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                vibratorManager?.defaultVibrator?.vibrate(
                    VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK)
                )
            } else {
                @Suppress("DEPRECATION")
                val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator?.vibrate(VibrationEffect.createOneShot(10, VibrationEffect.DEFAULT_AMPLITUDE))
                } else {
                    @Suppress("DEPRECATION")
                    vibrator?.vibrate(10)
                }
            }
        } catch (_: Exception) {
            // Silently ignore vibration errors
        }
    }

    fun updatePreview() {
        if (expression.isEmpty()) {
            displayResult = "0"
            return
        }
        try {
            val result = evaluateExpression(expression, isDegreeMode)
            if (!result.isNaN() && result.isFinite()) {
                displayResult = formatResult(result)
            }
        } catch (_: Exception) {
            // Don't update preview on parse errors
        }
    }

    fun appendToExpression(text: String) {
        if (justCalculated && text.first().isDigit()) {
            expression = text
            justCalculated = false
        } else {
            justCalculated = false
            expression += text
        }
        updatePreview()
    }

    fun calculate() {
        if (expression.isEmpty()) return
        try {
            val result = evaluateExpression(expression, isDegreeMode)
            val resultStr = formatResult(result)
            val expr = expression
            val newHistoryItem = CalculationHistory(expr, resultStr)
            history = listOf(newHistoryItem) + history.take(49)
            displayResult = resultStr
            expression = resultStr
            justCalculated = true

            // Sync to API
            scope.launch {
                historyManager.saveAndSync(expr, resultStr, "calc")
            }
        } catch (_: Exception) {
            displayResult = "Error"
        }
    }

    fun clear() {
        expression = ""
        displayResult = "0"
        justCalculated = false
    }

    fun backspace() {
        if (expression.isNotEmpty()) {
            val funcs = listOf("asin(", "acos(", "atan(", "sin(", "cos(", "tan(", "log(", "ln(", "√(")
            val removed = funcs.firstOrNull { expression.endsWith(it) }
            expression = if (removed != null) {
                expression.dropLast(removed.length)
            } else {
                expression.dropLast(1)
            }
            justCalculated = false
            updatePreview()
        }
    }

    fun toggleSign() {
        if (expression.isEmpty() || expression == "0") return
        expression = if (expression.startsWith("(-")) {
            expression.removePrefix("(-").let {
                if (it.endsWith(")")) it.dropLast(1) else it
            }
        } else {
            "(-$expression)"
        }
        updatePreview()
    }

    if (showHistory) {
        HistorySheet(
            history = history,
            onDismiss = { showHistory = false },
            onClear = {
                history = emptyList()
                scope.launch { historyManager.clearHistoryFromApi() }
            },
            onSelect = { item ->
                expression = item.result
                displayResult = item.result
                justCalculated = true
                showHistory = false
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F172A))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        // Display Area
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.22f),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 10.dp)
                    .pointerInput(Unit) {
                        detectHorizontalDragGestures { _, dragAmount ->
                            if (dragAmount < -20) {
                                backspace()
                                vibrate()
                            }
                        }
                    },
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.End
            ) {
                // Mode indicators row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        // DEG/RAD badge
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (isDegreeMode) Color(0xFF1E3A5F) else Color(0xFF4C1D95),
                            onClick = {
                                isDegreeMode = !isDegreeMode
                                vibrate()
                                updatePreview()
                            }
                        ) {
                            Text(
                                text = if (isDegreeMode) "DEG" else "RAD",
                                color = Color(0xFF38BDF8),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                        // Memory indicator
                        if (memory != 0.0) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color(0xFF1E293B)
                            ) {
                                Text(
                                    text = "M",
                                    color = Color(0xFFFBBF24),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                    // History button
                    IconButton(
                        onClick = {
                            vibrate()
                            showHistory = true
                        },
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

                // Expression
                Text(
                    text = expression.ifEmpty { " " },
                    color = Color(0xFF94A3B8),
                    fontSize = 16.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.End
                )

                // Result
                Text(
                    text = displayResult,
                    color = Color(0xFF38BDF8),
                    fontSize = 38.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.End
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Button Grid
        val buttons = if (isSecondMode) {
            listOf(
                "MC", "MR", "M+", "M-", "AC",
                "sin⁻¹", "cos⁻¹", "tan⁻¹", "√", "÷",
                "x²", "x³", "eˣ", "10ˣ", "×",
                "7", "8", "9", "(", "−",
                "4", "5", "6", ")", "+",
                "1", "2", "3", "%", "2nd",
                "0", ".", "±", "⌫", "="
            )
        } else {
            listOf(
                "MC", "MR", "M+", "M-", "AC",
                "sin", "cos", "tan", "√", "÷",
                "log", "ln", "^", "!", "×",
                "7", "8", "9", "(", "−",
                "4", "5", "6", ")", "+",
                "1", "2", "3", "%", "2nd",
                "0", ".", "±", "⌫", "="
            )
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(5),
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp),
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.78f)
        ) {
            items(buttons) { button ->
                CalculatorButton(
                    text = button,
                    isOperator = button in listOf("÷", "×", "−", "+", "="),
                    isFunction = button in listOf(
                        "sin", "cos", "tan", "log", "ln", "√", "^", "!",
                        "sin⁻¹", "cos⁻¹", "tan⁻¹", "x²", "x³", "eˣ", "10ˣ"
                    ),
                    isMemory = button in listOf("MC", "MR", "M+", "M-"),
                    isSecond = button == "2nd",
                    isSecondActive = isSecondMode,
                    isSpecial = button in listOf("⌫", "(", ")", "%"),
                    onClick = {
                        vibrate()
                        when (button) {
                            "AC" -> clear()
                            "=" -> calculate()
                            "2nd" -> isSecondMode = !isSecondMode
                            "⌫" -> backspace()
                            "±" -> toggleSign()
                            // Memory
                            "MC" -> memory = 0.0
                            "MR" -> {
                                val memStr = formatResult(memory)
                                appendToExpression(memStr)
                            }
                            "M+" -> {
                                try {
                                    val val2 = evaluateExpression(expression, isDegreeMode)
                                    if (!val2.isNaN()) memory += val2
                                } catch (_: Exception) {}
                            }
                            "M-" -> {
                                try {
                                    val val2 = evaluateExpression(expression, isDegreeMode)
                                    if (!val2.isNaN()) memory -= val2
                                } catch (_: Exception) {}
                            }
                            // Operators
                            "÷" -> appendToExpression("÷")
                            "×" -> appendToExpression("×")
                            "−" -> appendToExpression("-")
                            "+" -> appendToExpression("+")
                            "^" -> appendToExpression("^")
                            "%" -> appendToExpression("%")
                            // Parentheses
                            "(" -> appendToExpression("(")
                            ")" -> appendToExpression(")")
                            // Functions
                            "sin" -> appendToExpression("sin(")
                            "cos" -> appendToExpression("cos(")
                            "tan" -> appendToExpression("tan(")
                            "sin⁻¹" -> appendToExpression("asin(")
                            "cos⁻¹" -> appendToExpression("acos(")
                            "tan⁻¹" -> appendToExpression("atan(")
                            "log" -> appendToExpression("log(")
                            "ln" -> appendToExpression("ln(")
                            "√" -> appendToExpression("√(")
                            "x²" -> appendToExpression("²")
                            "x³" -> appendToExpression("³")
                            "eˣ" -> appendToExpression("e^(")
                            "10ˣ" -> appendToExpression("10^(")
                            "!" -> appendToExpression("!")
                            // Constants
                            "π" -> appendToExpression("π")
                            "e" -> appendToExpression("e")
                            // Numbers & decimal
                            else -> appendToExpression(button)
                        }
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistorySheet(
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
                    "Calculation History",
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

            Spacer(modifier = Modifier.height(16.dp))

            if (history.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("📋", fontSize = 40.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "No history yet",
                            color = Color(0xFF64748B),
                            fontSize = 16.sp
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.heightIn(max = 400.dp)
                ) {
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
                                Text(
                                    item.expression,
                                    color = Color(0xFF94A3B8),
                                    fontSize = 14.sp
                                )
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

@Composable
fun CalculatorButton(
    text: String,
    isOperator: Boolean = false,
    isFunction: Boolean = false,
    isMemory: Boolean = false,
    isSecond: Boolean = false,
    isSecondActive: Boolean = false,
    isSpecial: Boolean = false,
    onClick: () -> Unit
) {
    val backgroundColor = when {
        isOperator && text == "=" -> Brush.linearGradient(listOf(Color(0xFF0EA5E9), Color(0xFF38BDF8)))
        isOperator -> Brush.linearGradient(listOf(Color(0xFF1E3A5F), Color(0xFF1E3A5F)))
        isFunction -> Brush.linearGradient(listOf(Color(0xFF172554), Color(0xFF172554)))
        isMemory -> Brush.linearGradient(listOf(Color(0xFF172554), Color(0xFF172554)))
        isSecond && isSecondActive -> Brush.linearGradient(listOf(Color(0xFFEF4444), Color(0xFFF87171)))
        isSecond -> Brush.linearGradient(listOf(Color(0xFF4C1D95), Color(0xFF6D28D9)))
        isSpecial -> Brush.linearGradient(listOf(Color(0xFF1E293B), Color(0xFF1E293B)))
        text == "AC" -> Brush.linearGradient(listOf(Color(0xFFEF4444), Color(0xFFF87171)))
        else -> Brush.linearGradient(listOf(Color(0xFF1E293B), Color(0xFF1E293B)))
    }

    val textColor = when {
        text == "=" -> Color.White
        isOperator -> Color(0xFF38BDF8)
        isFunction -> Color(0xFF34D399)
        isMemory -> Color(0xFFFBBF24)
        text == "AC" -> Color.White
        isSpecial -> Color(0xFF38BDF8)
        else -> Color(0xFFE2E8F0)
    }

    Button(
        onClick = onClick,
        modifier = Modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(14.dp)),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        contentPadding = PaddingValues(2.dp),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor, RoundedCornerShape(14.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                color = textColor,
                fontSize = when {
                    text.length > 4 -> 10.sp
                    text.length > 3 -> 11.sp
                    text.length > 2 -> 12.sp
                    else -> 14.sp
                },
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                maxLines = 1
            )
        }
    }
}

// ==================== Expression Parser ====================

private fun evaluateExpression(expr: String, isDegree: Boolean): Double {
    if (expr.isBlank()) return 0.0

    // Pre-process the expression
    var processed = expr
        .replace("×", "*")
        .replace("÷", "/")
        .replace("−", "-")
        .replace("π", PI.toString())
        .replace("²", "^2")
        .replace("³", "^3")

    // Replace standalone 'e' (not in function names) with Euler's number
    processed = processed.replace(Regex("(?<![a-zA-Z])e(?![a-zA-Z^])"), E.toString())

    val parser = ExpressionParser(processed, isDegree)
    return parser.parse()
}

private class ExpressionParser(private val input: String, private val isDegree: Boolean) {
    private var pos = 0

    fun parse(): Double {
        val result = parseExpression()
        return result
    }

    private fun parseExpression(): Double {
        var result = parseTerm()
        while (pos < input.length) {
            when (input[pos]) {
                '+' -> { pos++; result += parseTerm() }
                '-' -> { pos++; result -= parseTerm() }
                else -> break
            }
        }
        return result
    }

    private fun parseTerm(): Double {
        var result = parsePower()
        while (pos < input.length) {
            when (input[pos]) {
                '*' -> { pos++; result *= parsePower() }
                '/' -> { pos++; val d = parsePower(); result = if (d != 0.0) result / d else Double.NaN }
                '%' -> { pos++; result = result / 100.0 }
                else -> break
            }
        }
        return result
    }

    private fun parsePower(): Double {
        var result = parseUnaryPostfix()
        while (pos < input.length && input[pos] == '^') {
            pos++ // skip ^
            val exponent = parseUnaryPostfix()
            result = result.pow(exponent)
        }
        return result
    }

    private fun parseUnaryPostfix(): Double {
        var result = parseUnary()
        // Handle factorial
        while (pos < input.length && input[pos] == '!') {
            pos++
            result = factorial(result.toInt()).toDouble()
        }
        return result
    }

    private fun parseUnary(): Double {
        if (pos < input.length && input[pos] == '-') {
            pos++
            return -parseUnary()
        }
        if (pos < input.length && input[pos] == '+') {
            pos++
            return parseUnary()
        }
        return parsePrimary()
    }

    private fun parsePrimary(): Double {
        // Parentheses
        if (pos < input.length && input[pos] == '(') {
            pos++ // skip (
            val result = parseExpression()
            if (pos < input.length && input[pos] == ')') pos++ // skip )
            return result
        }

        // Functions
        val functions = listOf("asin", "acos", "atan", "sin", "cos", "tan", "log", "ln", "√")
        for (func in functions) {
            if (input.startsWith(func, pos)) {
                pos += func.length
                // Expect opening parenthesis
                if (pos < input.length && input[pos] == '(') {
                    pos++ // skip (
                    val arg = parseExpression()
                    if (pos < input.length && input[pos] == ')') pos++ // skip )
                    return applyFunction(func, arg)
                } else {
                    // No parenthesis, apply to next primary
                    val arg = parsePrimary()
                    return applyFunction(func, arg)
                }
            }
        }

        // Numbers
        val start = pos
        var hasDecimal = false
        if (pos < input.length && (input[pos].isDigit() || input[pos] == '.')) {
            while (pos < input.length && (input[pos].isDigit() || (input[pos] == '.' && !hasDecimal))) {
                if (input[pos] == '.') hasDecimal = true
                pos++
            }
            return input.substring(start, pos).toDoubleOrNull() ?: 0.0
        }

        return 0.0
    }

    private fun applyFunction(func: String, value: Double): Double {
        return when (func) {
            "sin" -> if (isDegree) sin(Math.toRadians(value)) else sin(value)
            "cos" -> if (isDegree) cos(Math.toRadians(value)) else cos(value)
            "tan" -> if (isDegree) tan(Math.toRadians(value)) else tan(value)
            "asin" -> if (isDegree) Math.toDegrees(asin(value)) else asin(value)
            "acos" -> if (isDegree) Math.toDegrees(acos(value)) else acos(value)
            "atan" -> if (isDegree) Math.toDegrees(atan(value)) else atan(value)
            "log" -> log10(value)
            "ln" -> ln(value)
            "√" -> sqrt(value)
            else -> value
        }
    }
}

private fun factorial(n: Int): Long {
    if (n < 0) return 0
    return if (n <= 1) 1 else n.toLong() * factorial(n - 1)
}

private fun formatResult(value: Double): String {
    if (value.isNaN()) return "Error"
    if (value.isInfinite()) return if (value > 0) "∞" else "-∞"
    return if (value == value.toLong().toDouble() && abs(value) < 1e15) {
        value.toLong().toString()
    } else {
        String.format("%.10f", value).trimEnd('0').trimEnd('.')
    }
}
