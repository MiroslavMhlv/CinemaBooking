package com.softuniproject.app.promotion.entity;

import com.softuniproject.app.cinema.entity.Cinema;
import jakarta.persistence.*;
import lombok.*;

import java.time.DayOfWeek;
import java.util.UUID;

@Builder
@Entity
@Table(name = "promotions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Promotion {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DayOfWeek dayOfWeek;

    @Column(nullable = false)
    private double discount;

    private String description;

    @ManyToOne
    @JoinColumn(name = "cinema_id", nullable = false)
    private Cinema cinema;
}
