package com.softuniproject.app.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginRequest {

    @NotBlank
    private String username;

    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;
}
