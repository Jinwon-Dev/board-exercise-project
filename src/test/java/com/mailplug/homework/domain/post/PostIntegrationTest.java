package com.mailplug.homework.domain.post;

import autoparams.AutoSource;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mailplug.homework.domain.IntegrationTest;
import com.mailplug.homework.domain.board.BoardType;
import com.mailplug.homework.domain.board.persistence.Board;
import com.mailplug.homework.domain.board.persistence.BoardRepository;
import com.mailplug.homework.domain.member.persistence.Member;
import com.mailplug.homework.domain.member.persistence.MemberRepository;
import com.mailplug.homework.domain.post.persistence.Post;
import com.mailplug.homework.domain.post.persistence.PostRepository;
import com.mailplug.homework.domain.post.web.dto.UpdatePostRequest;
import com.mailplug.homework.domain.post.web.dto.WritePostRequest;
import com.mailplug.homework.global.security.JwtTokenProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.is;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PostIntegrationTest extends IntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    @DisplayName("게시글 작성")
    class WritePost {

        @ParameterizedTest
        @AutoSource
        @DisplayName("요청시 정상적으로 저장되어야 한다.")
        void write_post(final Member member) throws Exception {

            // given
            final Member savedMember = memberRepository.save(member);
            boardRepository.save(new Board(BoardType.FREE));

            final String accessToken = jwtTokenProvider.createAccessToken(savedMember.getId());

            final var request = new WritePostRequest(
                    "title",
                    "content",
                    BoardType.FREE
            );

            // when
            final var perform = mockMvc.perform(post("/posts")
                    .contentType(APPLICATION_JSON)
                    .header("Authorization", "bearer " + accessToken)
                    .content(objectMapper.writeValueAsString(request)));

            // then
            perform.andExpect(status().isCreated())
                    .andExpect(jsonPath("$.data.id").exists());
        }

        @ParameterizedTest
        @AutoSource
        @DisplayName("요청시 로그인 상태여야 한다.")
        void write_post_not_member() throws Exception {

            // given
            boardRepository.save(new Board(BoardType.FREE));

            final var request = new WritePostRequest(
                    "title",
                    "content",
                    BoardType.FREE
            );

            // when
            final var perform = mockMvc.perform(post("/posts")
                    .contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)));

            // then
            perform.andExpect(status().isUnauthorized());
        }

        @ParameterizedTest
        @AutoSource
        @DisplayName("요청시 제목은 100자를 초과해서 작성할 수 없다.")
        void write_post_over_title(final Member member) throws Exception {

            // given
            final Member savedMember = memberRepository.save(member);
            boardRepository.save(new Board(BoardType.FREE));

            final String accessToken = jwtTokenProvider.createAccessToken(savedMember.getId());

            final var request = new WritePostRequest(
                    IntStream.range(0, 21)
                            .mapToObj(i -> "title")
                            .collect(Collectors.joining()),
                    "content",
                    BoardType.FREE
            );

            // when
            final var perform = mockMvc.perform(post("/posts")
                    .contentType(APPLICATION_JSON)
                    .header("Authorization", "bearer " + accessToken)
                    .content(objectMapper.writeValueAsString(request)));

            // then
            perform.andExpect(status().isBadRequest());
        }

        @ParameterizedTest
        @AutoSource
        @DisplayName("요청시 존재하지 않는 게시판이면 실패한다.")
        void write_post_not_exist_board(final Member member) throws Exception {

            // given
            final Member savedMember = memberRepository.save(member);

            final String accessToken = jwtTokenProvider.createAccessToken(savedMember.getId());

            final var request = new WritePostRequest(
                    "title",
                    "content",
                    BoardType.NOTICE
            );

            // when
            final var perform = mockMvc.perform(post("/posts")
                    .contentType(APPLICATION_JSON)
                    .header("Authorization", "bearer " + accessToken)
                    .content(objectMapper.writeValueAsString(request)));

            // then
            perform.andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("게시글 조회")
    class ReadPost {

        @ParameterizedTest
        @AutoSource
        @DisplayName("요청시 단건 게시글이 조회되고, 조회수가 1 증가한다.")
        void read_post(final Member member) throws Exception {

            // given
            memberRepository.save(member);
            final Board board = boardRepository.save(new Board(BoardType.FREE));

            final Post post = postRepository.save(new Post(
                    "title",
                    "content",
                    member,
                    board
            ));

            // when
            final var perform = mockMvc.perform(get("/posts/{postId}", post.getId())
                    .contentType(APPLICATION_JSON));

            // then
            perform.andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.views").value(1L));
        }

        @Test
        @DisplayName("존재하지 않는 게시글 조회시 조회에 실패한다.")
        void read_post_not_exist_post() throws Exception {

            // given

            // when
            final var perform = mockMvc.perform(get("/posts/{postId}", 1000L)
                    .contentType(APPLICATION_JSON));

            // then
            perform.andExpect(status().isNotFound());
        }

        @ParameterizedTest
        @AutoSource
        @DisplayName("게시판별 게시글 목록 조회시 게시판에 속한 게시글 목록이 페이징되어 조회된다.")
        void read_posts_by_paging(final Member member) throws Exception {

            // given
            memberRepository.save(member);
            final Board board = boardRepository.save(new Board(BoardType.FREE));

            savePostsForTest(member, board);

            // when
            final var perform = mockMvc.perform(get("/posts?name=free")
                    .contentType(APPLICATION_JSON));

            // then
            perform.andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.content.size()", is(5)));
        }

        @ParameterizedTest
        @AutoSource
        @DisplayName("제목으로 게시글을 검색하면 키워드가 제목에 포함된 게시글이 검색된다.")
        void read_posts_by_post_title(final Member member) throws Exception {

            // given
            memberRepository.save(member);
            final Board board = boardRepository.save(new Board(BoardType.FREE));

            savePostsForTest(member, board);

            // when
            final var perform = mockMvc.perform(get("/posts/search?keyword=title")
                    .contentType(APPLICATION_JSON));

            // then
            perform.andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.content.size()", is(5)));
        }

        private void savePostsForTest(final Member member, final Board board) {

            postRepository.saveAll(
                    IntStream.range(0, 10)
                            .mapToObj(i -> new Post("title" + i, "content", member, board))
                            .toList()
            );
        }
    }

    @Nested
    @DisplayName("게시글 수정")
    class UpdatePost {

        @ParameterizedTest
        @AutoSource
        @DisplayName("요청시 게시글이 정상적으로 수정되어 반영된다.")
        void update_post(final Member member) throws Exception {

            // given
            final Member savedMember = memberRepository.save(member);
            final Board board = boardRepository.save(new Board(BoardType.FREE));

            final String accessToken = jwtTokenProvider.createAccessToken(savedMember.getId());

            final Post post = new Post(
                    "title",
                    "content",
                    member,
                    board
            );
            postRepository.save(post);

            final var request = new UpdatePostRequest(
                    "test",
                    "test"
            );

            // when
            final var perform = mockMvc.perform(patch("/posts/{postId}", post.getId())
                    .contentType(APPLICATION_JSON)
                    .header("Authorization", "bearer " + accessToken)
                    .content(objectMapper.writeValueAsString(request)));

            // then
            perform.andExpect(status().isOk());
        }

        @ParameterizedTest
        @AutoSource
        @DisplayName("요청시 작성자가 아니라면 수정에 실패한다.")
        void update_post_not_writer(final Member member) throws Exception {

            // given
            memberRepository.save(member);
            final Board board = boardRepository.save(new Board(BoardType.FREE));

            final String accessToken = jwtTokenProvider.createAccessToken(100L);

            final Post post = new Post(
                    "title",
                    "content",
                    member,
                    board
            );
            postRepository.save(post);

            final var request = new UpdatePostRequest(
                    "test",
                    "test"
            );

            // when
            final var perform = mockMvc.perform(patch("/posts/{postId}", post.getId())
                    .contentType(APPLICATION_JSON)
                    .header("Authorization", "bearer " + accessToken)
                    .content(objectMapper.writeValueAsString(request)));

            // then
            perform.andExpect(status().isForbidden());
        }
    }
}
