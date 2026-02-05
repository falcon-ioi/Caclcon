<?php

use App\Http\Controllers\AuthController;
use App\Http\Controllers\CalculatorController;
use Illuminate\Support\Facades\Route;

// Auth routes (guest only)
Route::middleware('guest')->group(function () {
    Route::get('/login', [AuthController::class, 'showLogin'])->name('login');
    Route::post('/login', [AuthController::class, 'login']);
    Route::get('/register', [AuthController::class, 'showRegister'])->name('register');
    Route::post('/register', [AuthController::class, 'register']);
});

// Public routes
Route::get('/', [CalculatorController::class, 'index'])->name('home');
Route::get('auth/google', [AuthController::class, 'redirectToGoogle'])->name('auth.google');
Route::get('auth/google/callback', [AuthController::class, 'handleGoogleCallback']);

// Protected routes (auth required)
Route::middleware('auth')->group(function () {
    // Route::get('/', [CalculatorController::class, 'index'])->name('home'); // Moved up
    Route::post('/save-history', [CalculatorController::class, 'saveHistory'])->name('save-history');
    
    // Financial Plans
    Route::post('/financial/save', [CalculatorController::class, 'saveFinancialPlan'])->name('financial.save');
    Route::delete('/financial/delete/{id}', [CalculatorController::class, 'deleteFinancialPlan'])->name('financial.delete');
    
    // Health Tracker
    Route::post('/health/save', [CalculatorController::class, 'saveHealthLog'])->name('health.save');
    
    // Settings
    Route::post('/settings/update', [CalculatorController::class, 'updateSettings'])->name('settings.update');

    Route::get('/export/{format}', [CalculatorController::class, 'export'])->name('export');
    Route::post('/logout', [AuthController::class, 'logout'])->name('logout');
});
