<p align="center">
  <img src="docs/images/ecalc-banner.png" alt="E-Concalc Banner" width="600">
</p>

# 🧮 E-Concalc Monorepo by Falcon IOI

<p align="center">
  <img src="docs/images/logo.png" alt="E-Concalc Logo" width="100">
</p>

Welcome to **E-Concalc** (Electronic Converter & Calculator), a full-featured scientific calculator and converter platform with **cross-platform history synchronization**. This repository is a unified monorepo containing both the **Android Mobile App** and the **Laravel Website**.

---

## 🏗️ Project Structure

- **[e-concalc-mobile/](./e-concalc-mobile)**: Native Android application built with Kotlin and Jetpack Compose. Features authentication, API sync, and modern dark-themed UI.
- **[e-concalc-web/](./e-concalc-web)**: Full-featured website built with Laravel 10, featuring responsive dark-themed UI with glassmorphism effects, REST API, and PWA support.

---

## ✨ Features

### 📱 Android App (Mobile)
- **Scientific Calculator**: Full scientific operations including trigonometry, logarithms, and memory functions (M+, M-, MR, MC).
- **Unit Converter**: Convert between 15 categories (length, weight, temperature, speed, area, volume, etc.).
- **Currency Converter**: Real-time exchange rates with 52 currencies and offline caching.
- **Authentication**: Login/Register with username or Google Sign-In (Credential Manager API).
- **History Sync**: Per-user history synced to server via REST API (guest mode uses local storage only).
- **Dark Theme UI**: Sleek dark Slate/Sky interface with Jetpack Compose and Material 3.
- **Logout & Profile**: TopAppBar with profile dropdown and logout confirmation dialog.

### 🌐 Website (Web Platform)
- **Scientific Calculator**: Advanced calculator with keyboard support, 2nd function toggle, and `DEG/RAD` mode.
- **Unit Converter**: Comprehensive unit conversion across multiple categories.
- **Currency Converter**: Live exchange rates powered by external API.
- **Authentication**: Login/Register with username or Google OAuth (Laravel Socialite).
- **History Sync**: Per-user history saved to database via REST API (guest uses localStorage).
- **PWA Support**: Installable as a Progressive Web App with offline capabilities.
- **Responsive Design**: Glassmorphism dark-themed UI that works on all devices.

### 🔗 Cross-Platform Sync
Both platforms share the same backend REST API for history synchronization:
- `POST /api/history` — Save calculation/conversion history
- `GET /api/history` — Retrieve history (with optional `?tipe=calc|conv|currency` filter)
- `DELETE /api/history` — Clear all history

Login with the **same account** on both website and mobile app to see shared history.

---

## 🗄️ Database (MySQL)

| Table | Description |
|-------|-------------|
| `users` | User accounts (name, email, google_id, password) |
| `riwayat` | Calculation history linked to users via `user_id` FK |
| `personal_access_tokens` | Sanctum API tokens for mobile authentication |
| `sessions` | Web session management |

> **Guest mode**: Does not touch the database — uses localStorage (web) / SharedPreferences (mobile) only.

---

## 🚀 Quick Start

### 1. Setup Website
```bash
cd e-concalc-web
composer install
npm install
cp .env.example .env
php artisan key:generate
```

Edit `.env` and configure MySQL:
```env
DB_CONNECTION=mysql
DB_HOST=127.0.0.1
DB_PORT=3306
DB_DATABASE=ecalc
DB_USERNAME=root
DB_PASSWORD=
```

Create database and run migrations:
```bash
# Create 'ecalc' database in phpMyAdmin first, then:
php artisan migrate:fresh
php artisan serve --port=8080
```

### 2. Setup Android
- Open `e-concalc-mobile/` in **Android Studio**.
- Sync Gradle.
- Build & Run on emulator or device.
- **Note**: Uses `http://10.0.2.2:8080/` for API (emulator only). For physical devices, update `BASE_URL` in `ApiClient.kt` to your local IP.

---

## 🛠️ Tech Stack

| Component | Technology |
|-----------|------------|
| **Backend** | Laravel 10, PHP 8.1+ |
| **Frontend** | Blade + Vanilla JS + CSS |
| **Mobile** | Kotlin 2.0, Jetpack Compose, Material 3 |
| **Database** | MySQL 8.0 |
| **Auth** | Laravel Sanctum (API tokens) + Google OAuth |
| **API** | REST API with JSON responses |
| **HTTP Client** | Retrofit 2 + OkHttp (mobile) |
| **PWA** | Service Worker + Web App Manifest |

---

## 👨‍💻 Author
Developed with ❤️ by **Falcon IOI**

---

© 2026 E-Concalc by Falcon IOI. All rights reserved.
