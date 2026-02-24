<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    /**
     * Run the migrations.
     * Cleanup: drop unused tables from removed features (financial, health)
     */
    public function up(): void
    {
        Schema::dropIfExists('financial_plans');
        Schema::dropIfExists('health_logs');

        if (Schema::hasColumn('users', 'settings')) {
            Schema::table('users', function (Blueprint $table) {
                $table->dropColumn('settings');
            });
        }
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::create('financial_plans', function (Blueprint $table) {
            $table->id();
            $table->foreignId('user_id')->constrained()->onDelete('cascade');
            $table->string('title');
            $table->string('type');
            $table->json('data');
            $table->timestamps();
        });

        Schema::create('health_logs', function (Blueprint $table) {
            $table->id();
            $table->foreignId('user_id')->constrained()->onDelete('cascade');
            $table->float('weight');
            $table->float('height');
            $table->float('bmi');
            $table->string('category');
            $table->timestamps();
        });

        Schema::table('users', function (Blueprint $table) {
            $table->json('settings')->nullable()->after('password');
        });
    }
};
