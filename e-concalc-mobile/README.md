# 📱 E-Concalc Mobile - Android Client

Aplikasi native Android untuk platform E-Concalc, dibangun dengan **Kotlin** dan **Jetpack Compose**. Aplikasi ini berfungsi sebagai interface mobile yang menyediakan fitur kalkulator, konverter satuan, dan konverter mata uang secara offline.

![Kotlin](https://img.shields.io/badge/Kotlin-1.9+-7F52FF?logo=kotlin&logoColor=white)
![Compose](https://img.shields.io/badge/Jetpack--Compose-1.5+-4285F4?logo=jetpackcompose&logoColor=white)
![Material3](https://img.shields.io/badge/Material--3-UI-6750A4?logo=materialdesign&logoColor=white)
![Android](https://img.shields.io/badge/Android-API_24+-3DDC84?logo=android&logoColor=white)

---

## 📋 Fitur Utama

| ID | Fitur | Deskripsi |
|----|---------|-----------| 
| **FR-M01** | **Scientific Calculator** | Kalkulator scientific lengkap dengan operasi trigonometri, logaritma, pangkat, dan konstanta. |
| **FR-M02** | **Unit Converter** | Konversi satuan meliputi panjang, berat, suhu, kecepatan, area, volume, dan lainnya. |
| **FR-M03** | **Currency Converter** | Konverter mata uang dengan kurs real-time dan dukungan offline caching. |
| **FR-M04** | **Dark Theme** | Antarmuka gelap modern menggunakan Material 3 dengan animasi smooth. |
| **FR-M05** | **Offline-First** | Semua fitur utama berfungsi tanpa koneksi internet. |
| **FR-M06** | **Navigation** | Bottom navigation untuk perpindahan antar fitur yang intuitif. |

---

## 📖 Deskripsi

E-Concalc Mobile adalah aplikasi Android native yang dirancang sebagai pendamping mobile dari platform E-Concalc Web. Aplikasi ini menyediakan tiga fitur utama (Kalkulator, Unit Converter, Currency Converter) dalam antarmuka Material 3 yang modern dengan dukungan dark theme.

---

## 📝 SRS - Feature List

### Functional Requirements

| ID | Feature | Deskripsi | Status |
|----|---------|-----------|--------|
| FR-01 | Scientific Calculator | Operasi dasar + trigonometri, log, ln, pangkat, akar, faktorial | ✅ Done |
| FR-02 | Unit Converter | Konversi 10+ kategori satuan | ✅ Done |
| FR-03 | Currency Converter | Konversi mata uang dengan API kurs | ✅ Done |
| FR-04 | Dark Theme | Material 3 dark theme | ✅ Done |
| FR-05 | Bottom Navigation | Navigasi antar screen | ✅ Done |
| FR-06 | Offline Mode | Cache data untuk akses offline | ✅ Done |

### Non-Functional Requirements

| ID | Requirement | Deskripsi |
|----|-------------|-----------|
| NFR-01 | Performance | Kalkulasi instan, UI 60fps |
| NFR-02 | Usability | Material 3 design, navigasi intuitif |
| NFR-03 | Compatibility | Android API 24+ (Android 7.0+) |
| NFR-04 | Reliability | Offline-first, error handling |

---

## 🎨 Mock-Up / Screenshots

> 💡 *Screenshots akan ditambahkan. Silakan berikan gambar mockup untuk ditampilkan di sini.*

### 1. Kalkulator Scientific
<!-- ![Calculator](docs/screenshots/01_calculator.png) -->

### 2. Unit Converter
<!-- ![Converter](docs/screenshots/02_converter.png) -->

### 3. Currency Converter
<!-- ![Currency](docs/screenshots/03_currency.png) -->

---

## 🚀 Panduan Instalasi

### Prerequisites
- **Android Studio** (Koala atau versi terbaru)
- **JDK** 17+
- **Android SDK** API 24+

### Langkah-langkah Setup

1. **Clone & Open**: 
   ```bash
   git clone https://github.com/falcon-ioi/Caclcon.git
   ```
   Buka folder `e-concalc-mobile/` di Android Studio.

2. **Gradle Sync**: Jalankan proses *sync* Gradle dan tunggu hingga selesai.

3. **Build**: Jalankan `Build > Make Project` atau langsung klik `Run`.

### ⚠️ Troubleshooting

| Error | Solusi |
|-------|--------|
| `Gradle sync failed` | Update Android Studio dan Gradle plugin |
| `SDK not found` | Set SDK path di `local.properties` |
| `Build failed` | Clean project: `Build > Clean Project` |

---

## 📁 Struktur Project

```
e-concalc-mobile/
├── app/
│   └── src/main/java/com/example/e_concalcmobile/
│       ├── navigation/
│       │   └── Screen.kt              # Navigation routes
│       └── ui/
│           ├── screens/
│           │   ├── CalculatorScreen.kt # Scientific calculator
│           │   ├── ConverterScreen.kt  # Unit converter
│           │   └── CurrencyScreen.kt   # Currency converter
│           └── theme/                  # Material 3 theme
├── build.gradle.kts
├── settings.gradle.kts
└── gradle.properties
```

---

## 📜 License
All rights reserved.

## 👨‍💻 Author
**Falcon IOI**
