package com.mailplug.homework.domain.auth.application;

import autoparams.AutoSource;
import com.mailplug.homework.domain.auth.web.dto.LoginRequest;
import com.mailplug.homework.domain.auth.web.dto.LoginResponse;
import com.mailplug.homework.domain.auth.web.dto.SignUpRequest;
import com.mailplug.homework.domain.auth.web.dto.SignUpResponse;
import com.mailplug.homework.domain.member.application.MemberMapper;
import com.mailplug.homework.domain.member.persistence.Member;
import com.mailplug.homework.domain.member.persistence.MemberRepository;
import com.mailplug.homework.global.exception.auth.LoginFailException;
import com.mailplug.homework.global.exception.member.EmailOverlapException;
import com.mailplug.homework.global.security.EncryptHelper;
import com.mailplug.homework.global.security.JwtTokenProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private EncryptHelper encryptHelper;

    @Mock
    private MemberMapper memberMapper;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Nested
    @DisplayName("회원가입")
    class SignUp {


        @ParameterizedTest
        @AutoSource
        @DisplayName("회원가입이 정상적으로 이루어져 회원이 저장된다.")
        void signup(final SignUpRequest request) {

            // given
            given(encryptHelper.encrypt(any(), any())).willReturn(request.password());

            final var mockMember = new Member(
                    request.email(),
                    encryptHelper.encrypt(request.password(), "test")
            );
            given(memberMapper.createMemberFromRequest(request)).willReturn(mockMember);

            final var member = memberMapper.createMemberFromRequest(request);
            given(memberRepository.existsByEmail(request.email())).willReturn(false);

            final var mockResponse = new SignUpResponse(1L, member.getEmail());
            given(memberMapper.entityToMemberSignUpResponse(any())).willReturn(mockResponse);

            // when
            final var response = authService.signUp(request);

            // then
            assertSoftly(softly -> softly.assertThat(response.email()).isEqualTo(member.getEmail()));
        }

        @ParameterizedTest
        @AutoSource
        @DisplayName("이메일이 중복되어 회원가입에 실패한다.")
        void signup_fail_email_overlap(final SignUpRequest request) {

            // given
            given(encryptHelper.encrypt(any(), any())).willReturn(request.password());

            final var mockMember = new Member(
                    request.email(),
                    encryptHelper.encrypt(request.password(), "test")
            );

            given(memberMapper.createMemberFromRequest(request)).willReturn(mockMember);
            memberMapper.createMemberFromRequest(request);

            given(memberRepository.existsByEmail(request.email())).willReturn(true);

            // when

            // then
            assertSoftly(softly -> softly.assertThatThrownBy(() -> authService.signUp(request))
                    .isInstanceOf(EmailOverlapException.class));
        }
    }

    @Nested
    @DisplayName("로그인")
    class Login {

        @ParameterizedTest
        @AutoSource
        @DisplayName("이메일과 비밀번호가 일치하여 로그인에 성공한다.")
        void login(final Member member) {

            // given
            final var request = new LoginRequest(member.getEmail(), member.getPassword());

            given(memberRepository.findMemberByEmail(request.email())).willReturn(Optional.of(member));
            given(encryptHelper.isMatch(any(), any())).willReturn(true);
            given(jwtTokenProvider.createAccessToken(any())).willReturn("accessToken");

            final var mockResponse = new LoginResponse(
                    member.getEmail(),
                    "accessToken",
                    "bearer"
            );
            given(memberMapper.mapToLoginResponse(member, "accessToken")).willReturn(mockResponse);

            // when
            final var response = authService.login(request);

            // then
            assertSoftly(softly -> softly.assertThat(response.accessToken()).isEqualTo("accessToken"));
        }

        @ParameterizedTest
        @AutoSource
        @DisplayName("DB에 이메일이 존재하지 않으면 로그인에 실패한다.")
        void login_fail_not_exist_email(final LoginRequest request) {

            // given
            given(memberRepository.findMemberByEmail(request.email())).willThrow(LoginFailException.class);

            // when

            // then
            assertSoftly(softly -> softly.assertThatThrownBy(() -> authService.login(request))
                    .isInstanceOf(LoginFailException.class));
        }

        @ParameterizedTest
        @AutoSource
        @DisplayName("DB에 저장된 비밀번호와 일치하지 않아 로그인에 실패한다.")
        void login_fail_invalid_password(final Member member) {

            // given
            final var request = new LoginRequest(
                    member.getEmail(),
                    member.getPassword()
            );

            given(memberRepository.findMemberByEmail(request.email())).willReturn(Optional.of(member));
            given(encryptHelper.isMatch(any(), any())).willReturn(false);

            // when

            // then
            assertSoftly(softly -> softly.assertThatThrownBy(() -> authService.login(request))
                    .isInstanceOf(LoginFailException.class));
        }
    }
}