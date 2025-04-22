package com.benchmark.restApi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Collections;
import java.io.IOException;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.HttpRequest;
import org.springframework.web.client.ResponseErrorHandler;

@Configuration
public class OmdbConfig {

    @Value("${omdb.api.key}")
    private String omdbApiKey;

    @Value("${omdb.api.url}")
    private String omdbApiUrl;

    @Value("${proxy.host}")
    private String proxyHost;

    @Value("${proxy.port}")
    private int proxyPort;

    @Bean
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();

        

        RestTemplate restTemplate = new RestTemplate(factory);

        MappingJackson2HttpMessageConverter jacksonConverter = new MappingJackson2HttpMessageConverter();
        
        restTemplate.getMessageConverters().add(jacksonConverter);

        restTemplate.setInterceptors(Collections.singletonList(
            (request, body, execution) -> {
                request.getHeaders().add("Accept-Encoding", "gzip, deflate");
                return execution.execute(request, body);
            }
        ));

        System.out.println("RestTemplate avec Proxy et Compression: " + restTemplate);

        return restTemplate;
    }

    public String getOmdbApiKey() {
        return omdbApiKey;
    }

    public String getOmdbApiUrl() {
        return omdbApiUrl;
    }
}
