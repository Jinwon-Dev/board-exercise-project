package com.mailplug.homework.domain.auth.web;

import com.mailplug.homework.domain.auth.application.AuthService;
import com.mailplug.homework.domain.auth.web.dto.LoginRequest;
import com.mailplug.homework.domain.auth.web.dto.LoginResponse;
import com.mailplug.homework.domain.auth.web.dto.SignUpRequest;
import com.mailplug.homework.domain.auth.web.dto.SignUpResponse;
import com.mailplug.homework.global.response.CommonResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<CommonResponse> signUp(@RequestBody @Valid SignUpRequest request) {

        final SignUpResponse dataResponse = authService.signUp(request);
        return ResponseEntity.status(CREATED).body(CommonResponse.newInstance(dataResponse));
    }

    @PostMapping("/login")
    public ResponseEntity<CommonResponse> login(@RequestBody @Valid final LoginRequest request) {

        final LoginResponse dataResponse = authService.login(request);
        return ResponseEntity.ok(CommonResponse.newInstance(dataResponse));
    }
}
