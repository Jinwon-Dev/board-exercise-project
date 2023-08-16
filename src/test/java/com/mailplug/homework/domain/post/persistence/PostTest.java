package com.mailplug.homework.domain.post.persistence;

import autoparams.AutoSource;
import com.mailplug.homework.domain.board.persistence.Board;
import com.mailplug.homework.domain.member.persistence.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

class PostTest {

    @Nested
    @DisplayName("게시글 조회수 증가")
    class IncreaseView {

        @ParameterizedTest
        @AutoSource
        @DisplayName("게시글 조회시 조회수가 1 증가한다.")
        void increase_view(final Member member, final Board board) {

            // given
            final Post post = new Post(
                    "title",
                    "content",
                    member,
                    board
            );

            // when
            post.increaseView();

            // then
            assertSoftly(softly -> softly.assertThat(post.getViews()).isEqualTo(1));
        }

    }
}