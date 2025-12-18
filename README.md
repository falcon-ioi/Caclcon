# Dokumentasi Project - Calcon

**Nama:** [Isi Nama Anda] | **Kelas:** [Isi Kelas] | **NPM:** [Isi NPM]

## 1. Deskripsi
**Calcon** adalah aplikasi web multifungsi (SuperApp) yang menggabungkan fitur kalkulator ilmiah dan konverter satuan profesional dalam satu platform. Aplikasi ini dirancang untuk memberikan kemudahan bagi pengguna dalam melakukan perhitungan matematis kompleks maupun konversi berbagai satuan internasional secara cepat, akurat, dan terintegrasi dengan sistem penyimpanan riwayat berbasis database.

## 2. User Story
Sebagai user, saya ingin:
* Mendaftar dan Login untuk menyimpan data riwayat perhitungan saya secara pribadi.
* Menggunakan kalkulator ilmiah untuk operasi matematika tingkat lanjut seperti trigonometri, logaritma, dan akar pangkat.
* Mengonversi berbagai kategori satuan (Panjang, Berat, Suhu, Waktu, Data, Kecepatan) secara instan.
* Melihat riwayat aktivitas perhitungan dan konversi yang telah dilakukan sebelumnya.

## 3. SRS (Software Requirements Specification)

### Feature List (Daftar Fitur)

1.  **Autentikasi Pengguna:**
    * Register akun baru.
    * Login session untuk keamanan data pengguna.
2.  **Kalkulator Ilmiah:**
    * Operasi aritmatika dasar dan fungsi ilmiah (sin, cos, tan, log, ln).
    * Fitur Memory (MC, MR, M+, M-) dan mode perhitungan (DEG/RAD).
    * Fitur tombol sekunder (2nd) untuk fungsi invers.
3.  **Konverter Satuan Pro:**
    * 6 Kategori Utama: Panjang, Berat, Suhu, Waktu, Data Digital, dan Kecepatan.
    * Konversi real-time saat pengguna memasukkan nilai angka.
4.  **Data Management:**
    * Penyimpanan riwayat operasi secara otomatis menggunakan AJAX ke database MySQL.
    * Visualisasi tabel riwayat aktivitas pada dashboard pengguna.

## 4. UML Diagram

### a. Use Case Diagram
Diagram ini menggambarkan interaksi pengguna dengan sistem Calcon, mulai dari proses autentikasi hingga penggunaan fitur utama kalkulator dan konverter yang datanya tersimpan ke dalam sistem.

![Use Case Diagram](./images/UseCase_Diagram.png) 
*(Catatan: Pastikan Anda mengunggah gambar diagram ke folder images)*

### b. Activity Diagram
Menggambarkan alur aktivitas saat pengguna melakukan perhitungan: dimulai dari pengecekan login, penginputan data pada kalkulator/konverter, proses validasi logika oleh sistem, hingga penyimpanan otomatis ke database riwayat.

![Activity Diagram](./images/Activity_Diagram.png)

### c. Sequence Diagram
Menjelaskan urutan pertukaran pesan antara komponen **User**, **Web Interface** (JavaScript), dan **Server** (PHP/MySQL) saat pengguna menekan tombol hitung untuk menyimpan data secara asinkron (AJAX).

![Sequence Diagram](./images/Sequence_Diagram.png)
