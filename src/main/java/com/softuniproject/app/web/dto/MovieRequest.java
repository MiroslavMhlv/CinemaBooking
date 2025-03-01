package com.softuniproject.app.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MovieRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String genre;

    @Positive
    private int duration;
}
