package com.mailplug.homework.domain.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mailplug.homework.domain.IntegrationTest;
import com.mailplug.homework.domain.auth.web.dto.LoginRequest;
import com.mailplug.homework.domain.auth.web.dto.SignUpRequest;
import com.mailplug.homework.domain.member.application.MemberMapper;
import com.mailplug.homework.domain.member.persistence.Member;
import com.mailplug.homework.domain.member.persistence.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthIntegrationTest extends IntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private MemberRepository memberRepository;

    @Nested
    @DisplayName("회원가입")
    class SignUp {

        @Test
        @DisplayName("입력 정보가 적합하여 회원 가입에 성공하여 DB에 저장된다.")
        void signup() throws Exception {

            // given
            final var request = new SignUpRequest(
                    "email@gmail.com",
                    "test1234"
            );

            // when
            final var perform = mockMvc.perform(post("/auth/signup")
                    .contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)));

            // then
            perform.andExpect(status().isCreated())
                    .andExpect(jsonPath("$.data.email").value(request.email()));
        }

        @Nested
        @DisplayName("로그인")
        class Login {

            @Test
            @DisplayName("이메일과 비밀번호가 DB에 존재하고, 일치하여 로그인에 성공한다.")
            void login() throws Exception {

                // given
                final var request = new SignUpRequest(
                        "email123@gmail.com",
                        "test1234"
                );

                final Member member = memberMapper.createMemberFromRequest(request);
                memberRepository.save(member);

                final var loginRequest = new LoginRequest(request.email(), request.password());

                // when
                final var perform = mockMvc.perform(post("/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)));

                // then
                perform.andExpect(status().isOk())
                        .andExpect(jsonPath("$.data.accessToken").isNotEmpty());
            }
        }
    }
}
