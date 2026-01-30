@extends('layouts.app')

@section('title', 'Register - E-Concalc')

@section('styles')
<style>
    * {
        margin: 0;
        padding: 0;
        box-sizing: border-box;
        font-family: 'Poppins', sans-serif;
    }

    body {
        min-height: 100vh;
        display: flex;
        align-items: center;
        justify-content: center;
        background: linear-gradient(135deg, #0f172a 0%, #1e293b 50%, #0f172a 100%);
        padding: 20px;
    }

    .register-wrapper {
        width: 100%;
        max-width: 400px;
        animation: fadeIn 0.5s ease both;
    }

    @keyframes fadeIn {
        from { opacity: 0; transform: translateY(30px); }
        to { opacity: 1; transform: translateY(0); }
    }

    .register-header {
        text-align: center;
        margin-bottom: 30px;
    }

    .app-name {
        font-size: 1.8rem;
        font-weight: 700;
        background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
        -webkit-background-clip: text;
        background-clip: text;
        -webkit-text-fill-color: transparent;
    }

    .app-tagline {
        color: #64748b;
        font-size: 0.8rem;
        letter-spacing: 2px;
        text-transform: uppercase;
    }

    .register-card {
        background: #f8f9fa;
        border-radius: 24px;
        padding: 35px 30px;
        box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
    }

    .input-group {
        margin-bottom: 18px;
    }

    .input-group input {
        width: 100%;
        padding: 16px 20px;
        background: rgba(79, 172, 254, 0.08);
        border: none;
        border-radius: 50px;
        font-size: 1rem;
        color: #334155;
        transition: all 0.3s;
    }

    .input-group input::placeholder {
        color: #94a3b8;
    }

    .input-group input:focus {
        outline: none;
        background: rgba(79, 172, 254, 0.15);
        box-shadow: 0 0 0 3px rgba(79, 172, 254, 0.2);
    }

    .btn-register {
        width: 100%;
        padding: 16px;
        background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
        border: none;
        border-radius: 50px;
        color: #0f172a;
        font-size: 1rem;
        font-weight: 600;
        letter-spacing: 3px;
        text-transform: uppercase;
        cursor: pointer;
        transition: all 0.3s;
        margin-top: 10px;
    }

    .btn-register:hover {
        transform: translateY(-3px);
        box-shadow: 0 10px 30px rgba(79, 172, 254, 0.4);
    }

    .alert-error {
        background: rgba(239, 68, 68, 0.1);
        border: 1px solid rgba(239, 68, 68, 0.3);
        color: #ef4444;
        padding: 12px 16px;
        border-radius: 12px;
        margin-bottom: 20px;
        font-size: 0.9rem;
        text-align: center;
    }

    .login-link {
        text-align: center;
        margin-top: 25px;
        color: #94a3b8;
        font-size: 0.9rem;
    }

    .login-link a {
        color: #4facfe;
        text-decoration: none;
        font-weight: 500;
    }

    .login-link a:hover {
        text-decoration: underline;
    }

    .copyright {
        text-align: center;
        margin-top: 30px;
        color: #475569;
        font-size: 0.75rem;
    }
</style>
@endsection

@section('content')
<div class="register-wrapper">
    <div class="register-header">
        <h1 class="app-name">E-Concalc</h1>
        <p class="app-tagline">Create Your Account</p>
    </div>

    <div class="register-card">
        @if($errors->any())
            <div class="alert-error">
                {{ $errors->first() }}
            </div>
        @endif
        
        <form method="POST" action="{{ route('register') }}">
            @csrf
            <div class="input-group">
                <input type="text" name="username" required placeholder="Username" minlength="3" value="{{ old('username') }}">
            </div>
            
            <div class="input-group">
                <input type="password" name="password" required placeholder="Password" minlength="6">
            </div>
            
            <div class="input-group">
                <input type="password" name="password_confirmation" required placeholder="Konfirmasi Password">
            </div>
            
            <button type="submit" class="btn-register">Daftar</button>
        </form>
    </div>

    <div class="login-link">
        Sudah punya akun? <a href="{{ route('login') }}">Login disini</a>
    </div>

    <p class="copyright">© 2026 E-Concalc. All rights reserved.</p>
</div>
@endsection
