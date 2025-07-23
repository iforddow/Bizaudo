package com.iforddow.bizaudo.request.user.auth;

import lombok.Data;

import java.util.UUID;

@Data
public class ChangePasswordRequest {

    private UUID id;
    private String oldPassword;
    private String newPassword;
    private String confirmNewPassword;

}
