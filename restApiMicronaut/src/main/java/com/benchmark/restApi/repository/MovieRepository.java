package com.benchmark.restApi.repository;

import com.benchmark.restApi.model.Movie;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

@Repository
public interface MovieRepository extends CrudRepository<Movie, Long> {
}
