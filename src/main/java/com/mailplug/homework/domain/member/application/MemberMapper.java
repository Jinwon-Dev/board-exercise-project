package com.mailplug.homework.domain.member.application;

import com.mailplug.homework.domain.auth.web.dto.LoginResponse;
import com.mailplug.homework.domain.auth.web.dto.SignUpRequest;
import com.mailplug.homework.domain.auth.web.dto.SignUpResponse;
import com.mailplug.homework.domain.member.persistence.Member;
import com.mailplug.homework.global.security.EncryptHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberMapper {

    private final EncryptHelper encryptHelper;

    @Value("${bcrypt.secret.salt}")
    private String salt;

    public Member createMemberFromRequest(final SignUpRequest request) {

        return new Member(
                request.email(),
                encryptHelper.encrypt(request.password(), salt)
        );
    }

    public SignUpResponse entityToMemberSignUpResponse(final Member member) {

        return new SignUpResponse(member.getId(), member.getEmail());
    }

    public LoginResponse mapToLoginResponse(final Member member, final String accessToken) {

        final String tokenType = "bearer";
        final String email = member.getEmail();

        return new LoginResponse(email, accessToken, tokenType);
    }
}
