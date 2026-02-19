@echo off
echo Starting E-Concalc Website...
:: Try to launch XAMPP Control Panel if found
if exist "C:\xampp\xampp-control.exe" (
    echo Launching XAMPP Control Panel...
    start "" "C:\xampp\xampp-control.exe"
) else (
    echo [INFO] XAMPP Control Panel not found at C:\xampp.
    echo Please start Apache & MySQL manually for phpMyAdmin access.
)

:: Open phpMyAdmin (requires XAMPP/MySQL running)
start "" "http://localhost/phpmyadmin"

:: Open the browser after a short delay to allow the server to start
timeout /t 3
start "" "http://127.0.0.1:8000"

:: Start the Laravel development server
php artisan serve

pause
