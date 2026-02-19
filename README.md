# 🧮 E-Concalc Monorepo

Welcome to **E-Concalc** (Electronic Converter & Calculator), a full-featured calculator and converter platform. This repository is a unified monorepo containing both the **Android Mobile App** and the **Laravel Website**.

---

## 🏗️ Project Structure

```
E-Concalc/
├── e-concalc-web/        # Laravel website (PHP, Blade, CSS, JS)
├── e-concalc-mobile/     # Native Android app (Kotlin, Jetpack Compose)
├── .gitignore
└── README.md
```

- `e-concalc-web/`: Full-featured web application built with **Laravel**, featuring a responsive dark-themed UI with glassmorphism effects.
- `e-concalc-mobile/`: Native Android application built with **Kotlin** and **Jetpack Compose**, delivering an offline-first mobile experience.

---

## ✨ Features

### 📱 Android App (Mobile)
- **Scientific Calculator**: Full scientific operations with memory functions (M+, M-, MR, MC)
- **Unit Converter**: Convert between length, weight, temperature, speed, area, volume, and more
- **Currency Converter**: Real-time exchange rates with offline caching
- **Dark Theme**: Modern dark UI with smooth animations
- **Offline-First**: All core features work without internet

### 🌐 Website (Web)
- **Scientific Calculator**: Advanced calculator with keyboard support and 2nd function toggle
- **Unit Converter**: Comprehensive unit conversion across multiple categories
- **Currency Converter**: Live exchange rates powered by API
- **PWA Support**: Installable as a Progressive Web App
- **History**: Calculation history stored via local storage

---

## 🚀 Quick Start

### 1. Setup Website

```bash
cd e-concalc-web
composer install
npm install
cp .env.example .env
php artisan key:generate
php artisan serve
```

### 2. Setup Android

1. Open `e-concalc-mobile/` in **Android Studio**
2. Sync Gradle
3. Build & Run on emulator or device

---

## 👨‍💻 Author

Developed with ❤️ by **FalitoNGL**

© 2026 E-Concalc. All rights reserved.
