package com.benchmark.restApi.controller;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("/api/internal")
public class InternalProcessController {

    @GET
    @Path("/process")
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
