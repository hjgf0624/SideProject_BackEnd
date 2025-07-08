package com.github.hjgf0624.sideproject.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPwdResetDTO {
    private String mail;
    private String newPwd;
    private String confirmPwd;
    private String verificationCode;
}
