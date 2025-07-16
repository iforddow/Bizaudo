package com.iforddow.bizaudo.dto.user;

import com.iforddow.bizaudo.jpa.entity.Role;
import com.iforddow.bizaudo.jpa.entity.User;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public record UserPublicDTO(UUID id, String email, boolean enabled, boolean emailVerified,
                            Instant lastActive, Set<String> roles) {

    public UserPublicDTO(User user) {
        this(
                user.getId(),
                user.getEmail(),
                user.getEnabled(),
                user.getEmailVerified(),
                user.getLastActive(),
                extractRoles(user)
        );
    }

    private static Set<String> extractRoles(User user) {
        Set<String> roles = new HashSet<>();
        for (Role role : user.getRoles()) {
            roles.add(role.getCodeName());
        }
        return roles;
    }

}
