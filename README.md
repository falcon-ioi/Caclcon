# 📚 Dokumentasi Project (Progress Report)

## E-Concalc - Electronic Conversion Calculator
![Laravel](https://img.shields.io/badge/Laravel-10.x-red?logo=laravel)
![PHP](https://img.shields.io/badge/PHP-8.1+-blue?logo=php)
![JavaScript](https://img.shields.io/badge/JavaScript-ES6+-yellow?logo=javascript)
![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?logo=mysql&logoColor=white)
![Google](https://img.shields.io/badge/OAuth-Google-4285F4?logo=google)

---

## 📖 Deskripsi
**E-Concalc** (Electronic Conversion Calculator) adalah platform kalkulator berbasis web yang modern dan komprehensif. Aplikasi ini dirancang untuk memenuhi berbagai kebutuhan perhitungan pengguna dalam satu tempat, mulai dari perhitungan matematis kompleks, konversi satuan, estimasi perencanaan finansial, hingga pemantauan kesehatan tubuh.

Dengan antarmuka **Dark Mode** yang premium dan responsif, E-Concalc memberikan pengalaman pengguna yang nyaman dan efisien di berbagai perangkat, serta mendukung sistem login hibrida (Guest & Google OAuth).

### Tujuan Utama:
- **All-in-One Tool**: Menggabungkan berbagai alat hitung (Kalkulator, Konverter, Finansial, Kesehatan) dalam satu aplikasi
- **User Experience Premium**: Menyajikan antarmuka modern dengan efek glassmorphism dan neon glow
- **Produktivitas**: Membantu pengguna melakukan perhitungan sehari-hari dengan cepat dan akurat
- **Pencatatan Riwayat**: Menyimpan log aktivitas perhitungan untuk referensi di masa mendatang
- **Aksesibilitas Tinggi**: Mendukung fitur Voice Command untuk input suara dan akses instan bagi pengguna tamu

### Tech Stack:
- **Backend:** Laravel 10 (PHP 8.1+)
- **Frontend:** Blade Templates + Vanilla JavaScript
- **Styling:** Vanilla CSS (Modern Dark UI)
- **Database:** MySQL 8.0
- **Auth:** Google OAuth (Laravel Socialite) + Custom Guard
- **Server:** Apache (XAMPP)

---

## 📋 User Story

| ID | User Story | Priority |
|----|------------|----------|
| US-01 | Sebagai user, saya ingin melakukan perhitungan matematika ilmiah (sin, cos, tan, log) | High |
| US-02 | Sebagai user, saya ingin masuk menggunakan akun Google agar lebih cepat | High |
| US-03 | Sebagai user, saya ingin mengonversi berbagai satuan (panjang, berat, suhu, dll) | High |
| US-04 | Sebagai user, saya ingin menghitung bunga tabungan atau cicilan pinjaman | High |
| US-05 | Sebagai user, saya ingin mengetahui BMI dan berat badan ideal saya | Medium |
| US-06 | Sebagai user, saya ingin melihat riwayat perhitungan yang pernah saya lakukan | Medium |
| US-07 | Sebagai user, saya ingin menggunakan perintah suara untuk berhitung | Medium |
| US-08 | Sebagai user, saya ingin menyimpan rencana finansial saya ke database | Medium |

---

## 📝 SRS - Feature List

### Functional Requirements
| ID | Feature | Deskripsi | Status |
|----|---------|-----------|--------|
| FR-01 | Authentication | Login Google via Socialite & Guest Access | ✅ Done |
| FR-02 | Scientific Calculator | Fungsi trigonometri, logaritma, eksponen, memory | ✅ Done |
| FR-03 | Unit Converter | Konversi 7+ Kategori (Panjang, Berat, Suhu, Data, dll) | ✅ Done |
| FR-04 | Currency Converter | Konversi mata uang asing real-time | ✅ Done |
| FR-05 | Financial Calculator | Hitung Bunga (Simple/Compound), Cicilan, Diskon | ✅ Done |
| FR-06 | Health Calculator | Hitung BMI & Berat Ideal (Devine Formula) | ✅ Done |
| FR-07 | Voice Command | Input perhitungan menggunakan suara (Web Speech API) | ✅ Done |
| FR-08 | History Log | Mencatat riwayat aktivitas perhitungan user | ✅ Done |
| FR-09 | Save Data | Menyimpan Rencana Finansial & Log Kesehatan | ✅ Done |
| FR-10 | Data Export | Ekspor riwayat ke format CSV/PDF | ✅ Done |

### Non-Functional Requirements
| ID | Requirement | Deskripsi |
|----|-------------|-----------|
| NFR-01 | Aesthetics | Desain modern dengan efek glassmorphism dan neon glow |
| NFR-02 | Performance | Waktu muat halaman yang cepat dan ringan (< 2 detik) |
| NFR-03 | Compatibility | Berjalan lancar di browser modern (Chrome, Edge, Firefox) |
| NFR-04 | Usability | Navigasi intuitif antar fitur dengan tab-based interface |
| NFR-05 | Security | Validasi input server-side & CSRF protection |

---

## 📊 UML Diagrams

### Use Case Diagram
```mermaid
flowchart LR
    subgraph Actors
        User(("👤 User"))
        Guest(("👤 Guest"))
    end
    
    subgraph E-Concalc["📱 E-Concalc System"]
        UC1["🔐 Login Google"]
        UC2["🔢 Scientific Calculation"]
        UC3["🗣️ Voice Command"]
        UC4["📐 Unit Conversion"]
        UC5["💱 Currency Conversion"]
        UC6["💰 Financial Planning"]
        UC7["❤️ Health Tracking"]
        UC8["💾 Save History/Plans"]
    end

    User --> UC1
    User --> UC2
    User --> UC3
    User --> UC4
    User --> UC5
    User --> UC6
    User --> UC7
    User --> UC8
    
    Guest --> UC2
    Guest --> UC3
    Guest --> UC4
        Guest --> UC5
```

### Activity Diagram - Financial Planning
```mermaid
flowchart TD
    A(["🚀 Start"]) --> B["📂 Select Financial Tab"]
    B --> C["📊 Choose Type (Simple/Compound/Loan)"]
    C --> D["✏️ Input Parameters"]
    D --> E{"✅ Input Valid?"}
    
    E -->|Yes| F["🔢 Calculate Result & Show UI"]
    F --> G{"💾 User clicks Save?"}
    
    G -->|Yes| H{"🔐 Is Authenticated?"}
    H -->|Yes| I["📡 AJAX Post to /financial/save"]
    I --> J{"✅ Server Success?"}
    J -->|Yes| K["🔔 Show 'Saved' Toast"]
    J -->|No| L["⚠️ Show Error"]
    
    H -->|No| M["🔒 Redirect to Login"]
    
    G -->|No| N(["🏁 End"])
    E -->|No| O["⚠️ Show Validation Error"]
    O --> D
```

### Sequence Diagram - Save Financial Plan
```mermaid
sequenceDiagram
    actor User
    participant View as UI (Blade/JS)
    participant Route as Web Routes
    participant Controller as FinancialController
    participant Model as FinancialPlan
    participant DB as Database

    User->>View: Input Data & Click Save
    View->>View: Client-side Validation
    View->>Route: POST /financial/save
    Route->>Controller: saveFinancialPlan(Request)
    Controller->>Controller: Validate & Auth Check
    Controller->>Model: Create([user_id, title, data])
    Model->>DB: INSERT INTO financial_plans
    DB-->>Model: ID created
    Model-->>Controller: Plan Object
    Controller-->>View: JSON Response {success: true}
    View-->>User: Show Toast "Berhasil Disimpan"
    View->>View: Reload Page (Update List)
```

### ERD (Entity Relationship Diagram)
```mermaid
erDiagram
    USERS ||--o{ FINANCIAL_PLANS : manages
    USERS ||--o{ HEALTH_LOGS : tracks
    USERS ||--o{ SESSIONS : has
    
    USERS {
        bigint id PK
        string name
        string email
        string google_id UK
        string password
        remember_token token
        timestamp created_at
    }
    
    FINANCIAL_PLANS {
        bigint id PK
        bigint user_id FK
        string title
        string type
        json data
        timestamp created_at
    }
    
    HEALTH_LOGS {
        bigint id PK
        bigint user_id FK
        float weight
        float height
        float bmi
        string category
        timestamp created_at
    }
    
    SESSIONS {
        string id PK
        bigint user_id FK
        string ip_address
        text user_agent
        text payload
        int last_activity
    }
```

---

## 🎨 Mock-Up / Screenshots

### 1. Scientific Calculator
![Calculator Page](public/images/screenshot-calculator.png)

### 2. Unit Converter
![Converter Page](public/images/screenshot-converter.png)

### 3. Currency Converter
![Currency Page](public/images/screenshot-currency.png)

### 4. Financial Calculator
![Financial Page](public/images/screenshot-financial.png)

### 5. Health Calculator
![Health Page](public/images/screenshot-health.png)

---

## 🔄 SDLC (Software Development Life Cycle)

**Metodologi:** Agile - Iterative Development

| Phase | Aktivitas | Output |
|-------|-----------|--------|
| **1. Planning** | Requirement gathering, user story | PRD, User Stories |
| **2. Analysis** | SRS, feature prioritization | Feature List, SRS Doc |
| **3. Design** | UML diagrams, database design, mockups | UML, ERD, Mockups |
| **4. Development** | Coding, unit testing | Source code, tests |
| **5. Testing** | Integration testing, UAT | Test cases, bug reports |
| **6. Deployment** | Server setup, deployment | Live application |

### Timeline
```
Minggu 1     : Planning & Analysis
Minggu 2     : Design (UML, Mockups, Database)
Minggu 3-5   : Development Sprint 1 (Core Features - Calculator, Converter)
Minggu 6-7   : Development Sprint 2 (Financial, Health, Google Auth)
Minggu 8     : Testing, Bug Fixes & Deployment
```

---

## 🚀 Instalasi

### Prerequisites
Pastikan Anda sudah menginstall:
- **PHP** >= 8.1
- **Composer** >= 2.0
- **Node.js** >= 18.0
- **MySQL** (via XAMPP/Laragon)
- **Git**

### Langkah 1: Clone Repository
```bash
git clone https://github.com/falcon-ioi/Caclcon.git
cd Caclcon
```

### Langkah 2: Install Dependencies
```bash
# Install PHP dependencies
composer install

# Install Node.js dependencies
npm install
```

### Langkah 3: Konfigurasi Environment & Google OAuth
```bash
# Copy file environment
cp .env.example .env

# Generate application key
php artisan key:generate
```

**Edit file `.env`** dan sesuaikan:

```env
DB_CONNECTION=mysql
DB_HOST=127.0.0.1
DB_DATABASE=ecalc_db
DB_USERNAME=root
DB_PASSWORD=

# Google OAuth (Wajib untuk Login)
GOOGLE_CLIENT_ID=your_google_client_id
GOOGLE_CLIENT_SECRET=your_google_client_secret
GOOGLE_REDIRECT_URI=http://localhost:8000/auth/google/callback
```

### Langkah 4: Setup Database
```bash
# Jalankan migrasi tabel
php artisan migrate
```

### Langkah 5: Build & Run
```bash
# Build assets
npm run build

# Jalankan server
php artisan serve
```

Aplikasi akan berjalan di: **http://localhost:8000**

### 🔐 Akun & Login
- **Guest Mode**: Langsung akses fitur Kalkulator & Konverter tanpa login.
- **Full Access**: Gunakan tombol "Sign in with Google" untuk fitur Simpan & Riwayat.

---

## 📁 Struktur Database
```
users              → User accounts (id, name, email, google_id)
financial_plans    → Data perencanaan finansial (user_id, title, type, data json)
health_logs        → Pencatatan BMI & kesehatan (user_id, bmi, weight, height)
riwayat_aktivitas  → Log history perhitungan umum
```

---

## 📁 Struktur Folder
```
ECalc/
├── app/
│   ├── Http/Controllers/   → Auth, Calculator, Financial logic
│   └── Models/             → Eloquent Models (User, FinancialPlan)
├── database/migrations/    → Schema definitions
├── public/                 → Compiled assets & Images
├── resources/views/        → Blade templates (UI)
└── routes/web.php          → Route definitions
```

---

## 🌟 Fitur Unggulan

| Fitur | Deskripsi |
|-------|-----------|
| 🔢 **Scientific Calculator** | Trigonometri, Eksponen, Voice Command |
| 📐 **Multi-Unit Converter** | 7 Kategori Konversi Satuan |
| 💵 **Real-time Currency** | Kurs Mata Uang Asing Live |
| 💰 **Financial Tools** | Bunga Sederhana, Majemuk, Cicilan, Diskon |
| ❤️ **Health Tracker** | BMI Calculator & Ideal Weight |
| 🔐 **Hybrid Auth** | Login Google + Akses Tamu |

---

## 📜 License
This project is open-sourced software licensed under the [MIT license](https://opensource.org/licenses/MIT).

---

## 👨‍💻 Author
**Falcon IOI**
- GitHub: [falcon-ioi](https://github.com/falcon-ioi)

---

⭐ **Star this repo if you find it useful!**
