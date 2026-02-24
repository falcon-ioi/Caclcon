@extends('layouts.app')

@section('title', 'E-Concalc - Electronic Conversion Calculator')

@section('styles')
<meta name="theme-color" content="#4facfe">
<meta name="description" content="E-Concalc - Kalkulator ilmiah, konverter satuan, dan mata uang">
<link rel="apple-touch-icon" href="{{ asset('images/logo.png') }}">
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<style>
    .history-section { margin-top: 2rem; background: rgba(0,0,0,0.2); padding: 15px; border-radius: 12px; }

    /* === Splash Screen === */
    .splash-overlay {
        position: fixed;
        top: 0; left: 0;
        width: 100%; height: 100%;
        background: #0f172a;
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;
        z-index: 9999;
        transition: opacity 0.5s ease, visibility 0.5s ease;
    }

    .splash-overlay.fade-out {
        opacity: 0;
        visibility: hidden;
    }

    .splash-logo {
        width: 90px;
        height: 90px;
        background: linear-gradient(135deg, #4facfe, #00f2fe);
        border-radius: 24px;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 42px;
        animation: splashPop 0.6s cubic-bezier(0.16, 1, 0.3, 1) forwards;
        box-shadow: 0 10px 40px rgba(79, 172, 254, 0.3);
    }

    .splash-title {
        font-size: 1.8rem;
        font-weight: 700;
        color: #00d4ff;
        margin-top: 16px;
        opacity: 0;
        animation: splashFadeIn 0.5s ease 0.3s forwards;
    }

    .splash-tagline {
        color: #475569;
        font-size: 0.85rem;
        margin-top: 6px;
        opacity: 0;
        animation: splashFadeIn 0.5s ease 0.5s forwards;
    }

    .splash-bar {
        width: 120px;
        height: 3px;
        background: rgba(255,255,255,0.08);
        border-radius: 3px;
        margin-top: 28px;
        overflow: hidden;
        opacity: 0;
        animation: splashFadeIn 0.3s ease 0.6s forwards;
    }

    .splash-bar-fill {
        width: 0;
        height: 100%;
        background: linear-gradient(90deg, #4facfe, #00f2fe);
        border-radius: 3px;
        animation: splashLoad 1.2s ease 0.7s forwards;
    }

    @keyframes splashPop {
        from { transform: scale(0.5); opacity: 0; }
        to { transform: scale(1); opacity: 1; }
    }

    @keyframes splashFadeIn {
        from { opacity: 0; transform: translateY(8px); }
        to { opacity: 1; transform: translateY(0); }
    }

    @keyframes splashLoad {
        to { width: 100%; }
    }
</style>
@endsection

@section('content')
{{-- Splash Screen --}}
<div class="splash-overlay" id="splash-screen">
    <div class="splash-logo">🖩</div>
    <div class="splash-title">E-Concalc</div>
    <div class="splash-tagline">Electronic Conversion Calculator</div>
    <div class="splash-bar"><div class="splash-bar-fill"></div></div>
</div>

<script>
    // Show splash only once per session
    (function() {
        const splash = document.getElementById('splash-screen');
        if (sessionStorage.getItem('econcalc_splash_shown')) {
            splash.style.display = 'none';
        } else {
            sessionStorage.setItem('econcalc_splash_shown', '1');
            setTimeout(function() {
                splash.classList.add('fade-out');
                setTimeout(function() { splash.style.display = 'none'; }, 500);
            }, 2000);
        }
    })();

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
        @auth
            <span style="color: #94a3b8; font-size: 0.85rem; margin-right: 10px;">👤 {{ Auth::user()->name }}</span>
            <form method="POST" action="{{ route('logout') }}" style="display: inline;">
                @csrf
                <button type="submit" style="background: rgba(239,68,68,0.2); border: 1px solid rgba(239,68,68,0.3); color: #fca5a5; padding: 6px 14px; border-radius: 8px; cursor: pointer; font-family: 'Poppins', sans-serif; font-size: 0.8rem;">Logout</button>
            </form>
        @else
            <a href="{{ route('login') }}" style="background: linear-gradient(135deg, #4facfe, #00f2fe); color: #0f172a; padding: 6px 14px; border-radius: 8px; text-decoration: none; font-size: 0.8rem; font-weight: 600;">Login</a>
        @endauth
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
    // Pass auth state to JS
    window.AUTH = {
        isLoggedIn: {{ Auth::check() ? 'true' : 'false' }},
        user: {!! Auth::check() ? json_encode(['id' => Auth::id(), 'name' => Auth::user()->name]) : 'null' !!},
        csrfToken: '{{ csrf_token() }}'
    };
</script>
<script src="{{ asset('js/script.js') }}?v={{ time() }}"></script>
<script src="{{ asset('js/currency.js') }}?v={{ time() }}"></script>
@endsection
