package com.benchmark.restApi.controller;

import com.benchmark.restApi.model.Movie;
import com.benchmark.restApi.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.benchmark.restApi.repository.MovieRepository;

import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @Autowired
    private MovieRepository movieRepository;

    // Ajouter un film
    @PostMapping
    public Movie saveMovie(@RequestBody Movie movie) {
        return movieService.saveMovie(movie);
    }

    // Rechercher un film par titre
    @GetMapping
    public Movie getMovie(@RequestParam String title) {
        return movieService.getMovieByTitle(title);
    }

    @GetMapping("/{id}")
    public Movie getMovieById(@PathVariable Long id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found with id " + id));
    }

    @GetMapping("/random")
    public Movie getRandomMovie() {
        List<Movie> movies = movieRepository.findAll();
        if (movies.isEmpty()) {
            throw new RuntimeException("No movies found");
        }
        Random random = new Random();
        return movies.get(random.nextInt(movies.size()));
    }
}
