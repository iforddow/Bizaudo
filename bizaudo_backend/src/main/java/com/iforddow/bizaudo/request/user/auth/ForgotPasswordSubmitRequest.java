package com.iforddow.bizaudo.request.user.auth;

import lombok.Data;
import java.util.UUID;

@Data
public class ForgotPasswordSubmitRequest {

    UUID token;
    String newPassword;
    String confirmNewPassword;

}
