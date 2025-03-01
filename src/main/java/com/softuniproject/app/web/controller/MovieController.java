package com.softuniproject.app.web.controller;

import com.softuniproject.app.movie.entity.Movie;
import com.softuniproject.app.movie.service.MovieService;
import com.softuniproject.app.web.dto.MovieRequest;
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
@RequestMapping("/movies")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;
    private final DtoMapper dtoMapper;

    // 🔹 Преглед на всички филми
    @GetMapping
    public ModelAndView getAllMovies() {
        List<Movie> movies = movieService.getAllMovies();
        ModelAndView modelAndView = new ModelAndView("movies");
        modelAndView.addObject("movies", movies);
        return modelAndView;
    }

    // 🔹 Зареждане на формата за добавяне на нов филм
    @GetMapping("/add")
    public ModelAndView getAddMovieForm() {
        ModelAndView modelAndView = new ModelAndView("add-movie");
        modelAndView.addObject("movieRequest", new MovieRequest());
        return modelAndView;
    }

    // 🔹 Добавяне на нов филм
    @PostMapping("/add")
    public ModelAndView addMovie(@Valid MovieRequest movieRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ModelAndView("add-movie");
        }

        movieService.addMovie(dtoMapper.mapMovieRequestToMovie(movieRequest));
        return new ModelAndView("redirect:/movies");
    }

    // 🔹 Изтриване на филм
    @PostMapping("/{id}/delete")
    public String deleteMovie(@PathVariable UUID id) {
        movieService.deleteMovie(id);
        return "redirect:/movies";
    }
}
