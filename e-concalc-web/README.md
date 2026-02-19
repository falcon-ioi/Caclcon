# 📚 Dokumentasi Project (Progress Report)

## E-Concalc Web — Scientific Calculator & Converter Platform

<p align="center">
  <img src="public/images/logo.png" alt="E-Concalc Logo" width="120">
</p>

![Laravel](https://img.shields.io/badge/Laravel-10-red?logo=laravel)
![PHP](https://img.shields.io/badge/PHP-8.1+-blue?logo=php)
![JavaScript](https://img.shields.io/badge/JavaScript-ES6+-F7DF1E?logo=javascript&logoColor=black)
![CSS3](https://img.shields.io/badge/CSS3-Custom-1572B6?logo=css3)
![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?logo=mysql&logoColor=white)
![PWA](https://img.shields.io/badge/PWA-Supported-5A0FC8?logo=pwa)

---

## 📖 Deskripsi

E-Concalc Web adalah platform kalkulator ilmiah dan konverter berbasis web yang dirancang untuk kebutuhan akademik dan penggunaan sehari-hari. Aplikasi ini mendukung **kalkulator ilmiah** lengkap dengan fungsi trigonometri dan memori, **konverter satuan** multi-kategori, serta **konverter mata uang** real-time.

### Tujuan Utama:
- Menyediakan kalkulator ilmiah online yang lengkap dan responsif
- Mendukung konversi satuan untuk berbagai kategori (panjang, berat, suhu, kecepatan, luas, volume)
- Menyediakan konversi mata uang real-time menggunakan API eksternal
- Mendukung instalasi sebagai Progressive Web App (PWA)
- Menyimpan riwayat perhitungan menggunakan local storage

### Tech Stack:
- **Backend:** Laravel 10
- **Frontend:** Blade Templates + Vanilla JavaScript
- **Styling:** Vanilla CSS (Glassmorphism Dark Theme)
- **Database:** MySQL 8.0
- **Build:** Vite
- **PWA:** Service Worker + Web App Manifest

---

## 📋 User Story

| ID | User Story | Priority |
|----|------------|----------|
| US-01 | Sebagai user, saya ingin menggunakan kalkulator ilmiah dengan operasi trigonometri, logaritma, dan fungsi memori | High |
| US-02 | Sebagai user, saya ingin menggunakan keyboard untuk input perhitungan agar lebih cepat | High |
| US-03 | Sebagai user, saya ingin toggle antara mode `DEG` dan `RAD` untuk perhitungan trigonometri | Medium |
| US-04 | Sebagai user, saya ingin mengonversi satuan antar berbagai kategori (panjang, berat, suhu, dll.) | High |
| US-05 | Sebagai user, saya ingin mengonversi mata uang dengan kurs real-time | High |
| US-06 | Sebagai user, saya ingin melihat riwayat perhitungan yang saya lakukan | Medium |
| US-07 | Sebagai user, saya ingin menginstall aplikasi sebagai PWA di perangkat saya | Low |
| US-08 | Sebagai user, saya ingin menggunakan tombol `2nd` untuk mengakses fungsi sekunder (sin⁻¹, cos⁻¹, dll.) | Medium |

---

## 📝 SRS - Feature List

### Functional Requirements

| ID | Feature | Deskripsi | Status |
|----|---------|-----------|--------|
| FR-01 | Scientific Calculator | Kalkulator dengan operasi dasar dan ilmiah | ✅ Done |
| FR-02 | 2nd Function Toggle | Tombol 2nd untuk fungsi invers (sin⁻¹, cos⁻¹, tan⁻¹, dll.) | ✅ Done |
| FR-03 | Memory Functions | M+, M-, MR, MC untuk manajemen memori kalkulator | ✅ Done |
| FR-04 | DEG/RAD Mode | Toggle antara mode Degree dan Radian | ✅ Done |
| FR-05 | Keyboard Input | Dukungan input via keyboard fisik | ✅ Done |
| FR-06 | Unit Converter | Konversi antar satuan multi-kategori | ✅ Done |
| FR-07 | Currency Converter | Konversi mata uang dengan kurs real-time | ✅ Done |
| FR-08 | Calculation History | Riwayat perhitungan disimpan di local storage | ✅ Done |
| FR-09 | PWA Support | Installable sebagai Progressive Web App | ✅ Done |
| FR-10 | Responsive Design | Tampilan responsif untuk desktop dan mobile | ✅ Done |

### Non-Functional Requirements

| ID | Requirement | Deskripsi |
|----|-------------|-----------|
| NFR-01 | Performance | Perhitungan instan tanpa loading |
| NFR-02 | Usability | UI intuitif dengan dark theme dan glassmorphism |
| NFR-03 | Offline | Fitur kalkulator dan konverter satuan bekerja offline via PWA |
| NFR-04 | Compatibility | Kompatibel dengan semua browser modern |
| NFR-05 | Security | CSRF protection, input validation |

---

## 📊 UML Diagrams

### Use Case Diagram

```mermaid
flowchart LR
    User(("👤 User"))

    subgraph UC_CALC ["🖩 Kalkulator Ilmiah"]
        UC1["Melakukan Perhitungan Dasar\n+, -, ×, ÷, %"]
        UC2["Menggunakan Fungsi Ilmiah\nsin, cos, tan, log, ln, √"]
        UC3["Toggle Mode DEG / RAD"]
        UC4["Toggle Fungsi 2nd\nsin⁻¹, cos⁻¹, tan⁻¹, eˣ"]
        UC5["Menggunakan Memori\nMC, MR, M+, M-"]
        UC6["Input via Keyboard"]
    end

    subgraph UC_CONV ["📐 Konverter Satuan"]
        UC7["Pilih Kategori Satuan\npanjang, berat, suhu, waktu,\nkecepatan, luas, volume, data,\nenergi, gaya, sudut, frekuensi, daya"]
        UC8["Konversi Antar Satuan"]
        UC9["Swap Satuan Asal & Tujuan"]
    end

    subgraph UC_CURR ["💱 Konverter Mata Uang"]
        UC10["Konversi Mata Uang\n160+ mata uang dunia"]
        UC11["Swap Mata Uang"]
        UC12["Refresh Kurs Terbaru"]
    end

    subgraph UC_OTHER ["📱 Fitur Umum"]
        UC13["Melihat Riwayat Perhitungan"]
        UC14["Menghapus Riwayat"]
        UC15["Install sebagai PWA"]
        UC16["Ganti Tema Dark / Light"]
    end

    User --> UC_CALC
    User --> UC_CONV
    User --> UC_CURR
    User --> UC_OTHER

    UC10 -. "fetch API" .-> API["🌐 ExchangeRate API\napi.exchangerate-api.com"]
```

### Activity Diagram - Alur Utama Aplikasi

```mermaid
flowchart TD
    A([User Membuka E-Concalc]) --> B[Laravel Mengirim Halaman]
    B --> C[Service Worker Mendaftarkan Cache]
    C --> D[Halaman Dimuat dengan Tab Kalkulator Aktif]
    D --> E{User Memilih Tab}

    E -->|"🖩 Kalkulator"| F[Tab Kalkulator Aktif]
    F --> F1[User Menekan Tombol Angka / Operator]
    F1 --> F2{User Tekan '=' ?}
    F2 -->|Ya| F3["calculate\(\) — Evaluasi Ekspresi"]
    F3 --> F4[Tampilkan Hasil di Display]
    F4 --> F5["saveHistory\(\) → localStorage"]
    F5 --> F1
    F2 -->|Tidak| F1

    E -->|"📐 Konverter"| G[Tab Konverter Aktif]
    G --> G1["Pilih Kategori\n(length, weight, temperature, dll.)"]
    G1 --> G2["updateUnits\(\) — Isi Dropdown Satuan"]
    G2 --> G3[User Input Nilai]
    G3 --> G4["convert\(\) — Hitung dengan Faktor Konversi"]
    G4 --> G5[Tampilkan Hasil Konversi]
    G5 --> G3

    E -->|"💱 Mata Uang"| H[Tab Mata Uang Aktif]
    H --> H1["fetchExchangeRates\(\)\nGET api.exchangerate-api.com"]
    H1 --> H2{API Berhasil?}
    H2 -->|Ya| H3[Simpan Rates ke Variable]
    H2 -->|Gagal| H4[Tampilkan Pesan Error]
    H3 --> H5[User Input Jumlah & Pilih Mata Uang]
    H5 --> H6["convertCurrency\(\) — Hitung Konversi"]
    H6 --> H7[Tampilkan Hasil & Info Kurs]
    H7 --> H5
```

### Sequence Diagram - Interaksi Ketiga Fitur

```mermaid
sequenceDiagram
    actor User
    participant Browser as Browser / JavaScript
    participant Laravel as Laravel Server
    participant SW as Service Worker
    participant LS as Local Storage
    participant API as ExchangeRate API

    Note over User, API: === Memuat Halaman ===
    User->>Browser: Akses localhost:8000
    Browser->>Laravel: GET / (route home)
    Laravel-->>Browser: Render calculator/index.blade.php
    Browser->>SW: Register service worker
    SW->>SW: Cache static assets (CSS, JS, images)

    Note over User, API: === Kalkulator Ilmiah ===
    User->>Browser: Klik tombol angka & operator
    Browser->>Browser: appendNumber() / appendOperator()
    User->>Browser: Klik tombol '='
    Browser->>Browser: calculate() — eval ekspresi
    Browser-->>User: Tampilkan hasil di display
    Browser->>LS: saveHistory(operationText)

    Note over User, API: === Konverter Satuan ===
    User->>Browser: Pilih tab Konverter
    Browser->>Browser: switchTab('conv')
    User->>Browser: Pilih kategori (misal: length)
    Browser->>Browser: updateUnits() — isi dropdown
    User->>Browser: Input nilai & pilih satuan
    Browser->>Browser: convert() — hitung dengan factors
    Browser-->>User: Tampilkan hasil konversi

    Note over User, API: === Konverter Mata Uang ===
    User->>Browser: Pilih tab Mata Uang
    Browser->>Browser: switchTab('currency')
    Browser->>API: GET /v4/latest/USD
    API-->>Browser: JSON {rates: {IDR: 15800, ...}}
    User->>Browser: Input jumlah & pilih mata uang
    Browser->>Browser: convertCurrency()
    Browser-->>User: Tampilkan hasil & kurs

    Note over User, API: === Riwayat ===
    User->>Browser: Lihat section Riwayat
    Browser->>LS: loadHistory()
    LS-->>Browser: Array of history items
    Browser-->>User: Render daftar riwayat
```

### Class Diagram

```mermaid
classDiagram
    class CalculatorController {
        +index() View
        +export(Request request, String format) Redirect
    }

    class Controller {
        <<abstract>>
    }

    class Routes {
        GET / → CalculatorController.index
        GET /export/format → CalculatorController.export
    }

    class ScriptJS {
        -display: HTMLElement
        -currentInput: String
        -isDegree: Boolean
        -isSecondary: Boolean
        -memoryValue: Number
        +switchTab(tab) void
        +appendNumber(num) void
        +appendOperator(op) void
        +appendFunction(func) void
        +appendPoint() void
        +clearDisplay() void
        +backspace() void
        +calculate() void
        +toggleMode() void
        +toggleSecondary() void
        +memoryClear() void
        +memoryRead() void
        +memoryAdd() void
        +memorySub() void
        +updateUnits() void
        +convert() void
        +swapUnits() void
        +saveHistory(text) void
        +loadHistory() void
        +clearHistory() void
        +toggleTheme() void
    }

    class CurrencyJS {
        -CURRENCY_API: String
        -exchangeRates: Object
        -ratesLastUpdated: Date
        -currencies: Object
        +initCurrencyDropdowns() void
        +fetchExchangeRates() Promise
        +updateRateStatus() void
        +convertCurrency() void
        +swapCurrencies() void
        +refreshRates() void
    }

    class ServiceWorkerSW {
        -STATIC_CACHE: String
        -DYNAMIC_CACHE: String
        -CACHE_LIMIT: Number
        +install() void
        +activate() void
        +fetch() Response
        +cacheFirst(request) Response
        +networkFirst(request) Response
        +staleWhileRevalidate(request) Response
        +limitCacheSize(name, max) void
    }

    class LocalStorage {
        +getItem(key) String
        +setItem(key, value) void
        +removeItem(key) void
    }

    class ExchangeRateAPI {
        <<external>>
        +GET /v4/latest/USD : JSON
    }

    Controller <|-- CalculatorController
    Routes --> CalculatorController : maps to
    CalculatorController --> BladeTemplate : renders
    BladeTemplate --> ScriptJS : loads
    BladeTemplate --> CurrencyJS : loads
    BladeTemplate --> ServiceWorkerSW : registers
    ScriptJS --> LocalStorage : read/write history
    CurrencyJS --> ExchangeRateAPI : fetch rates
    ServiceWorkerSW --> LocalStorage : cache management

    class BladeTemplate {
        +calculator/index.blade.php
        -tab: Kalkulator
        -tab: Konverter Satuan
        -tab: Mata Uang
        -section: Riwayat
    }
```

---

## 🎨 Mock-Up / Screenshots

> **Catatan:** Screenshot akan ditambahkan setelah mockup tersedia.

### 1. Halaman Kalkulator
<!-- ![Calculator](docs/screenshots/01_calculator.png) -->

### 2. Halaman Konverter Satuan
<!-- ![Unit Converter](docs/screenshots/02_converter.png) -->

### 3. Halaman Konverter Mata Uang
<!-- ![Currency Converter](docs/screenshots/03_currency.png) -->

---

## 🔄 SDLC (Software Development Life Cycle)

**Metodologi:** Waterfall dengan iterasi

| Phase | Aktivitas | Output |
|-------|-----------|--------|
| **1. Planning** | Requirement gathering, user story | PRD, User Stories |
| **2. Analysis** | SRS, feature prioritization | Feature List, SRS Doc |
| **3. Design** | UI mockups, database design | Mockups, ERD |
| **4. Development** | Coding, unit testing | Source code |
| **5. Testing** | Feature testing, browser testing | Test cases |
| **6. Deployment** | Server setup, deployment | Live application |
| **7. Maintenance** | Bug fixes, feature updates | Patches, updates |

---

## 🚀 Instalasi

### Prerequisites

Pastikan Anda sudah menginstall:
- **PHP** >= 8.1
- **Composer** >= 2.0
- **Node.js** >= 18.0
- **NPM** >= 9.0
- **MySQL** >= 8.0
- **Git**

### Langkah 1: Clone Repository

```bash
git clone https://github.com/falcon-ioi/Caclcon.git
cd Caclcon/e-concalc-web
```

### Langkah 2: Install Dependencies

```bash
# Install PHP dependencies
composer install

# Install Node.js dependencies
npm install
```

### Langkah 3: Konfigurasi Environment

```bash
# Copy file environment
cp .env.example .env

# Generate application key
php artisan key:generate
```

**Edit file `.env`** dan sesuaikan konfigurasi database:

```env
DB_CONNECTION=mysql
DB_HOST=127.0.0.1
DB_PORT=3306
DB_DATABASE=ecalc
DB_USERNAME=root
DB_PASSWORD=
```

### Langkah 4: Build Assets

```bash
# Build untuk production
npm run build

# atau untuk development (dengan hot reload)
npm run dev
```

### Langkah 5: Jalankan Server

```bash
php artisan serve
```

Aplikasi akan berjalan di: **http://localhost:8000**

### ⚠️ Troubleshooting

| Error | Solusi |
|-------|--------|
| `Vite manifest not found` | Jalankan `npm run build` |
| `Permission denied` | Jalankan `chmod -R 775 storage bootstrap/cache` |
| `Class not found` | Jalankan `composer dump-autoload` |

---

## 📁 Struktur Project

```
e-concalc-web/
├── app/
│   └── Http/Controllers/        # Controller (CalculatorController)
├── public/
│   ├── css/style.css            # Stylesheet utama (dark theme)
│   ├── images/                  # Logo & banner
│   ├── manifest.json            # PWA manifest
│   └── sw.js                    # Service Worker
├── resources/
│   └── views/
│       └── calculator/          # Blade templates
├── routes/
│   └── web.php                  # Route definitions
├── composer.json
├── vite.config.js
└── ...
```

---

## 📜 License

MIT License

## 👨‍💻 Author

Developed with ❤️ by **Falcon IOI**
