package pl.sportaihub.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import pl.sportaihub.enums.TaskStatus;

public record TaskUpdateRequest(

        @NotBlank(message = "Task title is required")
        @Size(max = 200)
        String title,

        @Size(max = 2000)
        String description,

        @NotNull(message = "Task status is required")
        TaskStatus status,

        @NotNull(message = "Project id is required")
        Long projectId,

        Long assignedMemberId
) {
}