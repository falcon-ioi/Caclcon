@echo off
echo ==========================================
echo    Starting E-Concalc Website Server
echo ==========================================
echo.

:: Try to launch XAMPP Control Panel if found
if exist "C:\xampp\xampp-control.exe" (
    echo [OK] Launching XAMPP Control Panel...
    start "" "C:\xampp\xampp-control.exe"
) else (
    echo [INFO] XAMPP Control Panel not found at C:\xampp.
    echo Please start Apache ^& MySQL manually for phpMyAdmin access.
)

:: Open phpMyAdmin (requires XAMPP/MySQL running)
echo [OK] Opening phpMyAdmin...
start "" "http://localhost/phpmyadmin"

:: Start the Laravel development server in the background
echo [OK] Starting Laravel server on 0.0.0.0:8080...
echo.

:: Open the browser after a short delay to allow the server to start
timeout /t 3
start "" "http://127.0.0.1:8080"

:: Start Laravel with --host=0.0.0.0 so mobile emulator can connect
php artisan serve --host=0.0.0.0 --port=8080

pause
