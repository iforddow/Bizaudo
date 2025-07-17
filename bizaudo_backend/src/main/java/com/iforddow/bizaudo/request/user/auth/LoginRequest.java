package com.iforddow.bizaudo.request.user.auth;

import lombok.Data;

@Data
public class LoginRequest {

    private String email;
    private String password;

}
