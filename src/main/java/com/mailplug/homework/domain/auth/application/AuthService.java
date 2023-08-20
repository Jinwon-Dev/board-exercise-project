package com.mailplug.homework.domain.auth.application;

import com.mailplug.homework.domain.auth.web.dto.LoginRequest;
import com.mailplug.homework.domain.auth.web.dto.LoginResponse;
import com.mailplug.homework.domain.auth.web.dto.SignUpRequest;
import com.mailplug.homework.domain.auth.web.dto.SignUpResponse;
import com.mailplug.homework.domain.member.application.MemberMapper;
import com.mailplug.homework.domain.member.persistence.Member;
import com.mailplug.homework.domain.member.persistence.MemberRepository;
import com.mailplug.homework.global.exception.auth.AuthExceptionExecutor;
import com.mailplug.homework.global.exception.member.MemberExceptionExecutor;
import com.mailplug.homework.global.security.EncryptHelper;
import com.mailplug.homework.global.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final EncryptHelper encryptHelper;
    private final MemberMapper memberMapper;

    @Transactional
    public SignUpResponse signUp(final SignUpRequest request) {

        if (memberRepository.existsByEmail(request.email())) {
            throw MemberExceptionExecutor.EmailOverlap();
        }

        final Member member = memberMapper.signUpRequestToEntity(request);
        memberRepository.save(member);

        return memberMapper.entityToSignUpResponse(member);
    }

    public LoginResponse login(final LoginRequest request) {

        final Member member = memberRepository.findMemberByEmail(request.email())
                .orElseThrow(AuthExceptionExecutor::LoginFail);

        if (!encryptHelper.isMatch(request.password(), member.getPassword())) {
            throw AuthExceptionExecutor.LoginFail();
        }

        final String accessToken = jwtTokenProvider.createAccessToken(member.getId());
        return memberMapper.entityToLoginResponse(member, accessToken);
    }
}
