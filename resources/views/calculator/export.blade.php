<!DOCTYPE html>
<html lang="id">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Export Riwayat - E-Concalc</title>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        
        body {
            font-family: 'Poppins', Arial, sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            padding: 20px;
        }
        
        .container {
            max-width: 900px;
            margin: 0 auto;
            background: rgba(255, 255, 255, 0.95);
            border-radius: 20px;
            box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
            padding: 40px;
            backdrop-filter: blur(10px);
        }
        
        .header {
            text-align: center;
            margin-bottom: 30px;
            padding-bottom: 20px;
            border-bottom: 2px solid #e2e8f0;
        }
        
        h1 {
            color: #0f172a;
            font-size: 2rem;
            font-weight: 700;
            margin-bottom: 10px;
        }
        
        .subtitle {
            color: #64748b;
            font-size: 0.95rem;
        }
        
        .stats-row {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
            gap: 15px;
            margin-bottom: 30px;
        }
        
        .stat-card {
            background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
            color: white;
            padding: 20px;
            border-radius: 12px;
            text-align: center;
        }
        
        .stat-card.secondary {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        }
        
        .stat-card.tertiary {
            background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
        }
        
        .stat-value {
            font-size: 2rem;
            font-weight: 700;
        }
        
        .stat-label {
            font-size: 0.85rem;
            opacity: 0.9;
        }
        
        .action-buttons {
            display: flex;
            justify-content: center;
            gap: 15px;
            margin-bottom: 30px;
            flex-wrap: wrap;
        }
        
        .btn {
            display: inline-flex;
            align-items: center;
            gap: 8px;
            padding: 12px 24px;
            border: none;
            border-radius: 10px;
            font-size: 1rem;
            font-weight: 500;
            cursor: pointer;
            transition: all 0.3s ease;
            text-decoration: none;
        }
        
        .btn-primary {
            background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
            color: white;
        }
        
        .btn-secondary {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
        }
        
        .btn-success {
            background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%);
            color: white;
        }
        
        .btn-danger {
            background: linear-gradient(135deg, #ff416c 0%, #ff4b2b 100%);
            color: white;
        }
        
        .btn:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 20px rgba(0, 0, 0, 0.2);
        }
        
        table {
            width: 100%;
            border-collapse: collapse;
            margin: 20px 0;
            border-radius: 12px;
            overflow: hidden;
            box-shadow: 0 5px 20px rgba(0, 0, 0, 0.1);
        }
        
        th, td {
            padding: 15px;
            text-align: left;
        }
        
        th {
            background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
            color: white;
            font-weight: 600;
            text-transform: uppercase;
            font-size: 0.85rem;
            letter-spacing: 0.5px;
        }
        
        tbody tr {
            background: white;
            transition: all 0.3s ease;
        }
        
        tbody tr:nth-child(even) {
            background: #f8fafc;
        }
        
        tbody tr:hover {
            background: #e0f2fe;
            transform: scale(1.01);
        }
        
        td {
            border-bottom: 1px solid #e2e8f0;
            color: #334155;
        }
        
        .no-data {
            text-align: center;
            padding: 40px;
            color: #64748b;
        }
        
        .back-link {
            display: inline-flex;
            align-items: center;
            gap: 8px;
            color: #4facfe;
            text-decoration: none;
            margin-top: 20px;
            font-weight: 500;
        }
        
        .back-link:hover {
            text-decoration: underline;
        }
        
        @media print {
            body {
                background: white;
                padding: 0;
            }
            
            .container {
                box-shadow: none;
                padding: 20px;
            }
            
            .action-buttons,
            .back-link {
                display: none !important;
            }
            
            .stat-card {
                -webkit-print-color-adjust: exact;
                print-color-adjust: exact;
            }
        }
        
        @media (max-width: 600px) {
            .container {
                padding: 20px;
            }
            
            h1 {
                font-size: 1.5rem;
            }
            
            .btn {
                padding: 10px 18px;
                font-size: 0.9rem;
            }
            
            th, td {
                padding: 10px;
                font-size: 0.85rem;
            }
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>📊 Riwayat E-Concalc</h1>
            <p class="subtitle">Diekspor pada: {{ now()->format('d M Y H:i:s') }}</p>
        </div>
        
        <div class="stats-row">
            <div class="stat-card">
                <div class="stat-value">{{ count($data) }}</div>
                <div class="stat-label">Total Riwayat</div>
            </div>
            @if(count($data) > 0)
            <div class="stat-card secondary">
                <div class="stat-value">{{ $data->first()->created_at->format('d M') }}</div>
                <div class="stat-label">Terbaru</div>
            </div>
            <div class="stat-card tertiary">
                <div class="stat-value">{{ $data->last()->created_at->format('d M') }}</div>
                <div class="stat-label">Terlama</div>
            </div>
            @endif
        </div>

        <div class="action-buttons">
            <button class="btn btn-primary" onclick="window.print()">
                🖨️ Print / Save PDF
            </button>
            <button class="btn btn-success" onclick="exportCSV()">
                📥 Export CSV
            </button>
            <a href="{{ route('home') }}" class="btn btn-secondary">
                🏠 Kembali
            </a>
        </div>
        
        @if(count($data) > 0)
        <table id="history-table">
            <thead>
                <tr>
                    <th style="width: 60px;">No</th>
                    <th>Operasi</th>
                    <th style="width: 150px;">Waktu</th>
                </tr>
            </thead>
            <tbody>
                @foreach($data as $index => $row)
                <tr>
                    <td>{{ $index + 1 }}</td>
                    <td>{{ $row->operasi }}</td>
                    <td>{{ $row->created_at->format('d M Y H:i') }}</td>
                </tr>
                @endforeach
            </tbody>
        </table>
        @else
        <div class="no-data">
            <p style="font-size: 3rem; margin-bottom: 10px;">📭</p>
            <p>Belum ada riwayat perhitungan</p>
        </div>
        @endif
        
        <a href="{{ route('home') }}" class="back-link">
            ← Kembali ke Kalkulator
        </a>
    </div>

    <script>
        function exportCSV() {
            const table = document.getElementById('history-table');
            if (!table) {
                alert('Tidak ada data untuk diexport');
                return;
            }

            let csv = [];
            const rows = table.querySelectorAll('tr');
            
            // Add BOM for Excel UTF-8 compatibility
            csv.push('\uFEFF');
            
            rows.forEach(row => {
                const cols = row.querySelectorAll('th, td');
                const rowData = [];
                cols.forEach(col => {
                    // Escape quotes and wrap in quotes
                    let data = col.innerText.replace(/"/g, '""');
                    rowData.push(`"${data}"`);
                });
                csv.push(rowData.join(','));
            });

            const csvContent = csv.join('\n');
            const blob = new Blob([csvContent], { type: 'text/csv;charset=utf-8;' });
            const link = document.createElement('a');
            const url = URL.createObjectURL(blob);
            
            const now = new Date();
            const filename = `riwayat_econcalc_${now.getFullYear()}${String(now.getMonth()+1).padStart(2,'0')}${String(now.getDate()).padStart(2,'0')}.csv`;
            
            link.setAttribute('href', url);
            link.setAttribute('download', filename);
            link.style.visibility = 'hidden';
            document.body.appendChild(link);
            link.click();
            document.body.removeChild(link);
        }
    </script>
</body>
</html>
