package com.benchmark.restApi.config;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class OmdbConfig {

    @ConfigProperty(name = "omdb.api.url")
    String omdbApiUrl;

    @ConfigProperty(name = "omdb.api.key")
    String omdbApiKey;

    public String getOmdbApiUrl() {
        return omdbApiUrl;
    }

    public String getOmdbApiKey() {
        return omdbApiKey;
    }
}
