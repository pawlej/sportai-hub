package pl.sportaihub.dto;

import pl.sportaihub.enums.TaskStatus;

import java.time.LocalDateTime;

public record TaskResponse(
        Long id,
        String title,
        String description,
        TaskStatus status,
        Long projectId,
        String projectName,
        Long assignedMemberId,
        String assignedMemberName,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}