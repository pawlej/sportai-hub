package pl.sportaihub.dto;

import pl.sportaihub.enums.MemberSpecialization;

import java.time.LocalDateTime;

public record MemberResponse(
        Long id,
        String firstName,
        String lastName,
        String email,
        MemberSpecialization specialization,
        boolean active,
        LocalDateTime createdAt
) {
}