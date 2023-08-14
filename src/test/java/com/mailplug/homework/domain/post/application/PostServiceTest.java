package com.mailplug.homework.domain.post.application;

import autoparams.AutoSource;
import com.mailplug.homework.domain.board.BoardType;
import com.mailplug.homework.domain.board.persistence.Board;
import com.mailplug.homework.domain.board.persistence.BoardRepository;
import com.mailplug.homework.domain.member.persistence.Member;
import com.mailplug.homework.domain.member.persistence.MemberRepository;
import com.mailplug.homework.domain.post.persistence.PostRepository;
import com.mailplug.homework.domain.post.web.dto.WritePostRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @InjectMocks
    private PostService postService;

    @Spy
    private PostMapper postMapper;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private BoardRepository boardRepository;

    @Nested
    @DisplayName("게시글 작성")
    class WritePost {

        @ParameterizedTest
        @AutoSource
        @DisplayName("요청시 정상적으로 저장되어야 한다.")
        void write_post(final Member member) {

            // given
            final Board board = new Board(BoardType.FREE);
            boardRepository.save(board);

            final var request = new WritePostRequest(
                    "title",
                    "content",
                    BoardType.FREE
            );

            final var post = postMapper.writePostRequestToEntity(request, member, board);

            given(postRepository.save(any())).willReturn(post);
            given(memberRepository.findById(any())).willReturn(Optional.of(member));
            given(boardRepository.findBoardByName(any())).willReturn(Optional.of(board));

            // when
            final var response = postService.writePost(request, 1L);

            // then
            assertSoftly(softly -> {
                softly.assertThat(response.content()).isEqualTo(request.content());
                softly.assertThat(response.title()).isEqualTo(request.title());
            });
        }

        @Test
        @DisplayName("요청시 존재하지 않는 게시판이라면 실패한다.")
        void write_post_not_exist_board() {

            // given
            final Board board = new Board(BoardType.FREE);
            boardRepository.save(board);

            // when

            // then
            assertSoftly(softly -> softly.assertThatThrownBy(() -> new WritePostRequest(
                            "title", "content", BoardType.valueOf("    ")))
                    .isInstanceOf(IllegalArgumentException.class));
        }
    }
}