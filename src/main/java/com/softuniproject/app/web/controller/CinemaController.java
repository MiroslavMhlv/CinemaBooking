package com.softuniproject.app.web.controller;

import com.softuniproject.app.cinema.entity.Cinema;
import com.softuniproject.app.cinema.service.CinemaService;
import com.softuniproject.app.web.dto.CinemaRequest;
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
@RequestMapping("/cinemas")
@RequiredArgsConstructor
public class CinemaController {

    private final CinemaService cinemaService;

    // 🔹 Преглед на всички кина
    @GetMapping
    public ModelAndView getAllCinemas() {
        List<Cinema> cinemas = cinemaService.getAllCinemas();
        ModelAndView modelAndView = new ModelAndView("cinemas");
        modelAndView.addObject("cinemas", cinemas);
        return modelAndView;
    }

    // 🔹 Зареждане на формата за добавяне на кино
    @GetMapping("/add")
    public ModelAndView getAddCinemaForm() {
        ModelAndView modelAndView = new ModelAndView("add-cinema");
        modelAndView.addObject("cinemaRequest", new CinemaRequest());
        return modelAndView;
    }

    // 🔹 Добавяне на ново кино
    @PostMapping("/add")
    public ModelAndView addCinema(@Valid CinemaRequest cinemaRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ModelAndView("add-cinema");
        }

        cinemaService.addCinema(DtoMapper.mapCinemaRequestToCinema(cinemaRequest));
        return new ModelAndView("redirect:/cinemas");
    }

    // 🔹 Изтриване на кино
    @PostMapping("/{id}/delete")
    public String deleteCinema(@PathVariable UUID id) {
        cinemaService.deleteCinema(id);
        return "redirect:/cinemas";
    }
}
