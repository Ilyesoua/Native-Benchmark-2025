docker-compose -f docker-compose.yml up --build -d

New-Item -ItemType Directory -Force -Path "metrics-output"

$csv_path = "metrics-output\docker_metrics_$(Get-Date -Format 'yyyyMMddHHmmss').csv"

"Time,Container Name,CPU %,Memory Usage" | Out-File -FilePath $csv_path -Encoding UTF8

while ($true) {
    $timestamp = Get-Date -Format 'yyyy-MM-dd HH:mm:ss'
    $dockerStats = docker stats api-benchmark --no-stream --format "{{.Name}},{{.CPUPerc}},{{.MemUsage}}"
    
    "$timestamp,$dockerStats" | Out-File -FilePath $csv_path -Append -Encoding UTF8
    
    Start-Sleep -Seconds 1
}
