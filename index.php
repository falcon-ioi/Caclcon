<?php
session_start();
require 'koneksi.php';

if (!isset($_SESSION['user_id'])) {
    header("Location: login.php");
    exit();
}

$user_id = $_SESSION['user_id'];
$username = $_SESSION['username'];

// Ambil riwayat
$query = "SELECT * FROM riwayat WHERE user_id = $user_id ORDER BY waktu DESC LIMIT 10";
$result = mysqli_query($conn, $query);
?>

<!DOCTYPE html>
<html lang="id">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard - Kalkulator & Konverter</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>

<header>
    <h1>Web Tools</h1>
    <div class="user-info">
        <span>Halo, <b><?= htmlspecialchars($username) ?></b></span>
        <a href="logout.php" class="btn-logout">Logout</a>
    </div>
</header>

<div class="container">
    <!-- Tab Navigation -->
    <div class="tabs">
        <button class="tab-btn active" onclick="switchTab('calc')">
            <span class="icon">🖩</span> Kalkulator
        </button>
        <button class="tab-btn" onclick="switchTab('conv')">
            <span class="icon">↹</span> Konverter
        </button>
    </div>

    <!-- Kalkulator Panel -->
    <div class="card panel" id="calc-panel">
        <h2>Kalkulator Ilmiah</h2>
        <!-- Top Controls -->
        <div class="calc-controls" style="display: flex; justify-content: space-between; margin-bottom: 10px;">
            <button class="calc-btn-sm" id="btn-2nd" onclick="toggleSecondary()">2nd</button>
            <button class="calc-btn-sm" id="btn-mode" onclick="toggleMode()">DEG</button>
            <div class="mem-controls">
                <button class="calc-btn-sm" onclick="memoryClear()">MC</button>
                <button class="calc-btn-sm" onclick="memoryRead()">MR</button>
                <button class="calc-btn-sm" onclick="memoryAdd()">M+</button>
                <button class="calc-btn-sm" onclick="memorySub()">M-</button>
            </div>
        </div>

        <input type="text" class="calc-display" id="calc-display" readonly>
        
        <div class="calc-grid scientific">
            <!-- Row 1 -->
            <button class="calc-btn secondary" onclick="clearDisplay()">AC</button>
            <button class="calc-btn secondary" onclick="backspace()">C</button>
            <button class="calc-btn secondary" onclick="backspace()">&#9003;</button>
            <button class="calc-btn secondary" onclick="appendOperator('(')">(</button>
            <button class="calc-btn secondary" onclick="appendOperator(')')">)</button>
            
            <!-- Row 2 -->
            <button class="calc-btn func" id="btn-sin" onclick="appendFunction('sin')">sin</button>
            <button class="calc-btn func" id="btn-cos" onclick="appendFunction('cos')">cos</button>
            <button class="calc-btn func" id="btn-tan" onclick="appendFunction('tan')">tan</button>
            <button class="calc-btn func" id="btn-ln" onclick="appendFunction('ln')">ln</button>
            <button class="calc-btn func" id="btn-log" onclick="appendFunction('log')">log</button>

            <!-- Row 3 -->
            <button class="calc-btn func" onclick="appendOperator('^')">xʸ</button>
            <button class="calc-btn func" id="btn-sqrt" onclick="appendFunction('sqrt')">&radic;x</button>
            <button class="calc-btn func" onclick="appendFunction('cbrt')">³&radic;x</button>
            <button class="calc-btn func" onclick="appendNumber(Math.PI.toFixed(8))">&pi;</button>
            <button class="calc-btn func" onclick="appendNumber(Math.E.toFixed(8))">e</button>

            <!-- Row 4 -->
            <button class="calc-btn" onclick="appendNumber('7')">7</button>
            <button class="calc-btn" onclick="appendNumber('8')">8</button>
            <button class="calc-btn" onclick="appendNumber('9')">9</button>
            <button class="calc-btn operator" onclick="appendOperator('*')">&times;</button>
            <button class="calc-btn operator" onclick="appendOperator('/')">/</button>

            <!-- Row 5 -->
            <button class="calc-btn" onclick="appendNumber('4')">4</button>
            <button class="calc-btn" onclick="appendNumber('5')">5</button>
            <button class="calc-btn" onclick="appendNumber('6')">6</button>
            <button class="calc-btn operator" onclick="appendOperator('+')">+</button>
            <button class="calc-btn operator" onclick="appendOperator('-')">-</button>

            <!-- Row 6 -->
            <button class="calc-btn" onclick="appendNumber('1')">1</button>
            <button class="calc-btn" onclick="appendNumber('2')">2</button>
            <button class="calc-btn" onclick="appendNumber('3')">3</button>
            <button class="calc-btn func" onclick="appendOperator('E')">EXP</button>
            <button class="calc-btn func" onclick="appendOperator('^')">x²</button>

            <!-- Row 7 -->
            <button class="calc-btn" onclick="appendNumber('0')">0</button>
            <button class="calc-btn" onclick="appendNumber('00')">00</button>
            <button class="calc-btn" onclick="appendPoint()">.</button>
            <button class="calc-btn func" onclick="appendOperator('%')">%</button>
            <button class="calc-btn equal" onclick="calculate()">=</button>
        </div>
    </div>

    <!-- Konverter Panel -->
    <div class="card panel" id="conv-panel" style="display: none;">
        <h2 style="text-align: center; margin-bottom: 2rem;">Konverter Satuan Pro</h2>
        
        <!-- Type Selector Centered -->
        <div class="conv-type-selector">
            <select id="conv-type" onchange="updateUnits()">
                <option value="length">Panjang (Length)</option>
                <option value="weight">Berat (Weight)</option>
                <option value="temperature">Suhu (Temperature)</option>
                <option value="time">Waktu (Time)</option>
                <option value="data">Data Digital</option>
                <option value="speed">Kecepatan (Speed)</option>
            </select>
        </div>

        <div class="conv-container">
            <!-- Input Card -->
            <div class="conv-box">
                <div class="conv-header">Input</div>
                <input type="number" id="conv-value" placeholder="0" oninput="convert()">
                <select id="conv-from" onchange="convert()"></select>
            </div>

            <!-- Swap Icon -->
            <div class="conv-swap">
                <span>&#8644;</span>
            </div>

            <!-- Output Card -->
            <div class="conv-box output">
                <div class="conv-header">Result</div>
                <div class="result-display" id="conv-result-val">0</div>
                <select id="conv-to" onchange="convert()"></select>
            </div>
        </div>
        
        <!-- Hidden legacy button, logic now on input change, but keeping for script compatibility if needed, or remove -->
        <!-- <button class="btn-convert" onclick="convert()">Konversi</button> -->
        
        <p class="result-detail" id="conv-result">...</p>
    </div>
</div>

<!-- Riwayat -->
<div class="history-section">
    <h2>Riwayat Aktivitas Anda</h2>
    <table>
        <thead>
            <tr>
                <th>Operasi / Konversi</th>
                <th>Waktu</th>
            </tr>
        </thead>
        <tbody id="history-body">
            <?php while ($row = mysqli_fetch_assoc($result)) : ?>
            <tr>
                <td><?= htmlspecialchars($row['operasi']) ?></td>
                <td><?= $row['waktu'] ?></td>
            </tr>
            <?php endwhile; ?>
        </tbody>
    </table>
</div>

<script src="script.js"></script>

</body>
</html>
