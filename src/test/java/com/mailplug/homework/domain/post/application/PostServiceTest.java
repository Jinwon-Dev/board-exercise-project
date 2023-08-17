package com.mailplug.homework.domain.post.application;

import autoparams.AutoSource;
import autoparams.customization.Customization;
import com.mailplug.homework.domain.board.BoardType;
import com.mailplug.homework.domain.board.persistence.Board;
import com.mailplug.homework.domain.board.persistence.BoardRepository;
import com.mailplug.homework.domain.customization.PostCustomization;
import com.mailplug.homework.domain.member.persistence.Member;
import com.mailplug.homework.domain.member.persistence.MemberRepository;
import com.mailplug.homework.domain.post.persistence.Post;
import com.mailplug.homework.domain.post.persistence.PostRepository;
import com.mailplug.homework.domain.post.web.dto.UpdatePostRequest;
import com.mailplug.homework.domain.post.web.dto.WritePostRequest;
import com.mailplug.homework.global.exception.post.PostNotFoundException;
import com.mailplug.homework.global.exception.post.WriterNotSameException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.BDDMockito.*;

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

    @Nested
    @DisplayName("게시글 조회")
    class ReadPost {

        @ParameterizedTest
        @AutoSource
        @DisplayName("요청시 단건이 조회되고 조회수가 1 증가한다.")
        void read_post(final Post post) {

            // given
            given(postRepository.findById(any())).willReturn(Optional.of(post));

            // when
            final var response = postService.readPost(post.getId());

            // then
            assertSoftly(softly -> softly.assertThat(response.views()).isEqualTo(1L));
        }

        @Test
        @DisplayName("요청시 존재하지 않는 게시글이라면 실패한다.")
        void read_post_not_exist_post() {

            // given

            // when

            // then
            assertSoftly(softly -> softly.assertThatThrownBy(() -> postService.readPost(1L))
                    .isInstanceOf(PostNotFoundException.class));
        }

        @ParameterizedTest
        @AutoSource
        @Customization(PostCustomization.class)
        @DisplayName("게시판별 게시글 목록 조회시 게시판에 속한 게시글 목록이 페이징되어 조회된다.")
        void read_posts_by_paging(final List<Post> posts) {

            // given
            final var pagedPosts = new PageImpl<>(posts);
            given(postRepository.findAllPostsByBoardName(any(), any())).willReturn(pagedPosts);

            // when
            final var pageable = PageRequest.of(0, 3);
            final var response = postService.readPostList(BoardType.FREE, pageable);

            // then
            assertSoftly(softly -> softly.assertThat(response.getSize()).isEqualTo(3));
        }

        @ParameterizedTest
        @AutoSource
        @Customization(PostCustomization.class)
        @DisplayName("제목으로 게시글을 검색하면 키워드가 제목에 포함된 게시글이 검색된다.")
        void read_posts_by_post_title(final List<Post> posts) {

            // given
            final var pagedPosts = new PageImpl<>(posts);
            given(postRepository.findAllPostsByPostTitle(any(), any())).willReturn(pagedPosts);

            // when
            final var pageable = PageRequest.of(0, 3);
            final var response = postService.readPostListByPostTitle("title", pageable);

            // then
            assertSoftly(softly -> {
                softly.assertThat(response.getSize()).isEqualTo(3);
                softly.assertThat(response.getContent().get(0).title().contains("title")).isTrue();
                softly.assertThat(response.getContent().get(1).title().contains("title")).isTrue();
                softly.assertThat(response.getContent().get(2).title().contains("title")).isTrue();
            });
        }
    }

    @Nested
    @DisplayName("게시글 수정")
    class UpdatePost {

        @ParameterizedTest
        @AutoSource
        @DisplayName("게시글 수정시 작성자가 아니라면 수정에 실패한다.")
        void update_post_not_writer(final Post post) {

            // given
            given(postRepository.findById(any())).willReturn(Optional.of(post));

            final var request = new UpdatePostRequest(
                    "title",
                    "content"
            );

            // when

            // then
            assertSoftly(softly -> softly.assertThatThrownBy(() -> postService.updatePost(post.getId(), 100L, request))
                    .isInstanceOf(WriterNotSameException.class));
        }
    }

    @Nested
    @DisplayName("게시글 삭제")
    class DeletePost {

        @ParameterizedTest
        @AutoSource
        @DisplayName("요청시 작성자가 아니라면 삭제에 실패한다.")
        void delete_post(final Post post) {

            // given
            given(postRepository.findById(any())).willReturn(Optional.of(post));

            // when

            // then
            assertSoftly(softly -> {
                softly.assertThatThrownBy(() -> postService.deletePost(post.getId(), 1000L))
                        .isInstanceOf(WriterNotSameException.class);
            });
        }

    }
}