<p align="center">
  <img src="docs/images/ecalc-banner.png" alt="E-Concalc Banner" width="600">
</p>

# 🧮 E-Concalc Monorepo by Falcon IOI

<p align="center">
  <img src="docs/images/logo.png" alt="E-Concalc Logo" width="100">
</p>

Welcome to **E-Concalc** (Electronic Converter & Calculator), a full-featured scientific calculator and converter platform. This repository is a unified monorepo containing both the **Android Mobile App** and the **Laravel Website**.

---

## 🏗️ Project Structure

- **[e-concalc-mobile/](./e-concalc-mobile)**: Native Android application built with Kotlin and Jetpack Compose. Features a modern dark-themed UI with smooth animations.
- **[e-concalc-web/](./e-concalc-web)**: Full-featured website built with Laravel 10, featuring a responsive dark-themed UI with glassmorphism effects and PWA support.

---

## ✨ Features

### 📱 Android App (Mobile)
- **Scientific Calculator**: Full scientific operations including trigonometry, logarithms, and memory functions (M+, M-, MR, MC).
- **Unit Converter**: Convert between length, weight, temperature, speed, area, volume, and more.
- **Currency Converter**: Real-time exchange rates with offline caching support.
- **Dark Theme UI**: Sleek dark interface with Jetpack Compose and Material 3.
- **Calculation History**: Persistent history managed via `HistoryManager` with local storage.
- **Offline-First**: All core features work without internet connection.

### 🌐 Website (Web Platform)
- **Scientific Calculator**: Advanced calculator with keyboard support, 2nd function toggle, and `DEG/RAD` mode.
- **Unit Converter**: Comprehensive unit conversion across multiple categories.
- **Currency Converter**: Live exchange rates powered by external API.
- **PWA Support**: Installable as a Progressive Web App with offline capabilities.
- **Calculation History**: History stored via browser local storage.
- **Responsive Design**: Glassmorphism dark-themed UI that works on all devices.

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
- Open `e-concalc-mobile/` in **Android Studio**.
- Sync Gradle.
- Build & Run on emulator or device.

---

## 👨‍💻 Author
Developed with ❤️ by **Falcon IOI**

---

© 2026 E-Concalc by Falcon IOI. All rights reserved.
