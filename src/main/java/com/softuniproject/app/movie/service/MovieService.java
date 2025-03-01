package com.softuniproject.app.movie.service;

import com.softuniproject.app.exception.DomainException;
import com.softuniproject.app.movie.entity.Movie;
import com.softuniproject.app.movie.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;

    // Връщане на всички филми (с кеширане)
    @Cacheable(value = "movies")
    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    // Връщане на филм по ID
    public Movie getMovieById(UUID id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new DomainException("Movie with id [%s] does not exist.".formatted(id)));
    }

    // Създаване на нов филм
    @CacheEvict(value = "movies", allEntries = true)
    public Movie addMovie(Movie movie) {
        Movie savedMovie = movieRepository.save(movie);
        log.info("Movie [{}] added successfully.", savedMovie.getTitle());
        return savedMovie;
    }

    // Изтриване на филм
    @CacheEvict(value = "movies", allEntries = true)
    public void deleteMovie(UUID id) {
        if (!movieRepository.existsById(id)) {
            throw new DomainException("Movie with id [%s] does not exist.".formatted(id));
        }
        movieRepository.deleteById(id);
        log.info("Movie with id [{}] deleted successfully.", id);
    }

}
