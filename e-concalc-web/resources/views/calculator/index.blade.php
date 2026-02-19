@extends('layouts.app')

@section('title', 'E-Concalc - Electronic Conversion Calculator')

@section('styles')
<meta name="theme-color" content="#4facfe">
<meta name="description" content="E-Concalc - Kalkulator ilmiah, konverter satuan, dan mata uang">
<link rel="apple-touch-icon" href="{{ asset('images/logo.png') }}">
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<style>
    /* Simplification: Removed styles for plans and charts */
    .history-section { margin-top: 2rem; background: rgba(0,0,0,0.2); padding: 15px; border-radius: 12px; }
</style>
@endsection

@section('content')
<script>
    if ('serviceWorker' in navigator) {
        window.addEventListener('load', () => {
            navigator.serviceWorker.register('/sw.js')
                .then(reg => console.log('SW registered'))
                .catch(err => console.log('SW registration failed:', err));
        });
    }
</script>

<header>
    <h1>E-Concalc</h1>
    <div class="user-info">
        <!-- Login/Register buttons removed -->
    </div>
</header>

<div class="container">
    <!-- Tab Navigation -->
    <div class="tabs">
        <button class="tab-btn active" onclick="switchTab('calc')">
            <span class="icon">🖩</span> Kalkulator
        </button>
        <button class="tab-btn" onclick="switchTab('conv')">
            <span class="icon">📐</span> Konverter
        </button>
        <button class="tab-btn" onclick="switchTab('currency')">
            <span class="icon">💱</span> Mata Uang
        </button>
    </div>

    <!-- Kalkulator Panel -->
    <div class="card panel" id="calc-panel">
        <h2>Kalkulator Ilmiah</h2>
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
            <button class="calc-btn secondary" onclick="clearDisplay()">AC</button>
            <button class="calc-btn secondary" onclick="backspace()">C</button>
            <button class="calc-btn secondary" onclick="backspace()">&#9003;</button>
            <button class="calc-btn secondary" onclick="appendOperator('(')">(</button>
            <button class="calc-btn secondary" onclick="appendOperator(')')">)</button>
            
            <button class="calc-btn func" id="btn-sin" onclick="appendFunction('sin')">sin</button>
            <button class="calc-btn func" id="btn-cos" onclick="appendFunction('cos')">cos</button>
            <button class="calc-btn func" id="btn-tan" onclick="appendFunction('tan')">tan</button>
            <button class="calc-btn func" id="btn-ln" onclick="appendFunction('ln')">ln</button>
            <button class="calc-btn func" id="btn-log" onclick="appendFunction('log')">log</button>

            <button class="calc-btn func" onclick="appendOperator('^')">xʸ</button>
            <button class="calc-btn func" id="btn-sqrt" onclick="appendFunction('sqrt')">√x</button>
            <button class="calc-btn func" onclick="appendFunction('cbrt')">³√x</button>
            <button class="calc-btn func" onclick="appendNumber(Math.PI.toFixed(8))">π</button>
            <button class="calc-btn func" onclick="appendNumber(Math.E.toFixed(8))">e</button>

            <button class="calc-btn" onclick="appendNumber('7')">7</button>
            <button class="calc-btn" onclick="appendNumber('8')">8</button>
            <button class="calc-btn" onclick="appendNumber('9')">9</button>
            <button class="calc-btn operator" onclick="appendOperator('*')">×</button>
            <button class="calc-btn operator" onclick="appendOperator('/')">/</button>

            <button class="calc-btn" onclick="appendNumber('4')">4</button>
            <button class="calc-btn" onclick="appendNumber('5')">5</button>
            <button class="calc-btn" onclick="appendNumber('6')">6</button>
            <button class="calc-btn operator" onclick="appendOperator('+')">+</button>
            <button class="calc-btn operator" onclick="appendOperator('-')">-</button>

            <button class="calc-btn" onclick="appendNumber('1')">1</button>
            <button class="calc-btn" onclick="appendNumber('2')">2</button>
            <button class="calc-btn" onclick="appendNumber('3')">3</button>
            <button class="calc-btn func" onclick="appendOperator('E')">EXP</button>
            <button class="calc-btn func" onclick="appendOperator('^')">x²</button>

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
        
        <div class="conv-type-selector">
            <select id="conv-type" onchange="updateUnits()">
                <option value="length">Panjang (Length)</option>
                <option value="weight">Berat (Weight)</option>
                <option value="temperature">Suhu (Temperature)</option>
                <option value="time">Waktu (Time)</option>
                <option value="data">Data Digital</option>
                <option value="speed">Kecepatan (Speed)</option>
                <option value="area">Luas (Area)</option>
                <option value="volume">Volume</option>
                <option value="pressure">Tekanan (Pressure)</option>
                <option value="energy">Energi (Energy)</option>
                <option value="force">Gaya (Force)</option>
                <option value="angle">Sudut (Angle)</option>
                <option value="frequency">Frekuensi (Frequency)</option>
                <option value="power">Daya (Power)</option>
            </select>
        </div>

        <div class="conv-container">
            <div class="conv-box">
                <div class="conv-header">Input</div>
                <input type="number" id="conv-value" placeholder="0" oninput="convert()">
                <select id="conv-from" onchange="convert()"></select>
            </div>

            <div class="conv-swap" onclick="swapUnits()">
                <span>&#8644;</span>
            </div>

            <div class="conv-box output">
                <div class="conv-header">Result</div>
                <div class="result-display" id="conv-result-val">0</div>
                <select id="conv-to" onchange="convert()"></select>
            </div>
        </div>
        
        <p class="result-detail" id="conv-result">...</p>
    </div>

    <!-- Currency Panel -->
    <div class="card panel" id="currency-panel" style="display: none;">
        <h2 style="text-align: center; margin-bottom: 1rem;">💱 Konverter Mata Uang</h2>
        <p id="curr-status" style="text-align: center; color: #94a3b8; margin-bottom: 2rem;">Memuat kurs...</p>
        
        <div class="conv-container">
            <div class="conv-box">
                <div class="conv-header">Dari</div>
                <input type="number" id="curr-value" placeholder="0" oninput="convertCurrency()">
                <select id="curr-from" onchange="convertCurrency()"></select>
            </div>

            <div class="conv-swap" onclick="swapCurrencies()">
                <span>&#8644;</span>
            </div>

            <div class="conv-box output">
                <div class="conv-header">Ke</div>
                <div class="result-display" id="curr-result-val">0</div>
                <select id="curr-to" onchange="convertCurrency()"></select>
            </div>
        </div>
        
        <p id="curr-rate" style="text-align: center; color: #94a3b8; margin-top: 1.5rem;"></p>
        <button class="btn-refresh" onclick="refreshRates()">🔄 Refresh Kurs</button>
    </div>
</div>

<!-- Riwayat -->
<div class="history-section">
    <div class="history-header">
        <h2>Riwayat Aktivitas Anda</h2>
        <div class="export-buttons">
            <button onclick="clearHistory()" class="btn-export" style="background-color: #ef4444;">🗑️ Hapus Riwayat</button>
        </div>
    </div>
    <table>
        <thead>
            <tr>
                <th>Operasi / Konversi</th>
                <th>Waktu</th>
            </tr>
        </thead>
        <tbody id="history-body">
            <!-- Populated by JS -->
        </tbody>
    </table>
</div>

<!-- Theme Toggle -->
<button class="theme-toggle" onclick="toggleTheme()" title="Toggle Dark/Light Mode">
    <span id="theme-icon">🌙</span>
</button>
@endsection

@section('scripts')
<script>
    // Removed Auth checks
</script>
<script src="{{ asset('js/script.js') }}?v={{ time() }}"></script>
<script src="{{ asset('js/currency.js') }}?v={{ time() }}"></script>
<!-- Removed financial.js and health.js -->
@endsection
