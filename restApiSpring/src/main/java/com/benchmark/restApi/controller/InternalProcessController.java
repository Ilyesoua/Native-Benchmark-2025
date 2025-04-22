package com.benchmark.restApi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/internal")
public class InternalProcessController {

    @GetMapping("/process")
    public String runInternalProcess() {
        // Traitement CPU-bound lourd simulé
        long result = heavyCpuTask();
        return "Traitement terminé avec résultat : " + result;
    }

    private long heavyCpuTask() {
        long sum = 0;
        for (long i = 0; i < 500_000_000L; i++) {
            sum += i;
        }
        return sum;
    }
}
