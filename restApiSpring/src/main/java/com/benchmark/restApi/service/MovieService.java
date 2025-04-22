package com.benchmark.restApi.service;

import com.benchmark.restApi.config.OmdbConfig;
import com.benchmark.restApi.model.Movie;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import com.benchmark.restApi.repository.MovieRepository;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

@Service
public class MovieService {

    private static final Logger logger = LoggerFactory.getLogger(MovieService.class);

    @Autowired
    private OmdbConfig omdbConfig;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MovieRepository movieRepository;

    public Movie saveMovie(Movie movie) {
        return movieRepository.save(movie);
    }


    public Movie getMovieByTitle(String title) {
        try {
            // Construire l'URL de la requête avec paramètres
            String url = UriComponentsBuilder.fromHttpUrl(omdbConfig.getOmdbApiUrl())
                    .queryParam("t", title)
                    .queryParam("apikey", omdbConfig.getOmdbApiKey())
                    .toUriString();

            // Effectuer la requête GET et obtenir la réponse sous forme de tableau d'octets
            ResponseEntity<byte[]> response = restTemplate.getForEntity(url, byte[].class);
            HttpHeaders headers = response.getHeaders();
            logger.info("Response headers: {}", headers);

            byte[] responseBody = response.getBody();
            if (responseBody != null && headers.get("Content-Encoding") != null && headers.get("Content-Encoding").contains("gzip")) {
                // Si la réponse est compressée en gzip, décompresser
                responseBody = decompressGzip(responseBody);
            }

            // Convertir la réponse en chaîne de caractères
            String responseBodyString = new String(responseBody);

            logger.info("Response body (raw): {}", responseBodyString);

            // Désérialiser la réponse JSON en objet Movie
            return objectMapper.readValue(responseBodyString, Movie.class);

        } catch (HttpClientErrorException e) {
            logger.error("Error occurred while fetching movie data: {}", e.getMessage(), e);
        } catch (IOException e) {
            logger.error("Error occurred while decompressing or parsing JSON response: {}", e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Unexpected error: {}", e.getMessage(), e);
        }
        return null;
    }

    // Méthode pour décompresser les données gzip
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
