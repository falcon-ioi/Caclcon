<?php

use App\Http\Controllers\CalculatorController;
use Illuminate\Support\Facades\Route;

// Public routes
Route::get('/', [CalculatorController::class, 'index'])->name('home');
// Export route remains useful for local calculations too
Route::get('/export/{format}', [CalculatorController::class, 'export'])->name('export');
