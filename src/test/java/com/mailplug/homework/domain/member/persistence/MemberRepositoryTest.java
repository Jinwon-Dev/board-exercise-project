package com.mailplug.homework.domain.member.persistence;

import autoparams.AutoSource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Nested
    @DisplayName("회원 유무 확인")
    class ExistsByEmail {

        @ParameterizedTest
        @AutoSource
        @DisplayName("이메일을 조회하여 회원의 존재 여부를 확인할 수 있다.")
        void exists_by_email(final Member member) {

            // given
            memberRepository.save(member);

            // when
            final var result = memberRepository.existsByEmail(member.getEmail());

            // then
            assertSoftly(softly -> softly.assertThat(result).isTrue());
        }
    }

    @Nested
    @DisplayName("이메일로 회원 조회")
    class FindMemberByEmail {

        @ParameterizedTest
        @AutoSource
        @DisplayName("이메일을 이용하여 회원을 조회할 수 있다.")
        void find_by_email(final Member member) {

            // given
            memberRepository.save(member);

            // when
            final var findMember = memberRepository.findMemberByEmail(member.getEmail());

            // then
            assertSoftly(softly -> {
                softly.assertThat(findMember.get().getEmail()).isEqualTo(member.getEmail());
                softly.assertThat(findMember.get().getId()).isEqualTo(member.getId());
            });
        }

    }
}