package com.iforddow.bizaudo.service.user;

import com.iforddow.bizaudo.dto.user.UserDTO;
import com.iforddow.bizaudo.exception.ResourceNotFoundException;
import com.iforddow.bizaudo.jpa.entity.user.User;
import com.iforddow.bizaudo.repository.auth.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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
    public ResponseEntity<Map<String, Object>> getUser(UUID id) {

        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        UserDTO userDto = new UserDTO(user, true);

        return ResponseEntity.ok(Map.of("result", userDto));

    }

}