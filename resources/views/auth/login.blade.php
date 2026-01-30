@extends('layouts.app')

@section('title', 'Login - E-Concalc')

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

    .splash-screen {
        position: fixed;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        background: linear-gradient(135deg, #0f172a 0%, #1e293b 100%);
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;
        z-index: 9999;
        transition: opacity 0.5s ease, visibility 0.5s ease;
    }

    .splash-screen.hidden {
        opacity: 0;
        visibility: hidden;
    }

    .splash-logo {
        animation: logoAnimation 2s ease-in-out;
    }

    @keyframes logoAnimation {
        0% { transform: scale(0) rotate(-180deg); opacity: 0; }
        50% { transform: scale(1.2) rotate(10deg); opacity: 1; }
        70% { transform: scale(0.9) rotate(-5deg); }
        100% { transform: scale(1) rotate(0deg); opacity: 1; }
    }

    .splash-title {
        margin-top: 20px;
        font-size: 2.5rem;
        font-weight: 700;
        background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
        -webkit-background-clip: text;
        background-clip: text;
        -webkit-text-fill-color: transparent;
        animation: fadeInUp 1s ease 0.5s both;
    }

    .splash-subtitle {
        color: #64748b;
        font-size: 0.9rem;
        letter-spacing: 3px;
        text-transform: uppercase;
        animation: fadeInUp 1s ease 0.8s both;
    }

    @keyframes fadeInUp {
        from { opacity: 0; transform: translateY(20px); }
        to { opacity: 1; transform: translateY(0); }
    }

    .login-wrapper {
        width: 100%;
        max-width: 400px;
        animation: fadeIn 0.5s ease 2.5s both;
    }

    @keyframes fadeIn {
        from { opacity: 0; transform: translateY(30px); }
        to { opacity: 1; transform: translateY(0); }
    }

    .login-header {
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

    .login-card {
        background: #f8f9fa;
        border-radius: 24px;
        padding: 35px 30px;
        box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
    }

    .input-group {
        margin-bottom: 18px;
        position: relative;
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

    .remember-group {
        display: flex;
        align-items: center;
        margin: 20px 0;
        color: #64748b;
        font-size: 0.9rem;
    }

    .remember-group label {
        display: flex;
        align-items: center;
        gap: 8px;
        cursor: pointer;
    }

    .remember-group input[type="checkbox"] {
        width: 18px;
        height: 18px;
        accent-color: #4facfe;
        cursor: pointer;
    }

    .btn-login {
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

    .btn-login:hover {
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

    .register-link {
        text-align: center;
        margin-top: 25px;
        color: #94a3b8;
        font-size: 0.9rem;
    }

    .register-link a {
        color: #4facfe;
        text-decoration: none;
        font-weight: 500;
    }

    .register-link a:hover {
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
<!-- Splash Screen -->
<div class="splash-screen" id="splashScreen">
    <div class="splash-logo">
        <svg width="100" height="100" viewBox="0 0 100 100" fill="none" xmlns="http://www.w3.org/2000/svg">
            <defs>
                <linearGradient id="logoGrad" x1="0%" y1="0%" x2="100%" y2="100%">
                    <stop offset="0%" style="stop-color:#4facfe" />
                    <stop offset="100%" style="stop-color:#00f2fe" />
                </linearGradient>
            </defs>
            <rect x="15" y="10" width="70" height="80" rx="10" stroke="url(#logoGrad)" stroke-width="4" fill="none"/>
            <rect x="22" y="18" width="56" height="18" rx="4" fill="url(#logoGrad)" opacity="0.3"/>
            <rect x="22" y="42" width="12" height="10" rx="2" fill="url(#logoGrad)" opacity="0.6"/>
            <rect x="38" y="42" width="12" height="10" rx="2" fill="url(#logoGrad)" opacity="0.6"/>
            <rect x="54" y="42" width="12" height="10" rx="2" fill="url(#logoGrad)" opacity="0.6"/>
            <rect x="70" y="42" width="8" height="10" rx="2" fill="url(#logoGrad)"/>
            <rect x="22" y="56" width="12" height="10" rx="2" fill="url(#logoGrad)" opacity="0.6"/>
            <rect x="38" y="56" width="12" height="10" rx="2" fill="url(#logoGrad)" opacity="0.6"/>
            <rect x="54" y="56" width="12" height="10" rx="2" fill="url(#logoGrad)" opacity="0.6"/>
            <rect x="70" y="56" width="8" height="24" rx="2" fill="url(#logoGrad)"/>
            <rect x="22" y="70" width="12" height="10" rx="2" fill="url(#logoGrad)" opacity="0.6"/>
            <rect x="38" y="70" width="28" height="10" rx="2" fill="url(#logoGrad)" opacity="0.6"/>
        </svg>
    </div>
    <h1 class="splash-title">E-Concalc</h1>
    <p class="splash-subtitle">Electronic Conversion Calculator</p>
</div>

<!-- Login Form -->
<div class="login-wrapper">
    <div class="login-header">
        <h1 class="app-name">E-Concalc</h1>
        <p class="app-tagline">Electronic Conversion Calculator</p>
    </div>

    <div class="login-card">
        @if(session('error'))
            <div class="alert-error">{{ session('error') }}</div>
        @endif
        
        <form method="POST" action="{{ route('login') }}">
            @csrf
            <div class="input-group">
                <input type="text" name="username" required placeholder="Username" value="{{ old('username') }}">
            </div>
            
            <div class="input-group">
                <input type="password" name="password" id="password" required placeholder="Password">
            </div>
            
            <div class="remember-group">
                <label>
                    <input type="checkbox" name="remember">
                    Keep me logged in
                </label>
            </div>
            
            <button type="submit" class="btn-login">Login</button>
        </form>
    </div>

    <div class="register-link">
        Belum punya akun? <a href="{{ route('register') }}">Daftar disini</a>
    </div>

    <p class="copyright">© 2026 E-Concalc. All rights reserved.</p>
</div>
@endsection

@section('scripts')
<script>
    setTimeout(() => {
        document.getElementById('splashScreen').classList.add('hidden');
    }, 2500);
</script>
@endsection
