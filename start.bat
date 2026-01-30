@echo off
title E-Concalc Server
color 0B

echo.
echo  ============================================
echo        E-Concalc - Electronic Calculator
echo  ============================================
echo.
echo  [INFO] Clearing cache...
call php artisan optimize:clear >nul 2>&1

echo  [INFO] Starting Laravel Development Server...
echo.
echo  +-----------------------------------------+
echo  ^|  Server:  http://localhost:8000        ^|
echo  ^|  Press Ctrl+C to stop the server       ^|
echo  +-----------------------------------------+
echo.

start "" "http://localhost:8000"
php artisan serve

pause
