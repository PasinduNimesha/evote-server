package com.evote.server.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "Identity card number is required")
    @Size(max = 20)
    private String identityCardNumber;

    @NotBlank(message = "Name is required")
    @Size(max = 100)
    private String name;

    @NotBlank(message = "Mobile number is required")
    @Size(max = 20)
    private String mobileNumber;

    @NotBlank(message = "Residence ID is required")
    @Size(max = 100)
    private String residenceId;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Size(max = 100)
    private String email;

    @NotBlank(message = "Date of birth is required")
    private String dateOfBirth;
}
