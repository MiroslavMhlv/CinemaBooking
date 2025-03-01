package com.softuniproject.app.web.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookingRequest {

    @NotNull
    private UUID movieId;

    @NotNull
    private UUID cinemaId;

    @NotNull
    private UUID ticketId;

    @Positive
    private double totalPrice;
}
