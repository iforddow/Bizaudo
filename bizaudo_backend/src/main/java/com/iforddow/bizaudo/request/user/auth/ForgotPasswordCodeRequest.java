package com.iforddow.bizaudo.request.user.auth;

import lombok.Data;

import java.util.UUID;

@Data
public class ForgotPasswordCodeRequest {

    String email;
    String code;

}
