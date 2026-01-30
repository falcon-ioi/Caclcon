@extends('layouts.app')

@section('title', 'E-Concalc - Electronic Conversion Calculator')

@section('styles')
<meta name="theme-color" content="#4facfe">
<meta name="description" content="E-Concalc - Kalkulator ilmiah, konverter satuan, mata uang, finansial, dan kesehatan">
<link rel="apple-touch-icon" href="{{ asset('logo.png') }}">
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<style>
    .saved-plans { margin-top: 2rem; border-top: 1px solid rgba(255,255,255,0.1); padding-top: 1rem; }
    .plan-item { background: rgba(255,255,255,0.05); padding: 10px; margin-bottom: 10px; border-radius: 8px; display: flex; justify-content: space-between; align-items: center; }
    .plan-info { display: flex; flex-direction: column; }
    .plan-title { font-weight: bold; }
    .plan-date { font-size: 0.8rem; opacity: 0.7; }
    .btn-delete { background: #ef4444; color: white; border: none; padding: 5px 10px; border-radius: 5px; cursor: pointer; }
    .chart-container { margin-top: 2rem; background: rgba(255,255,255,0.05); padding: 15px; border-radius: 12px; }
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
        <span>Halo, <b>{{ $username }}</b></span>
        <form action="{{ route('logout') }}" method="POST" style="display: inline;">
            @csrf
            <button type="submit" class="btn-logout">Logout</button>
        </form>
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
        <button class="tab-btn" onclick="switchTab('finance')">
            <span class="icon">💰</span> Finansial
        </button>
        <button class="tab-btn" onclick="switchTab('health')">
            <span class="icon">🏥</span> Kesehatan
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
                <option value="length">📏 Panjang (Length)</option>
                <option value="weight">⚖️ Berat (Weight)</option>
                <option value="temperature">🌡️ Suhu (Temperature)</option>
                <option value="time">⏱️ Waktu (Time)</option>
                <option value="data">💾 Data Digital</option>
                <option value="speed">🚀 Kecepatan (Speed)</option>
                <option value="area">📐 Luas (Area)</option>
                <option value="volume">🧪 Volume</option>
                <option value="pressure">🔧 Tekanan (Pressure)</option>
                <option value="energy">⚡ Energi (Energy)</option>
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

    <!-- Financial Panel -->
    <div class="card panel" id="finance-panel" style="display: none;">
        <h2 style="text-align: center; margin-bottom: 1.5rem;">💰 Kalkulator Finansial</h2>
        
        <div class="sub-tabs">
            <button class="sub-tab-btn active" onclick="switchFinanceTab('simple')">Bunga Sederhana</button>
            <button class="sub-tab-btn" onclick="switchFinanceTab('compound')">Bunga Majemuk</button>
            <button class="sub-tab-btn" onclick="switchFinanceTab('loan')">Cicilan</button>
            <button class="sub-tab-btn" onclick="switchFinanceTab('discount')">Diskon</button>
        </div>

        <div id="finance-simple" class="finance-form">
            <div class="input-group">
                <label>Modal Awal (Rp)</label>
                <input type="number" id="fi-principal" placeholder="1000000">
            </div>
            <div class="input-group">
                <label>Suku Bunga (%/tahun)</label>
                <input type="number" id="fi-rate" placeholder="5">
            </div>
            <div class="input-group">
                <label>Jangka Waktu (tahun)</label>
                <input type="number" id="fi-time" placeholder="1">
            </div>
            <button class="btn-primary" onclick="calculateSimpleInterest()">Hitung</button>
            <div id="fi-result" class="calc-result"></div>
        </div>

        <div id="finance-compound" class="finance-form" style="display:none;">
            <div class="input-group">
                <label>Modal Awal (Rp)</label>
                <input type="number" id="ci-principal" placeholder="1000000">
            </div>
            <div class="input-group">
                <label>Suku Bunga (%/tahun)</label>
                <input type="number" id="ci-rate" placeholder="5">
            </div>
            <div class="input-group">
                <label>Jangka Waktu (tahun)</label>
                <input type="number" id="ci-time" placeholder="1">
            </div>
            <div class="input-group">
                <label>Frekuensi Bunga/Tahun</label>
                <select id="ci-compound">
                    <option value="1">Tahunan (1x)</option>
                    <option value="4">Kuartalan (4x)</option>
                    <option value="12" selected>Bulanan (12x)</option>
                    <option value="365">Harian (365x)</option>
                </select>
            </div>
            <button class="btn-primary" onclick="calculateCompoundInterest()">Hitung</button>
            <div id="ci-result" class="calc-result"></div>
        </div>

        <div id="finance-loan" class="finance-form" style="display:none;">
            <div class="input-group">
                <label>Jumlah Pinjaman (Rp)</label>
                <input type="number" id="loan-principal" placeholder="100000000">
            </div>
            <div class="input-group">
                <label>Suku Bunga (%/tahun)</label>
                <input type="number" id="loan-rate" placeholder="10">
            </div>
            <div class="input-group">
                <label>Tenor (bulan)</label>
                <input type="number" id="loan-months" placeholder="12">
            </div>
            <button class="btn-primary" onclick="calculateLoan()">Hitung Cicilan</button>
            <div id="loan-result" class="calc-result"></div>
        </div>

        <div id="finance-discount" class="finance-form" style="display:none;">
            <div class="input-group">
                <label>Harga Asli (Rp)</label>
                <input type="number" id="disc-original" placeholder="500000">
            </div>
            <div class="input-group">
                <label>Diskon (%)</label>
                <input type="number" id="disc-percent" placeholder="20">
            </div>
            <button class="btn-primary" onclick="calculateDiscount()">Hitung Diskon</button>
            <div id="disc-result" class="calc-result"></div>
        </div>

        <!-- Save Plan Section -->
        <div class="input-group" style="margin-top: 20px; border-top: 1px solid #334155; padding-top: 20px;">
            <label>Simpan Perhitungan Ini</label>
            <div style="display: flex; gap: 10px;">
                <input type="text" id="plan-title" placeholder="Nama Rencana (mis: KPR Rumah)" style="flex: 1;">
                <button class="btn-primary" onclick="saveFinancialPlan()" style="margin: 0; width: auto;">💾 Simpan</button>
            </div>
        </div>

        <!-- Saved Plans List -->
        <div class="saved-plans" style="position: relative;">
            <div id="save-toast" class="save-toast">
                <span>✅</span> Rencana Tersimpan!
            </div>
            
            <h3>📂 Rencana Tersimpan</h3>
            <div id="plans-list">
                @foreach($financialPlans as $plan)
                <div class="plan-item" id="plan-{{ $plan->id }}">
                    <div class="plan-info">
                        <span class="plan-title">{{ $plan->title }}</span>
                        <span class="plan-date">{{ $plan->type }} - {{ $plan->created_at->format('d M Y') }}</span>
                    </div>
                    <button class="btn-delete" onclick="deletePlan({{ $plan->id }})">Hapus</button>
                </div>
                @endforeach
            </div>
        </div>
    </div>

    <!-- Health Panel -->
    <div class="card panel" id="health-panel" style="display: none;">
        <h2 style="text-align: center; margin-bottom: 2rem;">🏥 Kalkulator Kesehatan</h2>
        
        <div class="health-section">
            <h3>Kalkulator BMI</h3>
            <div class="input-group">
                <label>Sistem Satuan</label>
                <select id="bmi-unit" onchange="updateBMILabels()">
                    <option value="metric">Metrik (kg, cm)</option>
                    <option value="imperial">Imperial (lb, inch)</option>
                </select>
            </div>
            <div class="input-group">
                <label id="weight-label">Berat (kg)</label>
                <input type="number" id="bmi-weight" placeholder="70">
            </div>
            <div class="input-group">
                <label id="height-label">Tinggi (cm)</label>
                <input type="number" id="bmi-height" placeholder="170">
            </div>
            <button class="btn-primary" onclick="calculateBMI()">Hitung BMI</button>
            <div id="bmi-result" class="calc-result"></div>
            
            <button class="btn-primary" onclick="saveBMI()" style="margin-top: 10px; background: #10b981;">💾 Simpan ke Log Kesehatan</button>

            <div class="chart-container">
                <canvas id="healthChart"></canvas>
            </div>
        </div>

        <hr style="border-color: rgba(255,255,255,0.1); margin: 2rem 0;">

        <div class="health-section">
            <h3>Kalkulator Berat Badan Ideal</h3>
            <div class="input-group">
                <label>Tinggi Badan (cm)</label>
                <input type="number" id="ideal-height" placeholder="170">
            </div>
            <div class="input-group">
                <label>Jenis Kelamin</label>
                <select id="ideal-gender">
                    <option value="male">Pria</option>
                    <option value="female">Wanita</option>
                </select>
            </div>
            <button class="btn-primary" onclick="calculateIdealWeight()">Hitung Berat Ideal</button>
            <div id="ideal-result" class="calc-result"></div>
        </div>
    </div>
</div>

<!-- Riwayat -->
<div class="history-section">
    <div class="history-header">
        <h2>Riwayat Aktivitas Anda</h2>
        <div class="export-buttons">
            <a href="{{ route('export', 'csv') }}" class="btn-export">📄 Export CSV</a>
            <a href="{{ route('export', 'pdf') }}" class="btn-export">📑 Export PDF</a>
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
            @foreach($riwayat as $row)
            <tr>
                <td>{{ $row->operasi }}</td>
                <td>{{ $row->created_at }}</td>
            </tr>
            @endforeach
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
    window.healthData = @json($healthLogs);
    window.userSettings = @json($settings);
</script>
<script src="{{ asset('js/script.js') }}"></script>
<script src="{{ asset('js/financial.js') }}"></script>
<script src="{{ asset('js/health.js') }}"></script>
<script src="{{ asset('js/currency.js') }}"></script>
@endsection
