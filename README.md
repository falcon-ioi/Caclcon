# 🧮 E-Concalc Monorepo by Falcon IOI

Welcome to **E-Concalc** (Electronic Converter & Calculator), a full-featured calculator and converter platform. This repository is a unified monorepo containing both the **Android Mobile App** and the **Laravel Web Application**.

<p align="center">
  <img src="e-concalc-web/public/images/logo.png" alt="E-Concalc Logo" width="150"/>
</p>

---

## 🏗️ Project Structure

- **[e-concalc-web/](./e-concalc-web)**: Web application built with **Laravel**, featuring a responsive dark-themed UI with glassmorphism effects and PWA support.
- **[e-concalc-mobile/](./e-concalc-mobile)**: Native Android application built with **Kotlin** and **Jetpack Compose**, delivering an offline-first mobile experience.

---

## ✨ Features

### 📱 Android App (Mobile)
- **Scientific Calculator**: Full scientific operations including trigonometry, logarithm, power, and constants.
- **Unit Converter**: Convert between length, weight, temperature, speed, area, volume, and more.
- **Currency Converter**: Real-time exchange rates with offline support.
- **Dark Theme UI**: Modern dark interface with smooth animations and Material 3 design.
- **Offline-First**: All core features work without internet connectivity.

### 🌐 Website (Web Platform)
- **Scientific Calculator**: Advanced calculator with keyboard support, memory functions (M+, M-, MR, MC), and 2nd function toggle.
- **Unit Converter**: Comprehensive unit conversion across 10+ categories.
- **Currency Converter**: Live exchange rates powered by external API.
- **PWA Support**: Installable as a Progressive Web App with offline caching.
- **Calculation History**: History stored via local storage for quick reference.
- **Responsive Design**: Glassmorphism dark theme with micro-animations.

---

## 🚀 Quick Start

### 1. Setup Website (Laravel)
```bash
cd e-concalc-web
composer install
npm install
cp .env.example .env
php artisan key:generate
php artisan serve
```

### 2. Setup Android App
- Open `e-concalc-mobile/` in **Android Studio**.
- Sync Gradle.
- Build & Run on emulator or device.

---

## 👨‍💻 Author
Developed with ❤️ by **Falcon IOI**

---

© 2026 E-Concalc by Falcon IOI. All rights reserved.
