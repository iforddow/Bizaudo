package com.iforddow.bizaudo.service.user;

import com.iforddow.bizaudo.dto.user.UserDTO;
import com.iforddow.bizaudo.exception.ResourceNotFoundException;
import com.iforddow.bizaudo.jpa.entity.user.User;
import com.iforddow.bizaudo.repository.auth.UserRepository;
import com.iforddow.bizaudo.request.user.auth.ChangePasswordRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

/**
* A class for user service REST methods.
*
* @author IFD
* @since 2024-07-17
* */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /**
    * A method to delete a user by their id.
    *
    * @author IFD
    * @since 2025-07-17
    * */
    @CacheEvict(value = "userCache", key = "'user' + #id")
    @Transactional
    public ResponseEntity<Map<String, Object>> delete(UUID id) {

        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        userRepository.delete(user);

        return ResponseEntity.ok(Map.of("result", "User successfully deleted"));

    }

    /**
    * A method to get a user by their id.
    *
    * @author IFD
    * @since 2025-07-17
    * */
    @Cacheable(value = "userCache", key = "'user:' + #id", unless = "#result == null")
    public UserDTO getUser(UUID id) {

        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return new UserDTO(user, true);

    }

}