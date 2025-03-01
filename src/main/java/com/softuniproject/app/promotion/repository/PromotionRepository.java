package com.softuniproject.app.promotion.repository;

import com.softuniproject.app.cinema.entity.Cinema;
import com.softuniproject.app.promotion.entity.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.List;
import java.util.UUID;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, UUID> {
    List<Promotion> findByCinemaAndDayOfWeek(Cinema cinema, DayOfWeek day);
}
