<?php
session_start();
require 'koneksi.php';

if (!isset($_SESSION['user_id'])) {
    echo "error: unauthorized";
    exit();
}

if ($_SERVER["REQUEST_METHOD"] == "POST" && isset($_POST['operasi'])) {
    $user_id = $_SESSION['user_id'];
    $operasi = mysqli_real_escape_string($conn, $_POST['operasi']);
    
    $query = "INSERT INTO riwayat (user_id, operasi) VALUES ('$user_id', '$operasi')";
    
    if (mysqli_query($conn, $query)) {
        echo "success";
    } else {
        echo "error: " . mysqli_error($conn);
    }
}
?>
