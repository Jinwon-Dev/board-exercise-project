package com.mailplug.homework.domain.post.persistence;

import autoparams.AutoSource;
import com.mailplug.homework.domain.board.persistence.Board;
import com.mailplug.homework.domain.member.persistence.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;

import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

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

    @Nested
    @DisplayName("게시글 수정")
    class UpdatePost {

        private final Member member = mock();

        @ParameterizedTest
        @AutoSource
        @DisplayName("게시글 수정시 정상적으로 수정되어 반영된다.")
        void update_post(final Board board) {

            // given
            final Post post = new Post(
                    "title",
                    "content",
                    member,
                    board
            );

            given(member.getId()).willReturn(1L);

            // when
            post.update(member.getId(), "test", "test");

            // then
            assertSoftly(softly -> {
                softly.assertThat(post.getTitle()).isEqualTo("test");
                softly.assertThat(post.getContent()).isEqualTo("test");
            });
        }
    }
}