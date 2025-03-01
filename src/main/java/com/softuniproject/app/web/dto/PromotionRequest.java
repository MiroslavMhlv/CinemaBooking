package com.softuniproject.app.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PromotionRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @NotNull
    private double discount;

}
