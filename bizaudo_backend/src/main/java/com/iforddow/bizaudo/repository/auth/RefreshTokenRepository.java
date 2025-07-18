package com.iforddow.bizaudo.repository.auth;

import com.iforddow.bizaudo.jpa.entity.rbac.RefreshToken;
import com.iforddow.bizaudo.jpa.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {
    Optional<RefreshToken> findByTokenHash(String tokenHash);

    List<RefreshToken> findAllByUser(User user);
}
