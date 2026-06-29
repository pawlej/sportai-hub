package pl.sportaihub.dto;

import pl.sportaihub.enums.ProjectStatus;

import java.time.LocalDateTime;

public record ProjectResponse(
        Long id,
        String name,
        String description,
        ProjectStatus status,
        Long leaderId,
        String leaderName,
        LocalDateTime createdAt
) {
}