<?php

use App\Http\Controllers\AuthController;
use App\Http\Controllers\CalculatorController;
use Illuminate\Support\Facades\Route;

// Public routes
Route::get('/', [CalculatorController::class, 'index'])->name('home');
Route::get('/export/{format}', [CalculatorController::class, 'export'])->name('export');

// Auth routes
Route::get('/login', [AuthController::class, 'showLogin'])->name('login');
Route::post('/login', [AuthController::class, 'login']);
Route::get('/register', [AuthController::class, 'showRegister'])->name('register');
Route::post('/register', [AuthController::class, 'register']);
Route::post('/logout', [AuthController::class, 'logout'])->name('logout')->middleware('auth');

// Google OAuth
Route::get('/auth/google', [AuthController::class, 'redirectToGoogle'])->name('auth.google');
Route::get('/auth/google/callback', [AuthController::class, 'handleGoogleCallback']);
