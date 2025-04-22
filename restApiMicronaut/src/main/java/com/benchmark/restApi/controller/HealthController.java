package com.benchmark.restApi.controller;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

import java.util.Map;

@Controller("/api/health")
public class HealthController {

    @Get
    public Map<String, String> healthCheck() {
        return Map.of("status", "ok");
    }
}
