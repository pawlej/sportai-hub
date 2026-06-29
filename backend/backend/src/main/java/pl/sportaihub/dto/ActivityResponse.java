package pl.sportaihub.dto;

import pl.sportaihub.enums.ActivityType;

import java.time.LocalDateTime;

public record ActivityResponse(
        Long id,
        ActivityType type,
        String message,
        LocalDateTime createdAt
) {
}