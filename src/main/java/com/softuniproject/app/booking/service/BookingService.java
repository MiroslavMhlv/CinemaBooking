package com.softuniproject.app.booking.service;

import com.softuniproject.app.booking.entity.Booking;
import com.softuniproject.app.booking.entity.BookingStatus;
import com.softuniproject.app.booking.repository.BookingRepository;
import com.softuniproject.app.cinema.entity.Cinema;
import com.softuniproject.app.cinema.service.CinemaService;
import com.softuniproject.app.exception.DomainException;
import com.softuniproject.app.movie.entity.Movie;
import com.softuniproject.app.movie.service.MovieService;
import com.softuniproject.app.ticket.entity.Ticket;
import com.softuniproject.app.ticket.service.TicketService;
import com.softuniproject.app.user.entity.User;
import com.softuniproject.app.user.repository.UserRepository;
import org.slf4j.Logger;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class BookingService {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(BookingService.class);
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final TicketService ticketService;
    private final MovieService movieService;
    private final CinemaService cinemaService;

    public BookingService(BookingRepository bookingRepository, UserRepository userRepository, TicketService ticketService, MovieService movieService, CinemaService cinemaService) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.ticketService = ticketService;
        this.movieService = movieService;
        this.cinemaService = cinemaService;
    }

    @Cacheable(value = "bookings", key = "#user.id")
    public List<Booking> getBookingsByUser(User user) {
        return bookingRepository.findByUser(user);
    }

    @CacheEvict(value = "bookings", allEntries = true)
    @Transactional
    public Booking createBooking(User user, Booking booking) {

        if (booking.getTotalPrice() > user.getBalance()) {
            throw new DomainException("Insufficient balance for booking.");
        }

        user.setBalance(user.getBalance() - booking.getTotalPrice());
        userRepository.save(user);

        booking.setUser(user);
        booking.setStatus(BookingStatus.PENDING);
        booking.setScreeningTime(LocalDateTime.now().plusDays(1));

        Booking savedBooking = bookingRepository.save(booking);
        log.info("New booking created with ID [{}] for user [{}]", savedBooking.getId(), user.getUsername());
        return savedBooking;
    }

    @CacheEvict(value = "bookings", allEntries = true)
    @Transactional
    public Booking confirmBooking(UUID bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new DomainException("Booking with ID [%s] not found".formatted(bookingId)));

        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new DomainException("Only PENDING bookings can be confirmed.");
        }

        booking.setStatus(BookingStatus.CONFIRMED);
        Booking updatedBooking = bookingRepository.save(booking);
        log.info("Booking [{}] confirmed.", updatedBooking.getId());
        return updatedBooking;
    }

    @CacheEvict(value = "bookings", allEntries = true)
    @Transactional
    public Booking cancelBooking(UUID bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new DomainException("Booking with ID [%s] not found".formatted(bookingId)));

        if (booking.getStatus() == BookingStatus.CONFIRMED) {
            throw new DomainException("Confirmed bookings cannot be canceled.");
        }

        booking.setStatus(BookingStatus.CANCELLED);
        Booking updatedBooking = bookingRepository.save(booking);
        log.info("Booking [{}] canceled.", updatedBooking.getId());
        return updatedBooking;
    }

    public Booking getBookingById(UUID bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new DomainException("Booking with ID [%s] not found".formatted(bookingId)));
    }

    public Movie getMovieById(UUID movieId) {
        return movieService.getMovieById(movieId);
    }

    public Cinema getCinemaById(UUID cinemaId) {
        return cinemaService.getCinemaById(cinemaId);
    }

    public Ticket getTicketById(UUID ticketId) {
        return ticketService.getTicketById(ticketId);
    }
}
