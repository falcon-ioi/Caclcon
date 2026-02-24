@extends('layouts.app')

@section('title', 'Daftar - E-Concalc')

@section('styles')
<style>
    .auth-container {
        min-height: 100vh;
        display: flex;
        align-items: center;
        justify-content: center;
        padding: 20px;
    }

    .auth-card {
        background: rgba(30, 41, 59, 0.95);
        border-radius: 20px;
        padding: 40px;
        width: 100%;
        max-width: 420px;
        box-shadow: 0 20px 60px rgba(0, 0, 0, 0.5);
        border: 1px solid rgba(255, 255, 255, 0.05);
    }

    .auth-card h1 {
        text-align: center;
        color: #00d4ff;
        font-size: 1.8rem;
        margin-bottom: 8px;
    }

    .auth-card p.subtitle {
        text-align: center;
        color: #94a3b8;
        margin-bottom: 30px;
        font-size: 0.9rem;
    }

    .form-group {
        margin-bottom: 18px;
    }

    .form-group label {
        display: block;
        color: #cbd5e1;
        margin-bottom: 6px;
        font-size: 0.85rem;
        font-weight: 500;
    }

    .form-group input {
        width: 100%;
        padding: 12px 16px;
        background: rgba(0, 0, 0, 0.3);
        border: 1px solid rgba(255, 255, 255, 0.1);
        border-radius: 10px;
        color: white;
        font-size: 1rem;
        font-family: 'Poppins', sans-serif;
        transition: border-color 0.3s;
        box-sizing: border-box;
    }

    .form-group input:focus {
        outline: none;
        border-color: #00d4ff;
        box-shadow: 0 0 0 3px rgba(0, 212, 255, 0.1);
    }

    .btn-register {
        width: 100%;
        padding: 14px;
        border: none;
        border-radius: 10px;
        background: linear-gradient(135deg, #4facfe, #00f2fe);
        color: #0f172a;
        font-size: 1rem;
        font-weight: 600;
        font-family: 'Poppins', sans-serif;
        cursor: pointer;
        transition: transform 0.2s, box-shadow 0.2s;
        margin-top: 10px;
    }

    .btn-register:hover {
        transform: translateY(-2px);
        box-shadow: 0 6px 20px rgba(79, 172, 254, 0.4);
    }

    .auth-link {
        text-align: center;
        margin-top: 24px;
        color: #94a3b8;
        font-size: 0.85rem;
    }

    .auth-link a {
        color: #00d4ff;
        text-decoration: none;
        font-weight: 500;
    }

    .auth-link a:hover {
        text-decoration: underline;
    }

    .error-message {
        background: rgba(239, 68, 68, 0.15);
        border: 1px solid rgba(239, 68, 68, 0.3);
        border-radius: 8px;
        padding: 10px 14px;
        color: #fca5a5;
        font-size: 0.85rem;
        margin-bottom: 18px;
    }

    .divider {
        display: flex;
        align-items: center;
        margin: 24px 0;
        color: #64748b;
        font-size: 0.8rem;
    }

    .divider::before,
    .divider::after {
        content: '';
        flex: 1;
        height: 1px;
        background: rgba(255, 255, 255, 0.1);
    }

    .divider span {
        padding: 0 16px;
    }

    .btn-google {
        width: 100%;
        padding: 12px;
        border: 1px solid rgba(255, 255, 255, 0.15);
        border-radius: 10px;
        background: rgba(255, 255, 255, 0.05);
        color: white;
        font-size: 0.95rem;
        font-family: 'Poppins', sans-serif;
        cursor: pointer;
        transition: background 0.3s;
        display: flex;
        align-items: center;
        justify-content: center;
        gap: 10px;
        text-decoration: none;
    }

    .btn-google:hover {
        background: rgba(255, 255, 255, 0.1);
    }

    .btn-google img {
        width: 20px;
        height: 20px;
    }

    .skip-link {
        text-align: center;
        margin-top: 16px;
    }

    .skip-link a {
        color: #64748b;
        text-decoration: none;
        font-size: 0.8rem;
    }

    .skip-link a:hover {
        color: #94a3b8;
    }
</style>
@endsection

@section('content')
<div class="auth-container">
    <div class="auth-card">
        <h1>🖩 E-Concalc</h1>
        <p class="subtitle">Buat akun untuk sinkronisasi riwayat</p>

        @if ($errors->any())
            <div class="error-message">
                @foreach ($errors->all() as $error)
                    {{ $error }}<br>
                @endforeach
            </div>
        @endif

        <form method="POST" action="{{ route('register') }}">
            @csrf
            <div class="form-group">
                <label for="username">Username</label>
                <input type="text" id="username" name="username" value="{{ old('username') }}" required autofocus placeholder="Masukkan username">
            </div>

            <div class="form-group">
                <label for="password">Password</label>
                <input type="password" id="password" name="password" required placeholder="Minimal 6 karakter">
            </div>

            <div class="form-group">
                <label for="password_confirmation">Konfirmasi Password</label>
                <input type="password" id="password_confirmation" name="password_confirmation" required placeholder="Ulangi password">
            </div>

            <button type="submit" class="btn-register">Daftar</button>
        </form>

        <div class="divider"><span>atau</span></div>

        <a href="{{ route('auth.google') }}" class="btn-google">
            <svg width="20" height="20" viewBox="0 0 24 24">
                <path fill="#4285F4" d="M22.56 12.25c0-.78-.07-1.53-.2-2.25H12v4.26h5.92a5.06 5.06 0 0 1-2.2 3.32v2.77h3.57c2.08-1.92 3.28-4.74 3.28-8.1z"/>
                <path fill="#34A853" d="M12 23c2.97 0 5.46-.98 7.28-2.66l-3.57-2.77c-.98.66-2.23 1.06-3.71 1.06-2.86 0-5.29-1.93-6.16-4.53H2.18v2.84C3.99 20.53 7.7 23 12 23z"/>
                <path fill="#FBBC05" d="M5.84 14.09c-.22-.66-.35-1.36-.35-2.09s.13-1.43.35-2.09V7.07H2.18C1.43 8.55 1 10.22 1 12s.43 3.45 1.18 4.93l2.85-2.22.81-.62z"/>
                <path fill="#EA4335" d="M12 5.38c1.62 0 3.06.56 4.21 1.64l3.15-3.15C17.45 2.09 14.97 1 12 1 7.7 1 3.99 3.47 2.18 7.07l3.66 2.84c.87-2.6 3.3-4.53 6.16-4.53z"/>
            </svg>
            Daftar dengan Google
        </a>

        <div class="auth-link">
            Sudah punya akun? <a href="{{ route('login') }}">Masuk disini</a>
        </div>

        <div class="skip-link">
            <a href="{{ route('home') }}">← Lanjutkan tanpa login</a>
        </div>
    </div>
</div>
@endsection
