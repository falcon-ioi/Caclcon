<?php

namespace App\Http\Controllers;

use App\Models\Riwayat;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;

use App\Models\FinancialPlan;
use App\Models\HealthLog;

class CalculatorController extends Controller
{
    public function index()
    {
        $user = Auth::user();
        
        $riwayat = Riwayat::where('user_id', $user->id)
            ->orderBy('created_at', 'desc')
            ->limit(10)
            ->get();

        $financialPlans = FinancialPlan::where('user_id', $user->id)
            ->orderBy('created_at', 'desc')
            ->get();

        $healthLogs = HealthLog::where('user_id', $user->id)
            ->orderBy('created_at', 'asc') // Ascending for chart
            ->get();
            
        $settings = $user->settings ?? ['theme' => 'dark'];

        return view('calculator.index', [
            'username' => $user->name,
            'riwayat' => $riwayat,
            'financialPlans' => $financialPlans,
            'healthLogs' => $healthLogs,
            'settings' => $settings
        ]);
    }

    public function saveHistory(Request $request)
    {
        $request->validate([
            'operasi' => 'required|string'
        ]);

        Riwayat::create([
            'user_id' => Auth::id(),
            'operasi' => $request->operasi
        ]);

        return response()->json(['success' => true]);
    }

    public function saveFinancialPlan(Request $request)
    {
        $request->validate([
            'title' => 'required|string',
            'type' => 'required|string',
            'data' => 'required|array'
        ]);

        $plan = FinancialPlan::create([
            'user_id' => Auth::id(),
            'title' => $request->title,
            'type' => $request->type,
            'data' => $request->data
        ]);

        return response()->json([
            'success' => true,
            'plan' => [
                'id' => $plan->id,
                'title' => $plan->title,
                'type' => $plan->type,
                'created_at_formatted' => $plan->created_at->format('d M Y')
            ]
        ]);
    }

    public function deleteFinancialPlan($id)
    {
        $plan = FinancialPlan::where('user_id', Auth::id())->where('id', $id)->firstOrFail();
        $plan->delete();
        return response()->json(['success' => true]);
    }

    public function saveHealthLog(Request $request)
    {
        $request->validate([
            'weight' => 'required|numeric',
            'height' => 'required|numeric',
            'bmi' => 'required|numeric',
            'category' => 'required|string'
        ]);

        HealthLog::create([
            'user_id' => Auth::id(),
            'weight' => $request->weight,
            'height' => $request->height,
            'bmi' => $request->bmi,
            'category' => $request->category
        ]);

        return response()->json(['success' => true]);
    }

    public function updateSettings(Request $request)
    {
        $request->validate([
            'theme' => 'required|string'
        ]);

        $user = Auth::user();
        $settings = $user->settings ?? [];
        $settings['theme'] = $request->theme;
        $user->settings = $settings;
        $user->save();

        return response()->json(['success' => true]);
    }


    public function export(Request $request, $format)
    {
        $data = Riwayat::where('user_id', Auth::id())
            ->orderBy('created_at', 'desc')
            ->get();

        if ($format === 'csv') {
            $filename = 'riwayat_econcalc_' . date('Y-m-d') . '.csv';
            $headers = [
                'Content-Type' => 'text/csv',
                'Content-Disposition' => "attachment; filename=\"$filename\""
            ];

            $callback = function() use ($data) {
                $file = fopen('php://output', 'w');
                fputcsv($file, ['Operasi', 'Waktu']);
                foreach ($data as $row) {
                    fputcsv($file, [$row->operasi, $row->created_at]);
                }
                fclose($file);
            };

            return response()->stream($callback, 200, $headers);
        }

        return view('calculator.export', ['data' => $data]);
    }
}
