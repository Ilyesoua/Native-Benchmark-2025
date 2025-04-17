package com.benchmark.restApi.service;

import com.benchmark.restApi.config.OmdbConfig;
import com.benchmark.restApi.model.Movie;
import com.benchmark.restApi.repository.MovieRepository;
import jakarta.enterprise.context.ApplicationScoped;
import com.benchmark.restApi.client.MovieApiClient;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jboss.logging.Logger;
import jakarta.transaction.Transactional;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

@ApplicationScoped
public class MovieService {

    private static final Logger logger = Logger.getLogger(MovieService.class);

    @Inject
    OmdbConfig omdbConfig;

    @Inject
    ObjectMapper objectMapper;

    @RestClient
    MovieApiClient movieApiClient;

    @Inject
    MovieRepository movieRepository;

    @Transactional
    public Movie saveMovie(Movie movie) {
        movieRepository.persist(movie); // persist ne retourne rien
        return movie;
    }

    public Movie getMovieByTitle(String title) {
        try {
            Movie movie = movieApiClient.getMovieByTitle(title, omdbConfig.getOmdbApiKey());
            logger.infof("Movie fetched: %s", movie);
            return movie;
        } catch (Exception e) {
            logger.errorf(e, "Error occurred while fetching movie data: %s", e.getMessage());
            return null;
        }
    }
    
}
