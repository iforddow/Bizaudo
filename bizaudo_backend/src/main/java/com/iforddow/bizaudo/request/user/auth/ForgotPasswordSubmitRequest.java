package com.iforddow.bizaudo.request.user.auth;

import lombok.Data;

import java.util.UUID;

@Data
public class ForgotPasswordSubmitRequest {

    String email;
    String token;
    String newPassword;
    String confirmNewPassword;

}
