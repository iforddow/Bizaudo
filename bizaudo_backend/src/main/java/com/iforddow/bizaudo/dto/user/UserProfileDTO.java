package com.iforddow.bizaudo.dto.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.iforddow.bizaudo.jpa.entity.user.UserProfile;

import java.time.Instant;
import java.util.UUID;

public record UserProfileDTO(
        @JsonInclude(JsonInclude.Include.NON_NULL)
        UUID id,
        String firstName,
        String lastName,
        Instant createdAt,
        Instant lastUpdatedAt
) {
    public UserProfileDTO(UserProfile userProfile, boolean includeId) {
        this(
                includeId ? userProfile.getId() : null,
                userProfile.getFirstName(),
                userProfile.getLastName(),
                userProfile.getCreatedAt(),
                userProfile.getLastUpdated()
        );
    }
}