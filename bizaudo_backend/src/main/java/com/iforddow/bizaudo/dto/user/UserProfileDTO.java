package com.iforddow.bizaudo.dto.user;

import com.iforddow.bizaudo.jpa.entity.user.UserProfile;

import java.time.Instant;
import java.util.UUID;

public record UserProfileDTO(UUID id, String firstName, String lastName,
                             Instant createdAt, Instant lastUpdatedAt) {

    // Constructor without id
    public UserProfileDTO(String firstName, String lastName, Instant createdAt, Instant lastUpdatedAt) {
        this(null, firstName, lastName, createdAt, lastUpdatedAt);
    }

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
