package com.softuniproject.app.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserEditRequest {

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @Email(message = "Invalid email format")
    @NotBlank
    private String email;
}
