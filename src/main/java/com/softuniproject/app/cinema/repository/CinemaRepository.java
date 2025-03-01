package com.softuniproject.app.cinema.repository;

import com.softuniproject.app.cinema.entity.Cinema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CinemaRepository extends JpaRepository<Cinema, UUID> {
}
