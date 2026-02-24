<p align="center">
  <img src="../docs/images/logo.png" alt="E-Concalc Logo" width="100">
</p>

# 📱 E-Concalc Mobile - Android Client

Aplikasi native Android untuk platform E-Concalc, dibangun dengan **Kotlin** dan **Jetpack Compose**. Mendukung **autentikasi** (Login/Register + Google Sign-In), **sinkronisasi riwayat** via REST API, dan mode **guest** offline.

![Kotlin](https://img.shields.io/badge/Kotlin-2.0+-7F52FF?logo=kotlin&logoColor=white)
![Compose](https://img.shields.io/badge/Jetpack--Compose-Latest-4285F4?logo=jetpackcompose&logoColor=white)
![Material3](https://img.shields.io/badge/Material--3-UI-6750A4?logo=materialdesign&logoColor=white)
![Sanctum](https://img.shields.io/badge/Auth-Sanctum-FF2D20?logo=laravel&logoColor=white)
![MinSDK](https://img.shields.io/badge/Min--SDK-24-34A853?logo=android&logoColor=white)

---

## 📋 Fitur Utama

| ID | Fitur | Deskripsi |
|----|-------|-----------|
| **FR-M01** | **Scientific Calculator** | Kalkulator ilmiah lengkap dengan operasi trigonometri, logaritma, dan fungsi memori (M+, M-, MR, MC). |
| **FR-M02** | **2nd Function** | Toggle untuk mengakses fungsi invers (sin⁻¹, cos⁻¹, tan⁻¹, ln, e^x). |
| **FR-M03** | **DEG/RAD Mode** | Pergantian antara mode Degree dan Radian untuk perhitungan trigonometri. |
| **FR-M04** | **Unit Converter** | Konversi satuan 15 kategori: panjang, berat, suhu, kecepatan, luas, volume, dll. |
| **FR-M05** | **Currency Converter** | Konversi 52 mata uang real-time dengan caching offline. |
| **FR-M06** | **Login / Register** | Autentikasi username/password via REST API + Laravel Sanctum tokens. |
| **FR-M07** | **Google Sign-In** | Login dengan akun Google via Credential Manager API + opsi tambah akun baru. |
| **FR-M08** | **History Sync** | Riwayat per-user disinkronkan ke server via REST API. Guest menggunakan local storage. |
| **FR-M09** | **Logout** | TopAppBar dengan profil dropdown, info user, dan dialog konfirmasi logout. |
| **FR-M10** | **Dark Theme** | UI gelap modern Slate/Sky dengan Material 3 dan animasi halus. |

---

## 🔗 REST API Endpoints

Semua API dilindungi `auth:sanctum` (kecuali login/register):

| Method | Endpoint | Deskripsi |
|--------|----------|-----------|
| `POST` | `/api/login` | Login (username + password) |
| `POST` | `/api/register` | Register akun baru |
| `POST` | `/api/login/google` | Login/Register via Google |
| `POST` | `/api/logout` | Logout (revoke token) |
| `GET` | `/api/user` | Info user saat ini |
| `GET` | `/api/history?tipe=calc` | Ambil riwayat (filter: `calc`, `conv`, `currency`) |
| `POST` | `/api/history` | Simpan riwayat baru |
| `DELETE` | `/api/history` | Hapus semua riwayat |
| `DELETE` | `/api/history/{id}` | Hapus riwayat tertentu |

---

## 🚀 Panduan Instalasi

### Prerequisites
- **Android Studio** (Koala atau versi terbaru)
- **JDK** 17+
- **Android SDK** API 24+
- **Backend server** Laravel berjalan di `http://10.0.2.2:8080/` (emulator) atau IP lokal (device fisik)

### Langkah-langkah Setup

1. **Clone & Open**: Buka folder `e-concalc-mobile` di Android Studio.
    ```bash
    git clone https://github.com/falcon-ioi/Caclcon.git
    cd Caclcon/e-concalc-mobile
    ```
2. **Gradle Sync**: Jalankan proses *sync* Gradle dan tunggu hingga selesai.
3. **Konfigurasi API** (opsional): Jika menggunakan device fisik, ubah `BASE_URL` di `ApiClient.kt`:
    ```kotlin
    private const val BASE_URL = "http://192.168.x.x:8080/"
    ```
4. **Build & Run**: Klik `Run` atau tekan `Shift + F10`.

---

## 🏗️ Arsitektur & Struktur

```
e-concalc-mobile/
├── app/src/main/
│   ├── AndroidManifest.xml              # Permissions & config
│   ├── res/xml/network_security_config.xml  # HTTP cleartext policy
│   └── java/com/example/e_concalcmobile/
│       ├── MainActivity.kt              # Entry point + TopAppBar + Logout
│       ├── api/
│       │   ├── ApiClient.kt             # Retrofit client + auth interceptor
│       │   └── ApiService.kt            # API endpoints definition
│       ├── navigation/
│       │   └── Screen.kt                # Navigation routes (Splash/Login/Register/Main)
│       ├── ui/screens/
│       │   ├── SplashScreen.kt          # Auto-login check
│       │   ├── LoginScreen.kt           # Login + Google Sign-In + Add account
│       │   ├── RegisterScreen.kt        # Registration form
│       │   ├── CalculatorScreen.kt      # Scientific Calculator + API sync
│       │   ├── ConverterScreen.kt       # Unit Converter + history
│       │   └── CurrencyScreen.kt        # Currency Converter + history
│       ├── ui/theme/
│       │   ├── Color.kt                 # Slate/Sky color palette
│       │   ├── Theme.kt                 # Dark theme config
│       │   └── Type.kt                  # Typography
│       └── utils/
│           ├── HistoryManager.kt        # Per-user history + API sync
│           └── TokenManager.kt          # Auth token & user info management
├── build.gradle.kts                      # Dependencies (Retrofit, Sanctum, etc.)
├── settings.gradle.kts
└── gradle/libs.versions.toml
```

---

## 🛠️ Tech Stack

| Komponen | Teknologi |
|----------|-----------|
| **Language** | Kotlin 2.0+ |
| **UI Framework** | Jetpack Compose + Material 3 |
| **Navigation** | Compose Navigation |
| **HTTP Client** | Retrofit 2 + OkHttp + Gson |
| **Auth** | Laravel Sanctum (Bearer tokens) |
| **Google Sign-In** | Credential Manager API + Google Identity |
| **Icons** | Material Icons Extended |
| **Local Storage** | SharedPreferences (per-user keys) |
| **Min SDK** | Android 7.0 (API 24) |
| **Target SDK** | Android 15 (API 35) |
| **Build System** | Gradle 9.2 (Kotlin DSL) |

---

## 🔄 Alur User & Guest

```
┌──────────────┐
│  Splash Screen │
│  (Token check) │
└──────┬───────┘
       │
       ├── Token valid ──────► Main Screen (logged in)
       │                         ├── Calculator + API sync
       │                         ├── Converter + API sync
       │                         └── Currency + API sync
       │
       └── No token ──────► Login Screen
                              ├── Username/Password login
                              ├── Google Sign-In
                              ├── Add Google account
                              ├── Register link
                              └── Skip (Guest mode)
                                    └── Main Screen (guest)
                                          ├── Calculator (localStorage only)
                                          ├── Converter (localStorage only)
                                          └── Currency (localStorage only)
```

---

## 👨‍💻 Author

Developed with ❤️ by **Falcon IOI**
