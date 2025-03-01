package com.softuniproject.app.booking.entity;

import com.softuniproject.app.cinema.entity.Cinema;
import com.softuniproject.app.movie.entity.Movie;
import com.softuniproject.app.ticket.entity.Ticket;
import com.softuniproject.app.user.entity.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    @ManyToOne
    @JoinColumn(name = "cinema_id", nullable = false)
    private Cinema cinema;

    @ManyToOne
    @JoinColumn(name = "ticket_id", nullable = false)
    private Ticket ticket;

    @Column(nullable = false)
    private LocalDateTime screeningTime;

    @Column(nullable = false)
    private double totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status;

    public Booking(UUID id, User user, Movie movie, Cinema cinema, Ticket ticket, LocalDateTime screeningTime, double totalPrice, BookingStatus status) {
        this.id = id;
        this.user = user;
        this.movie = movie;
        this.cinema = cinema;
        this.ticket = ticket;
        this.screeningTime = screeningTime;
        this.totalPrice = totalPrice;
        this.status = status;
    }

    public Booking() {
    }

    public static BookingBuilder builder() {
        return new BookingBuilder();
    }

    public UUID getId() {
        return this.id;
    }

    public User getUser() {
        return this.user;
    }

    public Movie getMovie() {
        return this.movie;
    }

    public Cinema getCinema() {
        return this.cinema;
    }

    public Ticket getTicket() {
        return this.ticket;
    }

    public LocalDateTime getScreeningTime() {
        return this.screeningTime;
    }

    public double getTotalPrice() {
        return this.totalPrice;
    }

    public BookingStatus getStatus() {
        return this.status;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public void setCinema(Cinema cinema) {
        this.cinema = cinema;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public void setScreeningTime(LocalDateTime screeningTime) {
        this.screeningTime = screeningTime;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public static class BookingBuilder {
        private UUID id;
        private User user;
        private Movie movie;
        private Cinema cinema;
        private Ticket ticket;
        private LocalDateTime screeningTime;
        private double totalPrice;
        private BookingStatus status;

        BookingBuilder() {
        }

        public BookingBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public BookingBuilder user(User user) {
            this.user = user;
            return this;
        }

        public BookingBuilder movie(Movie movie) {
            this.movie = movie;
            return this;
        }

        public BookingBuilder cinema(Cinema cinema) {
            this.cinema = cinema;
            return this;
        }

        public BookingBuilder ticket(Ticket ticket) {
            this.ticket = ticket;
            return this;
        }

        public BookingBuilder screeningTime(LocalDateTime screeningTime) {
            this.screeningTime = screeningTime;
            return this;
        }

        public BookingBuilder totalPrice(double totalPrice) {
            this.totalPrice = totalPrice;
            return this;
        }

        public BookingBuilder status(BookingStatus status) {
            this.status = status;
            return this;
        }

        public Booking build() {
            return new Booking(this.id, this.user, this.movie, this.cinema, this.ticket, this.screeningTime, this.totalPrice, this.status);
        }

        public String toString() {
            return "Booking.BookingBuilder(id=" + this.id + ", user=" + this.user + ", movie=" + this.movie + ", cinema=" + this.cinema + ", ticket=" + this.ticket + ", screeningTime=" + this.screeningTime + ", totalPrice=" + this.totalPrice + ", status=" + this.status + ")";
        }
    }
}
