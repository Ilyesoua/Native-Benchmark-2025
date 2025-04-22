package com.benchmark.restApi.repository;

import com.benchmark.restApi.model.Movie;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MovieRepository implements PanacheRepositoryBase<Movie, Long> {
    // PanacheRepositoryBase fournit déjà des méthodes de gestion des entités comme persist(), find(), delete(), etc.
}
