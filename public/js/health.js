// --- Health Calculator Logic ---

function calculateBMI() {
    const weight = parseFloat(document.getElementById('bmi-weight').value) || 0;
    const height = parseFloat(document.getElementById('bmi-height').value) || 0;
    const unit = document.getElementById('bmi-unit').value;

    let weightKg = weight;
    let heightM = height / 100; // default: height in cm

    // Convert if imperial
    if (unit === 'imperial') {
        weightKg = weight * 0.453592; // lb to kg
        heightM = height * 0.0254; // inches to meters
    }

    if (weightKg <= 0 || heightM <= 0) {
        document.getElementById('bmi-result').innerHTML = '<p style="color: #94a3b8;">Masukkan berat dan tinggi badan</p>';
        return;
    }

    const bmi = weightKg / (heightM * heightM);
    let category, color, advice;

    if (bmi < 18.5) {
        category = 'Kurus (Underweight)';
        color = '#60a5fa'; // blue
        advice = 'Disarankan untuk menambah asupan kalori dan konsultasi dengan ahli gizi.';
    } else if (bmi < 25) {
        category = 'Normal';
        color = '#34d399'; // green
        advice = 'Berat badan Anda ideal! Pertahankan pola hidup sehat.';
    } else if (bmi < 30) {
        category = 'Gemuk (Overweight)';
        color = '#fbbf24'; // yellow
        advice = 'Disarankan untuk meningkatkan aktivitas fisik dan mengatur pola makan.';
    } else {
        category = 'Obesitas';
        color = '#f87171'; // red
        advice = 'Konsultasikan dengan dokter untuk program penurunan berat badan yang sehat.';
    }

    document.getElementById('bmi-result').innerHTML = `
        <div class="bmi-display">
            <div class="bmi-value" style="color: ${color}">${bmi.toFixed(1)}</div>
            <div class="bmi-category" style="color: ${color}">${category}</div>
            <div class="bmi-bar">
                <div class="bmi-indicator" style="left: ${Math.min(Math.max((bmi - 10) / 35 * 100, 0), 100)}%"></div>
            </div>
            <div class="bmi-scale">
                <span>Kurus</span>
                <span>Normal</span>
                <span>Gemuk</span>
                <span>Obesitas</span>
            </div>
            <p class="bmi-advice">${advice}</p>
        </div>
    `;

    if (typeof saveHistory === 'function') {
        saveHistory(`BMI: ${weight}${unit === 'imperial' ? 'lb' : 'kg'}, ${height}${unit === 'imperial' ? 'in' : 'cm'} = ${bmi.toFixed(1)} (${category})`);
    }
}

function updateBMILabels() {
    const unit = document.getElementById('bmi-unit').value;
    document.getElementById('weight-label').textContent = unit === 'metric' ? 'Berat (kg)' : 'Berat (lb)';
    document.getElementById('height-label').textContent = unit === 'metric' ? 'Tinggi (cm)' : 'Tinggi (inch)';
}

// Ideal Weight Calculator (Devine Formula)
function calculateIdealWeight() {
    const height = parseFloat(document.getElementById('ideal-height').value) || 0;
    const gender = document.getElementById('ideal-gender').value;

    if (height <= 0) {
        document.getElementById('ideal-result').innerHTML = '<p style="color: #94a3b8;">Masukkan tinggi badan</p>';
        return;
    }

    const heightInches = height / 2.54; // cm to inches
    const heightOver5Feet = heightInches - 60;

    let idealWeight;
    if (gender === 'male') {
        idealWeight = 50 + 2.3 * heightOver5Feet;
    } else {
        idealWeight = 45.5 + 2.3 * heightOver5Feet;
    }

    // BMI range 18.5-25
    const minWeight = 18.5 * Math.pow(height / 100, 2);
    const maxWeight = 25 * Math.pow(height / 100, 2);

    document.getElementById('ideal-result').innerHTML = `
        <div class="result-item">
            <span class="label">Berat Ideal (Devine):</span>
            <span class="value">${idealWeight.toFixed(1)} kg</span>
        </div>
        <div class="result-item">
            <span class="label">Rentang Sehat (BMI 18.5-25):</span>
            <span class="value">${minWeight.toFixed(1)} - ${maxWeight.toFixed(1)} kg</span>
        </div>
    `;

    if (typeof saveHistory === 'function') {
        saveHistory(`Berat Ideal: ${height}cm, ${gender === 'male' ? 'Pria' : 'Wanita'} = ${idealWeight.toFixed(1)}kg`);
    }
}

// --- Health Tracker & Chart ---
function saveBMI() {
    if (!checkAuth()) return;

    const weight = parseFloat(document.getElementById('bmi-weight').value) || 0;
    const height = parseFloat(document.getElementById('bmi-height').value) || 0;

    // We recalculate simply to be safe
    let weightKg = weight;
    let heightM = height / 100;
    const unit = document.getElementById('bmi-unit').value;

    if (unit === 'imperial') {
        weightKg = weight * 0.453592;
        heightM = height * 0.0254;
    }

    if (weightKg <= 0 || heightM <= 0) return alert('Data tidak valid');

    const bmi = weightKg / (heightM * heightM);
    let category = bmi < 18.5 ? 'Underweight' : bmi < 25 ? 'Normal' : bmi < 30 ? 'Overweight' : 'Obesity';

    const csrfToken = document.querySelector('meta[name="csrf-token"]').getAttribute('content');

    fetch('/health/save', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'X-CSRF-TOKEN': csrfToken
        },
        body: JSON.stringify({
            weight: weightKg,
            height: height * (unit === 'imperial' ? 2.54 : 1), // store height in cm always? Schema is float. Let's assume cm.
            bmi: bmi,
            category: category
        })
    })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                alert('Log kesehatan tersimpan!');
                // Add to local data for chart update
                if (!window.healthData) window.healthData = [];
                window.healthData.push({
                    created_at: new Date().toISOString(),
                    bmi: bmi,
                    weight: weightKg
                });
                renderHealthChart();
            }
        })
        .catch(error => console.error('Error:', error));
}

let healthChartInstance = null;

function renderHealthChart() {
    const ctx = document.getElementById('healthChart');
    if (!ctx || !window.healthData || window.healthData.length === 0) return;

    if (healthChartInstance) {
        healthChartInstance.destroy();
    }

    const labels = window.healthData.map(d => new Date(d.created_at).toLocaleDateString());
    const data = window.healthData.map(d => d.bmi);

    healthChartInstance = new Chart(ctx, {
        type: 'line',
        data: {
            labels: labels,
            datasets: [{
                label: 'Perkembangan BMI',
                data: data,
                borderColor: '#4facfe',
                backgroundColor: 'rgba(79, 172, 254, 0.2)',
                tension: 0.4,
                fill: true
            }]
        },
        options: {
            responsive: true,
            scales: {
                y: {
                    beginAtZero: false,
                    suggestedMin: 15,
                    suggestedMax: 35,
                    grid: { color: 'rgba(255, 255, 255, 0.1)' },
                    ticks: { color: '#94a3b8' }
                },
                x: {
                    grid: { color: 'rgba(255, 255, 255, 0.1)' },
                    ticks: { color: '#94a3b8' }
                }
            },
            plugins: {
                legend: { labels: { color: '#fff' } }
            }
        }
    });
}

// Initial Render
document.addEventListener('DOMContentLoaded', renderHealthChart);
