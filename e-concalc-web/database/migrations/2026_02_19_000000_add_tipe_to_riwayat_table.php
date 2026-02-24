<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    public function up(): void
    {
        Schema::table('riwayat', function (Blueprint $table) {
            $table->string('tipe', 20)->default('calc')->after('operasi');
        });
    }

    public function down(): void
    {
        Schema::table('riwayat', function (Blueprint $table) {
            $table->dropColumn('tipe');
        });
    }
};
