package com.softuniproject.app.movie.entity;

import com.softuniproject.app.booking.entity.Booking;
import com.softuniproject.app.ticket.entity.Ticket;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Builder
@Entity
@Table(name = "movies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String genre;

    @Column(nullable = false)
    private int duration;

    @OneToMany(mappedBy = "movie", fetch = FetchType.EAGER)
    private List<Ticket> tickets;

    @OneToMany(mappedBy = "movie", fetch = FetchType.EAGER)
    private List<Booking> bookings;
}