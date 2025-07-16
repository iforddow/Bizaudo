package com.iforddow.bizaudo.repository;

import com.iforddow.bizaudo.jpa.entity.User;
import com.iforddow.bizaudo.jpa.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserProfileRepository extends JpaRepository<UserProfile, UUID> {

    Optional<UserProfile> findByUser(User user);

}
