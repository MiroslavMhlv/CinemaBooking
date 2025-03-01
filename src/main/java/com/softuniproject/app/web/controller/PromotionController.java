package com.softuniproject.app.web.controller;

import com.softuniproject.app.promotion.entity.Promotion;
import com.softuniproject.app.promotion.service.PromotionService;
import com.softuniproject.app.web.dto.PromotionRequest;
import com.softuniproject.app.web.mapper.DtoMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/promotions")
@RequiredArgsConstructor
public class PromotionController {

    private final PromotionService promotionService;
    private final DtoMapper dtoMapper;

    // 🔹 Преглед на всички промоции
    @GetMapping
    public ModelAndView getAllPromotions() {
        List<Promotion> promotions = promotionService.getAllPromotions();
        ModelAndView modelAndView = new ModelAndView("promotions");
        modelAndView.addObject("promotions", promotions);
        return modelAndView;
    }

    // 🔹 Зареждане на формата за добавяне на нова промоция
    @GetMapping("/add")
    public ModelAndView getAddPromotionForm() {
        ModelAndView modelAndView = new ModelAndView("add-promotion");
        modelAndView.addObject("promotionRequest", new PromotionRequest());
        return modelAndView;
    }

    // 🔹 Добавяне на нова промоция
    @PostMapping("/add")
    public ModelAndView addPromotion(@Valid PromotionRequest promotionRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ModelAndView("add-promotion");
        }

        promotionService.addPromotion(dtoMapper.mapPromotionRequestToPromotion(promotionRequest));
        return new ModelAndView("redirect:/promotions");
    }

    // 🔹 Изтриване на промоция
    @PostMapping("/{id}/delete")
    public String deletePromotion(@PathVariable UUID id) {
        promotionService.deletePromotion(id);
        return "redirect:/promotions";
    }
}
