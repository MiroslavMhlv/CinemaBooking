package com.softuniproject.app.user.entity;

import com.softuniproject.app.booking.entity.Booking;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Builder
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(nullable = false)
    private double balance = 100.0;

    private String firstName;

    private String lastName;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<Booking> bookings;
}