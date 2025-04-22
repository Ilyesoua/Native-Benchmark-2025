package com.benchmark.restApi.controller;

import com.benchmark.restApi.model.Movie;
import com.benchmark.restApi.service.MovieService;
import com.benchmark.restApi.repository.MovieRepository;

import io.micronaut.http.annotation.*;

import jakarta.inject.Inject;

import java.util.List;
import java.util.Random;

@Controller("/api/movies")
public class MovieController {

    @Inject
    MovieService movieService;

    @Inject
    MovieRepository movieRepository;

    @Post
    public Movie saveMovie(@Body Movie movie) {
        return movieService.saveMovie(movie);
    }

    @Get
    public Movie getMovie(@QueryValue String title) {
        return movieService.getMovieByTitle(title);
    }

    @Get("/{id}")
    public Movie getMovieById(@PathVariable Long id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found with id " + id));
    }

    @Get("/random")
    public Movie getRandomMovie() {
        List<Movie> movies = movieRepository.findAll();
        if (movies.isEmpty()) {
            throw new RuntimeException("No movies found");
        }
        Random random = new Random();
        return movies.get(random.nextInt(movies.size()));
    }
}
