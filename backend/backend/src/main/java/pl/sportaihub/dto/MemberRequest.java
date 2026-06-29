package pl.sportaihub.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import pl.sportaihub.enums.MemberSpecialization;

public record MemberRequest(

        @NotBlank(message = "First name is required")
        @Size(max = 100)
        String firstName,

        @NotBlank(message = "Last name is required")
        @Size(max = 100)
        String lastName,

        @NotBlank(message = "Email is required")
        @Email(message = "Email has invalid format")
        @Size(max = 200)
        String email,

        @NotNull(message = "Specialization is required")
        MemberSpecialization specialization,

        boolean active
) {
}