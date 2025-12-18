<?php
require 'koneksi.php';

if (isset($_POST['register'])) {
    $username = $_POST['username'];
    $password = password_hash($_POST['password'], PASSWORD_DEFAULT);

    $check = mysqli_query($conn, "SELECT * FROM users WHERE username = '$username'");
    if (mysqli_num_rows($check) > 0) {
        $error = "Username sudah terdaftar!";
    } else {
        $query = "INSERT INTO users (username, password) VALUES ('$username', '$password')";
        if (mysqli_query($conn, $query)) {
            echo "<script>alert('Registrasi berhasil! Silakan login.'); window.location='login.php';</script>";
        } else {
            $error = "Terjadi kesalahan: " . mysqli_error($conn);
        }
    }
}
?>

<!DOCTYPE html>
<html lang="id">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Register - Karya SuperApp</title>
    <link rel="stylesheet" href="style.css">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;600&display=swap" rel="stylesheet">
</head>
<body class="auth-body">
    <div class="split-screen">
        <div class="left-panel">
            <div class="content">
                <h1>Bergabunglah</h1>
                <p>Nikmati fitur eksklusif Kalkulator Ilmiah dan Konverter Satuan Pro.</p>
            </div>
        </div>
        <div class="right-panel">
            <div class="auth-card">
                <h2>Buat Akun Baru</h2>
                <p class="subtitle">Isi data diri Anda</p>
                
                <?php if (isset($error)) echo "<div class='alert error'>$error</div>"; ?>
                
                <form method="POST">
                    <div class="input-group">
                        <label>Username</label>
                        <input type="text" name="username" required placeholder="Pilih username">
                    </div>
                    <div class="input-group">
                        <label>Password</label>
                        <input type="password" name="password" required placeholder="Buat password">
                    </div>
                    <button type="submit" name="register" class="btn-primary">Daftar Sekarang</button>
                </form>
                
                <div class="auth-footer">
                    Sudah punya akun? <a href="login.php">Login disini</a>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
