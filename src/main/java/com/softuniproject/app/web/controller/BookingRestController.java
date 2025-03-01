package com.softuniproject.app.web.controller;

import com.softuniproject.app.booking.entity.Booking;
import com.softuniproject.app.booking.service.BookingService;
import com.softuniproject.app.user.entity.User;
import com.softuniproject.app.web.dto.BookingRequest;
import com.softuniproject.app.web.mapper.DtoMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingRestController {

    private final BookingService bookingService;
    private final DtoMapper dtoMapper;

    @GetMapping
    public ResponseEntity<List<Booking>> getUserBookings(@AuthenticationPrincipal User user) {
        List<Booking> bookings = bookingService.getBookingsByUser(user);
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Booking> getBookingById(@PathVariable UUID id) {
        Booking booking = bookingService.getBookingById(id);
        return ResponseEntity.ok(booking);
    }

    @PostMapping
    public ResponseEntity<Booking> createBooking(@Valid @RequestBody BookingRequest bookingRequest,
                                                 @AuthenticationPrincipal User user) {
        Booking booking = dtoMapper.mapBookingRequestToBooking(
                bookingRequest, user,
                bookingService.getMovieById(bookingRequest.getMovieId()),
                bookingService.getCinemaById(bookingRequest.getCinemaId()),
                bookingService.getTicketById(bookingRequest.getTicketId()));

        Booking savedBooking = bookingService.createBooking(user, booking);
        return ResponseEntity.created(URI.create("/api/bookings/" + savedBooking.getId())).body(savedBooking);
    }

    @PutMapping("/{id}/confirm")
    public ResponseEntity<Booking> confirmBooking(@PathVariable UUID id) {
        Booking updatedBooking = bookingService.confirmBooking(id);
        return ResponseEntity.ok(updatedBooking);
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<Booking> cancelBooking(@PathVariable UUID id) {
        Booking updatedBooking = bookingService.cancelBooking(id);
        return ResponseEntity.ok(updatedBooking);
    }
}
