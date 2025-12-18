// --- Tab Logic ---
function switchTab(tab) {
    document.getElementById('calc-panel').style.display = 'none';
    document.getElementById('conv-panel').style.display = 'none';

    const tabs = document.querySelectorAll('.tab-btn');
    tabs.forEach(t => t.classList.remove('active'));

    if (tab === 'calc') {
        document.getElementById('calc-panel').style.display = 'block';
        tabs[0].classList.add('active');
    } else {
        document.getElementById('conv-panel').style.display = 'block';
        tabs[1].classList.add('active');
    }
}

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
function memoryClear() {
    memoryValue = 0;
    currentInput = '';
    display.value = ''; // Optional feedback
}

function memoryRead() {
    if (currentInput === '') {
        currentInput = memoryValue.toString();
    } else {
        currentInput += memoryValue.toString();
    }
    display.value = currentInput;
}

function memoryAdd() {
    try {
        let val = new Function('return ' + currentInput)(); // Simple eval for now
        if (!isNaN(val)) memoryValue += val;
    } catch (e) { }
}

function memorySub() {
    try {
        let val = new Function('return ' + currentInput)();
        if (!isNaN(val)) memoryValue -= val;
    } catch (e) { }
}


function appendNumber(num) {
    currentInput += num;
    display.value = currentInput;
}

function appendOperator(op) {
    if (currentInput === '' && op !== '-') return;
    currentInput += op;
    display.value = currentInput;
}

function appendFunction(func) {
    if (['sin', 'cos', 'tan', 'asin', 'acos', 'atan', 'log', 'ln', 'sqrt', 'cbrt'].includes(func)) {
        currentInput += func + '(';
    } else if (func === 'ex') {
        currentInput += 'e^(';
    } else if (func === '10x') {
        currentInput += '10^(';
    } else {
        currentInput += func;
    }
    display.value = currentInput;
}

function appendPoint() {
    const segments = currentInput.split(/[\+\-\*\/]/);
    const lastSegment = segments[segments.length - 1];

    if (!lastSegment.includes('.')) {
        currentInput += '.';
        display.value = currentInput;
    }
}

function clearDisplay() {
    currentInput = '';
    display.value = '';
}

function backspace() {
    currentInput = currentInput.slice(0, -1);
    display.value = currentInput;
}

function calculate() {
    try {
        if (currentInput === '') return;

        let evalString = currentInput;

        // --- PRE-PROCESSING ---

        // Handle Trigonometry (Mode Aware)
        // Note: Inverse trig returns radians by default in JS.
        // If DEG mode, we convert result of asin/acos/atan FROM rad TO deg.

        if (isDegree) {
            // Normal Trig: Input is Deg -> Convert to Rad -> Calc
            evalString = evalString.replace(/sin\(/g, 'dSin(');
            evalString = evalString.replace(/cos\(/g, 'dCos(');
            evalString = evalString.replace(/tan\(/g, 'dTan(');

            // Inverse Trig: Output is Rad -> Convert to Deg
            evalString = evalString.replace(/asin\(/g, 'dAsin(');
            evalString = evalString.replace(/acos\(/g, 'dAcos(');
            evalString = evalString.replace(/atan\(/g, 'dAtan(');
        } else {
            evalString = evalString.replace(/sin\(/g, 'Math.sin(');
            evalString = evalString.replace(/cos\(/g, 'Math.cos(');
            evalString = evalString.replace(/tan\(/g, 'Math.tan(');
            evalString = evalString.replace(/asin\(/g, 'Math.asin(');
            evalString = evalString.replace(/acos\(/g, 'Math.acos(');
            evalString = evalString.replace(/atan\(/g, 'Math.atan(');
        }

        evalString = evalString.replace(/log\(/g, 'Math.log10(');
        evalString = evalString.replace(/ln\(/g, 'Math.log(');
        evalString = evalString.replace(/sqrt\(/g, 'Math.sqrt(');
        evalString = evalString.replace(/cbrt\(/g, 'Math.cbrt(');
        evalString = evalString.replace(/\^/g, '**');
        evalString = evalString.replace(/E/g, '*10**');
        evalString = evalString.replace(/(\d+(\.\d+)?)%/g, '($1/100)');

        // Helpers for DEG mode
        window.dSin = (d) => Math.sin(d * Math.PI / 180);
        window.dCos = (d) => Math.cos(d * Math.PI / 180);
        window.dTan = (d) => Math.tan(d * Math.PI / 180);

        window.dAsin = (v) => Math.asin(v) * 180 / Math.PI;
        window.dAcos = (v) => Math.acos(v) * 180 / Math.PI;
        window.dAtan = (v) => Math.atan(v) * 180 / Math.PI;

        let result = new Function('return ' + evalString)();

        let operationString = currentInput + ' = ' + result;
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
    length: ['m', 'cm', 'km', 'mm', 'inch', 'ft'],
    weight: ['kg', 'g', 'mg', 'lb', 'oz'],
    temperature: ['C', 'F', 'K'],
    time: ['s', 'min', 'h', 'day'],
    data: ['B', 'KB', 'MB', 'GB', 'TB'],
    speed: ['m/s', 'km/h', 'mph', 'knot']
};

const factors = {
    length: { m: 1, cm: 0.01, km: 1000, mm: 0.001, inch: 0.0254, ft: 0.3048 },
    weight: { kg: 1, g: 0.001, mg: 0.000001, lb: 0.453592, oz: 0.0283495 },
    time: { s: 1, min: 60, h: 3600, day: 86400 },
    data: { B: 1, KB: 1024, MB: 1048576, GB: 1073741824, TB: 1099511627776 },
    speed: { 'm/s': 1, 'km/h': 0.277778, mph: 0.44704, knot: 0.514444 }
};

function updateUnits() {
    const typeElem = document.getElementById('conv-type');
    if (!typeElem) return;

    const type = typeElem.value;
    const fromSelect = document.getElementById('conv-from');
    const toSelect = document.getElementById('conv-to');

    fromSelect.innerHTML = '';
    toSelect.innerHTML = '';

    if (units[type]) {
        units[type].forEach(unit => {
            fromSelect.add(new Option(unit, unit));
            toSelect.add(new Option(unit, unit));
        });
    }
}

window.onload = updateUnits;

let debounceTimer;

function convert() {
    const type = document.getElementById('conv-type').value;
    const valInput = document.getElementById('conv-value');
    const val = parseFloat(valInput.value);
    const from = document.getElementById('conv-from').value;
    const to = document.getElementById('conv-to').value;

    // UI Elements
    const resultDisplay = document.getElementById('conv-result-val');
    const resultDetail = document.getElementById('conv-result');

    if (isNaN(val)) {
        resultDisplay.innerText = "0";
        return;
    }

    let result;

    if (type === 'temperature') {
        let valInC;
        if (from === 'C') valInC = val;
        else if (from === 'F') valInC = (val - 32) * 5 / 9;
        else if (from === 'K') valInC = val - 273.15;

        if (to === 'C') result = valInC;
        else if (to === 'F') result = (valInC * 9 / 5) + 32;
        else if (to === 'K') result = valInC + 273.15;
    } else {
        if (!factors[type] || !factors[type][from] || !factors[type][to]) return;
        const valInBase = val * factors[type][from];
        result = valInBase / factors[type][to];
    }

    let formattedResult = parseFloat(result.toFixed(6));

    // Update Big Display
    resultDisplay.innerText = formattedResult;

    const resultString = `${val} ${from} = ${formattedResult} ${to}`;
    resultDetail.innerText = resultString;

    // Debounce Save to DB to avoid spamming while typing
    clearTimeout(debounceTimer);
    debounceTimer = setTimeout(() => {
        saveHistory(resultString);
    }, 1000); // Save after 1 second of inactivity
}

// --- AJAX Logic ---
function saveHistory(operationText) {
    const formData = new FormData();
    formData.append('operasi', operationText);

    fetch('simpan_riwayat.php', {
        method: 'POST',
        body: formData
    })
        .then(response => response.text())
        .then(data => {
            if (data.trim() === 'success') {
                addToTable(operationText);
            }
        })
        .catch(error => console.error('Error:', error));
}

function addToTable(text) {
    const tbody = document.getElementById('history-body');
    if (!tbody) return;

    const row = tbody.insertRow(0);
    const cellOp = row.insertCell(0);
    const cellTime = row.insertCell(1);

    cellOp.textContent = text;

    const now = new Date();
    const dateStr = now.getFullYear() + '-' +
        String(now.getMonth() + 1).padStart(2, '0') + '-' +
        String(now.getDate()).padStart(2, '0') + ' ' +
        String(now.getHours()).padStart(2, '0') + ':' +
        String(now.getMinutes()).padStart(2, '0') + ':' +
        String(now.getSeconds()).padStart(2, '0');

    cellTime.textContent = dateStr;

    if (tbody.rows.length > 10) {
        tbody.deleteRow(10);
    }
}
