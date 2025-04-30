#!/bin/bash

docker-compose -f docker-compose.yml up --build -d

mkdir -p metrics-output

csv_path="metrics-output/docker_metrics_$(date +%Y%m%d%H%M%S).csv"

echo "Time,Container Name,CPU %,Memory Usage" > "$csv_path"

while true; do
    timestamp=$(date '+%Y-%m-%d %H:%M:%S')
    docker stats api-benchmark --no-stream --format "$timestamp,{{.Name}},{{.CPUPerc}},{{.MemUsage}}" >> "$csv_path"
    sleep 1
done
