package com.softuniproject.app.web.controller;

import com.softuniproject.app.booking.entity.Booking;
import com.softuniproject.app.booking.service.BookingService;
import com.softuniproject.app.user.entity.User;
import com.softuniproject.app.user.service.UserService;
import com.softuniproject.app.web.dto.BookingRequest;
import com.softuniproject.app.web.mapper.DtoMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;
    private final UserService userService;
    private final DtoMapper dtoMapper;  // 🔹 Добавяме DtoMapper като зависимост

    // 🔹 Преглед на всички резервации на текущия потребител
    @GetMapping
    public ModelAndView getUserBookings(@AuthenticationPrincipal User user) {
        List<Booking> bookings = bookingService.getBookingsByUser(user);
        ModelAndView modelAndView = new ModelAndView("bookings");
        modelAndView.addObject("bookings", bookings);
        return modelAndView;
    }

    // 🔹 Зареждане на формата за нова резервация
    @GetMapping("/create")
    public ModelAndView getCreateBookingForm() {
        ModelAndView modelAndView = new ModelAndView("create-booking");
        modelAndView.addObject("bookingRequest", new BookingRequest());
        return modelAndView;
    }

    // 🔹 Създаване на резервация
    @PostMapping("/create")
    public ModelAndView createBooking(@Valid BookingRequest bookingRequest, BindingResult bindingResult,
                                      @AuthenticationPrincipal User user) {
        if (bindingResult.hasErrors()) {
            return new ModelAndView("create-booking");
        }

        Booking booking = dtoMapper.mapBookingRequestToBooking(bookingRequest, user,
                bookingService.getMovieById(bookingRequest.getMovieId()),
                bookingService.getCinemaById(bookingRequest.getCinemaId()),
                bookingService.getTicketById(bookingRequest.getTicketId()));

        bookingService.createBooking(user, booking);
        return new ModelAndView("redirect:/bookings");
    }

    // 🔹 Отмяна на резервация
    @PostMapping("/{id}/cancel")
    public String cancelBooking(@PathVariable UUID id) {
        bookingService.cancelBooking(id);
        return "redirect:/bookings";
    }
}
