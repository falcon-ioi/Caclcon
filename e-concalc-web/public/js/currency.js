// --- Currency Converter Logic ---

const CURRENCY_API = 'https://api.exchangerate-api.com/v4/latest/USD';
let exchangeRates = null;
let ratesLastUpdated = null;

const currencies = {
    'USD': 'United States Dollar',
    'AED': 'UAE Dirham',
    'AFN': 'Afghan Afghani',
    'ALL': 'Albanian Lek',
    'AMD': 'Armenian Dram',
    'ANG': 'Netherlands Antillean Guilder',
    'AOA': 'Angolan Kwanza',
    'ARS': 'Argentine Peso',
    'AUD': 'Australian Dollar',
    'AWG': 'Aruban Florin',
    'AZN': 'Azerbaijani Manat',
    'BAM': 'Bosnia-Herzegovina Mark',
    'BBD': 'Barbadian Dollar',
    'BDT': 'Bangladeshi Taka',
    'BGN': 'Bulgarian Lev',
    'BHD': 'Bahraini Dinar',
    'BIF': 'Burundian Franc',
    'BMD': 'Bermudan Dollar',
    'BND': 'Brunei Dollar',
    'BOB': 'Bolivian Boliviano',
    'BRL': 'Brazilian Real',
    'BSD': 'Bahamian Dollar',
    'BTN': 'Bhutanese Ngultrum',
    'BWP': 'Botswana Pula',
    'BYN': 'Belarusian Ruble',
    'BZD': 'Belize Dollar',
    'CAD': 'Canadian Dollar',
    'CDF': 'Congolese Franc',
    'CHF': 'Swiss Franc',
    'CLP': 'Chilean Peso',
    'CNY': 'Chinese Yuan',
    'COP': 'Colombian Peso',
    'CRC': 'Costa Rican Colón',
    'CUP': 'Cuban Peso',
    'CVE': 'Cape Verdean Escudo',
    'CZK': 'Czech Koruna',
    'DJF': 'Djiboutian Franc',
    'DKK': 'Danish Krone',
    'DOP': 'Dominican Peso',
    'DZD': 'Algerian Dinar',
    'EGP': 'Egyptian Pound',
    'ERN': 'Eritrean Nakfa',
    'ETB': 'Ethiopian Birr',
    'EUR': 'Euro',
    'FJD': 'Fijian Dollar',
    'FKP': 'Falkland Islands Pound',
    'FOK': 'Faroese Króna',
    'GBP': 'British Pound Sterling',
    'GEL': 'Georgian Lari',
    'GGP': 'Guernsey Pound',
    'GHS': 'Ghanaian Cedi',
    'GIP': 'Gibraltar Pound',
    'GMD': 'Gambian Dalasi',
    'GNF': 'Guinean Franc',
    'GTQ': 'Guatemalan Quetzal',
    'GYD': 'Guyanaese Dollar',
    'HKD': 'Hong Kong Dollar',
    'HNL': 'Honduran Lempira',
    'HRK': 'Croatian Kuna',
    'HTG': 'Haitian Gourde',
    'HUF': 'Hungarian Forint',
    'IDR': 'Indonesian Rupiah',
    'ILS': 'Israeli New Shekel',
    'IMP': 'Manx Pound',
    'INR': 'Indian Rupee',
    'IQD': 'Iraqi Dinar',
    'IRR': 'Iranian Rial',
    'ISK': 'Icelandic Króna',
    'JEP': 'Jersey Pound',
    'JMD': 'Jamaican Dollar',
    'JOD': 'Jordanian Dinar',
    'JPY': 'Japanese Yen',
    'KES': 'Kenyan Shilling',
    'KGS': 'Kyrgystani Som',
    'KHR': 'Cambodian Riel',
    'KID': 'Kiribati Dollar',
    'KMF': 'Comorian Franc',
    'KRW': 'South Korean Won',
    'KWD': 'Kuwaiti Dinar',
    'KYD': 'Cayman Islands Dollar',
    'KZT': 'Kazakhstani Tenge',
    'LAK': 'Laotian Kip',
    'LBP': 'Lebanese Pound',
    'LKR': 'Sri Lankan Rupee',
    'LRD': 'Liberian Dollar',
    'LSL': 'Lesotho Loti',
    'LYD': 'Libyan Dinar',
    'MAD': 'Moroccan Dirham',
    'MDL': 'Moldovan Leu',
    'MGA': 'Malagasy Ariary',
    'MKD': 'Macedonian Denar',
    'MMK': 'Burmese Kyat',
    'MNT': 'Mongolian Tugrik',
    'MOP': 'Macanese Pataca',
    'MRU': 'Mauritanian Ouguiya',
    'MUR': 'Mauritian Rupee',
    'MVR': 'Maldivian Rufiyaa',
    'MWK': 'Malawian Kwacha',
    'MXN': 'Mexican Peso',
    'MYR': 'Malaysian Ringgit',
    'MZN': 'Mozambican Metical',
    'NAD': 'Namibian Dollar',
    'NGN': 'Nigerian Naira',
    'NIO': 'Nicaraguan Córdoba',
    'NOK': 'Norwegian Krone',
    'NPR': 'Nepalese Rupee',
    'NZD': 'New Zealand Dollar',
    'OMR': 'Omani Rial',
    'PAB': 'Panamanian Balboa',
    'PEN': 'Peruvian Sol',
    'PGK': 'Papua New Guinean Kina',
    'PHP': 'Philippine Peso',
    'PKR': 'Pakistani Rupee',
    'PLN': 'Polish Zloty',
    'PYG': 'Paraguayan Guarani',
    'QAR': 'Qatari Rial',
    'RON': 'Romanian Leu',
    'RSD': 'Serbian Dinar',
    'RUB': 'Russian Ruble',
    'RWF': 'Rwandan Franc',
    'SAR': 'Saudi Riyal',
    'SBD': 'Solomon Islands Dollar',
    'SCR': 'Seychellois Rupee',
    'SDG': 'Sudanese Pound',
    'SEK': 'Swedish Krona',
    'SGD': 'Singapore Dollar',
    'SHP': 'Saint Helena Pound',
    'SLE': 'Sierra Leonean Leone',
    'SOS': 'Somali Shilling',
    'SRD': 'Surinamese Dollar',
    'SSP': 'South Sudanese Pound',
    'STN': 'São Tomé and Príncipe Dobra',
    'SYP': 'Syrian Pound',
    'SZL': 'Swazi Lilangeni',
    'THB': 'Thai Baht',
    'TJS': 'Tajikistani Somoni',
    'TMT': 'Turkmenistani Manat',
    'TND': 'Tunisian Dinar',
    'TOP': 'Tongan Paʻanga',
    'TRY': 'Turkish Lira',
    'TTD': 'Trinidad and Tobago Dollar',
    'TVD': 'Tuvaluan Dollar',
    'TWD': 'New Taiwan Dollar',
    'TZS': 'Tanzanian Shilling',
    'UAH': 'Ukrainian Hryvnia',
    'UGX': 'Ugandan Shilling',
    'UYU': 'Uruguayan Peso',
    'UZS': 'Uzbekistani Som',
    'VES': 'Venezuelan Bolívar Soberano',
    'VND': 'Vietnamese Dong',
    'VUV': 'Vanuatu Vatu',
    'WST': 'Samoan Tala',
    'XAF': 'Central African CFA Franc',
    'XCD': 'East Caribbean Dollar',
    'XDR': 'Special Drawing Rights',
    'XOF': 'West African CFA Franc',
    'XPF': 'CFP Franc',
    'YER': 'Yemeni Rial',
    'ZAR': 'South African Rand',
    'ZMW': 'Zambian Kwacha',
    'ZWL': 'Zimbabwean Dollar'
};

// Initialize currency dropdowns
function initCurrencyDropdowns() {
    const fromSelect = document.getElementById('curr-from');
    const toSelect = document.getElementById('curr-to');

    if (!fromSelect || !toSelect) return;

    fromSelect.innerHTML = '';
    toSelect.innerHTML = '';

    Object.keys(currencies).forEach(code => {
        fromSelect.add(new Option(`${code} - ${currencies[code]}`, code));
        toSelect.add(new Option(`${code} - ${currencies[code]}`, code));
    });

    // Set defaults
    fromSelect.value = 'USD';
    toSelect.value = 'IDR';
}

// Fetch exchange rates from API
async function fetchExchangeRates() {
    const cacheKey = 'econcalc_rates';
    const cacheTimeKey = 'econcalc_rates_time';

    // Check cache (24 hour validity)
    const cachedRates = localStorage.getItem(cacheKey);
    const cachedTime = localStorage.getItem(cacheTimeKey);

    if (cachedRates && cachedTime) {
        const cacheAge = Date.now() - parseInt(cachedTime);
        if (cacheAge < 24 * 60 * 60 * 1000) { // 24 hours
            exchangeRates = JSON.parse(cachedRates);
            ratesLastUpdated = new Date(parseInt(cachedTime));
            updateRateStatus();
            return;
        }
    }

    // Fetch fresh rates
    try {
        const statusEl = document.getElementById('curr-status');
        if (statusEl) statusEl.textContent = 'Memuat kurs...';

        const response = await fetch(CURRENCY_API);
        const data = await response.json();

        exchangeRates = data.rates;
        ratesLastUpdated = new Date();

        // Cache the rates
        localStorage.setItem(cacheKey, JSON.stringify(exchangeRates));
        localStorage.setItem(cacheTimeKey, Date.now().toString());

        updateRateStatus();
    } catch (error) {
        console.error('Error fetching rates:', error);
        const statusEl = document.getElementById('curr-status');
        if (statusEl) statusEl.textContent = 'Gagal memuat kurs. Menggunakan cache.';

        // Try to use cached rates even if expired
        if (cachedRates) {
            exchangeRates = JSON.parse(cachedRates);
        }
    }
}

function updateRateStatus() {
    const statusEl = document.getElementById('curr-status');
    if (statusEl && ratesLastUpdated) {
        const timeStr = ratesLastUpdated.toLocaleString('id-ID');
        statusEl.textContent = `Kurs terakhir update: ${timeStr}`;
    }
}

// Convert currency
function convertCurrency() {
    const resultValEl = document.getElementById('curr-result-val');
    const valueEl = document.getElementById('curr-value');
    const fromEl = document.getElementById('curr-from');
    const toEl = document.getElementById('curr-to');
    const rateEl = document.getElementById('curr-rate');

    if (!resultValEl || !valueEl || !fromEl || !toEl) return;

    if (!exchangeRates) {
        resultValEl.textContent = 'Loading...';
        return;
    }

    const amount = parseFloat(valueEl.value) || 0;
    const from = fromEl.value;
    const to = toEl.value;

    if (amount === 0) {
        resultValEl.textContent = '0';
        return;
    }

    // Convert through USD as base
    const amountInUSD = amount / exchangeRates[from];
    const result = amountInUSD * exchangeRates[to];

    const formattedResult = result.toLocaleString('id-ID', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
    });

    resultValEl.textContent = formattedResult;

    const rate = exchangeRates[to] / exchangeRates[from];
    if (rateEl) rateEl.textContent = `1 ${from} = ${rate.toFixed(4)} ${to}`;

    // Debounce save history
    clearTimeout(window.currencyDebounce);
    window.currencyDebounce = setTimeout(() => {
        if (typeof saveHistory === 'function') {
            saveHistory(`Kurs: ${amount.toLocaleString()} ${from} = ${formattedResult} ${to}`);
        }
    }, 1000);
}

// Swap currencies
function swapCurrencies() {
    const fromSelect = document.getElementById('curr-from');
    const toSelect = document.getElementById('curr-to');

    const temp = fromSelect.value;
    fromSelect.value = toSelect.value;
    toSelect.value = temp;

    convertCurrency();
}

// Refresh rates manually
async function refreshRates() {
    localStorage.removeItem('econcalc_rates');
    localStorage.removeItem('econcalc_rates_time');
    await fetchExchangeRates();
    convertCurrency();
}

// Initialize on load
document.addEventListener('DOMContentLoaded', function () {
    initCurrencyDropdowns();
    fetchExchangeRates().then(() => {
        convertCurrency();
    });
});
