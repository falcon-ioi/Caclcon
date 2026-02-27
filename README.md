<p align="center">
  <img src="docs/images/ecalc-banner.png" alt="E-Concalc Banner" width="600">
</p>

# E-Concalc Monorepo

E-Concalc (Electronic Converter & Calculator) adalah platform kalkulator ilmiah dan konverter lintas platform yang mendukung **sinkronisasi riwayat perhitungan** antar perangkat. Dirancang menggunakan arsitektur *Client-Server*, sistem ini memisahkan backend berbasis Laravel dengan klien mobile native Android menggunakan Kotlin & Jetpack Compose.

---

## 🏗️ Arsitektur & Repositori

Sistem ini dikelola dalam format *monorepo* yang terdiri dari dua komponen teknis utama:

- 🌐 **[E-Concalc Web (Backend & Frontend)](./e-concalc-web/README.md)**
  Berperan sebagai *Core System* dan *API Provider*. Mengelola logika kalkulator, konverter, basis data riwayat, autentikasi, dan menyediakan REST API untuk sinkronisasi lintas platform.

- 📱 **[E-Concalc Mobile (Android Client)](./e-concalc-mobile/README.md)**
  Aplikasi klien *native* Android yang mengonsumsi REST API untuk menyediakan kalkulator ilmiah, konverter satuan & mata uang, serta sinkronisasi riwayat secara efisien.

---

## 👥 User Story

| Aktor | Skenario | Kebutuhan | Tujuan |
| :--- | :--- | :--- | :--- |
| **Guest** | Perhitungan Ilmiah | Menggunakan kalkulator untuk operasi dasar & ilmiah tanpa login. | Kemudahan akses tanpa registrasi. |
| **Guest** | Konversi Satuan | Mengonversi satuan dari 15 kategori (panjang, suhu, berat, dll). | Mendapatkan hasil konversi cepat dan akurat. |
| **Guest** | Konversi Mata Uang | Mengonversi nilai mata uang real-time dari 52 mata uang dunia. | Mengetahui kurs aktual secara instan. |
| **User** | Autentikasi | Login/register menggunakan email atau Google OAuth. | Mengakses fitur sinkronisasi riwayat. |
| **User** | Sinkronisasi Riwayat | Menyimpan riwayat perhitungan ke server dan mengakses lintas perangkat. | Konsistensi data perhitungan antar platform. |
| **User** | PWA Install | Menginstal website sebagai Progressive Web App. | Mengakses kalkulator secara offline dan cepat. |

---

## 📑 Feature List (SRS) - Technical Detail

| Modul | Fitur | Deskripsi Teknis |
| :--- | :--- | :--- |
| **Calculator** | Scientific Calculator | Operasi matematika dasar & ilmiah (trigonometri, logaritma, faktorial) dengan mode DEG/RAD dan fungsi memori (M+, M-, MR, MC). |
| **Converter** | Unit Converter | Konversi antar satuan dalam 15 kategori termasuk panjang, berat, suhu, kecepatan, luas, volume, tekanan, dan lainnya. |
| **Currency** | Currency Converter | Konversi mata uang real-time menggunakan API eksternal dengan dukungan 52 mata uang dan offline caching. |
| **Security** | Auth System | Implementasi Laravel Sanctum untuk *bearer token management* dan Google Socialite/Credential Manager API. |
| **Security** | Input Validation | Validasi input pada sisi klien dan server, proteksi terhadap SQL Injection, XSS, dan CSRF. |
| **Sync** | REST API Gateway | Antarmuka komunikasi *stateless* (`POST/GET/DELETE /api/history`) untuk sinkronisasi riwayat JSON lintas platform. |
| **Mobile Core** | Jetpack Compose UI | Arsitektur Android modern dengan Material 3, navigasi deklaratif, dan dark theme responsif. |
| **Web Core** | PWA Support | Service Worker dan Web App Manifest untuk instalasi dan offline caching pada browser. |

---

## 📊 Dokumentasi Perancangan (UML)

Sistem dirancang menggunakan metodologi orientasi objek dengan dokumentasi UML sebagai berikut:

| Diagram | Visualisasi | Deskripsi Teknis |
| :--- | :---: | :--- |
| **Use Case** | <img src="./docs/diagrams/Usecase Diagram.png" width="300"> | Mendefinisikan interaksi antara Guest dan Authenticated User dengan sistem kalkulator, konverter, dan sinkronisasi. |
| **Activity** | <img src="./docs/diagrams/Activity Diagram.png" width="300"> | Menggambarkan alur kerja sistem mulai dari input kalkulator hingga sinkronisasi riwayat dan alur autentikasi. |
| **Sequence (Kalkulator)** | <img src="./docs/diagrams/Sequence Diagram Kalkulator Ilmiah.png" width="300"> | Menjelaskan urutan pertukaran pesan antar komponen untuk fungsi kalkulator ilmiah. |
| **Sequence (Konverter)** | <img src="./docs/diagrams/Sequence Diagram Konverter Satuan.png" width="300"> | Menjelaskan urutan pertukaran pesan antar komponen untuk fungsi konverter satuan. |
| **Sequence (Mata Uang)** | <img src="./docs/diagrams/Sequence Diagram Konverter Mata Uang.png" width="300"> | Menjelaskan urutan pertukaran pesan antar komponen untuk fungsi konverter mata uang. |
| **ERD** | <img src="./docs/diagrams/ERD (Entity Relationship Diagram).png" width="300"> | Struktur basis data logis yang menggambarkan relasi antar entitas users, riwayat, tokens, dan sessions. |

---

## 🔄 System Development Life Cycle (SDLC)

Pengembangan proyek ini mengikuti metodologi **Waterfall** yang terstruktur secara sekuensial:

| Fase | Aktivitas Utama | Output |
| :--- | :--- | :--- |
| **Requirements** | Analisis kebutuhan fungsional dan non-fungsional aplikasi kalkulator & konverter lintas platform. | Dokumen Spesifikasi Kebutuhan (9 KF, 5 KNF). |
| **Design** | Perancangan arsitektur Client-Server, UI/UX dark theme, skema database MySQL, dan diagram UML. | Diagram UML & Desain Antarmuka. |
| **Implementation** | Pengkodean backend (Laravel 10 + PHP 8.1), frontend web (Blade + JS), dan mobile (Kotlin 2.0 + Jetpack Compose). | *Source Code* Monorepo. |
| **Testing** | Unit testing, pengujian fungsional (10 test case), pengujian integrasi API, dan pengujian keamanan (SQL Injection, XSS). | Laporan Hasil Pengujian. |
| **Deployment** | Integrasi sistem melalui REST API, konfigurasi PWA, dan persiapan lingkungan produksi. | Sistem Siap Operasional. |

---

## 🗄️ Database Schema (MySQL)

| Tabel | Kolom | Deskripsi |
| :--- | :--- | :--- |
| `users` | id, name, email, password, google_id, email_verified_at, remember_token | Akun pengguna dengan dukungan Google OAuth. |
| `riwayat` | id, user_id (FK), operasi, tipe (calc/conv/currency), created_at | Riwayat perhitungan yang terhubung ke pengguna. |
| `personal_access_tokens` | tokenable_type, tokenable_id, name, token, abilities | Token API Laravel Sanctum untuk autentikasi mobile. |
| `sessions` | id, user_id, ip_address, user_agent, payload, last_activity | Manajemen sesi web. |

> **Mode Tamu:** Tidak menyentuh database — menggunakan localStorage (web) / SharedPreferences (mobile).

---

## 🛠️ Tech Stack

| Komponen | Teknologi |
| :--- | :--- |
| Sistem Operasi | Windows 11 |
| IDE (Web) | Visual Studio Code |
| IDE (Mobile) | Android Studio |
| Backend Framework | Laravel 10 (PHP 8.1+) |
| Frontend Web | Blade Template + Vanilla JS + CSS |
| Mobile | Kotlin 2.0, Jetpack Compose, Material Design 3 |
| Database | MySQL 8.0 |
| Autentikasi | Laravel Sanctum + Google OAuth (Socialite & Credential Manager API) |
| HTTP Client (Mobile) | Retrofit 2 + OkHttp |
| REST API | JSON Stateless API |
| PWA | Service Worker + Web App Manifest |

---

## 👨‍💻 Profil Pengembang

- **Nama:** Raffelino Hizkia Marbun
- **NIM:** 2423102065
- **Program Studi:** Rekayasa Keamanan Siber
- **Institusi:** Politeknik Siber dan Sandi Negara
- **Dosen Pembimbing:** Rahmat Purwoko, S.T., M.T.
- **Tahun Akademik:** 2025/2026
