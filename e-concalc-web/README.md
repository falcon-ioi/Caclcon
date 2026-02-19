# 📚 Dokumentasi Project (Progress Report)

## E-Concalc Web - Platform Kalkulator & Konverter Elektronik

![Laravel](https://img.shields.io/badge/Laravel-11-red?logo=laravel)
![PHP](https://img.shields.io/badge/PHP-8.2+-blue?logo=php)
![JavaScript](https://img.shields.io/badge/JavaScript-ES6+-F7DF1E?logo=javascript&logoColor=black)
![CSS3](https://img.shields.io/badge/CSS3-Dark--Theme-1572B6?logo=css3)
![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?logo=mysql&logoColor=white)
![PWA](https://img.shields.io/badge/PWA-Supported-5A0FC8?logo=pwa)

<p align="center">
  <img src="public/images/ecalc-banner.png" alt="E-Concalc Banner" width="600"/>
</p>

---

## 📖 Deskripsi

E-Concalc Web adalah platform kalkulator dan konverter berbasis web yang dirancang untuk kebutuhan akademik dan profesional. Aplikasi ini mendukung **Kalkulator Scientific** (dengan fungsi trigonometri, logaritma, dan memory), **Unit Converter** (10+ kategori konversi), serta **Currency Converter** (kurs real-time). Dibangun menggunakan dark theme dengan efek glassmorphism dan mendukung PWA (Progressive Web App).

---

## 📋 User Story

| ID | User Story | Priority |
|----|------------|----------|
| US-01 | Sebagai user, saya ingin menggunakan kalkulator scientific dengan keyboard support | High |
| US-02 | Sebagai user, saya ingin mengkonversi satuan antar kategori (panjang, berat, suhu, dll) | High |
| US-03 | Sebagai user, saya ingin mengkonversi mata uang dengan kurs real-time | High |
| US-04 | Sebagai user, saya ingin menyimpan history kalkulasi di local storage | Medium |
| US-05 | Sebagai user, saya ingin menginstall aplikasi sebagai PWA untuk akses offline | Medium |
| US-06 | Sebagai user, saya ingin menggunakan tombol 2nd untuk mengakses fungsi scientific tambahan | Medium |
| US-07 | Sebagai user, saya ingin menggunakan memory functions (M+, M-, MR, MC) | Low |

---

## 📝 SRS - Feature List

### Functional Requirements

| ID | Feature | Deskripsi | Status |
|----|---------|-----------|--------|
| FR-01 | Scientific Calculator | Kalkulator dengan operasi dasar, trigonometri, logaritma, pangkat, akar | ✅ Done |
| FR-02 | Unit Converter | Konversi satuan: panjang, berat, suhu, kecepatan, area, volume, dll | ✅ Done |
| FR-03 | Currency Converter | Konversi mata uang dengan kurs API real-time | ✅ Done |
| FR-04 | Keyboard Support | Input kalkulator via keyboard fisik | ✅ Done |
| FR-05 | Memory Functions | M+, M-, MR, MC untuk menyimpan nilai sementara | ✅ Done |
| FR-06 | 2nd Function Toggle | Tombol 2nd untuk mengakses sin⁻¹, cos⁻¹, tan⁻¹, dll | ✅ Done |
| FR-07 | Calculation History | Riwayat kalkulasi tersimpan di local storage | ✅ Done |
| FR-08 | PWA Support | Installable Progressive Web App dengan service worker | ✅ Done |
| FR-09 | Export History | Export riwayat kalkulasi | ✅ Done |
| FR-10 | Responsive Design | Tampilan responsif untuk desktop dan mobile browser | ✅ Done |

### Non-Functional Requirements

| ID | Requirement | Deskripsi |
|----|-------------|-----------|
| NFR-01 | Performance | Kalkulasi instan tanpa delay, caching kurs mata uang |
| NFR-02 | Usability | Dark theme dengan glassmorphism, micro-animations |
| NFR-03 | Accessibility | Keyboard support, semantic HTML |
| NFR-04 | Reliability | Offline support via PWA dan service worker |
| NFR-05 | Compatibility | Cross-browser support (Chrome, Firefox, Edge, Safari) |

---

## 🎨 Mock-Up / Screenshots

> 💡 *Screenshots akan ditambahkan. Silakan berikan gambar mockup untuk ditampilkan di sini.*

### 1. Halaman Kalkulator Scientific
<!-- ![Calculator](docs/screenshots/01_calculator.png) -->

### 2. Halaman Unit Converter
<!-- ![Unit Converter](docs/screenshots/02_unit_converter.png) -->

### 3. Halaman Currency Converter
<!-- ![Currency Converter](docs/screenshots/03_currency_converter.png) -->

---

## 🔄 SDLC (Software Development Life Cycle)

**Metodologi:** Waterfall dengan iterasi

| Phase | Aktivitas | Output |
|-------|-----------|--------|
| **1. Planning** | Requirement gathering, user story | PRD, User Stories |
| **2. Analysis** | SRS, feature prioritization | Feature List, SRS Doc |
| **3. Design** | UI/UX design, database design | Mockups, ERD |
| **4. Development** | Coding, unit testing | Source code, tests |
| **5. Testing** | Feature testing, cross-browser testing | Test cases |
| **6. Deployment** | Server setup, deployment | Live application |
| **7. Maintenance** | Bug fixes, feature updates | Patches, updates |

---

## 🚀 Instalasi

### Prerequisites
Pastikan Anda sudah menginstall:
- **PHP** >= 8.2
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

### Langkah 4: Jalankan Server
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
│   └── Http/Controllers/     # Controller (CalculatorController)
├── public/
│   ├── css/style.css          # Main stylesheet (dark theme)
│   ├── images/                # Logo & banner
│   ├── manifest.json          # PWA manifest
│   └── sw.js                  # Service Worker
├── resources/
│   └── views/calculator/      # Blade templates
├── routes/
│   └── web.php                # Route definitions
├── composer.json
└── .env.example
```

---

## 📜 License
All rights reserved.

## 👨‍💻 Author
**Falcon IOI**
