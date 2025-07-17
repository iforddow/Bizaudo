package com.iforddow.bizaudo.service.user;

import com.iforddow.bizaudo.dto.user.UserDTO;
import com.iforddow.bizaudo.dto.user.UserWithProfileDTO;
import com.iforddow.bizaudo.exception.ResourceNotFoundException;
import com.iforddow.bizaudo.jpa.entity.user.User;
import com.iforddow.bizaudo.mapper.user.UserMapper;
import com.iforddow.bizaudo.repository.auth.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional
    public ResponseEntity<Map<String, Object>> delete(UUID id) {

        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        userRepository.delete(user);

        return ResponseEntity.ok(Map.of("result", "User successfully deleted"));

    }


    public ResponseEntity<Map<String, Object>> getUser(UUID id) {

        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        UserWithProfileDTO userWithProfileDTO = userMapper.toUserWithProfileDTO(user);

        return ResponseEntity.ok(Map.of("result", userWithProfileDTO));

    }

}
