package com.mailplug.homework.domain.post.web;

import autoparams.AutoSource;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mailplug.homework.domain.board.BoardType;
import com.mailplug.homework.domain.post.application.PostService;
import com.mailplug.homework.domain.post.web.dto.ReadPostListResponse;
import com.mailplug.homework.domain.post.web.dto.ReadPostResponse;
import com.mailplug.homework.domain.post.web.dto.WritePostRequest;
import com.mailplug.homework.global.resolver.MemberIdResolver;
import com.mailplug.homework.global.security.AuthorizationExtractor;
import com.mailplug.homework.global.security.JwtTokenProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PostController.class)
@SpyBean({AuthorizationExtractor.class, MemberIdResolver.class})
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostService postService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @Spy
    private ObjectMapper objectMapper;

    @Nested
    @DisplayName("게시글 작성")
    class WritePost {

        @ParameterizedTest
        @AutoSource
        @DisplayName("요청시 정상적으로 저장되어야 한다.")
        void write_post(final WritePostRequest request) throws Exception {

            // given
            final String accessToken = jwtTokenProvider.createAccessToken(1L);

            // when
            final var perform = mockMvc.perform(post("/posts")
                    .contentType(APPLICATION_JSON)
                    .header("Authorization", "bearer " + accessToken)
                    .content(objectMapper.writeValueAsString(request)));

            // then
            perform.andExpect(status().isCreated());
        }

        @ParameterizedTest
        @AutoSource
        @DisplayName("요청시 회원이 아니라면 저장에 실패한다.")
        void write_post_not_member(final WritePostRequest request) throws Exception {

            // given

            // when
            final var perform = mockMvc.perform(post("/posts")
                    .contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)));

            // then
            perform.andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("요청시 게시글 제목이 100자를 초과하면 저장에 실패한다.")
        void write_post_over_title() throws Exception {

            // given
            final String accessToken = jwtTokenProvider.createAccessToken(1L);

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
    }

    @Nested
    @DisplayName("게시글 조회")
    class ReadPost {

        @ParameterizedTest
        @AutoSource
        @DisplayName("요청시 게시글 단건 조회에 성공한다.")
        void read_post(final ReadPostResponse response) throws Exception {

            // given
            given(postService.readPost(any())).willReturn(response);

            // when
            final var perform = mockMvc.perform(get("/posts/{postId}", 1L)
                    .contentType(APPLICATION_JSON));

            // then
            perform.andExpect(status().isOk());
        }

        @ParameterizedTest
        @AutoSource
        @DisplayName("게시판별 게시글 목록 조회시 게시판에 속한 게시글 목록이 페이징되어 조회된다.")
        void read_posts_by_paging(final List<ReadPostListResponse> responses) throws Exception {

            // given
            given(postService.readPostList(any(), any())).willReturn(new PageImpl<>(responses));

            // when
            final var perform = mockMvc.perform(get("/posts?name=free")
                    .contentType(APPLICATION_JSON));

            // then
            perform.andExpect(status().isOk());
        }

        @ParameterizedTest
        @AutoSource
        @DisplayName("제목으로 게시글을 검색하면 키워드가 제목에 포함된 게시글이 검색된다.")
        void read_posts_by_post_title(final List<ReadPostListResponse> responses) throws Exception {

            // given
            given(postService.readPostList(any(), any())).willReturn(new PageImpl<>(responses));

            // when
            final var perform = mockMvc.perform(get("/posts/search?keyword=title")
                    .contentType(APPLICATION_JSON));

            // then
            perform.andExpect(status().isOk());
        }

    }
}