package com.softuniproject.app.web.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CinemaRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String location;
}
