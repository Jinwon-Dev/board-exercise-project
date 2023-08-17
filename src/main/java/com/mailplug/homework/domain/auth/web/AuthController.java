package com.mailplug.homework.domain.auth.web;

import com.mailplug.homework.domain.auth.application.AuthService;
import com.mailplug.homework.domain.auth.web.dto.LoginRequest;
import com.mailplug.homework.domain.auth.web.dto.LoginResponse;
import com.mailplug.homework.domain.auth.web.dto.SignUpRequest;
import com.mailplug.homework.domain.auth.web.dto.SignUpResponse;
import com.mailplug.homework.global.response.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;

@Tag(name = "인증", description = "인증 관련 API")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "회원가입", description = "회원가입 API - 비회원 가능")
    @PostMapping("/signup")
    public ResponseEntity<CommonResponse> signUp(@RequestBody @Valid SignUpRequest request) {

        final SignUpResponse dataResponse = authService.signUp(request);
        return ResponseEntity.status(CREATED).body(CommonResponse.newInstance(dataResponse));
    }

    @Operation(summary = "로그인", description = "로그인 API - 비회원 불가")
    @PostMapping("/login")
    public ResponseEntity<CommonResponse> login(@RequestBody @Valid final LoginRequest request) {

        final LoginResponse dataResponse = authService.login(request);
        return ResponseEntity.ok(CommonResponse.newInstance(dataResponse));
    }
}
