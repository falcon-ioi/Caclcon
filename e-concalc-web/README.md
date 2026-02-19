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
graph TD
    subgraph "E-Concalc Web"
        UC1["🖩 Menggunakan Kalkulator Ilmiah"]
        UC2["🔢 Toggle Mode DEG/RAD"]
        UC3["💾 Fungsi Memori M+/M-/MR/MC"]
        UC4["🔄 Toggle Fungsi 2nd"]
        UC5["⌨️ Input via Keyboard"]
        UC6["📐 Konversi Satuan"]
        UC7["💱 Konversi Mata Uang"]
        UC8["📜 Melihat Riwayat"]
        UC9["📲 Install PWA"]
    end

    User["👤 User"] --> UC1
    User --> UC2
    User --> UC3
    User --> UC4
    User --> UC5
    User --> UC6
    User --> UC7
    User --> UC8
    User --> UC9

    UC7 -. "fetch API" .-> API["🌐 Exchange Rate API"]
```

### Activity Diagram - Calculator Flow

```mermaid
flowchart TD
    A([Start]) --> B[Buka Halaman E-Concalc]
    B --> C{Pilih Tab}

    C -->|Kalkulator| D[Tampilkan Kalkulator Ilmiah]
    C -->|Konverter| E[Tampilkan Konverter Satuan]
    C -->|Mata Uang| F[Tampilkan Konverter Mata Uang]

    D --> D1[Input Angka / Operator]
    D1 --> D2{Tekan =}
    D2 -->|Ya| D3[Hitung Hasil]
    D3 --> D4[Tampilkan Hasil di Display]
    D4 --> D5[Simpan ke Riwayat - Local Storage]
    D5 --> D1
    D2 -->|Tidak| D1

    E --> E1[Pilih Kategori Satuan]
    E1 --> E2[Input Nilai]
    E2 --> E3[Pilih Satuan Asal & Tujuan]
    E3 --> E4[Hitung Konversi]
    E4 --> E5[Tampilkan Hasil]

    F --> F1[Fetch Kurs dari API]
    F1 --> F2[Input Jumlah]
    F2 --> F3[Pilih Mata Uang Asal & Tujuan]
    F3 --> F4[Hitung Konversi Real-time]
    F4 --> F5[Tampilkan Hasil & Kurs]
```

### Sequence Diagram - Currency Conversion

```mermaid
sequenceDiagram
    actor User
    participant Browser
    participant Laravel as Laravel Server
    participant API as Exchange Rate API

    User->>Browser: Buka Tab Mata Uang
    Browser->>Laravel: GET / (route home)
    Laravel-->>Browser: Render Blade Template

    User->>Browser: Input jumlah & pilih mata uang
    Browser->>API: Fetch exchange rates
    API-->>Browser: JSON response (rates)
    Browser->>Browser: Hitung konversi (JavaScript)
    Browser-->>User: Tampilkan hasil konversi

    User->>Browser: Klik Refresh Kurs
    Browser->>API: Fetch latest rates
    API-->>Browser: Updated JSON rates
    Browser-->>User: Update tampilan kurs
```

### Class Diagram

```mermaid
classDiagram
    class CalculatorController {
        +index() View
        +export(Request, format) Redirect
    }

    class Controller {
        <<abstract>>
    }

    class BladeView {
        +calculator/index.blade.php
        -tabs: calc, conv, currency
        -history-section
    }

    class JavaScript_Script {
        +clearDisplay()
        +appendNumber(num)
        +appendOperator(op)
        +calculate()
        +backspace()
        +toggleDegRad()
        +toggle2nd()
        +memorySave()
        +memoryRecall()
        +memorySub()
        +memoryClear()
        +convert()
        +switchTab(tab)
        +saveHistory()
        +loadHistory()
    }

    class JavaScript_Currency {
        +fetchRates()
        +convertCurrency()
        +refreshRates()
    }

    class ServiceWorker {
        +install()
        +activate()
        +fetch()
    }

    class LocalStorage {
        +getItem(key)
        +setItem(key, value)
        +removeItem(key)
    }

    Controller <|-- CalculatorController
    CalculatorController --> BladeView : renders
    BladeView --> JavaScript_Script : includes
    BladeView --> JavaScript_Currency : includes
    BladeView --> ServiceWorker : registers
    JavaScript_Script --> LocalStorage : saves history
    JavaScript_Currency ..> ExternalAPI : fetches rates

    class ExternalAPI {
        <<external>>
        +GET /latest?base=USD
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
