<?php
session_start();
require 'koneksi.php';

if (isset($_SESSION['user_id'])) {
    header("Location: index.php");
    exit();
}

if (isset($_POST['login'])) {
    $username = $_POST['username'];
    $password = $_POST['password'];

    $result = mysqli_query($conn, "SELECT * FROM users WHERE username = '$username'");
    
    if (mysqli_num_rows($result) === 1) {
        $row = mysqli_fetch_assoc($result);
        if (password_verify($password, $row['password'])) {
            $_SESSION['user_id'] = $row['id'];
            $_SESSION['username'] = $username;
            header("Location: index.php");
            exit();
        } else {
            $error = "Password salah!";
        }
    } else {
        $error = "Username tidak ditemukan!";
    }
}
?>

<!DOCTYPE html>
<html lang="id">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - Karya SuperApp</title>
    <link rel="stylesheet" href="style.css">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;600&display=swap" rel="stylesheet">
</head>
<body class="auth-body">
    <div class="split-screen">
        <div class="left-panel">
            <div class="content">
                <h1>Karya SuperApp</h1>
                <p>Kelola kebutuhan konversi dan perhitungan Anda dalam satu aplikasi premium.</p>
            </div>
        </div>
        <div class="right-panel">
            <div class="auth-card">
                <h2>Selamat Datang</h2>
                <p class="subtitle">Masuk untuk melanjutkan</p>
                
                <?php if (isset($error)) echo "<div class='alert error'>$error</div>"; ?>
                
                <form method="POST">
                    <div class="input-group">
                        <label>Username</label>
                        <input type="text" name="username" required placeholder="Masukkan username">
                    </div>
                    <div class="input-group">
                        <label>Password</label>
                        <input type="password" name="password" required placeholder="Masukkan password">
                    </div>
                    <button type="submit" name="login" class="btn-primary">Masuk Sekarang</button>
                </form>
                
                <div class="auth-footer">
                    Belum punya akun? <a href="register.php">Daftar disini</a>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
