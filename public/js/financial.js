// --- Financial Calculator Logic ---
// Optimized version with input validation, debounce, and better UX

// Utility: Debounce function for auto-calculate
function debounce(func, wait = 300) {
    let timeout;
    return function executedFunction(...args) {
        const later = () => {
            clearTimeout(timeout);
            func(...args);
        };
        clearTimeout(timeout);
        timeout = setTimeout(later, wait);
    };
}

// Utility: Validate positive number
function validatePositiveNumber(value, fieldName) {
    const num = parseFloat(value);
    if (isNaN(num) || num < 0) {
        return { valid: false, message: `${fieldName} harus berupa angka positif` };
    }
    return { valid: true, value: num };
}

// Utility: Show error message
function showFieldError(elementId, message) {
    const element = document.getElementById(elementId);
    if (element) {
        element.innerHTML = `<div class="result-error" style="color: #ef4444; padding: 10px; background: rgba(239,68,68,0.1); border-radius: 8px;">⚠️ ${message}</div>`;
    }
}

// Helper: Format number with thousand separators
function formatNumber(num) {
    if (isNaN(num) || !isFinite(num)) return '0';
    return Math.round(num).toLocaleString('id-ID');
}

// Helper: Format currency with Rupiah
function formatCurrency(num) {
    if (isNaN(num) || !isFinite(num)) return 'Rp 0';
    return `Rp ${formatNumber(num)}`;
}

// Simple Interest: A = P(1 + rt)
function calculateSimpleInterest() {
    const principalInput = document.getElementById('fi-principal').value;
    const rateInput = document.getElementById('fi-rate').value;
    const timeInput = document.getElementById('fi-time').value;

    // Validation
    const principalValidation = validatePositiveNumber(principalInput, 'Modal awal');
    const rateValidation = validatePositiveNumber(rateInput, 'Suku bunga');
    const timeValidation = validatePositiveNumber(timeInput, 'Jangka waktu');

    if (!principalValidation.valid) {
        showFieldError('fi-result', principalValidation.message);
        return;
    }
    if (!rateValidation.valid) {
        showFieldError('fi-result', rateValidation.message);
        return;
    }
    if (!timeValidation.valid) {
        showFieldError('fi-result', timeValidation.message);
        return;
    }

    const principal = principalValidation.value || 0;
    const rate = rateValidation.value || 0;
    const time = timeValidation.value || 0;

    const interest = principal * (rate / 100) * time;
    const total = principal + interest;

    document.getElementById('fi-result').innerHTML = `
        <div class="result-item">
            <span class="label">Modal Awal:</span>
            <span class="value">${formatCurrency(principal)}</span>
        </div>
        <div class="result-item">
            <span class="label">Bunga (${rate}% × ${time} tahun):</span>
            <span class="value highlight-green">${formatCurrency(interest)}</span>
        </div>
        <div class="result-item total">
            <span class="label">Total Akhir:</span>
            <span class="value highlight-blue">${formatCurrency(total)}</span>
        </div>
    `;

    if (typeof saveHistory === 'function') {
        saveHistory(`Bunga Sederhana: P=${formatNumber(principal)}, r=${rate}%, t=${time} tahun = ${formatCurrency(total)}`);
    }
}

// Compound Interest: A = P(1 + r/n)^(nt)
function calculateCompoundInterest() {
    const principalInput = document.getElementById('ci-principal').value;
    const rateInput = document.getElementById('ci-rate').value;
    const timeInput = document.getElementById('ci-time').value;
    const compoundInput = document.getElementById('ci-compound').value;

    // Validation
    const principalValidation = validatePositiveNumber(principalInput, 'Modal awal');
    const rateValidation = validatePositiveNumber(rateInput, 'Suku bunga');
    const timeValidation = validatePositiveNumber(timeInput, 'Jangka waktu');
    const compoundValidation = validatePositiveNumber(compoundInput, 'Frekuensi');

    if (!principalValidation.valid) {
        showFieldError('ci-result', principalValidation.message);
        return;
    }
    if (!rateValidation.valid) {
        showFieldError('ci-result', rateValidation.message);
        return;
    }
    if (!timeValidation.valid) {
        showFieldError('ci-result', timeValidation.message);
        return;
    }
    if (!compoundValidation.valid) {
        showFieldError('ci-result', compoundValidation.message);
        return;
    }

    const principal = principalValidation.value || 0;
    const rate = rateValidation.value || 0;
    const time = timeValidation.value || 0;
    const compound = compoundValidation.value || 12;

    const r = rate / 100;
    const total = principal * Math.pow((1 + r / compound), compound * time);
    const interest = total - principal;

    // Calculate effective annual rate
    const effectiveRate = (Math.pow(1 + r / compound, compound) - 1) * 100;

    document.getElementById('ci-result').innerHTML = `
        <div class="result-item">
            <span class="label">Modal Awal:</span>
            <span class="value">${formatCurrency(principal)}</span>
        </div>
        <div class="result-item">
            <span class="label">Bunga Majemuk:</span>
            <span class="value highlight-green">${formatCurrency(interest)}</span>
        </div>
        <div class="result-item">
            <span class="label">Suku Bunga Efektif:</span>
            <span class="value">${effectiveRate.toFixed(2)}%/tahun</span>
        </div>
        <div class="result-item total">
            <span class="label">Total Akhir:</span>
            <span class="value highlight-blue">${formatCurrency(total)}</span>
        </div>
    `;

    if (typeof saveHistory === 'function') {
        saveHistory(`Bunga Majemuk: P=${formatNumber(principal)}, r=${rate}%, t=${time} tahun = ${formatCurrency(total)}`);
    }
}

// Loan/Mortgage: M = P[r(1+r)^n]/[(1+r)^n-1]
function calculateLoan() {
    const principalInput = document.getElementById('loan-principal').value;
    const rateInput = document.getElementById('loan-rate').value;
    const monthsInput = document.getElementById('loan-months').value;

    // Validation
    const principalValidation = validatePositiveNumber(principalInput, 'Jumlah pinjaman');
    const rateValidation = validatePositiveNumber(rateInput, 'Suku bunga');
    const monthsValidation = validatePositiveNumber(monthsInput, 'Jangka waktu');

    if (!principalValidation.valid) {
        showFieldError('loan-result', principalValidation.message);
        return;
    }
    if (!rateValidation.valid) {
        showFieldError('loan-result', rateValidation.message);
        return;
    }
    if (!monthsValidation.valid) {
        showFieldError('loan-result', monthsValidation.message);
        return;
    }

    const principal = principalValidation.value || 0;
    const annualRate = rateValidation.value || 0;
    const months = Math.round(monthsValidation.value) || 0;

    if (months === 0) {
        showFieldError('loan-result', 'Jangka waktu minimal 1 bulan');
        return;
    }

    const monthlyRate = annualRate / 100 / 12;
    let monthlyPayment;

    if (monthlyRate === 0) {
        monthlyPayment = principal / months;
    } else {
        monthlyPayment = principal * (monthlyRate * Math.pow(1 + monthlyRate, months)) / (Math.pow(1 + monthlyRate, months) - 1);
    }

    const totalPayment = monthlyPayment * months;
    const totalInterest = totalPayment - principal;
    const years = Math.floor(months / 12);
    const remainingMonths = months % 12;

    let durationText = '';
    if (years > 0) durationText += `${years} tahun `;
    if (remainingMonths > 0) durationText += `${remainingMonths} bulan`;
    if (!durationText) durationText = '0 bulan';

    document.getElementById('loan-result').innerHTML = `
        <div class="result-item">
            <span class="label">Pinjaman Pokok:</span>
            <span class="value">${formatCurrency(principal)}</span>
        </div>
        <div class="result-item">
            <span class="label">Jangka Waktu:</span>
            <span class="value">${durationText.trim()}</span>
        </div>
        <div class="result-item">
            <span class="label">Cicilan/Bulan:</span>
            <span class="value highlight-blue">${formatCurrency(monthlyPayment)}</span>
        </div>
        <div class="result-item">
            <span class="label">Total Bunga:</span>
            <span class="value highlight-orange">${formatCurrency(totalInterest)}</span>
        </div>
        <div class="result-item total">
            <span class="label">Total Bayar:</span>
            <span class="value">${formatCurrency(totalPayment)}</span>
        </div>
    `;

    if (typeof saveHistory === 'function') {
        saveHistory(`Cicilan: P=${formatNumber(principal)}, r=${annualRate}%/tahun, ${months} bulan = ${formatCurrency(monthlyPayment)}/bulan`);
    }
}

// Discount Calculator
function calculateDiscount() {
    const originalInput = document.getElementById('disc-original').value;
    const discountInput = document.getElementById('disc-percent').value;

    // Validation
    const originalValidation = validatePositiveNumber(originalInput, 'Harga asli');
    const discountValidation = validatePositiveNumber(discountInput, 'Diskon');

    if (!originalValidation.valid) {
        showFieldError('disc-result', originalValidation.message);
        return;
    }
    if (!discountValidation.valid) {
        showFieldError('disc-result', discountValidation.message);
        return;
    }

    const original = originalValidation.value || 0;
    const discount = discountValidation.value || 0;

    if (discount > 100) {
        showFieldError('disc-result', 'Diskon tidak boleh lebih dari 100%');
        return;
    }

    const savings = original * (discount / 100);
    const finalPrice = original - savings;

    document.getElementById('disc-result').innerHTML = `
        <div class="result-item">
            <span class="label">Harga Asli:</span>
            <span class="value" style="text-decoration: line-through; opacity: 0.7;">${formatCurrency(original)}</span>
        </div>
        <div class="result-item">
            <span class="label">Diskon (${discount}%):</span>
            <span class="value highlight-green">- ${formatCurrency(savings)}</span>
        </div>
        <div class="result-item total">
            <span class="label">Harga Akhir:</span>
            <span class="value highlight-blue">${formatCurrency(finalPrice)}</span>
        </div>
    `;

    if (typeof saveHistory === 'function') {
        saveHistory(`Diskon: ${formatCurrency(original)} - ${discount}% = ${formatCurrency(finalPrice)}`);
    }
}

// --- API & State Logic for Financial Plans ---
let activeTabType = 'simple';

function switchFinanceTab(tab) {
    activeTabType = tab;

    const tabs = ['simple', 'compound', 'loan', 'discount'];
    tabs.forEach(t => {
        const panel = document.getElementById(`finance-${t}`);
        const btn = document.querySelector(`[onclick="switchFinanceTab('${t}')"]`);
        if (panel) panel.style.display = t === tab ? 'block' : 'none';
        if (btn) btn.classList.toggle('active', t === tab);
    });
}

function saveFinancialPlan() {
    const titleInput = document.getElementById('plan-title');
    const title = titleInput ? titleInput.value.trim() : '';

    if (!title) {
        showNotification('Mohon isi nama rencana!', 'error');
        if (titleInput) titleInput.focus();
        return;
    }

    let data = {};
    let hasValidData = false;

    if (activeTabType === 'simple') {
        const principal = document.getElementById('fi-principal').value;
        const rate = document.getElementById('fi-rate').value;
        const time = document.getElementById('fi-time').value;
        hasValidData = principal || rate || time;
        data = {
            principal: principal,
            rate: rate,
            time: time,
            result: document.getElementById('fi-result').innerText
        };
    } else if (activeTabType === 'compound') {
        const principal = document.getElementById('ci-principal').value;
        const rate = document.getElementById('ci-rate').value;
        const time = document.getElementById('ci-time').value;
        hasValidData = principal || rate || time;
        data = {
            principal: principal,
            rate: rate,
            time: time,
            compound: document.getElementById('ci-compound').value,
            result: document.getElementById('ci-result').innerText
        };
    } else if (activeTabType === 'loan') {
        const principal = document.getElementById('loan-principal').value;
        const rate = document.getElementById('loan-rate').value;
        const months = document.getElementById('loan-months').value;
        hasValidData = principal || rate || months;
        data = {
            principal: principal,
            rate: rate,
            months: months,
            result: document.getElementById('loan-result').innerText
        };
    } else if (activeTabType === 'discount') {
        const original = document.getElementById('disc-original').value;
        const percent = document.getElementById('disc-percent').value;
        hasValidData = original || percent;
        data = {
            original: original,
            percent: percent,
            result: document.getElementById('disc-result').innerText
        };
    }

    if (!hasValidData) {
        showNotification('Mohon isi data perhitungan terlebih dahulu!', 'warning');
        return;
    }

    const csrfTokenMeta = document.querySelector('meta[name="csrf-token"]');
    const csrfToken = csrfTokenMeta ? csrfTokenMeta.getAttribute('content') : '';

    if (!csrfToken) {
        showNotification('Error: CSRF Token missing. Please refresh the page.', 'error');
        return;
    }

    // Show loading state
    const saveBtn = document.querySelector('button[onclick="saveFinancialPlan()"]');
    const originalText = saveBtn ? saveBtn.innerHTML : '';
    if (saveBtn) {
        saveBtn.innerHTML = '<span class="loading-spinner"></span> Menyimpan...';
        saveBtn.disabled = true;
    }

    fetch('/financial/save', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'X-CSRF-TOKEN': csrfToken,
            'Accept': 'application/json'
        },
        body: JSON.stringify({
            title: title,
            type: activeTabType,
            data: data
        })
    })
        .then(response => {
            if (!response.ok) {
                if (response.status === 419) {
                    throw new Error('Sesi telah berakhir. Silakan refresh halaman.');
                }
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            if (data.success) {
                showNotification('✅ Rencana berhasil disimpan!', 'success');

                // Clear the title input
                if (titleInput) titleInput.value = '';

                // Reload after delay to show new data
                setTimeout(() => {
                    location.reload();
                }, 1500);
            } else {
                showNotification('Gagal menyimpan: ' + (data.message || 'Unknown error'), 'error');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            showNotification(error.message || 'Terjadi kesalahan saat menyimpan.', 'error');
        })
        .finally(() => {
            if (saveBtn) {
                saveBtn.innerHTML = originalText;
                saveBtn.disabled = false;
            }
        });
}

function deletePlan(id) {
    if (!confirm('Hapus rencana ini?')) return;

    const csrfTokenMeta = document.querySelector('meta[name="csrf-token"]');
    const csrfToken = csrfTokenMeta ? csrfTokenMeta.getAttribute('content') : '';

    if (!csrfToken) {
        showNotification('Error: CSRF Token missing.', 'error');
        return;
    }

    const planElement = document.getElementById('plan-' + id);
    if (planElement) {
        planElement.style.opacity = '0.5';
        planElement.style.pointerEvents = 'none';
    }

    fetch('/financial/delete/' + id, {
        method: 'DELETE',
        headers: {
            'X-CSRF-TOKEN': csrfToken,
            'Accept': 'application/json'
        }
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to delete');
            }
            return response.json();
        })
        .then(data => {
            if (data.success) {
                if (planElement) {
                    planElement.style.transition = 'all 0.3s ease';
                    planElement.style.transform = 'translateX(-100%)';
                    planElement.style.opacity = '0';
                    setTimeout(() => planElement.remove(), 300);
                }
                showNotification('✅ Rencana berhasil dihapus!', 'success');
            } else {
                if (planElement) {
                    planElement.style.opacity = '1';
                    planElement.style.pointerEvents = 'auto';
                }
                showNotification('Gagal menghapus rencana.', 'error');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            if (planElement) {
                planElement.style.opacity = '1';
                planElement.style.pointerEvents = 'auto';
            }
            showNotification('Terjadi kesalahan saat menghapus.', 'error');
        });
}

// Notification helper
function showNotification(message, type = 'info') {
    // Try toast first
    const toast = document.getElementById('save-toast');
    if (toast) {
        toast.textContent = message;
        toast.className = `toast toast-${type} show`;
        setTimeout(() => {
            toast.classList.remove('show');
        }, 3000);
        return;
    }

    // Fallback to alert
    alert(message);
}

// Auto-calculate with debounce (optional - enable by calling initAutoCalculate())
function initAutoCalculate() {
    const debouncedSimple = debounce(calculateSimpleInterest, 500);
    const debouncedCompound = debounce(calculateCompoundInterest, 500);
    const debouncedLoan = debounce(calculateLoan, 500);
    const debouncedDiscount = debounce(calculateDiscount, 500);

    // Simple Interest inputs
    ['fi-principal', 'fi-rate', 'fi-time'].forEach(id => {
        const el = document.getElementById(id);
        if (el) el.addEventListener('input', debouncedSimple);
    });

    // Compound Interest inputs
    ['ci-principal', 'ci-rate', 'ci-time', 'ci-compound'].forEach(id => {
        const el = document.getElementById(id);
        if (el) el.addEventListener('input', debouncedCompound);
    });

    // Loan inputs
    ['loan-principal', 'loan-rate', 'loan-months'].forEach(id => {
        const el = document.getElementById(id);
        if (el) el.addEventListener('input', debouncedLoan);
    });

    // Discount inputs
    ['disc-original', 'disc-percent'].forEach(id => {
        const el = document.getElementById(id);
        if (el) el.addEventListener('input', debouncedDiscount);
    });
}

// Initialize when DOM is ready
document.addEventListener('DOMContentLoaded', function () {
    initAutoCalculate();
});
