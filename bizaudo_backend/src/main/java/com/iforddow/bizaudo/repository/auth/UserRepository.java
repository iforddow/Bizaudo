package com.iforddow.bizaudo.repository.auth;

import com.iforddow.bizaudo.jpa.entity.user.User;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    @NonNull
    Optional<User> findById(@NonNull UUID id);

    Optional<User> findByEmail(String email);
}
