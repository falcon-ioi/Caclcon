<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\Riwayat;
use Illuminate\Http\Request;

class HistoryController extends Controller
{
    /**
     * Get user's history
     */
    public function index(Request $request)
    {
        $query = $request->user()->riwayat()->orderBy('created_at', 'desc');

        // Filter by type if provided
        if ($request->has('tipe')) {
            $query->where('tipe', $request->tipe);
        }

        $history = $query->limit(100)->get();

        return response()->json([
            'data' => $history->map(function ($item) {
                return [
                    'id' => $item->id,
                    'operasi' => $item->operasi,
                    'tipe' => $item->tipe,
                    'waktu' => $item->created_at->toIso8601String(),
                ];
            }),
        ]);
    }

    /**
     * Store a new history entry
     */
    public function store(Request $request)
    {
        $request->validate([
            'operasi' => 'required|string|max:500',
            'tipe' => 'required|in:calc,conv,currency',
        ]);

        $riwayat = $request->user()->riwayat()->create([
            'operasi' => $request->operasi,
            'tipe' => $request->tipe,
        ]);

        return response()->json([
            'message' => 'History saved',
            'data' => [
                'id' => $riwayat->id,
                'operasi' => $riwayat->operasi,
                'tipe' => $riwayat->tipe,
                'waktu' => $riwayat->created_at->toIso8601String(),
            ],
        ], 201);
    }

    /**
     * Delete all user's history
     */
    public function clear(Request $request)
    {
        $request->user()->riwayat()->delete();

        return response()->json([
            'message' => 'All history cleared',
        ]);
    }

    /**
     * Delete a single history entry
     */
    public function destroy(Request $request, $id)
    {
        $riwayat = $request->user()->riwayat()->findOrFail($id);
        $riwayat->delete();

        return response()->json([
            'message' => 'History deleted',
        ]);
    }
}
