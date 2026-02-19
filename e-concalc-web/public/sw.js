// E-Concalc Service Worker - Optimized for Laravel
const CACHE_NAME = 'econcalc-v2';
const STATIC_CACHE = 'econcalc-static-v2';
const DYNAMIC_CACHE = 'econcalc-dynamic-v2';

// Static assets to cache immediately
const STATIC_ASSETS = [
    '/css/style.css',
    '/js/script.js',
    '/js/financial.js',
    '/js/health.js',
    '/js/currency.js',
    '/images/logo.png',
    '/manifest.json',
    'https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap'
];

// Pages to cache for offline access
const OFFLINE_PAGES = [
    '/',
    '/login'
];

// Cache size limits
const CACHE_LIMIT = 50;

// Helper: Limit cache size
async function limitCacheSize(cacheName, maxItems) {
    const cache = await caches.open(cacheName);
    const keys = await cache.keys();
    if (keys.length > maxItems) {
        await cache.delete(keys[0]);
        await limitCacheSize(cacheName, maxItems);
    }
}

// Install event - cache static assets
self.addEventListener('install', event => {
    console.log('[SW] Installing...');
    event.waitUntil(
        caches.open(STATIC_CACHE)
            .then(cache => {
                console.log('[SW] Caching static assets');
                return cache.addAll(STATIC_ASSETS);
            })
            .then(() => {
                // Pre-cache some pages
                return caches.open(CACHE_NAME).then(cache => {
                    return Promise.allSettled(
                        OFFLINE_PAGES.map(url =>
                            fetch(url)
                                .then(response => {
                                    if (response.ok) {
                                        return cache.put(url, response);
                                    }
                                })
                                .catch(() => console.log('[SW] Could not cache:', url))
                        )
                    );
                });
            })
            .then(() => self.skipWaiting())
            .catch(err => console.error('[SW] Install error:', err))
    );
});

// Activate event - clean old caches
self.addEventListener('activate', event => {
    console.log('[SW] Activating...');
    event.waitUntil(
        caches.keys()
            .then(cacheNames => {
                return Promise.all(
                    cacheNames
                        .filter(cacheName => {
                            return cacheName !== CACHE_NAME &&
                                cacheName !== STATIC_CACHE &&
                                cacheName !== DYNAMIC_CACHE;
                        })
                        .map(cacheName => {
                            console.log('[SW] Deleting old cache:', cacheName);
                            return caches.delete(cacheName);
                        })
                );
            })
            .then(() => self.clients.claim())
    );
});

// Fetch event - smart caching strategy
self.addEventListener('fetch', event => {
    const request = event.request;
    const url = new URL(request.url);

    // Skip non-GET requests
    if (request.method !== 'GET') {
        return;
    }

    // Skip cross-origin requests except fonts
    if (url.origin !== location.origin && !url.hostname.includes('fonts.')) {
        return;
    }

    // Skip API/Auth requests (should always be fresh)
    if (url.pathname.includes('/api/') ||
        url.pathname.includes('/save') ||
        url.pathname.includes('/delete') ||
        url.pathname.includes('/logout') ||
        url.pathname.includes('/sanctum')) {
        return;
    }

    // Strategy: Cache First for static assets
    if (isStaticAsset(url.pathname)) {
        event.respondWith(cacheFirst(request));
        return;
    }

    // Strategy: Network First for HTML pages
    if (request.headers.get('accept')?.includes('text/html')) {
        event.respondWith(networkFirst(request));
        return;
    }

    // Strategy: Stale While Revalidate for everything else
    event.respondWith(staleWhileRevalidate(request));
});

// Check if request is for static asset
function isStaticAsset(pathname) {
    const staticExtensions = ['.css', '.js', '.png', '.jpg', '.jpeg', '.gif', '.svg', '.ico', '.woff', '.woff2', '.ttf'];
    return staticExtensions.some(ext => pathname.endsWith(ext));
}

// Cache First Strategy
async function cacheFirst(request) {
    const cachedResponse = await caches.match(request);
    if (cachedResponse) {
        return cachedResponse;
    }

    try {
        const networkResponse = await fetch(request);
        if (networkResponse.ok) {
            const cache = await caches.open(STATIC_CACHE);
            cache.put(request, networkResponse.clone());
        }
        return networkResponse;
    } catch (error) {
        console.log('[SW] Cache first failed:', error);
        return new Response('Offline', { status: 503 });
    }
}

// Network First Strategy
async function networkFirst(request) {
    try {
        const networkResponse = await fetch(request);
        if (networkResponse.ok) {
            const cache = await caches.open(CACHE_NAME);
            cache.put(request, networkResponse.clone());
        }
        return networkResponse;
    } catch (error) {
        console.log('[SW] Network first fallback to cache');
        const cachedResponse = await caches.match(request);
        if (cachedResponse) {
            return cachedResponse;
        }

        // Return offline page
        const offlineResponse = await caches.match('/');
        if (offlineResponse) {
            return offlineResponse;
        }

        return new Response(
            '<html><body><h1>Offline</h1><p>Anda sedang offline. Silakan periksa koneksi internet.</p></body></html>',
            {
                headers: { 'Content-Type': 'text/html' },
                status: 503
            }
        );
    }
}

// Stale While Revalidate Strategy
async function staleWhileRevalidate(request) {
    const cachedResponse = await caches.match(request);

    const fetchPromise = fetch(request)
        .then(async networkResponse => {
            if (networkResponse.ok) {
                const cache = await caches.open(DYNAMIC_CACHE);
                cache.put(request, networkResponse.clone());
                await limitCacheSize(DYNAMIC_CACHE, CACHE_LIMIT);
            }
            return networkResponse;
        })
        .catch(() => cachedResponse);

    return cachedResponse || fetchPromise;
}

// Background Sync for failed POST requests (optional, for future use)
self.addEventListener('sync', event => {
    if (event.tag === 'sync-data') {
        console.log('[SW] Syncing data...');
        // Handle background sync here
    }
});

// Push Notifications (optional, for future use)
self.addEventListener('push', event => {
    if (event.data) {
        const data = event.data.json();
        const options = {
            body: data.body || 'Notifikasi dari E-Concalc',
            icon: '/images/logo.png',
            badge: '/images/logo.png',
            vibrate: [100, 50, 100],
            data: {
                url: data.url || '/'
            }
        };
        event.waitUntil(
            self.registration.showNotification(data.title || 'E-Concalc', options)
        );
    }
});

// Notification click handler
self.addEventListener('notificationclick', event => {
    event.notification.close();
    event.waitUntil(
        clients.openWindow(event.notification.data.url || '/')
    );
});

console.log('[SW] Service Worker loaded - v2');
