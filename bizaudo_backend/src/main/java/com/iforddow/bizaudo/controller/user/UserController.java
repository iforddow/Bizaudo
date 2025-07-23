package com.iforddow.bizaudo.controller.user;

import com.iforddow.bizaudo.dto.user.UserDTO;
import com.iforddow.bizaudo.exception.ResourceNotFoundException;
import com.iforddow.bizaudo.request.user.auth.ChangePasswordRequest;
import com.iforddow.bizaudo.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserDTO> getUser(@PathVariable UUID id) {

        UserDTO user = userService.getUser(id);

        if(user == null){
            throw new ResourceNotFoundException("User not found");
        }

        return ResponseEntity.ok().body(user);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable UUID id) {
        return userService.delete(id);
    }

}
