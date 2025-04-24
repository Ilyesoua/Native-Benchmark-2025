#!/bin/bash

process_name="rustc"
mkdir -p "/tmp/metrics-output"
csv_path="/tmp/metrics-output/cpu_mem_threads_$(date +%Y%m%d%H%M%S).csv"

# En-têtes CSV
echo "Timestamp,CPU_Usage_Percentage,Memory_Resident(MB),Memory_Virtual(MB),Thread_Count" > "$csv_path"

# Attente jusqu'à ce qu'au moins un processus rustc soit présent
while true; do
    pids=($(pgrep -f "$process_name"))
    if [[ ${#pids[@]} -gt 0 ]]; then
        echo "Processus trouvés: ${pids[@]}"
        break
    fi
    echo "En attente de processus '$process_name'..."
    sleep 2
done

# Initialisation
declare -A prev_cpu_time
prev_time=$(date +%s)

# Récupère les ticks par seconde
hertz=$(getconf CLK_TCK)
cores=$(nproc)

# Boucle de monitoring
while true; do
    pids=($(pgrep -f "$process_name"))
    [[ ${#pids[@]} -eq 0 ]] && break  # Sort si plus aucun rustc actif

    timestamp=$(date +"%Y-%m-%d %H:%M:%S")
    total_cpu_time=0
    total_rss=0
    total_vms=0
    total_threads=0

    for pid in "${pids[@]}"; do
        if [[ -d "/proc/$pid" ]]; then
            stat_vals=($(cat "/proc/$pid/stat"))
            utime=${stat_vals[13]}
            stime=${stat_vals[14]}
            total_time=$((utime + stime))

            # Calcule l'utilisation CPU pour ce PID
            prev=${prev_cpu_time[$pid]:-0}
            cpu_delta=$((total_time - prev))
            total_cpu_time=$((total_cpu_time + cpu_delta))
            prev_cpu_time[$pid]=$total_time

            # Mémoire
            rss_kb=$(grep VmRSS /proc/"$pid"/status | awk '{print $2}')
            vms_kb=$(grep VmSize /proc/"$pid"/status | awk '{print $2}')
            rss_kb=${rss_kb:-0}
            vms_kb=${vms_kb:-0}
            total_rss=$((total_rss + rss_kb))
            total_vms=$((total_vms + vms_kb))

            # Threads
            threads=$(grep Threads /proc/"$pid"/status | awk '{print $2}')
            total_threads=$((total_threads + threads))
        fi
    done

    # Temps écoulé
    now=$(date +%s)
    interval=$((now - prev_time))
    prev_time=$now

    # CPU usage global
    cpu_usage=$(echo "scale=2; 100 * $total_cpu_time / $interval / $hertz / $cores" | bc 2>/dev/null)

    # Conversion mémoire en MB
    rss_mb=$(echo "scale=2; $total_rss / 1024" | bc)
    vms_mb=$(echo "scale=2; $total_vms / 1024" | bc)

    # Enregistrement dans CSV
    echo "$timestamp,$cpu_usage,$rss_mb,$vms_mb,$total_threads" >> "$csv_path"

    sleep 1
done

echo "Monitoring terminé. CSV généré à $csv_path"
