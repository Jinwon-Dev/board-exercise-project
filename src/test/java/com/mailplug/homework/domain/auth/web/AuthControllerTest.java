package com.mailplug.homework.domain.auth.web;

import autoparams.AutoSource;
import autoparams.customization.Customization;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mailplug.homework.domain.auth.application.AuthService;
import com.mailplug.homework.domain.auth.web.dto.LoginRequest;
import com.mailplug.homework.domain.auth.web.dto.LoginResponse;
import com.mailplug.homework.domain.auth.web.dto.SignUpRequest;
import com.mailplug.homework.domain.auth.web.dto.SignUpResponse;
import com.mailplug.homework.domain.customization.MemberCustomization;
import com.mailplug.homework.domain.member.persistence.Member;
import com.mailplug.homework.global.security.JwtTokenProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @Spy
    private ObjectMapper objectMapper;

    @Nested
    @DisplayName("회원가입")
    class SignUp {

        @Test
        @DisplayName("입력한 정보가 유효성 검사에 통과하여 회원가입에 성공한다.")
        void signUp() throws Exception {

            // given
            final var request = new SignUpRequest(
                    "email@gmail.com",
                    "test1234"
            );

            final var response = new SignUpResponse(1L, request.email());
            given(authService.signUp(request)).willReturn(response);

            // when
            final var perform = mockMvc.perform(post("/auth/signup")
                    .contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)));

            // then
            perform.andExpect(status().isCreated());
        }

        @Test
        @DisplayName("입력한 정보가 유효성 검사에 적합하지 않아 회원가입에 실패한다.")
        void signup_fail_invalid_info() throws Exception {

            // given
            final var request = new SignUpRequest(
                    "email",
                    "test1234"
            );

            // when
            final var perform = mockMvc.perform(post("/auth/signup")
                    .contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)));

            // then
            perform.andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("로그인")
    class Login {

        @ParameterizedTest
        @AutoSource
        @Customization(MemberCustomization.class)
        @DisplayName("이메일과 비밀번호가 일치하여 로그인에 성공한다.")
        void login(final Member member) throws Exception {

            // given
            final var request = new LoginRequest(
                    member.getEmail(),
                    member.getPassword()
            );

            final String accessToken = jwtTokenProvider.createAccessToken(1L);

            final var response = new LoginResponse(
                    member.getEmail(),
                    accessToken,
                    "Bearer"
            );
            given(authService.login(request)).willReturn(response);

            // when
            final var perform = mockMvc.perform(post("/auth/login")
                    .contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)));

            // then
            perform.andExpect(status().isOk());
        }

        @Test
        @DisplayName("입력한 정보가 유효성 검사에 적합하지 않아 로그인에 실패한다.")
        void login_fail_invalid_info() throws Exception {

            // given
            final var request = new LoginRequest(
                    "email",
                    "test1234"
            );

            // when
            final var perform = mockMvc.perform(post("/auth/login")
                    .contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)));

            // then
            perform.andExpect(status().isBadRequest());
        }
    }
}