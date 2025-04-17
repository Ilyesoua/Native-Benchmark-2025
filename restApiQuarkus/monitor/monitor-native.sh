#!/bin/bash

process_name="java"  # Nom du processus à surveiller
mkdir -p "/tmp/metrics-output"
csv_path="/tmp/metrics-output/native_cpu_mem_threads_$(date +%Y%m%d%H%M%S).csv"

# En-têtes CSV
echo "Timestamp,CPU_Usage_Percentage,Memory_Resident(MB),Memory_Virtual(MB),Thread_Count" > "$csv_path"

# Récupérer le PID du processus (attend que le process soit là)
while true; do
    pid=$(pgrep -f "$process_name" | head -n 1)
    if [[ -n "$pid" ]]; then
        echo "Processus trouvé: $process_name (PID=$pid)"
        break
    fi
    echo "En attente du processus '$process_name'..."
    sleep 2
done

# Initialisation pour le calcul CPU
prev_total_time=0
prev_time=$(date +%s)

# Boucle de monitoring
while kill -0 "$pid" 2>/dev/null; do
    timestamp=$(date +"%Y-%m-%d %H:%M:%S")

    # Temps CPU en secondes (utime + stime)
    stat_file="/proc/$pid/stat"
    stat_vals=($(cat "$stat_file"))
    utime=${stat_vals[13]}
    stime=${stat_vals[14]}
    total_time=$((utime + stime))

    # Temps écoulé
    now=$(date +%s)
    interval=$((now - prev_time))
    cpu_delta=$((total_time - prev_total_time))

    # Nombre de cœurs logiques (pour normaliser l'usage CPU)
    cores=$(nproc)
    hertz=$(getconf CLK_TCK)
    cpu_usage=$(echo "scale=2; 100 * $cpu_delta / $interval / $hertz / $cores" | bc 2>/dev/null)

    # Mémoire physique (RSS) et virtuelle (VMS)
    rss_kb=$(grep VmRSS /proc/"$pid"/status | awk '{print $2}')
    vms_kb=$(grep VmSize /proc/"$pid"/status | awk '{print $2}')
    rss_mb=$(echo "scale=2; $rss_kb / 1024" | bc)
    vms_mb=$(echo "scale=2; $vms_kb / 1024" | bc)

    # Nombre de threads
    thread_count=$(grep Threads /proc/"$pid"/status | awk '{print $2}')

    # Écriture dans le CSV
    echo "$timestamp,$cpu_usage,$rss_mb,$vms_mb,$thread_count" >> "$csv_path"

    # Mise à jour des valeurs précédentes
    prev_total_time=$total_time
    prev_time=$now

    sleep 1
done

echo "Monitoring terminé. CSV généré à $csv_path"
