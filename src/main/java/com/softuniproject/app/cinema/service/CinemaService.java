package com.softuniproject.app.cinema.service;

import com.softuniproject.app.cinema.entity.Cinema;
import com.softuniproject.app.cinema.repository.CinemaRepository;
import com.softuniproject.app.exception.DomainException;
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
public class CinemaService {

    private final CinemaRepository cinemaRepository;

    // Връщане на всички кина (с кеширане)
    @Cacheable(value = "cinemas")
    public List<Cinema> getAllCinemas() {
        return cinemaRepository.findAll();
    }

    // Връщане на кино по ID
    public Cinema getCinemaById(UUID id) {
        return cinemaRepository.findById(id)
                .orElseThrow(() -> new DomainException("Cinema with id [%s] does not exist.".formatted(id)));
    }

    // Добавяне на ново кино
    @CacheEvict(value = "cinemas", allEntries = true)
    public Cinema addCinema(Cinema cinema) {
        Cinema savedCinema = cinemaRepository.save(cinema);
        log.info("Cinema [{}] added successfully.", savedCinema.getName());
        return savedCinema;
    }

    // Изтриване на кино
    @CacheEvict(value = "cinemas", allEntries = true)
    public void deleteCinema(UUID id) {
        if (!cinemaRepository.existsById(id)) {
            throw new DomainException("Cinema with id [%s] does not exist.".formatted(id));
        }
        cinemaRepository.deleteById(id);
        log.info("Cinema with id [{}] deleted successfully.", id);
    }
}
