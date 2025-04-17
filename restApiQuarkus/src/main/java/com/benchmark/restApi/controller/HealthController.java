package com.benchmark.restApi.controller;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import java.util.Map;

@Path("/api/health")
public class HealthController {

    @GET
    public Map<String, String> healthCheck() {
        return Map.of("status", "ok");
    }
}
