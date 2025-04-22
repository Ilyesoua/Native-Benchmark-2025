package com.benchmark.restApi.controller;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

@Controller("/api/internal")
public class InternalProcessController {

    @Get("/process")
    public String runInternalProcess() {
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
