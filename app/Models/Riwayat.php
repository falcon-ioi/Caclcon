<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Riwayat extends Model
{
    use HasFactory;

    protected $table = 'riwayat';

    protected $fillable = [
        'user_id',
        'operasi'
    ];

    public function user()
    {
        return $this->belongsTo(User::class);
    }
}
