// --- Tab Logic ---
function switchTab(tab) {
    const panels = {
        calc: document.getElementById('calc-panel'),
        conv: document.getElementById('conv-panel'),
        currency: document.getElementById('currency-panel')
    };

    // Hide all panels
    Object.values(panels).forEach(panel => {
        if (panel) panel.style.display = 'none';
    });

    // Remove active class from all tabs
    const tabs = document.querySelectorAll('.tab-btn');
    tabs.forEach(t => t.classList.remove('active'));

    // Show selected panel
    if (panels[tab]) {
        panels[tab].style.display = 'block';
    }

    // Activate tab button
    const tabIndex = { 'calc': 0, 'conv': 1, 'currency': 2 };
    if (tabs[tabIndex[tab]]) {
        tabs[tabIndex[tab]].classList.add('active');
    }
}

// --- Keyboard Shortcuts ---
document.addEventListener('keydown', function (e) {
    const activeEl = document.activeElement;
    const activeTag = activeEl.tagName.toLowerCase();
    const isCalcDisplay = activeEl.id === 'calc-display';

    if (['input', 'textarea', 'select'].includes(activeTag) && !isCalcDisplay) return;

    const calcPanel = document.getElementById('calc-panel');
    if (!calcPanel || calcPanel.style.display === 'none') return;

    const key = e.key;

    if (/^[0-9]$/.test(key)) {
        e.preventDefault();
        appendNumber(key);
    } else if (key === '.') {
        e.preventDefault();
        appendPoint();
    } else if (key === '+' || key === '-' || key === '*' || key === '/') {
        e.preventDefault();
        appendOperator(key);
    } else if (key === 'Enter' || key === '=') {
        e.preventDefault();
        calculate();
    } else if (key === 'Escape') {
        e.preventDefault();
        clearDisplay();
    } else if (key === 'Backspace') {
        e.preventDefault();
        backspace();
    } else if (key === '(' || key === ')') {
        e.preventDefault();
        appendOperator(key);
    } else if (key === '%') {
        e.preventDefault();
        appendOperator('%');
    }
});

// --- Kalkulator Logic ---
let display = document.getElementById('calc-display');
let currentInput = '';
let isDegree = true;
let isSecondary = false;
let memoryValue = 0;

function toggleMode() {
    isDegree = !isDegree;
    document.getElementById('btn-mode').innerText = isDegree ? "DEG" : "RAD";
}

function toggleSecondary() {
    isSecondary = !isSecondary;
    document.getElementById('btn-2nd').classList.toggle('active');
    
    // Toggle button labels
    const btnSin = document.getElementById('btn-sin');
    const btnCos = document.getElementById('btn-cos');
    const btnTan = document.getElementById('btn-tan');
    const btnLn = document.getElementById('btn-ln');
    const btnLog = document.getElementById('btn-log');
    const btnSqrt = document.getElementById('btn-sqrt');

    if (isSecondary) {
        btnSin.innerText = 'asin';
        btnCos.innerText = 'acos';
        btnTan.innerText = 'atan';
        btnLn.innerText = 'eˣ';
        btnLog.innerText = '10ˣ';
        btnSqrt.innerText = 'x²';

        btnSin.setAttribute('onclick', "appendFunction('asin')");
        btnCos.setAttribute('onclick', "appendFunction('acos')");
        btnTan.setAttribute('onclick', "appendFunction('atan')");
        btnLn.setAttribute('onclick', "appendFunction('ex')");
        btnLog.setAttribute('onclick', "appendFunction('10x')");
        btnSqrt.setAttribute('onclick', "appendOperator('^2')");
    } else {
        btnSin.innerText = 'sin';
        btnCos.innerText = 'cos';
        btnTan.innerText = 'tan';
        btnLn.innerText = 'ln';
        btnLog.innerText = 'log';
        btnSqrt.innerText = '√x';

        btnSin.setAttribute('onclick', "appendFunction('sin')");
        btnCos.setAttribute('onclick', "appendFunction('cos')");
        btnTan.setAttribute('onclick', "appendFunction('tan')");
        btnLn.setAttribute('onclick', "appendFunction('ln')");
        btnLog.setAttribute('onclick', "appendFunction('log')");
        btnSqrt.setAttribute('onclick', "appendFunction('sqrt')");
    }
}

// Memory Functions
function memoryClear() { memoryValue = 0; }
function memoryRead() {
    if (currentInput === '') currentInput = memoryValue.toString();
    else currentInput += memoryValue.toString();
    display.value = currentInput;
}
function memoryAdd() { try { let val = new Function('return ' + currentInput)(); if (!isNaN(val)) memoryValue += val; } catch (e) { } }
function memorySub() { try { let val = new Function('return ' + currentInput)(); if (!isNaN(val)) memoryValue -= val; } catch (e) { } }

function appendNumber(num) { currentInput += num; display.value = currentInput; }
function appendOperator(op) { if (currentInput === '' && op !== '-') return; currentInput += op; display.value = currentInput; }
function appendFunction(func) {
    if (['sin', 'cos', 'tan', 'asin', 'acos', 'atan', 'log', 'ln', 'sqrt', 'cbrt'].includes(func)) currentInput += func + '(';
    else if (func === 'ex') currentInput += 'e^(';
    else if (func === '10x') currentInput += '10^(';
    else currentInput += func;
    display.value = currentInput;
}

function appendPoint() {
    const segments = currentInput.split(/[\+\-\*\/]/);
    if (!segments[segments.length - 1].includes('.')) { currentInput += '.'; display.value = currentInput; }
}

function clearDisplay() { currentInput = ''; display.value = ''; }
function backspace() { currentInput = currentInput.slice(0, -1); display.value = currentInput; }

function calculate() {
    try {
        if (currentInput === '') return;
        let evalString = currentInput;

        // --- PRE-PROCESSING ---
        if (isDegree) {
            evalString = evalString.replace(/sin\(/g, 'dSin(').replace(/cos\(/g, 'dCos(').replace(/tan\(/g, 'dTan(');
            evalString = evalString.replace(/asin\(/g, 'dAsin(').replace(/acos\(/g, 'dAcos(').replace(/atan\(/g, 'dAtan(');
        } else {
            evalString = evalString.replace(/sin\(/g, 'Math.sin(').replace(/cos\(/g, 'Math.cos(').replace(/tan\(/g, 'Math.tan(');
            evalString = evalString.replace(/asin\(/g, 'Math.asin(').replace(/acos\(/g, 'Math.acos(').replace(/atan\(/g, 'Math.atan(');
        }

        evalString = evalString.replace(/log\(/g, 'Math.log10(').replace(/ln\(/g, 'Math.log(');
        evalString = evalString.replace(/sqrt\(/g, 'Math.sqrt(').replace(/cbrt\(/g, 'Math.cbrt(');
        evalString = evalString.replace(/\^/g, '**').replace(/E/g, '*10**');
        evalString = evalString.replace(/(\d+(\.\d+)?)%/g, '($1/100)');

        window.dSin = (d) => Math.sin(d * Math.PI / 180);
        window.dCos = (d) => Math.cos(d * Math.PI / 180);
        window.dTan = (d) => Math.tan(d * Math.PI / 180);
        window.dAsin = (v) => Math.asin(v) * 180 / Math.PI;
        window.dAcos = (v) => Math.acos(v) * 180 / Math.PI;
        window.dAtan = (v) => Math.atan(v) * 180 / Math.PI;

        let result = new Function('return ' + evalString)();
        let operationString = currentInput + ' = ' + result;
        
        // Format Result limit decimals
        if (!Number.isInteger(result)) result = parseFloat(result.toFixed(8));
        
        display.value = result;
        currentInput = result.toString();
        
        saveHistory(operationString);

    } catch (e) {
        console.error(e);
        display.value = 'Error';
        currentInput = '';
    }
}

// --- Konverter Logic ---
const units = {
    length: ['m', 'cm', 'km', 'mm', 'inch', 'ft', 'yard', 'mile'],
    weight: ['kg', 'g', 'mg', 'lb', 'oz', 'ton'],
    temperature: ['C', 'F', 'K'],
    time: ['s', 'min', 'h', 'day', 'week', 'month', 'year'],
    data: ['B', 'KB', 'MB', 'GB', 'TB', 'PB'],
    speed: ['m/s', 'km/h', 'mph', 'knot'],
    area: ['m²', 'cm²', 'km²', 'ha', 'acre', 'ft²'],
    volume: ['L', 'mL', 'gallon', 'm³', 'cup', 'fl oz'],
    pressure: ['Pa', 'kPa', 'bar', 'psi', 'atm'],
    energy: ['J', 'kJ', 'cal', 'kcal', 'kWh', 'BTU'],
    force: ['N', 'kN', 'lbf', 'dyn', 'kgf'],
    angle: ['deg', 'rad', 'grad', 'arcmin', 'arcsec'],
    frequency: ['Hz', 'kHz', 'MHz', 'GHz', 'rpm'],
    power: ['W', 'kW', 'MW', 'hp', 'BTU/h']
};

const factors = {
    length: { m: 1, cm: 0.01, km: 1000, mm: 0.001, inch: 0.0254, ft: 0.3048, yard: 0.9144, mile: 1609.34 },
    weight: { kg: 1, g: 0.001, mg: 0.000001, lb: 0.453592, oz: 0.0283495, ton: 1000 },
    time: { s: 1, min: 60, h: 3600, day: 86400, week: 604800, month: 2592000, year: 31536000 },
    data: { B: 1, KB: 1024, MB: 1048576, GB: 1073741824, TB: 1099511627776, PB: 1125899906842624 },
    speed: { 'm/s': 1, 'km/h': 0.277778, mph: 0.44704, knot: 0.514444 },
    area: { 'm²': 1, 'cm²': 0.0001, 'km²': 1000000, ha: 10000, acre: 4046.86, 'ft²': 0.092903 },
    volume: { L: 1, mL: 0.001, gallon: 3.78541, 'm³': 1000, cup: 0.236588, 'fl oz': 0.0295735 },
    pressure: { Pa: 1, kPa: 1000, bar: 100000, psi: 6894.76, atm: 101325 },
    energy: { J: 1, kJ: 1000, cal: 4.184, kcal: 4184, kWh: 3600000, BTU: 1055.06 },
    force: { N: 1, kN: 1000, lbf: 4.44822, dyn: 0.00001, kgf: 9.80665 },
    angle: { deg: 1, rad: 57.2958, grad: 0.9, arcmin: 0.016667, arcsec: 0.000278 },
    frequency: { Hz: 1, kHz: 1000, MHz: 1000000, GHz: 1000000000, rpm: 0.016667 },
    power: { W: 1, kW: 1000, MW: 1000000, hp: 745.7, 'BTU/h': 0.29307 }
};

function updateUnits() {
    const typeElem = document.getElementById('conv-type');
    if (!typeElem) return;
    const type = typeElem.value;
    const fromSelect = document.getElementById('conv-from');
    const toSelect = document.getElementById('conv-to');

    fromSelect.innerHTML = ''; toSelect.innerHTML = '';

    if (units[type]) {
        units[type].forEach(unit => {
            fromSelect.add(new Option(unit, unit));
            toSelect.add(new Option(unit, unit));
        });
    }
}

window.onload = function() {
    updateUnits();
    loadHistory(); // Load from LocalStorage
};

let debounceTimer;

function convert() {
    const type = document.getElementById('conv-type').value;
    const valInput = document.getElementById('conv-value');
    const val = parseFloat(valInput.value);
    const from = document.getElementById('conv-from').value;
    const to = document.getElementById('conv-to').value;
    const resultDisplay = document.getElementById('conv-result-val');
    const resultDetail = document.getElementById('conv-result');

    if (isNaN(val)) { resultDisplay.innerText = "0"; return; }

    let result;
    if (type === 'temperature') {
        let valInC = val;
        if (from === 'F') valInC = (val - 32) * 5 / 9;
        else if (from === 'K') valInC = val - 273.15;

        if (to === 'C') result = valInC;
        else if (to === 'F') result = (valInC * 9 / 5) + 32;
        else if (to === 'K') result = valInC + 273.15;
    } else {
        if (!factors[type] || !factors[type][from] || !factors[type][to]) return;
        result = val * factors[type][from] / factors[type][to];
    }

    let formattedResult = parseFloat(result.toFixed(6));
    resultDisplay.innerText = formattedResult;
    
    // Save to history after delay
    clearTimeout(debounceTimer);
    debounceTimer = setTimeout(() => {
        saveHistory(`${val} ${from} = ${formattedResult} ${to}`);
    }, 1500); 
}

function swapUnits() {
    const fromSelect = document.getElementById('conv-from');
    const toSelect = document.getElementById('conv-to');
    if (!fromSelect || !toSelect) return;
    const temp = fromSelect.value;
    fromSelect.value = toSelect.value;
    toSelect.value = temp;
    convert();
}

// --- LocalStorage History Logic ---
function saveHistory(operationText) {
    let history = JSON.parse(localStorage.getItem('econcalc_history') || '[]');
    const now = new Date().toLocaleString();
    
    // Add new item
    history.unshift({ op: operationText, time: now });
    
    // Limit to 20 items
    if (history.length > 20) history.pop();
    
    localStorage.setItem('econcalc_history', JSON.stringify(history));
    loadHistory();
}

function loadHistory() {
    const tbody = document.getElementById('history-body');
    if (!tbody) return;
    
    tbody.innerHTML = '';
    const history = JSON.parse(localStorage.getItem('econcalc_history') || '[]');
    
    history.forEach(item => {
        const row = tbody.insertRow();
        const cellOp = row.insertCell(0);
        const cellTime = row.insertCell(1);
        cellOp.textContent = item.op;
        cellTime.textContent = item.time;
    });
}

function clearHistory() {
    localStorage.removeItem('econcalc_history');
    loadHistory();
}

// --- Theme Toggle ---
function toggleTheme() {
    const html = document.documentElement;
    const currentTheme = html.getAttribute('data-theme');
    const newTheme = currentTheme === 'light' ? 'dark' : 'light';
    html.setAttribute('data-theme', newTheme);
    localStorage.setItem('econcalc-theme', newTheme);
    
    const icon = document.getElementById('theme-icon');
    if (icon) icon.textContent = newTheme === 'light' ? '☀️' : '🌙';
}

document.addEventListener('DOMContentLoaded', function () {
    let savedTheme = localStorage.getItem('econcalc-theme') || 'dark';
    document.documentElement.setAttribute('data-theme', savedTheme);
    const icon = document.getElementById('theme-icon');
    if (icon) icon.textContent = savedTheme === 'light' ? '☀️' : '🌙';
});
