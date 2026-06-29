package pl.sportaihub.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import pl.sportaihub.enums.ProjectStatus;

public record ProjectRequest(

        @NotBlank(message = "Project name is required")
        @Size(max = 150)
        String name,

        @Size(max = 2000)
        String description,

        @NotNull(message = "Project status is required")
        ProjectStatus status,

        Long leaderId
) {
}