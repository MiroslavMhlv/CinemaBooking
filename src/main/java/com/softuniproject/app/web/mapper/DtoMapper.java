package com.softuniproject.app.web.mapper;

import com.softuniproject.app.booking.entity.Booking;
import com.softuniproject.app.cinema.entity.Cinema;
import com.softuniproject.app.cinema.service.CinemaService;
import com.softuniproject.app.movie.entity.Movie;
import com.softuniproject.app.movie.service.MovieService;
import com.softuniproject.app.promotion.entity.Promotion;
import com.softuniproject.app.ticket.entity.Ticket;
import com.softuniproject.app.user.entity.User;
import com.softuniproject.app.web.dto.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DtoMapper {

    private final ModelMapper modelMapper;
    private final MovieService movieService;
    private final CinemaService cinemaService;

    public static UserEditRequest mapUserToUserEditRequest(User user) {
        return UserEditRequest.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .build();
    }

    public static void updateUserFromEditRequest(User user, UserEditRequest request) {
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
    }

    public static Cinema mapCinemaRequestToCinema(CinemaRequest request) {
        return Cinema.builder()
                .name(request.getName())
                .location(request.getLocation())
                .build();
    }

    public Booking mapBookingRequestToBooking(BookingRequest request, User user, Movie movie, Cinema cinema, Ticket ticket) {
        Booking booking = modelMapper.map(request, Booking.class);
        booking.setUser(user);
        booking.setMovie(movie);
        booking.setCinema(cinema);
        booking.setTicket(ticket);
        return booking;
    }

    public static Movie mapMovieRequestToMovie(MovieRequest request) {
        return Movie.builder()
                .title(request.getTitle())
                .genre(request.getGenre())
                .duration(request.getDuration())
                .build();
    }

    public static Promotion mapPromotionRequestToPromotion(PromotionRequest request) {
        return Promotion.builder()
                .name(request.getName())
                .description(request.getDescription())
                .discount(request.getDiscount())
                .build();
    }

    public Ticket mapTicketRequestToTicket(TicketRequest request) {
        return Ticket.builder()
                .movie(movieService.getMovieById(request.getMovieId()))
                .cinema(cinemaService.getCinemaById(request.getCinemaId()))
                .price(request.getPrice())
                .build();
    }
}
