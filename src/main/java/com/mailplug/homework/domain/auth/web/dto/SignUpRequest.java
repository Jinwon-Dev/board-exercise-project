package com.mailplug.homework.domain.auth.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record SignUpRequest(
        @NotBlank(message = "이메일을 입력해주세요.") @Email String email,
        @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=\\S+$).{4,20}",
                message = "비밀번호는 영문과 숫자가 포함된 4자 ~ 20자의 비밀번호여야 합니다.")
        @NotBlank(message = "비밀번호를 입력하세요.") String password
) {
}
