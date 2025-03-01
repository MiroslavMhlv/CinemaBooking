package com.softuniproject.app.web.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TicketRequest {

    @NotNull
    private UUID movieId;

    @NotNull
    private UUID cinemaId;

    @Positive
    private double price;
}
