package com.softuniproject.app.promotion.service;

import com.softuniproject.app.cinema.entity.Cinema;
import com.softuniproject.app.exception.DomainException;
import com.softuniproject.app.promotion.entity.Promotion;
import com.softuniproject.app.promotion.repository.PromotionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PromotionService {

    private final PromotionRepository promotionRepository;

    // Връщане на всички промоции (с кеширане)
    @Cacheable(value = "promotions")
    public List<Promotion> getAllPromotions() {
        return promotionRepository.findAll();
    }

    // Връщане на промоция по ID
    public Promotion getPromotionById(UUID id) {
        return promotionRepository.findById(id)
                .orElseThrow(() -> new DomainException("Promotion with id [%s] does not exist.".formatted(id)));
    }

    // Връщане на промоции за дадено кино и ден от седмицата
    public List<Promotion> getPromotionsByCinemaAndDay(Cinema cinema, DayOfWeek day) {
        return promotionRepository.findByCinemaAndDayOfWeek(cinema, day);
    }

    // Добавяне на нова промоция
    @CacheEvict(value = "promotions", allEntries = true)
    public Promotion addPromotion(Promotion promotion) {
        Promotion savedPromotion = promotionRepository.save(promotion);
        log.info("Promotion [{}] added successfully.", savedPromotion.getId());
        return savedPromotion;
    }

    // Изтриване на промоция
    @CacheEvict(value = "promotions", allEntries = true)
    public void deletePromotion(UUID id) {
        if (!promotionRepository.existsById(id)) {
            throw new DomainException("Promotion with id [%s] does not exist.".formatted(id));
        }
        promotionRepository.deleteById(id);
        log.info("Promotion with id [{}] deleted successfully.", id);
    }
}
