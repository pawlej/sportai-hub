package pl.sportaihub.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TaskRequest(

        @NotBlank(message = "Task title is required")
        @Size(max = 200)
        String title,

        @Size(max = 2000)
        String description,

        @NotNull(message = "Project id is required")
        Long projectId,

        Long assignedMemberId
) {
}