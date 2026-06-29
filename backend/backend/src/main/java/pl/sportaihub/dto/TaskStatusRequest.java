package pl.sportaihub.dto;

import jakarta.validation.constraints.NotNull;
import pl.sportaihub.enums.TaskStatus;

public record TaskStatusRequest(

        @NotNull(message = "Task status is required")
        TaskStatus status
) {
}