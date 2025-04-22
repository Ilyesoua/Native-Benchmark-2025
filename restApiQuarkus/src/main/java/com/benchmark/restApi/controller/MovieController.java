package com.benchmark.restApi.controller;

import com.benchmark.restApi.model.Movie;
import com.benchmark.restApi.service.MovieService;
import com.benchmark.restApi.repository.MovieRepository;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.util.List;
import java.util.Random;

@Path("/api/movies")
public class MovieController {

    @Inject
    MovieService movieService;

    @Inject
    MovieRepository movieRepository;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Movie saveMovie(Movie movie) {
        return movieService.saveMovie(movie);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Movie getMovie(@QueryParam("title") String title) {
        return movieService.getMovieByTitle(title);
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Movie getMovieById(@PathParam("id") Long id) {
        return movieRepository.findById(id);
    }

    @GET
    @Path("/random")
    @Produces(MediaType.APPLICATION_JSON)
    public Movie getRandomMovie() {
        List<Movie> movies = movieRepository.listAll();
        if (movies.isEmpty()) {
            throw new RuntimeException("No movies found");
        }
        Random random = new Random();
        return movies.get(random.nextInt(movies.size()));
    }
}
