package com.iforddow.bizaudo.request.user.auth;

import lombok.Data;

@Data
public class RegisterRequest {

    private String email;
    private String password;
    private String confirmPassword;

}
