<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

class CalculatorController extends Controller
{
    public function index()
    {
        // Simply return the view without user data
        return view('calculator.index', [
            'username' => 'Guest',
            'riwayat' => [],
            'financialPlans' => [],
            'healthLogs' => [],
            'settings' => ['theme' => 'dark']
        ]);
    }

    public function export(Request $request, $format)
    {
        // Export logic would assume client-side data for now since we removed backend storage
        // But to keep the route functional if needed, we return empty or redirect
        return redirect()->route('home');
    }
}
