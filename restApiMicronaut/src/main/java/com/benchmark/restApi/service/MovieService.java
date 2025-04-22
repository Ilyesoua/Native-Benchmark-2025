package com.benchmark.restApi.service;

import com.benchmark.restApi.config.OmdbConfig;
import com.benchmark.restApi.model.Movie;
import com.benchmark.restApi.repository.MovieRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.MutableHttpRequest;
import io.micronaut.http.HttpResponse;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

@Singleton
public class MovieService {

    private static final Logger logger = LoggerFactory.getLogger(MovieService.class);

    @Inject
    private OmdbConfig omdbConfig;

    @Inject
    @Client("/")  // On utilisera l'URL complète dans la requête
    private HttpClient httpClient;

    @Inject
    private ObjectMapper objectMapper;

    @Inject
    private MovieRepository movieRepository;

    public Movie saveMovie(Movie movie) {
        return movieRepository.save(movie);
    }

    public Movie getMovieByTitle(String title) {
        try {
            String url = omdbConfig.getApiUrl() + "?t=" + title + "&apikey=" + omdbConfig.getApiKey();

            MutableHttpRequest<?> request = HttpRequest.GET(url)
                    .header(HttpHeaders.ACCEPT_ENCODING, "gzip");

            HttpResponse<byte[]> response = httpClient.toBlocking().exchange(request, byte[].class);
            byte[] body = response.body();

            logger.info("Response headers: {}", response.getHeaders());

            if (body != null && isGzipped(response)) {
                body = decompressGzip(body);
            }

            String responseString = new String(body);
            logger.info("Response body (raw): {}", responseString);

            return objectMapper.readValue(responseString, Movie.class);

        } catch (Exception e) {
            logger.error("Error fetching movie from OMDB: {}", e.getMessage(), e);
        }
        return null;
    }

    private boolean isGzipped(HttpResponse<?> response) {
        return response.getHeaders()
                .getAll(HttpHeaders.CONTENT_ENCODING)
                .stream()
                .anyMatch(e -> e.toLowerCase().contains("gzip"));
    }

    private byte[] decompressGzip(byte[] compressedData) throws IOException {
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(compressedData);
             GZIPInputStream gzipInputStream = new GZIPInputStream(byteArrayInputStream);
             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = gzipInputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }

            return byteArrayOutputStream.toByteArray();
        }
    }
}
