package com.softuniproject.app.cinema.entity;

import com.softuniproject.app.booking.entity.Booking;
import com.softuniproject.app.promotion.entity.Promotion;
import com.softuniproject.app.ticket.entity.Ticket;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;


@Builder
@Entity
@Table(name = "cinemas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Cinema {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String location;

    @OneToMany(mappedBy = "cinema", fetch = FetchType.EAGER)
    private List<Ticket> tickets;

    @OneToMany(mappedBy = "cinema", fetch = FetchType.EAGER)
    private List<Booking> bookings;

    @OneToMany(mappedBy = "cinema", fetch = FetchType.EAGER)
    private List<Promotion> promotions;
}