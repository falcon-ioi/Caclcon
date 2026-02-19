<!DOCTYPE html>
<html lang="id">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=5.0">
    <meta name="csrf-token" content="{{ csrf_token() }}">
    
    <!-- SEO Meta Tags -->
    <title>@yield('title', 'E-Concalc - Kalkulator Elektronik Modern')</title>
    <meta name="description" content="@yield('meta_description', 'E-Concalc adalah aplikasi kalkulator elektronik modern dengan fitur kalkulator ilmiah, konversi mata uang, perencanaan keuangan, dan pelacak kesehatan. Gratis dan mudah digunakan.')">
    <meta name="keywords" content="kalkulator, kalkulator online, konversi satuan, kalkulator ilmiah, perencanaan keuangan, bunga majemuk, cicilan pinjaman, pelacak kesehatan, BMI calculator, E-Concalc">
    <meta name="author" content="E-Concalc Team">
    <meta name="robots" content="index, follow">
    <link rel="canonical" href="{{ url()->current() }}">
    
    <!-- Open Graph / Facebook -->
    <meta property="og:type" content="website">
    <meta property="og:url" content="{{ url()->current() }}">
    <meta property="og:title" content="@yield('title', 'E-Concalc - Kalkulator Elektronik Modern')">
    <meta property="og:description" content="@yield('meta_description', 'Aplikasi kalkulator elektronik modern dengan fitur lengkap: kalkulator ilmiah, konversi mata uang, perencanaan keuangan, dan pelacak kesehatan.')">
    <meta property="og:image" content="{{ asset('images/logo.png') }}">
    <meta property="og:locale" content="id_ID">
    <meta property="og:site_name" content="E-Concalc">
    
    <!-- Twitter Card -->
    <meta name="twitter:card" content="summary_large_image">
    <meta name="twitter:title" content="@yield('title', 'E-Concalc - Kalkulator Elektronik Modern')">
    <meta name="twitter:description" content="@yield('meta_description', 'Aplikasi kalkulator elektronik modern dengan fitur lengkap.')">
    <meta name="twitter:image" content="{{ asset('images/logo.png') }}">
    
    <!-- Theme & PWA -->
    <meta name="theme-color" content="#4facfe">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black-translucent">
    <meta name="apple-mobile-web-app-title" content="E-Concalc">
    <meta name="application-name" content="E-Concalc">
    <meta name="msapplication-TileColor" content="#4facfe">
    
    <!-- Fonts -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    
    <!-- Stylesheets -->
    <link rel="stylesheet" href="{{ asset('css/style.css') }}?v={{ config('app.version', '1.0.0') }}">
    
    <!-- PWA Manifest -->
    <link rel="manifest" href="{{ asset('manifest.json') }}">
    
    <!-- Favicon & Icons -->
    <link rel="icon" type="image/png" sizes="32x32" href="{{ asset('images/logo.png') }}">
    <link rel="icon" type="image/png" sizes="16x16" href="{{ asset('images/logo.png') }}">
    <link rel="apple-touch-icon" sizes="180x180" href="{{ asset('images/logo.png') }}">
    
    <!-- Structured Data (JSON-LD) -->
    <script type="application/ld+json">
    {
        "@context": "https://schema.org",
        "@type": "WebApplication",
        "name": "E-Concalc",
        "description": "Aplikasi kalkulator elektronik modern dengan fitur kalkulator ilmiah, konversi mata uang, perencanaan keuangan, dan pelacak kesehatan.",
        "url": "{{ url('/') }}",
        "applicationCategory": "UtilitiesApplication",
        "operatingSystem": "Web Browser",
        "offers": {
            "@type": "Offer",
            "price": "0",
            "priceCurrency": "IDR"
        },
        "author": {
            "@type": "Organization",
            "name": "E-Concalc Team"
        }
    }
    </script>
    
    @yield('styles')
</head>
<body>
    @yield('content')
    
    <!-- Toast Notification Container -->
    <div id="save-toast" class="toast" role="alert" aria-live="polite"></div>
    
    @yield('scripts')
    
    <!-- Service Worker Registration -->
    <script>
        if ('serviceWorker' in navigator) {
            window.addEventListener('load', function() {
                navigator.serviceWorker.register('/sw.js')
                    .then(function(registration) {
                        console.log('ServiceWorker registered:', registration.scope);
                    })
                    .catch(function(error) {
                        console.log('ServiceWorker registration failed:', error);
                    });
            });
        }
    </script>
</body>
</html>
