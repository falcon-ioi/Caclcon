# 📱 E-Concalc Mobile - Android Client

Aplikasi native Android untuk platform E-Concalc, dibangun dengan **Kotlin** dan **Jetpack Compose**. Aplikasi ini berfungsi sebagai kalkulator ilmiah dan konverter offline-first yang berdiri sendiri.

![Kotlin](https://img.shields.io/badge/Kotlin-2.0+-7F52FF?logo=kotlin&logoColor=white)
![Compose](https://img.shields.io/badge/Jetpack--Compose-Latest-4285F4?logo=jetpackcompose&logoColor=white)
![Material3](https://img.shields.io/badge/Material--3-UI-6750A4?logo=materialdesign&logoColor=white)
![MinSDK](https://img.shields.io/badge/Min--SDK-24-34A853?logo=android&logoColor=white)

---

## 📋 Fitur Utama

| ID | Fitur | Deskripsi |
|----|-------|-----------|
| **FR-M01** | **Scientific Calculator** | Kalkulator ilmiah lengkap dengan operasi trigonometri, logaritma, dan fungsi memori (M+, M-, MR, MC). |
| **FR-M02** | **2nd Function** | Toggle untuk mengakses fungsi invers (sin⁻¹, cos⁻¹, tan⁻¹, ln, e^x). |
| **FR-M03** | **DEG/RAD Mode** | Pergantian antara mode Degree dan Radian untuk perhitungan trigonometri. |
| **FR-M04** | **Unit Converter** | Konversi satuan multi-kategori: panjang, berat, suhu, kecepatan, luas, volume. |
| **FR-M05** | **Currency Converter** | Konversi mata uang real-time dengan dukungan caching offline. |
| **FR-M06** | **Calculation History** | Riwayat perhitungan persisten menggunakan `HistoryManager` dengan local storage. |
| **FR-M07** | **Dark Theme** | UI gelap modern dengan Material 3 dan animasi halus. |

---

## 🚀 Panduan Instalasi

### Prerequisites
- **Android Studio** (Koala atau versi terbaru)
- **JDK** 17+
- **Android SDK** API 24+

### Langkah-langkah Setup

1. **Clone & Open**: Buka folder `e-concalc-mobile` di Android Studio.
    ```bash
    git clone https://github.com/falcon-ioi/Caclcon.git
    cd Caclcon/e-concalc-mobile
    ```
2. **Gradle Sync**: Jalankan proses *sync* Gradle dan tunggu hingga selesai.
3. **Build & Run**: Klik `Run` atau tekan `Shift + F10` untuk menjalankan di emulator / device.

---

## 🏗️ Arsitektur & Struktur

```
e-concalc-mobile/
├── app/src/main/
│   └── java/com/example/e_concalcmobile/
│       ├── MainActivity.kt               # Entry point
│       ├── navigation/
│       │   └── Screen.kt                 # Navigation routes
│       ├── ui/screens/
│       │   ├── CalculatorScreen.kt       # Scientific Calculator UI
│       │   ├── ConverterScreen.kt        # Unit Converter UI
│       │   └── CurrencyScreen.kt         # Currency Converter UI
│       ├── ui/theme/
│       │   ├── Color.kt                  # Color palette
│       │   ├── Theme.kt                  # Dark theme config
│       │   └── Type.kt                   # Typography
│       └── utils/
│           └── HistoryManager.kt         # Local history storage
├── build.gradle.kts                       # Project build config
├── settings.gradle.kts
└── gradle/
```

---

## 🛠️ Tech Stack

| Komponen | Teknologi |
|----------|-----------|
| **Language** | Kotlin 2.0+ |
| **UI Framework** | Jetpack Compose + Material 3 |
| **Navigation** | Compose Navigation |
| **Icons** | Material Icons Extended |
| **JSON** | Gson 2.10.1 |
| **Min SDK** | Android 7.0 (API 24) |
| **Target SDK** | Android 16 (API 36) |
| **Build System** | Gradle (Kotlin DSL) |

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

## 👨‍💻 Author

Developed with ❤️ by **Falcon IOI**
