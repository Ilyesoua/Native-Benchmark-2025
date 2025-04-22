package com.benchmark.restApi.config;

import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.context.annotation.Requires;

@ConfigurationProperties("omdb")
@Requires(property = "omdb.api.key") // pour éviter que le bean ne se charge si la conf n’existe pas
public class OmdbConfig {

    private String apiKey;
    private String apiUrl;

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }
}
